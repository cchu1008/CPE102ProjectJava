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
	
	public Miner createMiner(String[] properties, Map<String, List<PImage>> iStore)
	{
		if (properties.length == 7)
		{
			Point p = getEntityPoint(properties);
			int actRate = Integer.parseInt(properties[3]);
			int animRate = Integer.parseInt(properties[4]);
			int rLim = Integer.parseInt(properties[5]);
			int rCount = Integer.parseInt(properties[6]);
			return new Miner(p, iStore.get(Miner.ID_KEY), actRate, animRate, rLim, rCount);
		}
		return null;
	}
	
	public Vein createVein(String[] properties, Map<String, List<PImage>> iStore)
	{
		if (properties.length == 5)
		{
			Point p = getEntityPoint(properties);
			int actRate = Integer.parseInt(properties[3]);
			int rDist = Integer.parseInt(properties[4]);
			return new Vein(p, actRate, rDist);
		}
		return null;
	}
	
	public Ore createOre(String[] properties, Map<String, List<PImage>> iStore)
	{
		if (properties.length == 4)
		{
			Point p = getEntityPoint(properties);
			int actRate = Integer.parseInt(properties[3]);
			return new Ore(p, actRate);
		}
		return null;
	}
	
	public Blacksmith createBlacksmith(String[] properties, Map<String, List<PImage>> iStore)
	{
		if (properties.length == 3)
		{
			return new Blacksmith(getEntityPoint(properties));
		}
		return null;
	}
	
	public Obstacle createObstacle(String[] properties, Map<String, List<PImage>> iStore)
	{
		if (properties.length == 3)
		{
			return new Obstacle(getEntityPoint(properties));
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