public class SaveLoad
{
	private static final int PROPERTY_KEY = 0;
	
	private static final String BGND_KEY = 'background';
	private static final int BGND_NUM_PROPERTIES = 4;
	private static final int BGND_NAME = 1;
	private static final int BGND_COL = 2;
	private static final int BGND_ROW = 3;
	
	private static final String MINER_KEY = 'miner';
	private static final int MINER_NUM_PROPERTIES = 7;
	private static final int MINER_NAME = 1;
	private static final int MINER_LIMIT = 4;
	private static final int MOINER_COL = 2;
	private static final int MINER_RATE = 5;
	private static final int MINER_ANIMATION_RATE = 6;
	
	private static final String OBSTACLE_KEY = 'obstacle';
	private static final int OBSTACLE_NUM_PROPERTIES = 4;
	private static final int OBSTACLE_NAME = 1;
	private static final int OBSTACLE_COL = 2;
	private static final int OBSTACLE_ROW = 3;
	
	private static final String ORE_KEY = 'ore';
	private static final int ORE_NUM_PROPERTIES = 5;
	private static final int ORE_NAME = 1;
	private static final int ORE_COL = 2;
	private static final int ORE_ROW = 3;
	private static final int ORE_RATE = 4;
	
	private static final String SMITH_KEY = 'blacksmith';
	private static final int SMITH_NUM_PROPERTIES = 7;
	private static final int SMITH_NAME = 1;
	private static final int SMITH_COL = 2;
	private static final int SMITH_ROW = 3;
	private static final int SMITH_LIMIT = 4;
	private static final int SMITH_RATE = 5;
	private static final int SMITH_REACH = 6;
	
	private static final String VEIN_KEY = 'vein';
	private static final int VEIN_NUM_PROPERTIES = 6;
	private static final int VEING_NAME = 1;
	private static final int VEIN_RATE = 4;
	private static final int VEIN_COL = 2;
	private static final int VEIN_ROW = 3;
	private static final int VEIN_REACH = 5;
	
	public static void loadWorld(WorldModel world, Map<String, List<PImage>> images, String file, boolean run)
	{
		Scanner in = new Scanner(new FileInputStream(file));
		while(in.hasNextLine())
		{
			String[] properties = in.nextLine().split("\\s");
			if (properties.length > 0)
			{
				if (properties[PROPERTY_KEY] == BGND_KEY)
					addBackground(world, properties, images);
				else
					addEntity(world, properties, images, run);
			}
		}
	}
	
	/** loadWorld with a default value for `run` */
	public static void loadWorld(WorldModel world, Map<String, List<PImage>> images, String file)
	{
		loacWorld(world, images, file, false);
	}
	
	public void addBackground(WorldModel world, String[] properties, Map<String, String> iStore)
	{
		if (properties.length >= BGND_NUM_PROPERTIES)
		{
			Point pt = new Point((int)properties[BGND_COL], (int)properties[BGND_ROW]);
			String name = properties[BGND_NAME];
			world.setBackground(pt, new Background(name, ImageStore.getImages(iStore, name)));
		}
	}
	
	public void addEntity(WorldModel world, String[] properties, Map<String, List<PImage>> iStore, boolean run)
	{
		Entity newEntity = createFromProperties(properties, iStore);
		//following if statement could use work...
		if (newEntity != null)
		{
			world.addEntity(newEntity);
			if (run)
				scheduleEntity(world, newEntity, iStore);
		}
	}
	
	public Entity createFromProperties(String[] properties, Map<String, List<PImage>> iStore)
	{
		int key = properties[PROPERTY_KEY];
		if (properties.length > 0)
		{
			if (key == MINER_KEY)
				return createMiner(properties, iStore);
			else if (key == VEIN_KEY)
				return createVein(properties, iStore);
			else if (key == ORE_KEY)
				return createOre(properties, iStore);
			else if (key == SMITH_KEY)
				return createBlacksmith(properties, iStore);
			else if (key == OBSTACLE_KEY)
				return createObstacle(properties, iStore);
		}
		return None;
	}
	
	public Entity createMiner(String[] properties, Map<String, String> iStore)
	{
		if (properties.length == MINER_NUM_PROPERTIES)
		{
			Entity miner = new Miner(new Point((int)properties[MINER_COL], (int)properties[MINER_ROW]), (int)properties[MINER_LIMIT], 0);
			
			return miner;
		}
		return None;
	}
	
	public Entity createVein(String[] properties, Map<String, String> iStore)
	{
		if (properties.length == VEIN_NUM_PROPERTIES)
		{
			Entity vein = new Vein(new Point((int)properties[VEIN_COL], (int)properties[VEIN_ROW]), (int)properties[VEIN_REACH]);
			
			return vein;
		}
		return None;
	}
	
	public Entity createOre(String[] properties, Map<String, String> iStore)
	{
		if (properties.length == ORE_NUM_PROPERTIES)
		{
			Entity ore = new Ore(new Point((int)properties[ORE_COL], (int)properties[ORE_ROW]));
			
			return ore;
		}
		return None;
	}
	
	public Entity createBlacksmith(String[] properties, Map<String, String> iStore)
	{
		if (properties.length == SMITH_NUM_PROPERTIES)
		{
			Entity smith = new Blacksmith(new Point((int)properties[SMITH_COL], (int)properties[SMITH_ROW]), (int)properties[SMITH_LIMIT], 0, (int)properties[SMITH_REACH]);
		
			return smith;
		}
		return None;
	}
	
	public Entity createObstacle(String[] properties, Map<String, String> iStore)
	{
		if (properties.length == OBSTACLE_NUM_PROPERTIES)
		{
			Entity obs = new Obstacle(new Point((int)properties[OBSTACLE_COL], (int)properties[OBSTACLE_ROW]));
			
			return obs;
		}
		return None;			
	}
	
	public static void scheduleEntity(WorldModel world, Entity entity, Map<String, String> iStore)
	{
		if (entity instanceof Miner && entity.getResourceCount() == 0) //Change below later!
			scheduleMiner(world, entity, 0, iStore);
		else if (entity instanceof Vein)
			scheduleVein(world, entity, 0, iStore);
		else if (entity instanceof Ore)
			scheduleOre(world, entity, 0, iStore);
	}			
	
}