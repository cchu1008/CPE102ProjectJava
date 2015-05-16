import processing.core.*;

public class ImageStore
{
	private static final String DEFAULT_IMAGE_NAME = "background_default";
	
	public static PImage createDefaultImage(int width, int height)
	{
		PImage surf = new PImage(width, height);
		PApplet temp = new PApplet();
		surf.loadPixels();
		for (int i = 0; i < surf.pixels.length; i++)
		{
			surf.pixels[i] = temp.color(127, 0);
		}
		surf.updatePixels();
		return surf;
	}
}
