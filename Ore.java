import processing.core.*;

public class Ore extends Actor
{
	public static final String ID_KEY = "ore";
	
	public Ore(Point position, PImage img, int actRate)
	{
		super(ID_KEY, position, img, actRate);
	}
}
