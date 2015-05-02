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
	
	protected String entityString()
	{
		String initial = super.entityString();
		return String.format("%s %d", initial, this.getResourceDistance());
	}
}