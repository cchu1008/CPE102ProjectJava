import processing.core.*;

public class Background
{
	public static final String ID_KEY = "background";
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
	
	public void setImage(PImage img)
	{
		this.image = img;
	}
}
