import processing.core.*;

public class Quake extends Animated
{
	public static final String ID_KEY = "quake";
	
	public Quake(Point position, List<PImage> images)
	{
		super(ID_KEY, position, img, 0);
	}
}
