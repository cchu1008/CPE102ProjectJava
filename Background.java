import processing.core.*;

public class Background
{
	private PImage image;
	private String name;
	
	public Background(String name, PImage img)
	{
		this.name = name;
		this.image = img;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public PImage getImage()
	{
		return this.image;
	}
}