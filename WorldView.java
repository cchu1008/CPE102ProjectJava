import processing.core.*;

public class WorldView extends PApplet
{
	private int viewCols;
	private int viewRows;
	private WorldModel world;
	private int tileWidth;
	private int tileHeight;
	private Rectangle viewport;
	private int numRows;
	private int numCols;
	//private Point mousePt;
	
	public void initialize(int viewCols, int viewRows, WorldModel world, int tileWidth, int tileHeight)
	{
		this.viewCols = viewCols;
		this.viewRows = viewRows;
		this.world = world;
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
		this.viewport = new Rectangle(0, 0, viewCols, viewRows);
		this.numRows = world.getWidth();
		this.numCols = world.getHeight();
		//this.mousePt = new Point(0, 0);
	}
	
	public void setup()
	{
		this.size(640, 480);
		
		(new Main()).setup(this);
	}
	
	public void draw()
	{
		;
	}
	
	public void drawBackground()
	{
		for (int y = 0; y < viewport.getHeight(); y++)
		{
			for (int x = 0; x < viewport.getWidth(); x++)
			{
				Point wPt = viewportToWorld(new Point(x, y));
				PImage img = this.world.getBackgroundImage(wPt);
				this.image(img, x * this.tileWidth, y * this.tileHeight);
			}
		}
	}
	
	public void drawEntities()
	{
		for (Entity entity: world.getEntities())
		{
			Point position = entity.getPosition();
			if (viewport.collidepoint(position.getXCoord(), position.getYCoord()))
			{
				Point vPt = worldToViewport(position);
				image(entity.getImage(), vPt.getXCoord() * this.tileWidth, vPt.getYCoord() * this.tileHeight);
			}
		}
	}
	
	public void drawViewport()
	{
		drawBackground();
		drawEntities();
	}
	
	public void updateView(int dX, int dY)
	{
		this.viewport = createShiftedViewport(viewDelta, numRows, numCols);
		//this.mouseImg = mouseImg;
		drawViewport();
	}
	
	/** updateView with a default value for `viewDelta` */
	public void updateView()
	{
		this.updateView(0, 0);
	}
	
	public Rectangle updateTile(Point viewTilePt, PImage surface)
	{
		int absX = viewTilePt.getXCoord() * tileWidth;
		int absY = viewTilePt.getYCoord() * tileHeight;
		
		image(surface, absX, absY);
		
		return new Rectangle(absX, absY, tileWidth, tileHeight);
	}
	
	public Point viewportToWorld(Point pt)
	{
		return new Point(pt.getXCoord() + viewport.getX(), pt.getYCoord() + viewport.getY());
	}
	
	public Point worldToViewport(Point pt)
	{
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
	
	/* Don't need this part yet!
	
	public PImage getTileImage(Point viewTilePt)
	{
		Point pt = viewportToWorld(viewTilePt);
		PImage bgnd = world.getBackgroundImage(pt);
		Entity occupant = world.getTileOccupant(pt);
		
		if (occupant != null)
		{
			PImage img = createImage(this.tileWidth, this.tileHeight);
			image(bgnd, 0, 0);
			image(occupant.getImage(), 0, 0);
			return img;
		}
		return bgnd;
	}
	public PImage createMouseSurface(booean occupied)
	{
		//Work on translating from pygame
		PImage surface = pygame.Surface((this.tileWidth, this.tileHeight));
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
}
