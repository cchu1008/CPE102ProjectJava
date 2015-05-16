import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.io.FileInputStream;
import processing.core.*;

public class ImageStore
{
	private static final String DEFAULT_IMAGE_NAME = "background_default";
	private static PApplet TEMP = new PApplet();
	
	public static PImage createDefaultImage(int width, int height)
	{
		PImage def = new PImage(width, height);
		def.loadPixels();
		for (int i = 0; i < def.pixels.length; i++)
		{
			def.pixels[i] = TEMP.color(127, 0);
		}
		def.updatePixels();
		return def;
	}
	
	public static Map<String, List<PImage>> loadImages(String filename, int tileWidth, int tileHeight)
	{
		Map<String, List<PImage>> images = new HashMap<String, List<PImage>>();
		try (Scanner imgList = new Scanner(new FileInputStream(filename)))
		{
			while (imgList.hasNextLine())
			{
				String[] line = imgList.nextLine().split("\\s");
				if (line.length >= 2)
				{
					if (!images.containsKey(line[0]))
					{
						images.put(line[0], new LinkedList<PImage>());
					}
					images.get(line[0]).add(TEMP.loadImage(line[1]));
				}
			}
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
		
		if (!images.containsKey(DEFAULT_IMAGE_NAME))
		{
			images.put(DEFAULT_IMAGE_NAME, new LinkedList<PImage>());
			images.get(DEFAULT_IMAGE_NAME).add(createDefaultImage(tileWidth, tileHeight));
		}
		
		return images;
	}
}
