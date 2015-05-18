public class SaveLoad
{
	public static void loadWorld(WorldModel world, Map<String, List<PImage>> images, String file, boolean run)
	{
		Scanner in = new Scanner(new FileInputStream(file));
		while(in.hasNextLine())
		{
			String[] properties = in.nextLine().split("\\s");
			if (properties.length > 0)
			{
				if (properties[0] == "background")
					addBackground(world, properties, images);
				else
					addEntity(world, properties, images, run);
			}
		}
	}
	
	/** loadWorld with a default value of false for `run` */
	public static void loadWorld(WorldModel world, Map<String, List<PImage>> images, String file)
	{
		loadWorld(world, images, file, false);
	}
	
	public void addBackground(WorldModel world, String[] properties, Map<String, String> iStore)
	{
		if (properties.length >= 4)
		{
			int x = Integer.parseInt(properties[2]);
			int y = Integer.parseInt(properties[3]);
			String name = properties[1];
			world.setBackground(new Point(x, y), new Background(name, iStore.get(name).get(0)));
		}
	}
	
	public void addEntity(WorldModel world, String[] properties, Map<String, List<PImage>> iStore, boolean run)
	{
		Entity newEntity = createFromProperties(properties, iStore);
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
			if (key.equals(Miner.ID_KEY))
				return createMiner(properties, iStore);
			else if (key.equals(Vein.ID_KEY))
				return createVein(properties, iStore);
			else if (key.equals(Ore.ID_KEY))
				return createOre(properties, iStore);
			else if (key.equals(Blacksmith.ID_KEY))
				return createBlacksmith(properties, iStore);
			else if (key.equals(Obstacle.ID_KEY))
				return createObstacle(properties, iStore);
		}
		return null;
	}
	
	private static Point getEntityPoint(String[] properties)
	{
		int x = Integer.parseInt(properties[1]);
		int y = Integer.parseInt(properties[2]);
		return new Point(x, y);
	}
	
	public Entity createMiner(String[] properties, Map<String, List<PImage>> iStore)
	{
		if (properties.length == 7)
		{
			Point p = getEntityPoint(properties);
			Entity miner = new Miner(p, (int)properties[MINER_LIMIT], 0);
			
			return miner;
		}
		return None;
	}
	
	public Entity createVein(String[] properties, Map<String, List<PImage>> iStore)
	{
		if (properties.length == VEIN_NUM_PROPERTIES)
		{
			Entity vein = new Vein(new Point((int)properties[VEIN_COL], (int)properties[VEIN_ROW]), (int)properties[VEIN_REACH]);
			
			return vein;
		}
		return None;
	}
	
	public Entity createOre(String[] properties, Map<String, List<PImage>> iStore)
	{
		if (properties.length == ORE_NUM_PROPERTIES)
		{
			Entity ore = new Ore(new Point((int)properties[ORE_COL], (int)properties[ORE_ROW]));
			
			return ore;
		}
		return None;
	}
	
	public Entity createBlacksmith(String[] properties, Map<String, List<PImage>> iStore)
	{
		if (properties.length == SMITH_NUM_PROPERTIES)
		{
			Entity smith = new Blacksmith(new Point((int)properties[SMITH_COL], (int)properties[SMITH_ROW]), (int)properties[SMITH_LIMIT], 0, (int)properties[SMITH_REACH]);
		
			return smith;
		}
		return null;
	}
	
	public Entity createObstacle(String[] properties, Map<String, List<PImage>> iStore)
	{
		if (properties.length == OBSTACLE_NUM_PROPERTIES)
		{
			Entity obs = new Obstacle(new Point((int)properties[OBSTACLE_COL], (int)properties[OBSTACLE_ROW]));
			
			return obs;
		}
		return null;
	}
	
	public static void scheduleEntity(WorldModel world, Entity entity, Map<String, List<PImage>> iStore)
	{
		if (entity instanceof Miner) //Change below later!
			Actions.scheduleMiner(world, entity, 0, iStore);
		else if (entity instanceof Vein)
			Actions.scheduleVein(world, entity, 0, iStore);
		else if (entity instanceof Ore)
			Actions.scheduleOre(world, entity, 0, iStore);
	}			
	
}