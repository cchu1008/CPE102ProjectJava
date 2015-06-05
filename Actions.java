import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.lang.Math;
import java.util.Random;
import java.util.function.LongConsumer;
import static java.lang.Math.abs;
import processing.core.*;

public class Actions
{
	private static final Random RANDOMIZER = new Random();
	
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
	
	public static void removeEntity(WorldModel world, Actor entity)
	{
		for (LongConsumer action : entity.getPendingActions())
			world.unscheduleAction(action);
		entity.clearPendingActions();
		world.removeEntity((Entity)entity);
	}
	
	
	/* Creating the actions */
	
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
	
	public static LongConsumer createOreTransformAction(WorldModel world, Actor entity, Map<String, List<PImage>> imageStore)
	{
		LongConsumer[] action = { null };
		action[0] = (long currentTicks) ->
		{
			entity.removePendingAction(action[0]);
			Entity blob = createBlob(world, entity.getPosition(), entity.getActionRate() / BLOB_RATE_SCALE, currentTicks, imageStore);
			
			removeEntity(world, entity);
			world.addEntity(blob);
		};
		return action[0];
	}
	
	public static LongConsumer createZombieAction(WorldModel world, Map<String, List<PImage>> imageStore, Zombie zom)
	{
		LongConsumer[] action = { null };
		action[0] = (long currentTicks) ->
		{
			zom.removePendingAction(action[0]);
			
			Point pos = zom.getPosition();
			OreBlob target = (OreBlob)world.findNearest(pos, OreBlob.class);
			
			Point found = zombieToBlob(world, zom, target);
			
			if (found != null)
			{
				Quake quakie = createQuake(world, found, currentTicks, imageStore);
				world.addEntity(quakie);
			}
			scheduleAction(world, zom, createZombieAction(world, imageStore, zom), currentTicks + (long)zom.getActionRate());
		};
		return action[0];
	}
	
	public static LongConsumer createBirdieAction(WorldModel world, Map<String, List<PImage>> imageStore, Birdie bird)
	{
		LongConsumer[] action = { null };
		action[0] = (long currentTicks) ->
		{
			bird.removePendingAction(action[0]);
			
			for (int dx = -1; dx <= 1; dx++)
			{
				for (int dy = -1; dy <= 1; dy++)
				{
					Point p = bird.getPosition().translate(dx, dy);
					if (world.withinBounds(p))
					{
						Background b = world.getBackground(p);
						switch (b.getName())
						{
							case "grass":
							case "default":
								world.setBackground(p, new Background("deadgrass", imageStore.get("deadgrass").get(0)));
								break;
							case "inverted_grass":
								world.setBackground(p, new Background("inverted_deadgrass", WorldView.invertImage(imageStore.get("deadgrass").get(0))));
								break;
						}
					}
				}
			}
			
			Point pos = bird.getPosition();
			Miner target = (Miner)world.findNearest(pos, Miner.class);
			
			Point found;
			
			if (target == null)
			{
				found = birdieToSpawn(world, bird);
				if (found != null)
				{
					Quake quakie = createQuake(world, found, currentTicks, imageStore);
					world.addEntity(quakie);
					return;
				}
			}
			else
			{
				found = birdieToMiner(world, bird, target);
				
				if (found != null)
				{
					Zombie zom = createZombie(world, found, target.getActionRate() * 2, target.getAnimationRate() * 2, currentTicks, imageStore);
					world.addEntity(zom);
				}
				
				if (!bird.getPosition().equals(pos) && RANDOMIZER.nextInt(100) == 0 && !world.isOccupied(pos))
				{
					Ore oreo = createOre(world, pos, currentTicks, imageStore);
					world.addEntity(oreo);
				}
			}
			
			scheduleAction(world, bird, createBirdieAction(world, imageStore, bird), currentTicks + (long)bird.getActionRate());
		};
		return action[0];
	}
	
	public static LongConsumer createOreBlobAction(WorldModel world, Map<String, List<PImage>> imageStore, OreBlob blob)
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
				Quake quake = createQuake(world, found, currentTicks, imageStore);
				world.addEntity(quake);
				nextTime = currentTicks + (long)blob.getActionRate() * 2;
			}
			scheduleAction(world, blob, createOreBlobAction(world, imageStore, blob), nextTime);
		};
		return action[0];
	}
	
	public static LongConsumer createMinerAction(WorldModel world, Miner miner, Map<String, List<PImage>> imageStore)
	{
		LongConsumer[] action = { null };
		action[0] = (long currentTicks) ->
		{
			miner.removePendingAction(action[0]);
			
			Point pos = miner.getPosition();
			Entity target = world.findNearest(pos, miner.isFull() ? Blacksmith.class : Ore.class);
			minerToTarget(world, miner, target);
			
			scheduleAction(world, miner, createMinerAction(world, miner, imageStore), currentTicks + (long)miner.getActionRate());
		};
		return action[0];
	}
	
	public static LongConsumer createVeinAction(WorldModel world, Vein vein, Map<String, List<PImage>> imageStore)
	{
		LongConsumer[] action = {null};
		action[0] = (long currentTicks) ->
		{
			vein.removePendingAction(action[0]);
			Point pos = vein.getPosition();
			Point open = findOpenAround(world, pos, vein.getResourceDistance());
			
			if (open != null)
			{
				Ore oreo = createOre(world, open, currentTicks, imageStore);
				world.addEntity(oreo);
			}
			scheduleAction(world, vein, createVeinAction(world, vein, imageStore), currentTicks + (long)vein.getActionRate());
		};
		return action[0];
	}
	
	
	/* Creating the entities */
	
	public static Zombie createZombie(WorldModel world, Point pt, int rate, int animRate, long ticks, Map<String, List<PImage>> imageStore)
	{
		Zombie zom = new Zombie(pt, imageStore.get(Zombie.ID_KEY), rate, animRate);
		scheduleZombie(world, zom, ticks, imageStore);
		
		return zom;
	}
	
	public static Birdie createBirdie(WorldModel world, Point pt, long ticks, Map<String, List<PImage>> imageStore)
	{
		Birdie bird = new Birdie(pt, imageStore.get(Birdie.ID_KEY), RANDOMIZER.nextInt(100) + 300, RANDOMIZER.nextInt(45) + 65);
		scheduleBirdie(world, bird, ticks, imageStore);
		
		return bird;
	}
	
	public static OreBlob createBlob(WorldModel world, Point pt, int rate, long ticks, Map<String, List<PImage>> imageStore)
	{
		OreBlob blob = new OreBlob(pt, imageStore.get(OreBlob.ID_KEY), rate, (RANDOMIZER.nextInt(BLOB_ANIMATION_MAX - BLOB_ANIMATION_MIN) + BLOB_ANIMATION_MIN) * BLOB_ANIMATION_RATE_SCALE);
		
		scheduleBlob(world, blob, ticks, imageStore);
		
		return blob;
	}
	
	public static Ore createOre(WorldModel world, Point pt, long ticks, Map<String, List<PImage>> imageStore)
	{
		Ore ore = new Ore(pt, imageStore.get(Ore.ID_KEY).get(0), (RANDOMIZER.nextInt(ORE_CORRUPT_MAX - ORE_CORRUPT_MIN) + ORE_CORRUPT_MIN));
		scheduleOre(world, ore, ticks, imageStore);
		
		return ore;
	}
	
	public static Quake createQuake(WorldModel world, Point pt, long ticks, Map<String, List<PImage>> imageStore)
	{
		Quake quake = new Quake(pt, imageStore.get(Quake.ID_KEY));
		scheduleQuake(world, quake, ticks);
		
		return quake;
	}
	
	public static Vein createVein(WorldModel world, Point pt, long ticks, Map<String, List<PImage>> imageStore)
	{
		Vein vein = new Vein(pt, imageStore.get(Vein.ID_KEY).get(0), RANDOMIZER.nextInt(VEIN_RATE_MAX - VEIN_RATE_MIN) + VEIN_RATE_MIN, 1);
		scheduleVein(world, vein, ticks, imageStore);
		
		return vein;
	}
	
	
	/* Scheduling the entities */
	
	public static void scheduleZombie(WorldModel world, Zombie zom, long ticks, Map<String, List<PImage>> imageStore)
	{
		scheduleAction(world, zom, createZombieAction(world, imageStore, zom), ticks + (long)zom.getActionRate());
		scheduleAnimation(world, zom);
	}
	
	public static void scheduleBirdie(WorldModel world, Birdie bird, long ticks, Map<String, List<PImage>> imageStore)
	{
		scheduleAction(world, bird, createBirdieAction(world, imageStore, bird), ticks + (long)bird.getActionRate());
		scheduleAnimation(world, bird);
	}
	
	public static void scheduleBlob(WorldModel world, OreBlob blob, long ticks, Map<String, List<PImage>> imageStore)
	{
		scheduleAction(world, blob, createOreBlobAction(world, imageStore, blob), ticks + (long)blob.getActionRate());
		scheduleAnimation(world, blob);
	}
	
	public static void scheduleMiner(WorldModel world, Miner miner, long ticks, Map<String, List<PImage>> imageStore)
	{
		scheduleAction(world, miner, createMinerAction(world, miner, imageStore), ticks + (long)miner.getActionRate());
		scheduleAnimation(world, miner);
	}
	
	public static void scheduleOre(WorldModel world, Ore ore, long ticks, Map<String, List<PImage>> imageStore)
	{
		scheduleAction(world, ore, createOreTransformAction(world, ore, imageStore), ticks + (long)ore.getActionRate());
	}
	
	public static void scheduleQuake(WorldModel world, Quake quake, long ticks)
	{
		scheduleAnimation(world, quake, QUAKE_STEPS);
		scheduleAction(world, quake, createEntityDeathAction(world, quake), ticks + (long)QUAKE_DURATION);
	}
	
	public static void scheduleVein(WorldModel world, Vein vein, long ticks, Map<String, List<PImage>> imageStore)
	{
		scheduleAction(world, vein, createVeinAction(world, vein, imageStore), ticks + (long)vein.getActionRate());
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
	
	
	/* Helper functions */
	
	private static Point blobToVein(WorldModel world, OreBlob blob, Vein vein)
	{
		if (vein == null) return null;
		Point start = blob.getPosition();
		Point finish = vein.getPosition();
		
		blob.buildPath(world, finish);
		if (adjacent(start, finish))
		{
			removeEntity(world, vein);
			return finish;
		}
		else
		{
			Point newPt = blob.nextPosition();
			Entity oldEntity = world.getTileOccupant(newPt);
			if (oldEntity instanceof Ore)
				removeEntity(world, (Actor)oldEntity);
			world.moveEntity(blob, newPt);
			return null;
		}
	}
	
	private static Point birdieToMiner(WorldModel world, Birdie bird, Miner mack)
	{
		if (mack == null) return null;
		Point start = bird.getPosition();
		Point finish = mack.getPosition();
		
		bird.buildPath(world, finish);
		if (start.equals(finish))
		{
			removeEntity(world, (Actor)mack);
			return finish;
		}
		else
		{
			Point nextPoint = bird.nextPosition();
			if (!world.isBirdieAt(nextPoint))
				world.moveEntity(bird, nextPoint);
		}
		return null;
	}
	
	private static Point birdieToSpawn(WorldModel world, Birdie bird)
	{
		Point start = bird.getPosition();
		Point finish = bird.getSpawnPoint();
		bird.buildPath(world, finish);
		
		if (start.equals(finish))
		{
			removeEntity(world, (Actor)bird);
			return finish;
		}
		else
		{
			Point nextPoint = bird.nextPosition();
			if (!world.isBirdieAt(nextPoint))
				world.moveEntity(bird, nextPoint);
		}
		return null;
	}
	
	private static Point zombieToBlob(WorldModel world, Zombie zom, OreBlob blobby)
	{
		if (blobby == null) return null;
		Point start = zom.getPosition();
		Point finish = blobby.getPosition();
		
		zom.buildPath(world, finish);
		if (adjacent(start, finish))
		{
			removeEntity(world, (Actor)blobby);
			return finish;
		}
		else
		{
			Point nextPoint = zom.nextPosition();
			if (!world.isOccupied(nextPoint))
				world.moveEntity(zom, nextPoint);
		}
		return null;
	}
	
	private static void minerToTarget(WorldModel world, Miner miner, Entity target)
	{
		if (target == null) return;
		Point start = miner.getPosition();
		Point finish = target.getPosition();
		
		miner.buildPath(world, finish);
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
			Point nextPoint = miner.nextPosition();
			world.moveEntity(miner, nextPoint);
		}
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
	
	private static boolean adjacent(Point p1, Point p2)
	{
		return ((p1.getXCoord() == p2.getXCoord() && abs(p1.getYCoord() - p2.getYCoord()) == 1)
				|| (p1.getYCoord() == p2.getYCoord() && abs(p1.getXCoord() - p2.getXCoord()) == 1));
	}
}