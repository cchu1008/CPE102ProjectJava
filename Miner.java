import java.util.List;
import processing.core.*;

public class Miner extends Animated
{
	public static final String ID_KEY = "miner";
	
	private int resourceLimit;
	private int resourceCount;
	
	protected Miner(Point position, List<PImage> images, int actRate, int animRate, int rLim, int rCount)
	{
		super(ID_KEY, position, images, actRate, animRate);
		this.resourceLimit = rLim;
		this.resourceCount = rCount;
	}
	
	public int getResourceLimit()
	{
		return this.resourceLimit;
	}
	
	public int getResourceCount()
	{
		return this.resourceCount;
	}
	
	public void incrementResourceCount()
	{
		this.resourceCount += 1;
	}
	
	public void setResourceCount(int num)
	{
		this.resourceCount = num;
	}
	
	public boolean isFull()
	{
		return this.resourceCount >= this.resourceLimit;
	}
	
	public Point nextPosition(WorldModel world, Point destPt)
	{
		int horiz = sign(destPt.getXCoord() - this.getPosition().getXCoord());
		Point newPt = new Point(this.getPosition().getXCoord() + horiz, this.getPosition().getYCoord());
		
		if (horiz == 0 || world.isOccupied(newPt))
		{
			int vert = sign(destPt.getYCoord() - this.getPosition().getYCoord());
			newPt = new Point(this.getPosition().getXCoord(), this.getPosition().getYCoord() + vert);
			
			if (vert == 0 || world.isOccupied(newPt))
			{
				newPt = new Point(this.getPosition().getXCoord(), this.getPosition().getYCoord());
			}
		}
		return newPt;
	}
	
	public String entityString()
	{
		String initial = super.entityString();
		return String.format("%s %d %d", initial, this.getResourceLimit(), this.getResourceCount());
	}
}
