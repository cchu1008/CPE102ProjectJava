import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.lang.Math;
import java.util.Random;
import java.util.function.LongConsumer;
import processing.core.*;

public class Actions
{
	private static final Random RANDOMIZER = WorldView.RANDOMIZER;
	
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
		LongConsumer[] action = {null};
		action[0] = (long currentTicks) ->
		{
			entity.removePendingAction(action[0]);
			entity.nextImage();
			
			if (repeatCount != 1)
			{
				scheduleAction(world, entity,
						createAnimationAction(world, entity, Math.max(repeatCount - 1, 0)),
						currentTicks + (long)entity.getAnimationRate());
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
		entity.clearPendingActions();
		world.removeEntity((Entity)entity);
	}
	
	public static OreBlob createBlob(WorldModel world, String name, Point pt, int rate, long ticks, Map<String, List<PImage>> iStore)
	{
		Entity blob = new OreBlob(name, pt, rate, iStore.get("blob").get(0), (RANDOMIZER.nextInt(BLOB_ANIMATION_MAX - BLOB_ANIMATION_MIN) + BLOB_ANIMATION_MIN) * BLOB_ANIMATION_RATE_SCALE);
		
		scheduleBlob(world, (OreBlob)blob, ticks, iStore);
		
		return (OreBlob)blob;
	}
	
	public static void scheduleBlob(WorldModel world, OreBlob blob, long ticks, Map<String, List<PImage>> iStore)
	{
		scheduleAction(world, blob, createOreBlobAction(world, iStore, blob), ticks + (long)blob.getActionRate());
		scheduleAnimation(world, blob);
	}
	
	public static LongConsumer createOreBlobAction(WorldModel world, Map<String, List<PImage>> iStore, Actor blob)
	{
		LongConsumer[] action = { null };
		action[0] = (long currentTicks) ->
		{
			blob.removePendingAction(action[0]);
			
			Point entityPt = blob.getPosition();
			Entity vein = world.findNearest(entityPt, Vein.class);
			List<Point> tiles = new ArrayList<Point>();
			boolean found = blobToVein(world, (Actor)vein, (OreBlob)blob, tiles);
			
			long nextTime = currentTicks + (long)blob.getActionRate();
			if (found)
			{
				Entity quake = createQuake(world, tiles.get(0), currentTicks, iStore);
				world.addEntity(quake);
				nextTime = currentTicks + (long)blob.getActionRate() * 2;
			}
			scheduleAction(world, blob, createOreBlobAction(world, iStore, blob), nextTime);
		};
		return action[0];			
	}
	
	public static boolean blobToVein(WorldModel world, Actor vein, OreBlob blob, List<Point> tiles)
	{
		Point entityPt = blob.getPosition();
		if (!(vein instanceof Vein))
		{
			tiles.add(entityPt);
			return false;
		}
		Point veinPt = vein.getPosition();
		if (Entity.adjacent(entityPt, veinPt))
		{
			removeEntity(world, vein);
			return true;
		}
		else
		{
			Point newPt = blob.blobNextPosition(world, veinPt);
			Entity oldEntity = world.getTileOccupant(newPt);
			if (oldEntity instanceof Ore)
				removeEntity(world, (Actor)oldEntity);
			world.moveEntity((Actor)blob, newPt);
			return false;
		}
	}
	
	public static void scheduleMiner(WorldModel world, Miner miner, long ticks, Map<String, List<PImage>> iStore)
	{
		scheduleAction(world, miner, createMinerAction(world, iStore), ticks + (long)miner.getActionRate());
		scheduleAnimation(world, miner);
	}
	
	public static 
	
	public static Ore createOre(WorldModel world, String name, Point pt, long ticks, Map<String, List<PImage>> iStore)
	{
		Entity ore = new Ore(name, pt, iStore.get("ore").get(0), (RANDOMIZER.nextInt(ORE_CORRUPT_MAX - ORE_CORRUPT_MIN) + ORE_CORRUPT_MIN));
		scheduleOre(world, (Ore)ore, ticks, iStore);
		
		return(Ore) ore;
	}
	
	public static void scheduleOre(WorldModel world, Ore ore, long ticks, Map<String, List<PImage>> iStore)
	{
		scheduleAction(world, ore, createOreTransformAction(world, ore, iStore), ticks + (long)ore.getActionRate());
	}
	
	public static Quake createQuake(WorldModel world, Point pt, long ticks, Map<String, List<PImage>> iStore)
	{
		Entity quake = new Quake("quake", pt, iStore.get("quake").get(0), QUAKE_ANIMATION_RATE);
		scheduleQuake(world, (Quake)quake, ticks);
		
		return (Quake)quake;
	}
	
	public static void scheduleQuake(WorldModel world, Quake quake, long ticks)
	{
		scheduleAnimation(world, quake, QUAKE_STEPS);
		scheduleAction(world, quake, createEntityDeathAction(world, quake), ticks + (long)QUAKE_DURATION);
	}
	
	public static Vein createVein(WorldModel world, String name, Point pt, long ticks, Map<String, List<PImage>> iStore)
	{
		Entity vein = new Vein("vein" + name, (RANDOMIZER.nextInt(VEIN_RATE_MAX - VEIN_RATE_MIN) + VEIN_RATE_MIN), pt, iStore.get("vein").get(0));
		scheduleVein(world, (Vein)vein, ticks);
		
		return (Vein)vein;
	}
	
	public static void scheduleVein(WorldModel world, Vein vein, long ticks, Map<String, List<PImage>> iStore)
	{
		scheduleAction(world, vein, vein.createVeinAction(world, iStore), ticks + (long)vein.getActionRate());
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
}