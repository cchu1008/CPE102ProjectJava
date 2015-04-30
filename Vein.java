public class Vein extends Entity
{
	private int resourceDistance;
	
	public Vein(Point position, int rDist)
	{
		super("vein", position);
		this.resourceDistance = rDist;
	}
	
	public int getResourceDistance()
	{
		return this.resourceDistance;
	}
}