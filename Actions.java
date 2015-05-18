import java.lang.Math;
import java.util.random;

public class Actions
{
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
	
	public List<Point> createAnimationAction(WorldModel world, Entity entity, int repeatCount)
	{
		List<Point> action = (int currentTicks) ->
		{
			entity.removePendingAction(action);
			entity.nextImage();
			
			if (repeatCount != 1)
			{
				scheduleAction(world, entity, createAnimationAction(world, entity, Math.max(repeatCount - 1, 0)), currentTicks + entity.getAnimationRate());
			}
			List<Point> fin = new ArrayList<Point>();
			fin.add(entity.getPosition());
			return fin;
		}
		return action;
	}
	
	public List<Point> createEntityDeathAction(WorldModel world, Entity entity)
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
	
	public List<Point> createOreTransformAction(WorldModel world, Entity entity, Map<String, List<PImage>> iStore)
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
	
	public void removeEntity(WorldModel world, Entity entity)
	{
		entity.clearPendingActions(world);
		world.removeEntity(entity);
	}
	
	public Blob createBlob(WorldModel world, String name, Point pt, int rate, int ticks, Map<String, List<PImage>> iStore)
	{
		Entity blob = new OreBlob(name, pt, rate, ImageStore.getImages(iStore, 'blob'), (random.nextInt(BLOB_ANIMATION_MAX - BLOB_ANIMATION_MIN) + BLOB_ANIMATION_MIN) * BLOB_ANIMATION_RATE_SCALE);
		
		scheduleBlob(world, blob, ticks, iStore);
		
		return blob;
	}
	
	public void scheduleBlob(WorldModel world, Entity blob, int ticks, Map<String, List<PImage>> iStore)
	{
		scheduleAction(world, blob, blob.createOreBlobAction(world, iStore), ticks + blob.getRate());
		scheduleAnimation(world, blob);
	}
	
	public static void scheduleMiner(WorldModel world, Entity miner, int ticks, Map<String, List<PImage>> iStore)
	{
		scheduleAction(world, miner, miner.createMinerAction(world, iStore), ticks + miner.getRate());
		scheduleAnimation(world, miner);
	}
	
	public Ore createOre(WorldModel world, String name, Point pt, int ticks, Map<String, List<PImage>> iStore)
	{
		Entity Ore = new Ore(name, pt, ImageStore.getImages(iStore, 'ore'), (random.nextInt(ORE_CORRUPT_MAX - ORE_CORRUPT_MIN) + ORE_CORRUPT_MIN));
		scheduleOre(world, ore, ticks, iStore);
		
		return ore;
	}
	
	public static void scheduleOre(WorldModel world, Entity ore, int ticks, Map<String, List<PImage>> iStore)
	{
		scheduleAction(world, ore, createOreTransformAction(world, ore, iStore), ticks + ore.getRate());
	}
	
	public Quake createQuake(WorldModel world, Point pt, int ticks, Map<String, List<PImage>> iStore)
	{
		Entity quake = new Quake('quake', pt, ImageStore.getImages(iStore, 'quake'), QUAKE_ANIMATION_RATE);
		sheduleQuake(world, quake, ticks);
		
		return quake;
	}
	
	public void scheduleQuake(WorldModel world, Entity quake, int ticks)
	{
		scheduleAnimation(world, quake, QUAKE_STEPS);
		scheduleAction(world, quake, createEntityDeathAction(world, quake), ticks + QUAKE_DURATION);
	}
	
	public Vein createVein(WorldModel world, String name, Point pt, int ticks, Map<String, List<PImage>> iStore)
	{
		Entity vein = new Vein('vein' + name, (random.nextInt(VEIN_RATE_MAX - VEIN_RATE_MIN) + VEIN_RATE_MIN), pt, ImageStore.getImages(iStore, 'vein'));
		
		return vein;
	}
	
	public static void scheduleVein(WorldModel world, Entity vein, int ticks, Map<String, List<PImage>> iStore)
	{
		scheduleAction(world, vein, vein.createVeinAction(world, iStore), ticks + vein.getRate());
	}
	
	public void scheduleAction(WorldModel world, Entity entity, Point[] action, int time)
	{
		entity.addPendingAction(action);
		world.scheduleAction(action, time);
	}
	
	public void scheduleAnimation(WorldModel world, Entity entity, int repeatCount=0)
	{
		scheduleAction(world, entity, createAnimationAction(world, entity, repeatCount), entity.getAnimationRate());
	}
}