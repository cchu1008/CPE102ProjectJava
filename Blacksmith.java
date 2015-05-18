import processing.core.*;

public class Blacksmith extends Entity
{
	private int resourceLimit;
	private int resourceCount;
	private int resourceDistance;
	
	public Blacksmith(Point pos, PImage img, int rLim, int rCount, int rDist)
	{
		super("blacksmith", pos, img);
		this.resourceLimit = rLim;
		this.resourceCount = rCount;
		this.resourceDistance = rDist;
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
	
	public int getResourceDistance()
	{
		return this.resourceDistance;
	}
	
	public String entityString()
	{
		String initial = super.entityString();
		return String.format("%s %d %d %d", initial, this.getResourceLimit(), this.getResourceCount(), this.getResourceDistance());
	}
}
