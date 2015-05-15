

public class WorldView
{
	private int viewCols;
	private int viewRows;
	private WorldModel world;
	private int tileWidth;
	private int tileHeight;
	private Rectangle viewport;
	private int numRows;
	private int numCols;
	private Point mousePt;
	
	public WorldView(int viewCols, int viewRows, ***screen, WorldModel world, int tileWidth, int tileHeight)
	{
		this.viewCols = viewCols;
		this.viewRows = viewRows;
		this.world = world;
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		this.viewport = new Rectangle(0, 0, viewCols, viewRows);
		this.numRows = world.getWidth();
		this.numCols = world.getHeight();
		this.mousePt = new Point(0, 0);
	}
	
	public void drawBackground()
	{
		for (int y = 0; y < viewport.getHeight(); y++)
		{
			for (int x = 0; x < viewport.getWidth(); x++)
			{
				Point wPt = viewportToWorld(new Point(x, y));
				//Work on converting from pygame!
				Image img = this.world.getBackgroundImage(wPt);
				image(img, (x * tileWidth), (y * tileHeight));
			}
		}
	}
	
	public void drawEntities()
	{
		for (Entity entity: world.entities)
		{
			if (viewport.collidepoint(entity.getXCoord(), entity.getYCoord()))
			{
				Point vPt = worldToViewport(new Point(entity.getXCoord(), entity.getYCoord()));
				image(entity.getImage(), vPt.getXCoord(), vPt.getYCoord());
			}
		}
	}
	
	public void drawViewport()
	{
		drawBackground();
		drawEntities();
	}
	
	public void updateView(int[] viewDelta=[0, 0])
	{
		this.viewport = createShiftedViewport(viewDelta, numRows, numCols);
		this.mouseImg = mouseImg;
		drawViewport();
		//Work on translating from pygame
		***update();
	}
	
	public void updateViewTiles(List<Rectangle> tiles)
	{
		List<Rectangle> rects = new ArrayList<Point>();
		//use tileWidth and Height for this
		for (Point tile : tiles)
		{
			if (viewport.collidepoint(tile.getXCoord(), tile.getYCoord()))
			{
				Point vPt = worldToViewport(tile);
				***img = getTileImage(vPt);
				rects.add(updateTile(vPt, img));
				if (mousePt.equals(vPt))
				{
					rects.add(updateMouseCursor());
				}
			}
		}
		//Work on translating from pygame
		***update(rects);
	}
	
	public Rectangle updateTile(Point viewTilePt, ***surface (image))
	{
		int absX = viewTilePt.getXCoord() * tileWidth;
		int absY = viewTilePt.getYCoord() * tileHeight;
		
		image(surface, absX, absY);
		
		return new Rectangle(absX, absY, tileWidth, tileHeight);
	}
	
	public Image getTileImage(Point viewTilePt)
	{
		Point pt = viewportToWorld(viewTilePt);
		Image bgnd = world.getBackgroundImage(pt);
		boolean occupant = world.getTileOccupant(pt);
		
		if (occupant)
		{
			//Work on translating from pygame
			Image img = pygame.Surface((this.tileWidth, this.tileHeight));
			image(bgnd, 0, 0);
			image(occupant.getImage(), 0, 0);
			return img;
		}
		else return bgnd;
	}
	/* Don't need this part yet!
	public Image createMouseSurface(booean occupied)
	{
		//Work on translating from pygame
		Image surface = pygame.Surface((this.tileWidth, this.tileHeight));
		surface.setAlpha(MOUSE_HOVER_ALPH);
		Color color = MOUSE_HOVOR_EMPTY_COLOR;
		
		if (occupied) color = MOUSE_HOVER_OCC_COLOR;
		//Work on translating from pygame
		surface.fill(color);
		if (mouseImg != None) image(mouseImg, 0, 0);
		return surface;
	}
	
	public Point updateMouseCursor()
	{
		return updateTile(mousePt, createMouseSurface(world.isOccupied(viewportToWorld(mousePt))));
	}
	
	public void mouseMove(Point newMousePt)
	{
		List<Rectangle> rects = new ArrayList<Point>();
		rects.add(updateTile(mousePt, getTileImage(mousePt)));
		//Work on changing from pygame
		if (viewport.collidepoint(newMousePt.getXCoord() + viewport.getX(), newMousePt.getYCoord() + viewport.getY())) mousePt = newMousePt;
		rects.add(updateMouseCursor());
		
		pygame.display.update(rects);
	}
	*/
	
	public Point viewportToWorld(Point pt)
	{
		//Work on changing from pygame! (viewport.left and viewport.top)
		return new Point(pt.getXCoord() + viewport.getX(), pt.getYCoord() + viewport.getY());
	}
	
	public Point worldToViewport(Point pt)
	{
		//Work on changing from pygame! (Same as above)
		return new Point(pt.getXCoord() - viewport.getX(), pt.getYCoord() - viewport.getY());
	}
	
	public Rectangle createShiftedViewport(int[] delta, int numRows, int numCols)
	{
		int newX = clamp(viewport.getX() + delta[0], 0, numCols - viewport.getWidth());
		int newY = clamp(viewport.getY() + delta[1], 0, numRows - viewport.getHeight());
		
		return new Rectangle(newX, newY, viewport.getWidth(), viewport.getHeight());
	}
	
	public static int clamp(int v, int low, int high)
	{
		return Math.min(high, Math.max(v, low));
	}
	
}
				

/*
import pygame
import worldmodel
import entities
import point

MOUSE_HOVER_ALPHA = 120
MOUSE_HOVER_EMPTY_COLOR = (0, 255, 0)
MOUSE_HOVER_OCC_COLOR = (255, 0, 0)

class WorldView:
   def __init__(self, view_cols, view_rows, screen, world, tile_width,
      tile_height, mouse_img=None):
      self.viewport = pygame.Rect(0, 0, view_cols, view_rows)
      self.screen = screen
      self.mouse_pt = point.Point(0, 0)
      self.world = world
      self.tile_width = tile_width
      self.tile_height = tile_height
      self.num_rows = world.num_rows
      self.num_cols = world.num_cols
      self.mouse_img = mouse_img


   def draw_background(self):
      for y in range(0, self.viewport.height):
         for x in range(0, self.viewport.width):
            w_pt = self.viewport_to_world(point.Point(x, y))
            img = self.world.get_background_image(w_pt)
            self.screen.blit(img, (x * self.tile_width, y * self.tile_height))


   def draw_entities(self):
      for entity in self.world.entities:
         if self.viewport.collidepoint(entity.position.x, entity.position.y):
            v_pt = self.world_to_viewport(entity.position)
            self.screen.blit(entity.get_image(),
               (v_pt.x * self.tile_width, v_pt.y * self.tile_height))


   def draw_viewport(self):
      self.draw_background()
      self.draw_entities()


   def update_view(self, view_delta=(0,0), mouse_img=None):
      self.viewport = self.create_shifted_viewport(view_delta,
         self.num_rows, self.num_cols)
      self.mouse_img = mouse_img
      self.draw_viewport()
      pygame.display.update()
      self.mouse_move(self.mouse_pt)


   def update_view_tiles(self, tiles):
      rects = []
      for tile in tiles:
         if self.viewport.collidepoint(tile.x, tile.y):
            v_pt = self.world_to_viewport(tile)
            img = self.get_tile_image(v_pt)
            rects.append(self.update_tile(v_pt, img))
            if self.mouse_pt.x == v_pt.x and self.mouse_pt.y == v_pt.y:
               rects.append(self.update_mouse_cursor())

      pygame.display.update(rects)


   def update_tile(self, view_tile_pt, surface):
      abs_x = view_tile_pt.x * self.tile_width
      abs_y = view_tile_pt.y * self.tile_height

      self.screen.blit(surface, (abs_x, abs_y))

      return pygame.Rect(abs_x, abs_y, self.tile_width, self.tile_height)


   def get_tile_image(self, view_tile_pt):
      pt = self.viewport_to_world(view_tile_pt)
      bgnd = self.world.get_background_image(pt)
      occupant = self.world.get_tile_occupant(pt)
      if occupant:
         img = pygame.Surface((self.tile_width, self.tile_height))
         img.blit(bgnd, (0, 0))
         img.blit(occupant.get_image(), (0,0))
         return img
      else:
         return bgnd


   def create_mouse_surface(self, occupied):
      surface = pygame.Surface((self.tile_width, self.tile_height))
      surface.set_alpha(MOUSE_HOVER_ALPHA)
      color = MOUSE_HOVER_EMPTY_COLOR
      if occupied:
         color = MOUSE_HOVER_OCC_COLOR
      surface.fill(color)
      if self.mouse_img:
         surface.blit(self.mouse_img, (0, 0))

      return surface


   def update_mouse_cursor(self):
      return self.update_tile(self.mouse_pt,
         self.create_mouse_surface(
            self.world.is_occupied(
               self.viewport_to_world(self.mouse_pt))))


   def mouse_move(self, new_mouse_pt):
      rects = []

      rects.append(self.update_tile(self.mouse_pt,
         self.get_tile_image(self.mouse_pt)))

      if self.viewport.collidepoint(new_mouse_pt.x + self.viewport.left,
         new_mouse_pt.y + self.viewport.top):
         self.mouse_pt = new_mouse_pt

      rects.append(self.update_mouse_cursor())

      pygame.display.update(rects)
      
   def viewport_to_world(self, pt):
      return point.Point(pt.x + self.viewport.left, pt.y + self.viewport.top)


   def world_to_viewport(self, pt):
      return point.Point(pt.x - self.viewport.left, pt.y - self.viewport.top)


   def create_shifted_viewport(self, delta, num_rows, num_cols):
      new_x = clamp(self.viewport.left + delta[0], 0, num_cols - self.viewport.width)
      new_y = clamp(self.viewport.top + delta[1], 0, num_rows - self.viewport.height)

      return pygame.Rect(new_x, new_y, self.viewport.width, self.viewport.height)

def clamp(v, low, high):
   return min(high, max(v, low))
   */