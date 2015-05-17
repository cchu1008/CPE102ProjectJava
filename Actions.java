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
	
	public Point[] createAnimationAction(WorldModel world, Entity entity, int repeatCount)
	{
		private Point[] action(int currentTicks)
		{
			entity.removePendingAction(action);
			entity.nextImage();
			
			if (repeatCount != 1)
			{
				scheduleAction(world, entity, createAnimationAction(world, entity, Math.max(repeatCount - 1, 0)), currentTicks + entity.getAnimationRate());
			}
			Point[] fin = new Point[1];
			fin[0] = entity.getPosition();
			return fin;
		}
		return action;
	}
	
	public Point[] createEntityDeathAction(WorldModel world, Entity entity)
	{
		private Point[] action(int currentTicks)
		{
			entity.removePendingAction(action);
			Point pt = entity.getPosition();
			removeEntity(world, entity);
			Point[] fin = new Point[1];
			fin[0] = pt;
			return fin;
		}
		return action;
	}
	
	public Point[] createOreTransformAction(WorldModel world, Entity entity, Map<String, List<PImage>> iStore)
	{
		private Point[] action(currentTicks)
		{
			entity.removePendingAction(action);
			Entity blob = createBlob(world, entity.getName() + " -- blob", entity.getPosition(), entity.getRate() / BLOB_RATE_SCALE, currentTicks, iStore);
			
			removeEntity(world, entity);
			world.addEntity(blob);
			
			Point[] fin = new Point[1];
			fin[0] = blob.getPosition();
			
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
}