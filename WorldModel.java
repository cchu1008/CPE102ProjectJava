import java.util.List;
import java.util.ArrayList;

public class WorldModel
{
	private int numRows;
	private int numCols;
	private Entity[][] occupancy;
	private List<Entity> entities;
	
	public WorldModel(int rows, int cols)
	{
		this.numRows = rows;
		this.numCols = cols;
		this.occupancy = new Entity[cols][rows];
		this.entities = new ArrayList<Entity>();
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
