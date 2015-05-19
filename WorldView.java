import java.util.Map;
import java.util.List;
import processing.core.*;

public class WorldView extends PApplet
{
	private int viewCols = 30;
	private int viewRows = 20;
	
	private int tileWidth = 32;
	private int tileHeight = 32;
	
	private int numCols = 40;
	private int numRows = 30;
	
	private Map<String, List<PImage>> imageList;
	
	private Rectangle viewport;
	
	private WorldModel world;
	
	public void setup()
	{
		this.viewport = new Rectangle(0, 0, this.viewCols, this.viewRows);
		
		this.imageList = ImageStore.loadImages("imagelist", this.tileWidth, this.tileHeight);
		this.world = new WorldModel(this.numCols, this.numRows, new Background("default", this.imageList.get("grass").get(0)));
		SaveLoad.loadWorld(world, imageList, "gaia.sav", true);
		
		this.size(this.viewCols * this.tileWidth, this.viewRows * this.tileHeight);
	}
	
	public void keyPressed()
	{
		switch (this.keyCode)
		{
			case UP:
				this.updateView(0, -1);
				break;
			case DOWN:
				this.updateView(0, 1);
				break;
			case LEFT:
				this.updateView(-1, 0);
				break;
			case RIGHT:
				this.updateView(1, 0);
				break;
		}
	}
	
	public void draw()
	{
		long time = System.currentTimeMillis();
		this.world.updateOnTime(time);
		this.updateView();
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
		this.viewport = createShiftedViewport(dX, dY);
		//this.mouseImg = mouseImg;
		drawViewport();
	}
	
	/** updateView with a default value for `viewDelta` */
	public void updateView()
	{
		this.updateView(0, 0);
	}
	
	public Point viewportToWorld(Point pt)
	{
		return new Point(pt.getXCoord() + viewport.getX(), pt.getYCoord() + viewport.getY());
	}
	
	public Point worldToViewport(Point pt)
	{
		return new Point(pt.getXCoord() - viewport.getX(), pt.getYCoord() - viewport.getY());
	}
	
	public Rectangle createShiftedViewport(int dX, int dY)
	{
		int newX = clamp(this.viewport.getX() + dX, 0, this.numCols - this.viewport.getWidth());
		int newY = clamp(this.viewport.getY() + dY, 0, this.numRows - this.viewport.getHeight());
		
		return new Rectangle(newX, newY, this.viewport.getWidth(), this.viewport.getHeight());
	}
	
	public static int clamp(int v, int low, int high)
	{
		return Math.min(high, Math.max(v, low));
	}
	
	
	public static void main(String[] arg)
	{
		PApplet.main("WorldView");
	}
	
	/* Don't need this part yet!
	
	public Rectangle updateTile(Point viewTilePt, PImage surface)
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
