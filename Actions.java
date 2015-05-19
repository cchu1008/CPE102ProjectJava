import java.util.Map;
import java.util.List;
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
	}/*
	
	public static LongConsumer createEntityDeathAction(WorldModel world, Entity entity)
	{
		List<Point> action = (int currentTicks) ->
		{
			entity.removePendingAction(action);
			Point pt = entity.getPosition();
			removeEntity(world, entity);
			List<Point> fin = new ArrayList<Point>();
			fin.add(pt);
			return fin;
		}
		return action;
	}
	
	public static LongConsumer createOreTransformAction(WorldModel world, Entity entity, Map<String, List<PImage>> iStore)
	{
		List<Point> action = (currentTicks) ->
		{
			entity.removePendingAction(action);
			Entity blob = createBlob(world, entity.getName() + " -- blob", entity.getPosition(), entity.getRate() / BLOB_RATE_SCALE, currentTicks, iStore);
			
			removeEntity(world, entity);
			world.addEntity(blob);
			
			List<Point> fin = new ArrayList<Point>();
			fin.add(blob.getPosition());
			
			return fin;
		}
		return action;
	}
	
	public static void removeEntity(WorldModel world, Entity entity)
	{
		entity.clearPendingActions(world);
		world.removeEntity(entity);
	}
	
	public static Blob createBlob(WorldModel world, String name, Point pt, int rate, int ticks, Map<String, List<PImage>> iStore)
	{
		Entity blob = new OreBlob(name, pt, rate, iStore.get("blob").get(0), (random.nextInt(BLOB_ANIMATION_MAX - BLOB_ANIMATION_MIN) + BLOB_ANIMATION_MIN) * BLOB_ANIMATION_RATE_SCALE);
		
		scheduleBlob(world, blob, ticks, iStore);
		
		return blob;
	}*/
	
	public static void scheduleBlob(WorldModel world, OreBlob blob, int ticks, Map<String, List<PImage>> iStore)
	{
		//scheduleAction(world, blob, blob.createOreBlobAction(world, iStore), ticks + blob.getRate());
		scheduleAnimation(world, blob);
	}
	
	public static void scheduleMiner(WorldModel world, Miner miner, int ticks, Map<String, List<PImage>> iStore)
	{
		//scheduleAction(world, miner, miner.createMinerAction(world, iStore), ticks + miner.getRate());
		scheduleAnimation(world, miner);
	}
	/*
	public static Ore createOre(WorldModel world, String name, Point pt, int ticks, Map<String, List<PImage>> iStore)
	{
		Entity Ore = new Ore(name, pt, iStore.get("ore").get(0), (random.nextInt(ORE_CORRUPT_MAX - ORE_CORRUPT_MIN) + ORE_CORRUPT_MIN));
		scheduleOre(world, ore, ticks, iStore);
		
		return ore;
	}
	*/
	public static void scheduleOre(WorldModel world, Ore ore, int ticks, Map<String, List<PImage>> iStore)
	{
		//scheduleAction(world, ore, createOreTransformAction(world, ore, iStore), ticks + ore.getRate());
	}
	/*
	public static Quake createQuake(WorldModel world, Point pt, int ticks, Map<String, List<PImage>> iStore)
	{
		Entity quake = new Quake("quake", pt, iStore.get("quake").get(0), QUAKE_ANIMATION_RATE);
		sheduleQuake(world, quake, ticks);
		
		return quake;
	}*/
	
	public static void scheduleQuake(WorldModel world, Quake quake, long ticks)
	{
		//scheduleAnimation(world, quake, QUAKE_STEPS);
		//scheduleAction(world, quake, createEntityDeathAction(world, quake), ticks + QUAKE_DURATION);
	}
	/*
	public static Vein createVein(WorldModel world, String name, Point pt, long ticks, Map<String, List<PImage>> iStore)
	{
		Entity vein = new Vein("vein" + name, (random.nextInt(VEIN_RATE_MAX - VEIN_RATE_MIN) + VEIN_RATE_MIN), pt, iStore.get("vein").get(0));
		
		return vein;
	}
	*/
	public static void scheduleVein(WorldModel world, Vein vein, long ticks, Map<String, List<PImage>> iStore)
	{
		//scheduleAction(world, vein, vein.createVeinAction(world, iStore), ticks + vein.getRate());
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