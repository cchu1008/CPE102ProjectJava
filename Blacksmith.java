import processing.core.*;

public class Blacksmith extends Entity
{
	public static final String ID_KEY = "blacksmith";
	
	private int resourceLimit;
	private int resourceCount;
	
	public Blacksmith(Point pos, PImage img, int rLim, int rCount)
	{
		super(ID_KEY, pos, img);
		this.resourceLimit = rLim;
		this.resourceCount = rCount;
	}
	
	public int getResourceCount()
	{
		return this.resourceCount;
	}
	
	public int getResourceLimit()
	{
		return this.resourceLimit;
	}
	
	public void setResourceCount(int n)
	{
		this.resourceCount = n;
	}
}
