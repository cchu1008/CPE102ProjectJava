import point
import actions

public class Background
{
   private int current_img;
   private String name;
   private int[] imgs;

   public Background(String name, int[] imgs)
   {
      this.name = name;
      this.imgs = imgs;
      this.current_img = 0
   }
      
   public String get_name()
   {
      return this.name;
   }
      
   public int[] get_images()
   {
      return this.imgs;
   }

   public int get_image()
   {
      return this.imgs[this.current_img];
   }
      
   public void next_image()
   {
      this.current_img = (this.current_img + 1) % len(this.imgs);
   }

   public String entity_string()
   {
      return 'unknown';
   }
}
      
public class Entity
{
	private String name;
	private Point position;
	private int[] imgs;
	private int current_img;
	
   public Entity(this, String name, Point position, int[] imgs)
   {
      this.name = name;
      this.position = position;
      this.imgs = imgs;
      this.current_img = 0;
   }
      
   public String get_name()
   {
      return this.name;
   }
      
   public void set_position(this, Point point)
   {
      this.position = point;
   }

   public Point get_position()
   {
      return this.position;
   }
      
   public int[] get_images()
   {
      return this.imgs;
   }

   public int get_image()
   {
      return this.imgs[this.current_img];
   }
      
   public void next_image()
   {
      this.current_img = (this.current_img + 1) % len(this.imgs);
   }
}
      
public class Pending_Actions
   extends Entity
{
	private String name;
	private Point position;
	private int[] imgs;
	private int current_img;
	private Actions[] pending_actions;
	
   public Pending_Actions(this, String name, Point position, int[] imgs)
   {
      super(name, position, imgs);
      this.pending_actions = new Actions[];
   }
      
   public void remove_pending_action(Actions action)
   {
      this.pending_actions.remove(action);
   }

   public void add_pending_action(Actions action)
   {
      this.pending_actions.append(action);
   }

   public Actions[] get_pending_actions()
   {
      return this.pending_actions;
   }

   public void clear_entity_pending_actions()
   {
      this.pending_actions = new Actions[];
   }

   public void clear_pending_actions(World world)
   {
      for action in this.get_pending_actions()
	  {
         world.unschedule_action(action);
	  }
      this.clear_entity_pending_actions();
   }
}
      

public class Miner
   extends Pending_Actions
{
	private String name;
	private int resource_limit;
	private Point position;
	private double rate;
	private int[] imgs;
	private double animation_rate;
	private int current_img;
	private int resource_count;
	
   public Miner(this, String name, int resource_limit, Point position, double rate, int[] imgs, double animation_rate)
   {
      super(name, position, imgs);
      this.resource_limit = resource_limit;
      this.rate = rate;
      this.animation_rate = animation_rate;
   }
      
   public double get_rate()
   {
      return this.rate;
   }
      
   public double get_animation_rate()
   {
      return this.animation_rate;
   }
      
   public int get_resource_count()
   {
      return this.resource_count;
   }

   public void set_resource_count(int n)
   {
      this.resource_count = n;
   }
      
   public int get_resource_limit()
   {
      return this.resource_limit;
   }
}
      

public class MinerNotFull
   extends Miner
{
	private String name;
	private int resource_limit;
	private Point position;
	private double rate;
	private int[] imgs;
	private double animation_rate;
	private int current_img;
	private int resource_count;
	
   public MinerNotFull(this, String name, int resource_limit, Point position, 
      double rate, int[] imgs, double animation_rate)
   {
      super(name, resource_limit, position, rate, imgs, animation_rate);
      this.resource_count = 0;
   }
      
   public String entity_string()
   {
      return ' '.join(['miner', this.name, str(this.position.x),
         str(this.position.y), str(this.resource_limit),
         str(this.rate), str(this.animation_rate)]);
   }
         
***public boolean miner_to_ore(World world, Entity ore)
   {
      Point entity_pt = this.position
      if(not ore)
	  {
         return ([entity_pt], False);
	  }
      Point ore_pt = ore.get_position()
      if (adjacent(entity_pt, ore_pt))
	  {
         this.set_resource_count(1 + this.resource_count);
         actions.remove_entity(world, ore);
         return ([ore_pt], True);
	  }
      else
	  {
         Point new_pt = next_position(world, entity_pt, ore_pt);
         return (world.move_entity(this, new_pt), False);
	  }
   }
         
   public Actions create_miner_not_full_action(World world, ***int[] i_store)
   {
	   private int current_ticks;
	   private Entity ore;
	   private Entity new_entity;
	   private Point entity_pt;
	   
      private int action(int current_ticks)
	  {
         this.remove_pending_action(action);

         entity_pt = this.position;
         ore = world.find_nearest(entity_pt, Ore);
         (tiles, found) = this.miner_to_ore(world, ore);

         new_entity = this;
         if (found)
		 {
            new_entity = try_transform_miner(world, this,
               try_transform_miner_not_full);
		 }

         actions.schedule_action(world, new_entity,
            new_entity.create_miner_action(world, i_store),
            current_ticks + new_entity.get_rate());
         return tiles;
	  }
      return action;
   }
      
   public Actions create_miner_action(World world, ***int[] image_store)
   {
      return this.create_miner_not_full_action(world, image_store);
   }
}

      
public class MinerFull
   extends Miner
{
	private String name;
	private int resource_limit;
	private Point position;
	private double rate;
	private int[] imgs;
	private int current_img;
	private double animation_rate;
	private int resource_count;
	
   public MinerFull(this, String name, int resource_limit, Point position, 
      double rate, int[] imgs, double animation_rate)
   {
      super(name, resource_limit, position, rate, imgs, animation_rate);
      this.resource_count = 2;
   }
      
   public String entity_string()
   {
      return 'unknown';
   }
      
***public boolean miner_to_smith(World world, Entity smith)
   {
      Point entity_pt = this.position;
      if (not smith)
	  {
         return ([entity_pt], False);
	  }
      Point smith_pt = smith.get_position();
	  
      if (adjacent(entity_pt, smith_pt))
	  {
         smith.set_resource_count( 
            smith.get_resource_count() +
            this.resource_count);
         this.resource_count = 0;
         return ([], True);
	  }
      else
	  {
         Point new_pt = next_position(world, entity_pt, smith_pt);
         return (world.move_entity(this, new_pt), False);
	  }
   }
         
   public Actions create_miner_full_action(World world, ***int[] i_store)
   {
	   private int current_ticks;
	   private Point entity_pt;
	   private Entity smith;
	   private Entity new_entity;
	   
      private int action(int current_ticks)
	  {
         this.remove_pending_action(action);

         entity_pt = this.position;
         smith = world.find_nearest(entity_pt, Blacksmith);
         (tiles, found) = this.miner_to_smith(world, smith);

         new_entity = this;
         if (found)
		 {
            new_entity = try_transform_miner(world, this,
               try_transform_miner_full);
		 }

         actions.schedule_action(world, new_entity,
            new_entity.create_miner_action(world, i_store),
            current_ticks + new_entity.get_rate());
         return tiles;
	  }
      return action;
   }
      
   public Actions create_miner_action(World world, ***int[] image_store))
   {
      return this.create_miner_full_action(world, image_store);
   }
}


public class Vein(Pending_Actions):
   def __init__(this, name, rate, position, imgs, resource_distance=1):
      super(Vein, this).__init__(name, position, imgs)
      this.rate = rate
      this.resource_distance = resource_distance

   def get_rate(this):
      return this.rate

   def get_resource_distance(this):
      return this.resource_distance

   def entity_string(this):
      return ' '.join(['vein', this.name, str(this.position.x),
         str(this.position.y), str(this.rate),
         str(this.resource_distance)])
         
   def create_vein_action(this, world, i_store):
      def action(current_ticks):
         this.remove_pending_action(action)

         open_pt = find_open_around(world, this.position,
            this.resource_distance)
         if open_pt:
            ore = actions.create_ore(world,
               "ore - " + this.name + " - " + str(current_ticks),
               open_pt, current_ticks, i_store)
            world.add_entity(ore)
            tiles = [open_pt]
         else:
            tiles = []

         actions.schedule_action(world, this,
            this.create_vein_action(world, i_store),
            current_ticks + this.rate)
         return tiles
      return action
      
public class Ore(Pending_Actions):
   def __init__(this, name, position, imgs, rate=5000):
      super(Ore, this).__init__(name, position, imgs)
      this.rate = rate

   def get_rate(this):
      return this.rate

   def entity_string(this):
      return ' '.join(['ore', this.name, str(this.position.x),
         str(this.position.y), str(this.rate)])
      
public class Blacksmith(Pending_Actions):
   def __init__(this, name, position, imgs, resource_limit, rate,
      resource_distance=1):
      super(Blacksmith, this).__init__(name, position, imgs)
      this.resource_limit = resource_limit
      this.resource_count = 0
      this.rate = rate
      this.resource_distance = resource_distance

   def get_rate(this):
      return this.rate

   def get_resource_distance(this):
      return this.resource_distance
      
   def get_resource_count(this):
      return this.resource_count

   def set_resource_count(this, n):
      this.resource_count = n
      
   def get_resource_limit(this):
      return this.resource_limit

   def entity_string(this):
      return ' '.join(['blacksmith', this.name, str(this.position.x),
         str(this.position.y), str(this.resource_limit),
         str(this.rate), str(this.resource_distance)])

public class Obstacle(Entity):
   def __init__(this, name, position, imgs):
      super(Obstacle, this).__init__(name, position, imgs)

   def entity_string(this):
      return ' '.join(['obstacle', this.name, str(this.position.x),
         str(this.position.y)])

public class OreBlob(Pending_Actions):
   def __init__(this, name, position, rate, imgs, animation_rate):
      super(OreBlob, this).__init__(name, position, imgs)
      this.rate = rate
      this.animation_rate = animation_rate

   def get_rate(this):
      return this.rate

   def get_animation_rate(this):
      return this.animation_rate

   def entity_string(this):
      return 'unknown'
      
   def blob_next_position(this, world, dest_pt):
      horiz = sign(dest_pt.x - this.position.x)
      new_pt = point.Point(this.position.x + horiz, this.position.y)

      if horiz == 0 or (world.is_occupied(new_pt) and
         not isinstance(world.get_tile_occupant(new_pt), Ore)):
         vert = sign(dest_pt.y - this.position.y)
         new_pt = point.Point(this.position.x, this.position.y + vert)

         if vert == 0 or (world.is_occupied(new_pt) and
            not isinstance(world.get_tile_occupant(new_pt), Ore)):
            new_pt = point.Point(this.position.x, this.position.y)

      return new_pt
      
   def blob_to_vein(this, world, vein):
      entity_pt = this.position
      if not vein:
         return ([entity_pt], False)
      vein_pt = vein.get_position()
      if adjacent(entity_pt, vein_pt):
         actions.remove_entity(world, vein)
         return ([vein_pt], True)
      else:
         new_pt = this.blob_next_position(world, vein_pt)
         old_entity = world.get_tile_occupant(new_pt)
         if isinstance(old_entity, Ore):
            actions.remove_entity(world, old_entity)
         return (world.move_entity(this, new_pt), False)
         
   def create_ore_blob_action(this, world, i_store):
      def action(current_ticks):
         this.remove_pending_action(action)

         entity_pt = this.position
         vein = world.find_nearest(entity_pt, Vein)
         (tiles, found) = this.blob_to_vein(world, vein)

         next_time = current_ticks + this.rate
         if found:
            quake = actions.create_quake(world, tiles[0], current_ticks, i_store)
            world.add_entity(quake)
            next_time = current_ticks + this.rate * 2

         actions.schedule_action(world, this,
            this.create_ore_blob_action(world, i_store),
            next_time)

         return tiles
      return action
      
public class Quake(Pending_Actions):
   def __init__(this, name, position, imgs, animation_rate):
      super(Quake, this).__init__(name, position, imgs)
      this.animation_rate = animation_rate

   def get_animation_rate(this):
      return this.animation_rate

   def entity_string(this):
      return 'unknown'
      
      
def try_transform_miner_full(world, entity):
   new_entity = MinerNotFull(
      entity.get_name(), entity.get_resource_limit(),
      entity.get_position(), entity.get_rate(),
      entity.get_images(), entity.get_animation_rate())

   return new_entity
      
def try_transform_miner_not_full(world, entity):
   if entity.resource_count < entity.resource_limit:
      return entity
   else:
      new_entity = MinerFull(
         entity.get_name(), entity.get_resource_limit(), entity.get_position(),
         entity.get_rate(), entity.get_images(), entity.get_animation_rate())
      return new_entity
      
def try_transform_miner(world, entity, transform):
   new_entity = transform(world, entity)
   if entity != new_entity:
      entity.clear_pending_actions(world)
      world.remove_entity_at(entity.get_position())
      world.add_entity(new_entity)
      actions.schedule_animation(world, new_entity)

   return new_entity

def adjacent(pt1, pt2):
   return ((pt1.x == pt2.x and abs(pt1.y - pt2.y) == 1) or
      (pt1.y == pt2.y and abs(pt1.x - pt2.x) == 1))

def sign(x):
   if x < 0:
      return -1
   elif x > 0:
      return 1
   else:
      return 0

def next_position(world, entity_pt, dest_pt):
   horiz = sign(dest_pt.x - entity_pt.x)
   new_pt = point.Point(entity_pt.x + horiz, entity_pt.y)

   if horiz == 0 or world.is_occupied(new_pt):
      vert = sign(dest_pt.y - entity_pt.y)
      new_pt = point.Point(entity_pt.x, entity_pt.y + vert)

      if vert == 0 or world.is_occupied(new_pt):
         new_pt = point.Point(entity_pt.x, entity_pt.y)

   return new_pt
   
def find_open_around(world, pt, distance):
   for dy in range(-distance, distance + 1):
      for dx in range(-distance, distance + 1):
         new_pt = point.Point(pt.x + dx, pt.y + dy)

         if (world.within_bounds(new_pt) and
            (not world.is_occupied(new_pt))):
            return new_pt

   return None
