import java.lang.Math;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Comparator;

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
	
	public List getEntities()
	{
		return this.entities;
	}
	
	public boolean isOccupied(Point position)
	{
		return this.withinBounds(position) && (this.getCell(position) != null);
	}
	
	private Entity getCell(Point p)
	{
		if (this.withinBounds(p))
		{
			return this.occupancies[p.getXCoord()][p.getYCoord()];
		}
		return null;
	}
	
	private void setCell(Point p, Entity ent)
	{
		if (this.withinBounds(p))
		{
			this.occupancies[p.getXCoord()][p.getYCoord()] = ent;
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
	
	@SuppressWarnings("unchecked")
	public Entity findNearest(Point pos, Class type)
	{
		if (this.entities.size() == 0)
		{
			return null;
		}
		
		Entity closestEntity = this.entities.get(0);
		
		if (this.entities.size() > 1)
		{
			double closestDistance = this.distance(closestEntity.getPosition(), pos);
			
			for (Entity ent : this.entities.subList(1, this.entities.size()))
			{
				if (type.isInstance(ent))
				{
					double dist = this.distance(ent.getPosition(), pos);
					if (dist < closestDistance)
					{
						closestEntity = ent;
						closestDistance = dist;
					}
				}
			}
		}
		
		return closestEntity;
	}
	
	private static double distance(Point pos1, Point pos2)
	{
		int dx = pos1.getXCoord() - pos2.getXCoord();
		int dy = pos1.getYCoord() - pos2.getYCoord();
		return Math.sqrt((double)((dx * dx) + (dy * dy)));
	}
}



/*import entities
import pygame
import ordered_list
import actions
import occ_grid
import point

public class WorldModel
{
	private int num_rows;
	private int num_cols;
	private Grid background;
	
	public WorldModel(this, int num_rows, int num_cols, Grid background)
	{
		this.background = new Grid(num_cols, num_rows, background);
		this.num_rows = num_rows;
		this.num_cols = num_cols;
		this.occupancy = new Grid(num_cols, num_rows, None);
		this.entities = new Entity[];
		this.action_queue = new OrderedList();
	}


	public boolean within_bounds(Point pt)
	{
		return (pt.x >= 0 && pt.x < this.num_cols &&
			pt.y >= 0 && pt.y < this.num_rows);
	}

	public boolean is_occupied(Point pt)
	{
		return (this.within_bounds(pt) &&
			this.occupancy.get_cell(pt) != None);
	}
			
	public Entity find_nearest(Point pt, ***type)
	{
		oftype = [(e, distance_sq(pt, e.get_position()))
			for e in this.entities if isinstance(e, type)];
			
		return nearest_entity(oftype);
	}

	public void add_entity(Entity entity)
	{
		Point pt = entity.get_position();
		if (this.within_bounds(pt))
	  {
			Entity old_entity = this.occupancy.get_cell(pt);
			if (old_entity != None)
		 {
				old_entity.clear_entity_pending_actions();
		 }
			this.occupancy.set_cell(pt, entity);
			this.entities.append(entity);
	  }
	}

	public Point[] move_entity(Entity entity, Point pt)
	{
		***Point[] tiles = new Point[];
		if (this.within_bounds(pt))
	  {
			Point old_pt = entity.get_position();
			this.occupancy.set_cell(old_pt, None);
			tiles.append(old_pt);
			this.occupancy.set_cell(pt, entity);
			tiles.append(pt);
			entity.set_position(pt);
	  }

		return tiles;
	}
		
	public void remove_entity(Entity entity)
	{
		this.remove_entity_at(entity.get_position());
	}

	public void remove_entity_at(Point pt)
	{
		if ((this.within_bounds(pt) && 
			 this.occupancy.get_cell(pt) != None))
	  {
			Entity entity = this.occupancy.get_cell(pt);
			entity.set_position(point.Point(-1, -1));
			this.entities.remove(entity);
			this.occupancy.set_cell(pt, None);
	  }
	}

	def schedule_action(this, action, time):
		this.action_queue.insert(action, time)
			
	def unschedule_action(this, action):
		this.action_queue.remove(action)
		
	public Point[] update_on_time(int ticks)
	{
		***Point[] tiles = new Point[];

		***next = this.action_queue.head();
		while (next && next.ord < ticks)
	  {
			this.action_queue.pop();
			tiles.extend(next.item(ticks));  # invoke action function
			***next = this.action_queue.head();
	  }

		return tiles;
	}
		
	public Image get_background_image(Point pt)
	{
		if (this.within_bounds(pt))
	  {
			return this.background.get_cell(pt).get_image();
	  }
	}
		
	public *** get_background(Point pt)
	{
		if (this.within_bounds(pt))
	  {
			return this.background.get_cell(pt);
	  }
	}
			
	public void set_background(Point pt, ***bgnd)
	{
		if (this.within_bounds(pt))
	  {
			this.background.set_cell(pt, ***bgnd);
	  }
	}
			
	public Entity get_tile_occupant(Point pt)
	{
		if (this.within_bounds(pt))
	  {
			return this.occupancy.get_cell(pt);
	  }
	}
			
	public Entity[] get_entities()
	{
		return this.entities;
	}
}   
		
def nearest_entity(entity_dists):
	if len(entity_dists) > 0:
		pair = entity_dists[0]
		for other in entity_dists:
			if other[1] < pair[1]:
				pair = other
		nearest = pair[0]
	else:
		nearest = None

	return nearest

def distance_sq(p1, p2):
	return (p1.x - p2.x)**2 + (p1.y - p2.y)**2
/* */
