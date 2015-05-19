import java.util.List;
import processing.core.*;

public abstract class Animated extends Actor
{
	private List<PImage> images;
	private int animationRate;
	private int currentImage = 0;
	
	protected Animated(String name, Point position, List<PImage> imageList, int actRate, int animRate)
	{
		super(name, position, imageList.get(0), actRate);
		this.images = imageList;
		this.animationRate = animRate;
	}
	
	public int getAnimationRate()
	{
		return this.animationRate;
	}
	
	public void nextImage()
	{
		this.currentImage++;
		if (this.currentImage >= this.images.size())
			this.currentImage = 0;
		this.setImage(this.images.get(this.currentImage));
	}
	
	public String entityString()
	{
		String initial = super.entityString();
		return String.format("%s %d", initial, this.getAnimationRate());
	}
}