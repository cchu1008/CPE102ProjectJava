import java.lang.Math;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Comparator;
import java.util.function.*;
import processing.core.*;

public class WorldModel
{
	private int width;
	private int height;
	private Entity[][] occupancies;
	private List<Entity> entities;
	private List<Birdie> birdies;
	private Background[][] backgrounds;
	private OrderedList actionQueue;
	private WorldView view;
	
	public WorldModel(int width, int height, Background back, WorldView view)
	{
		this.width = width;
		this.height = height;
		this.entities = new LinkedList<Entity>();
		this.birdies = new LinkedList<Birdie>();
		this.backgrounds = new Background[width][height];
		this.actionQueue = new OrderedList();
		this.view = view;
		
		for (int x = 0; x < width; x++)
		{
			for (int y = 0; y < height; y++)
			{
				this.backgrounds[x][y] = back;
			}
		}
	}
	
	public int getWidth()
	{
		return this.width;
	}
	
	public int getHeight()
	{
		return this.height;
	}
	
	public WorldView getWorldView()
	{
		return this.view;
	}
	
	public List<Entity> getEntities()
	{
		return this.entities;
	}
	
	public List<Birdie> getBirdies()
	{
		return this.birdies;
	}
	
	public boolean isOccupied(Point position)
	{
		if (this.withinBounds(position))
		{
			for (Entity ent : this.entities)
			{
				if (!(ent instanceof Birdie) && ent.getPosition().equals(position))
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public Entity getTileOccupant(Point p)
	{
		for (Entity ent : this.entities)
		{
			if (ent.getPosition().equals(p))
			{
				return ent;
			}
		}
		return null;
	}
	
	public List<Entity> getAllEntitiesAt(Point p)
	{
		List<Entity> entList = new LinkedList<Entity>();
		for (Entity ent : this.entities)
		{
			if (ent.getPosition().equals(p))
			{
				entList.add(ent);
			}
		}
		for (Birdie bird : this.birdies)
		{
			if (bird.getPosition().equals(p))
			{
				entList.add(bird);
			}
		}
		return entList;
	}
	
	public boolean isBirdieAt(Point pos)
	{
		for (Birdie bird : this.birdies)
		{
			if (bird.getPosition().equals(pos))
			{
				return true;
			}
		}
		return false;
	}
	
	public Background getBackground(Point p)
	{
		if (this.withinBounds(p))
		{
			return this.backgrounds[p.getXCoord()][p.getYCoord()];
		}
		return null;
	}
	
	public PImage getBackgroundImage(Point p)
	{
		Background b = this.getBackground(p);
		if (b != null)
		{
			return b.getImage();
		}
		return null;
	}
	
	public void setBackground(Point p, Background b)
	{
		if (this.withinBounds(p))
		{
			this.backgrounds[p.getXCoord()][p.getYCoord()] = b;
		}
	}
	
	public void setBackgroundImage(Point p, PImage i)
	{
		if (this.withinBounds(p))
		{
			this.backgrounds[p.getXCoord()][p.getYCoord()].setImage(i);
		}
	}
	
	public boolean withinBounds(Point position)
	{
		int x = position.getXCoord();
		int y = position.getYCoord();
		return x >= 0 && x < this.width && y >= 0 && y < this.height;
	}
	
	public void addEntity(Entity ent)
	{
		if (ent instanceof Birdie)
			this.birdies.add((Birdie)ent);
		else
			this.entities.add(ent);
	}
	
	public void moveEntity(Entity ent, Point newPoint)
	{
		ent.setPosition(newPoint);
	}
	
	public void removeEntity(Entity ent)
	{
		if (ent instanceof Birdie)
			this.birdies.remove((Birdie)ent);
		else
			this.entities.remove(ent);
	}
	
	public void removeEntityAt(Point pos)
	{
		Entity old = this.getTileOccupant(pos);
		if (old != null)
			this.removeEntity(old);
	}
	
	public void scheduleAction(LongConsumer action, long time)
	{
		actionQueue.insert(action, time);
	}
	
	public void unscheduleAction(LongConsumer action)
	{
		actionQueue.remove(action);
	}
	
	@SuppressWarnings("unchecked")
	public Entity findNearest(Point pos, Class type)
	{
		Entity closestEntity = null;
		
		if (this.entities.size() >= 1)
		{
			double closestDistance = -1.0D;
			
			for (Entity ent : this.entities)
			{
				if (type.isInstance(ent))
				{
					double dist = this.distance(ent.getPosition(), pos);
					if (dist < closestDistance || closestDistance < 0)
					{
						closestEntity = ent;
						closestDistance = dist;
					}
				}
			}
		}
		
		return closestEntity;
	}
	
	public void updateOnTime(long ticks)
	{
		ListItem next = actionQueue.head();
		while (next != null && next.getTime() < ticks)
		{
			actionQueue.pop();
			next.getAction().accept(ticks);
			next = actionQueue.head();
		}
	}
	
	public static double distance(Point pos1, Point pos2)
	{
		int dx = pos1.getXCoord() - pos2.getXCoord();
		int dy = pos1.getYCoord() - pos2.getYCoord();
		return Math.sqrt((double)((dx * dx) + (dy * dy)));
	}
}
