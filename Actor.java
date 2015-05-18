import processing.core.*;

public abstract class Actor extends Entity
{
	private int actionRate;
	
	protected Actor(String name, Point position, PImage img, int actRate)
	{
		super(name, position, img);
		this.actionRate = actRate;
	}
	
	public int getActionRate()
	{
		return this.actionRate;
	}
	
	public String entityString()
	{
		String initial = super.entityString();
		return String.format("%s %d", initial, this.getActionRate());
	}
}
