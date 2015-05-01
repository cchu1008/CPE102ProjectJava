public class Miner extends Entity
{
	private int resourceLimit;
	private int resourceCount;
	
	protected Miner(Point position, int rLim, int rCount)
	{
		super("miner", position);
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
	
	public boolean isFull()
	{
		return this.resourceCount >= this.resourceLimit;
	}
	
	protected String entityString(boolean notUseName)
	{
		notUseName = this.isFull();
		if(!notUseName)
		{
			return String.format("%s %s %f %f %f %f", 'miner', this.getName(), this.getPosition().getXCoord(), this.getPosition().getYCoord(), this.getResourceLimit(), this.getRate());
		}
		else
		{
			return 'unknown';
		}
	}
}