public class Blacksmith extends Entity
{
	private int resourceLimit;
	private int resourceCount;
	private int resourceDistance;
	
	public Blacksmith(Point pos, int rLim, int rCount, int rDist)
	{
		super("blacksmith", pos);
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
	
	public int getResourceDistance()
	{
		return this.resourceDistance;
	}
}
