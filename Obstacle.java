import processing.core.*;

public class Obstacle extends Entity
{
	public static final String ID_KEY = "obstacle";
	
	public Obstacle(Point position, PImage img)
	{
		super(ID_KEY, position, img);
	}
}
