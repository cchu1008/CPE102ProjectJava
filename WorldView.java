import java.util.Map;
import java.util.List;
import processing.core.*;

public class WorldView extends PApplet
{
	private static final int SPACE = 32;
	private static final int F = 70;
	
	private int viewCols = 20;
	private int viewRows = 15;
	
	private int tileWidth = 32;
	private int tileHeight = 32;
	
	private int numCols = 40;
	private int numRows = 30;
	
	private Rectangle viewport;
	
	private WorldModel world;
	
	private boolean paused = false;
	private int lastTime = 0;
	private int offset = 0;
	
	public void setup()
	{
		this.viewport = new Rectangle(0, 0, this.viewCols, this.viewRows);
		
		Map<String, List<PImage>> imageList = ImageStore.loadImages("imagelist", this.tileWidth, this.tileHeight);
		this.world = new WorldModel(this.numCols, this.numRows, new Background("default", this.imageList.get("grass").get(0)), this);
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
			case SPACE:
				this.paused = !this.paused;
				break;
			case F:
				if (!this.paused)
					this.offset -= 100;
				break;
		}
	}
	
	public void mousePressed()
	{
		Entity occ = this.world.getTileOccupant(this.mousePoint());
		if (occ != null)
		{
			Class target = (occ instanceof Miner) ? (((Miner)occ).isFull() ? Blacksmith.class : Ore.class) : ((occ instanceof OreBlob) ? Vein.class : null);
			if (target != null)
			{
				Entity nearest = world.findNearest(occ.getPosition(), Vein.class);
				if (nearest != null)
					((Actor)occ).buildPath(this.world, nearest.getPosition());
			}
		}
	}
	
	public void draw()
	{
		int time = millis();
		if (this.paused)
		{
			offset += time - this.lastTime;
		}
		else
		{
			this.world.updateOnTime(time - this.offset);
		}
		this.lastTime = time;
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
	
	public void drawPath()
	{
		Point mpt = mousePoint();
		Entity occ = this.world.getTileOccupant(mpt);
		if (occ != null && (occ instanceof Miner || occ instanceof OreBlob))
		{
			Actor actr = (Actor)occ;
			rectMode(CENTER);
			fill(15, 15, 15, 100);
			for (PathObj each : actr.getClosedSet())
			{
				Point pos = worldToViewport(each.getPos());
				rect((pos.getXCoord() * this.tileWidth) + (this.tileWidth / 2), (pos.getYCoord() * this.tileHeight) + (this.tileHeight / 2), 25, 25);
			}
			PathObj cur = actr.getTarget();
			fill(215, 15, 15, 255);
			while (cur != null)
			{
				Point pos = worldToViewport(cur.getPos());
				rect((pos.getXCoord() * this.tileWidth) + (this.tileWidth / 2), (pos.getYCoord() * this.tileHeight) + (this.tileHeight / 2), 16, 16);
				cur = cur.getCameFrom();
			}
		}
	}
	
	public void drawViewport()
	{
		this.drawBackground();
		this.drawEntities();
		this.drawPath();
	}
	
	public void updateView(int dX, int dY)
	{
		this.viewport = createShiftedViewport(dX, dY);
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
	
	private Point mousePoint()
	{
		Point mousePt = new Point((int)(this.mouseX / this.tileWidth), (int)(this.mouseY / this.tileHeight));
		return viewportToWorld(mousePt);
	}
	
	public static void main(String[] arg)
	{
		PApplet.main("WorldView");
	}
}
