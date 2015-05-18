import processing.core.*;

public class Vein extends Actor
{
	public static final String ID_KEY = "vein";
	
	private int resourceDistance;
	
	public Vein(Point position, PImage img, int actRate, int rDist)
	{
		super(ID_KEY, position, img, actRate);
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
