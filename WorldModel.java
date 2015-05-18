import java.lang.Math;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Comparator;
import processing.core.*;

public class WorldModel
{
	private int width;
	private int height;
	private Entity[][] occupancies;
	private List<Entity> entities;
	private Background[][] backgrounds;
	
	public WorldModel(int width, int height, Background back)
	{
		this.width = width;
		this.height = height;
		this.occupancies = new Entity[width][height];
		this.entities = new ArrayList<Entity>();
		this.backgrounds = new Background[width][height];
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
	
	public List<Entity> getEntities()
	{
		return this.entities;
	}
	
	public boolean isOccupied(Point position)
	{
		return this.withinBounds(position) && (this.getCell(position) != null);
	}
	
	public Entity getCell(Point p)
	{
		if (this.withinBounds(p))
		{
			return this.occupancies[p.getXCoord()][p.getYCoord()];
		}
		return null;
	}
	
	public void setCell(Point p, Entity ent)
	{
		if (this.withinBounds(p))
		{
			this.occupancies[p.getXCoord()][p.getYCoord()] = ent;
		}
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
		if (this.withinBounds(p))
		{
			return this.backgrounds[p.getXCoord()][p.getYCoord()].getImage();
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
		Point pos = ent.getPosition();
		if (this.withinBounds(pos))
		{
			Entity old = this.getCell(pos);
			if (old != null)
			{
				this.entities.remove(old);
			}
			this.setCell(pos, ent);
			this.entities.add(ent);
		}
	}
	
	public void moveEntity(Entity ent, Point newPoint)
	{
		Point pos = ent.getPosition();
		if (this.withinBounds(newPoint) && this.getCell(pos) == ent)
		{
			this.setCell(pos, null);
			this.setCell(newPoint, ent);
			ent.setPosition(newPoint);
		}
	}
	
	public void removeEntity(Entity ent)
	{
		this.removeEntityAt(ent.getPosition());
	}
	
	public void removeEntityAt(Point pos)
	{
		Entity old = this.getCell(pos);
		if (this.withinBounds(pos) && old != null)
		{
			this.setCell(pos, null);
			this.entities.remove(old);
		}
	}
	
	public static void scheduleAction(List<Point> action, int time)
	{
		actionQueue.add(action, time);
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
	
	public List<Rectangle> updateOnTime(int ticks)
	{
		List<Rectangle> tiles = new ArrayList<Rectangle>();
		
		//Assuming we make an actions class.
		List<Point> next = actionQueue.head();
		//What is next.ord???
		while (next != null && next.ord < ticks)
		{
			actionQueue.pop();
			tiles.add(next.item(ticks));
			next = actionQueue.head();
		}
		
		return tiles;
	}
	
	public static double distance(Point pos1, Point pos2)
	{
		int dx = pos1.getXCoord() - pos2.getXCoord();
		int dy = pos1.getYCoord() - pos2.getYCoord();
		return Math.sqrt((double)((dx * dx) + (dy * dy)));
	}
}
