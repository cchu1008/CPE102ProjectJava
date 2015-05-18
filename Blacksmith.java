import processing.core.*;

public class Blacksmith extends Entity
{
	private int resourceLimit;
	private int resourceCount;
	
	public Blacksmith(Point pos, PImage img, int rLim, int rCount)
	{
		super("blacksmith", pos, img);
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
	
	public String entityString()
	{
		String initial = super.entityString();
		return String.format("%s %d %d", initial, this.getResourceLimit(), this.getResourceCount());
	}
}
