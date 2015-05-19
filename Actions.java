import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.lang.Math;
import java.util.Random;
import java.util.function.LongConsumer;
import static java.lang.Math.abs;
import processing.core.*;

public class Actions
{
	private static final Random RANDOMIZER = new Random(101614);
	
	private static final int BLOB_RATE_SCALE = 4;
	private static final int BLOB_ANIMATION_RATE_SCALE = 50;
	private static final int BLOB_ANIMATION_MIN = 1;
	private static final int BLOB_ANIMATION_MAX = 3;
	
	private static final int ORE_CORRUPT_MIN = 20000;
	private static final int ORE_CORRUPT_MAX = 30000;
	
	private static final int QUAKE_STEPS = 10;
	private static final int QUAKE_DURATION = 1100;
	private static final int QUAKE_ANIMATION_RATE = 100;
	
	private static final int VEIN_SPAWN_DELAY = 500;
	private static final int VEIN_RATE_MIN = 8000;
	private static final int VEIN_RATE_MAX = 17000;
	
	public static LongConsumer createAnimationAction(WorldModel world, Animated entity, int repeatCount)
	{
		LongConsumer[] action = { null };
		action[0] = (long currentTicks) ->
		{
			entity.removePendingAction(action[0]);
			entity.nextImage();
			
			if (repeatCount != 1)
			{
				scheduleAction(world, entity, createAnimationAction(world, entity, Math.max(repeatCount - 1, 0)), currentTicks + (long)entity.getAnimationRate());
			}
		};
		return action[0];
	}
	
	public static LongConsumer createEntityDeathAction(WorldModel world, Actor entity)
	{
		LongConsumer[] action = { null };
		action[0] = (long currentTicks) ->
		{
			entity.removePendingAction(action[0]);
			Point pt = entity.getPosition();
			removeEntity(world, entity);
		};
		return action[0];
	}
	
	public static LongConsumer createOreTransformAction(WorldModel world, Actor entity, Map<String, List<PImage>> iStore)
	{
		LongConsumer[] action = { null };
		action[0] = (long currentTicks) ->
		{
			entity.removePendingAction(action[0]);
			Entity blob = createBlob(world, entity.getName() + " -- blob", entity.getPosition(), entity.getActionRate() / BLOB_RATE_SCALE, currentTicks, iStore);
			
			removeEntity(world, entity);
			world.addEntity(blob);
		};
		return action[0];
	}
	
	public static void removeEntity(WorldModel world, Actor entity)
	{
		for (LongConsumer action : entity.getPendingActions())
			world.unscheduleAction(action);
		entity.clearPendingActions();
		world.removeEntity((Entity)entity);
	}
	
	public static OreBlob createBlob(WorldModel world, String name, Point pt, int rate, long ticks, Map<String, List<PImage>> iStore)
	{
		OreBlob blob = new OreBlob(pt, iStore.get("blob"), rate, (RANDOMIZER.nextInt(BLOB_ANIMATION_MAX - BLOB_ANIMATION_MIN) + BLOB_ANIMATION_MIN) * BLOB_ANIMATION_RATE_SCALE);
		
		scheduleBlob(world, blob, ticks, iStore);
		
		return blob;
	}
	
	public static void scheduleBlob(WorldModel world, OreBlob blob, long ticks, Map<String, List<PImage>> iStore)
	{
		scheduleAction(world, blob, createOreBlobAction(world, iStore, blob), ticks + (long)blob.getActionRate());
		scheduleAnimation(world, blob);
	}
	
	public static LongConsumer createOreBlobAction(WorldModel world, Map<String, List<PImage>> iStore, OreBlob blob)
	{
		LongConsumer[] action = { null };
		action[0] = (long currentTicks) ->
		{
			blob.removePendingAction(action[0]);
			
			Point entityPt = blob.getPosition();
			Vein vein = (Vein)world.findNearest(entityPt, Vein.class);
			Point found = blobToVein(world, blob, vein);
			
			long nextTime = currentTicks + (long)blob.getActionRate();
			if (found != null)
			{
				Quake quake = createQuake(world, found, currentTicks, iStore);
				world.addEntity(quake);
				nextTime = currentTicks + (long)blob.getActionRate() * 2;
			}
			scheduleAction(world, blob, createOreBlobAction(world, iStore, blob), nextTime);
		};
		return action[0];
	}
	
	public static Point blobToVein(WorldModel world, OreBlob blob, Vein vein)
	{
		if (vein == null) return null;
		Point start = blob.getPosition();
		Point finish = vein.getPosition();
		
		if (adjacent(start, finish))
		{
			removeEntity(world, vein);
			return finish;
		}
		else
		{
			Point newPt = blob.blobNextPosition(world, finish);
			Entity oldEntity = world.getTileOccupant(newPt);
			if (oldEntity instanceof Ore)
				removeEntity(world, (Actor)oldEntity);
			world.moveEntity(blob, newPt);
			return null;
		}
	}
	
	public static void scheduleMiner(WorldModel world, Miner miner, long ticks, Map<String, List<PImage>> iStore)
	{
		scheduleAction(world, miner, createMinerAction(world, miner, iStore), ticks + (long)miner.getActionRate());
		scheduleAnimation(world, miner);
	}
	
	public static LongConsumer createMinerAction(WorldModel world, Miner miner, Map<String, List<PImage>> iStore)
	{
		LongConsumer[] action = { null };
		action[0] = (long currentTicks) ->
		{
			miner.removePendingAction(action[0]);
			
			Point pos = miner.getPosition();
			Entity target = world.findNearest(pos, miner.isFull() ? Blacksmith.class : Ore.class);
			minerToTarget(world, miner, target);
			
			scheduleAction(world, miner, createMinerAction(world, miner, iStore), currentTicks + (long)miner.getActionRate());
		};
		return action[0];
	}
	
	private static void minerToTarget(WorldModel world, Miner miner, Entity target)
	{
		if (target == null) return;
		Point start = miner.getPosition();
		Point finish = target.getPosition();
		
		if (adjacent(start, finish))
		{
			if (target instanceof Blacksmith)
			{
				miner.setResourceCount(0);
			}
			else
			{
				miner.incrementResourceCount();
				removeEntity(world, (Actor)target);
			}
		}
		else
		{
			Point nextPoint = miner.nextPosition(world, finish);
			world.moveEntity(miner, nextPoint);
		}
	}
	
	public static Ore createOre(WorldModel world, Point pt, long ticks, Map<String, List<PImage>> iStore)
	{
		Ore ore = new Ore(pt, iStore.get("ore").get(0), (RANDOMIZER.nextInt(ORE_CORRUPT_MAX - ORE_CORRUPT_MIN) + ORE_CORRUPT_MIN));
		scheduleOre(world, ore, ticks, iStore);
		
		return ore;
	}
	
	public static void scheduleOre(WorldModel world, Ore ore, long ticks, Map<String, List<PImage>> iStore)
	{
		scheduleAction(world, ore, createOreTransformAction(world, ore, iStore), ticks + (long)ore.getActionRate());
	}
	
	public static Quake createQuake(WorldModel world, Point pt, long ticks, Map<String, List<PImage>> iStore)
	{
		Quake quake = new Quake(pt, iStore.get("quake"));
		scheduleQuake(world, quake, ticks);
		
		return quake;
	}
	
	public static void scheduleQuake(WorldModel world, Quake quake, long ticks)
	{
		scheduleAnimation(world, quake, QUAKE_STEPS);
		scheduleAction(world, quake, createEntityDeathAction(world, quake), ticks + (long)QUAKE_DURATION);
	}
	
	public static Vein createVein(WorldModel world, String name, Point pt, long ticks, Map<String, List<PImage>> iStore)
	{
		Vein vein = new Vein(pt, iStore.get("vein").get(0), RANDOMIZER.nextInt(VEIN_RATE_MAX - VEIN_RATE_MIN) + VEIN_RATE_MIN, 1);
		scheduleVein(world, vein, ticks, iStore);
		
		return vein;
	}
	
	public static void scheduleVein(WorldModel world, Vein vein, long ticks, Map<String, List<PImage>> iStore)
	{
		scheduleAction(world, vein, createVeinAction(world, vein, iStore), ticks + (long)vein.getActionRate());
	}
	
	public static LongConsumer createVeinAction(WorldModel world, Vein vein, Map<String, List<PImage>> iStore)
	{
		LongConsumer[] action = {null};
		action[0] = (long currentTicks) ->
		{
			vein.removePendingAction(action[0]);
			Point pos = vein.getPosition();
			Point open = findOpenAround(world, pos, vein.getResourceDistance());
			
			if (open != null)
			{
				Ore oreo = createOre(world, open, currentTicks, iStore);
				world.addEntity(oreo);
			}
			scheduleAction(world, vein, createVeinAction(world, vein, iStore), currentTicks + (long)vein.getActionRate());
		};
		return action[0];
	}
	
	private static Point findOpenAround(WorldModel world, Point pos, int resDist)
	{
		for (int dy = -resDist; dy <= resDist; dy++)
		{
			for (int dx = -resDist; dx <= resDist; dx++)
			{
				Point pt = new Point(pos.getXCoord() + dx, pos.getYCoord() + dy);
				if (world.withinBounds(pt) && !world.isOccupied(pt)) return pt;
			}
		}
		return null;
	}
	
	public static void scheduleAction(WorldModel world, Actor entity, LongConsumer action, long time)
	{
		entity.addPendingAction(action);
		world.scheduleAction(action, time);
	}
	
	public static void scheduleAnimation(WorldModel world, Animated entity, int repeatCount)
	{
		scheduleAction(world, entity, createAnimationAction(world, entity, repeatCount), entity.getAnimationRate());
	}
	public static void scheduleAnimation(WorldModel world, Animated entity)
	{
		scheduleAnimation(world, entity, 0);
	}
	
	private static boolean adjacent(Point p1, Point p2)
	{
		return ((p1.getXCoord() == p2.getXCoord() && abs(p1.getYCoord() - p2.getYCoord()) == 1)
				|| (p1.getYCoord() == p2.getYCoord() && abs(p1.getXCoord() - p2.getXCoord()) == 1));
	}
}