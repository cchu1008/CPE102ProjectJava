
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
	
	public Actions createAnimationAction(WorldModel world, Entity entity, int repeatCount)
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
}