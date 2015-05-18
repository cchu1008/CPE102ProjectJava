import processing.core.*;

public class Vein extends Entity
{
	private int resourceDistance;
	
	public Vein(Point position, PImage img, int rDist)
	{
		super("vein", position, img);
		this.resourceDistance = rDist;
	}
	
	public int getResourceDistance()
	{
		return this.resourceDistance;
	}
	
	public String entityString()
	{
		String initial = super.entityString();
		return String.format("%s %d", initial, this.getResourceDistance());
	}
}
