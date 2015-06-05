import processing.core.*;
import java.util.List;
import java.util.LinkedList;

public class Zombie extends Animated
{
	public static final String ID_KEY = "zombie";
	
	public Zombie(Point position, List<PImage> imageList, int actRate, int animRate)
	{
		super(ID_KEY, position, imageList, actRate, animRate);
	}
}
