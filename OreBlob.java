import java.util.List;
import processing.core.*;

public class OreBlob extends Animated
{
	public static final String ID_KEY = "blob";
	
	public OreBlob(Point position, List<PImage> images, int actRate, int animRate)
	{
		super(ID_KEY, position, images, actRate, animRate);
	}
}
