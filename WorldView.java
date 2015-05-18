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
	private Point mousePt;
	
	public WorldView(int viewCols, int viewRows, WorldModel world, int tileWidth, int tileHeight)
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
				PImage img = this.world.getBackgroundImage(wPt);
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
	}
	
	public Rectangle updateTile(Point viewTilePt, ***surface (image))
	{
		int absX = viewTilePt.getXCoord() * tileWidth;
		int absY = viewTilePt.getYCoord() * tileHeight;
		
		image(surface, absX, absY);
		
		return new Rectangle(absX, absY, tileWidth, tileHeight);
	}
	
	public PImage getTileImage(Point viewTilePt)
	{
		Point pt = viewportToWorld(viewTilePt);
		PImage bgnd = world.getBackgroundImage(pt);
		boolean occupant = world.getTileOccupant(pt);
		
		if (occupant)
		{
			PImage img = createImage(this.tileWidth, this.tileHeight);
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
	
}
