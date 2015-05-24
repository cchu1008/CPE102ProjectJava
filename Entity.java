import static java.lang.Math.abs;
import processing.core.*;

public abstract class Entity
{
	private String name;
	private Point position;
	private PImage image;
	
	protected Entity(String name, Point position, PImage img)
	{
		this.name = name;
		this.position = position;
		this.image = img;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public Point getPosition()
	{
		return this.position;
	}
	
	public void setPosition(Point position)
	{
		this.position = position;
	}
	
	public PImage getImage()
	{
		return this.image;
	}
	
	public void setImage(PImage img)
	{
		this.image = img;
	}
	
	public String entityString()
	{
		return String.format("%s %d %d", this.getName(), this.getPosition().getXCoord(), this.getPosition().getYCoord());
	}
}
