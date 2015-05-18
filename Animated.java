import processing.core.*;

public abstract class Animated
{
	private List<PImage> sprites;
	private int animationRate;
	private int currentImage = 0;
	
	protected Animated(String name, Point position, List<PImage> imageList, int animRate)
	{
		super(name, position, imageList.get(0));
		this.sprites = imageList;
		this.animationRate = animRate;
	}
}