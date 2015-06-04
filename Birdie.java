import processing.core.*;
import java.util.List;

public class Birdie extends Animated
{
	public static final String ID_KEY = "birdie";
	
	public Birdie(Point position, List<PImage> imageList, int actRate, int animRate)
	{
		super(ID_KEY, position, imageList, actRate, animRate);
	}
}
