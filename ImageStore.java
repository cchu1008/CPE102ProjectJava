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
	private static final PApplet TEMP = new PApplet();
	
	public static Map<String, List<PImage>> loadImages(String filename, int tileWidth, int tileHeight)
	{
		Map<String, List<PImage>> images = new HashMap<String, List<PImage>>();
		try (Scanner imgList = new Scanner(new FileInputStream(filename)))
		{
			processFile(imgList, images);
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
		
		if (!images.containsKey(DEFAULT_IMAGE_NAME))
		{
			PImage def = getDefaultImage(tileWidth, tileHeight);
			images.put(DEFAULT_IMAGE_NAME, new LinkedList<PImage>());
			images.get(DEFAULT_IMAGE_NAME).add(def);
		}
		
		return images;
	}
	
	private static void processFile(Scanner imgList, Map<String, List<PImage>> images)
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
				
				PImage sprite = TEMP.loadImage(line[1]);
				
				if (line.length >= 5)
				{
					setKey(sprite, line);
				}
				
				images.get(line[0]).add(sprite);
			}
		}
	}
	
	private static void setKey(PImage sprite, String[] line)
	{
		PImage mask = TEMP.createImage(sprite.width, sprite.height, TEMP.RGB);
		mask.loadPixels();
		
		int red = Integer.parseInt(line[2]);
		int green = Integer.parseInt(line[3]);
		int blue = Integer.parseInt(line[4]);
		int key = TEMP.color(red, green, blue);
		
		for (int i = 0; i < mask.pixels.length; i++)
		{
			mask.pixels[i] = TEMP.color(mask.pixels[i] == key ? 0 : 255);
		}
		
		mask.updatePixels();
		
		sprite.mask(mask);
	}
	
	public static PImage getDefaultImage(int tileWidth, int tileHeight)
	{
		PImage def = TEMP.createImage(tileWidth, tileHeight, TEMP.ARGB);
		def.loadPixels();
		for (int i = 0; i < def.pixels.length; i++)
			def.pixels[i] = TEMP.color(127);
		def.updatePixels();
		return def;
	}
	
	public static void main(String[] args)
	{
		Map<String, List<PImage>> test = loadImages("imagelist", 32, 32);
		for (Map.Entry<String, List<PImage>> entry : test.entrySet())
		{
			System.out.print(entry.getKey());
			System.out.print(": ");
			System.out.println(entry.getValue().size());
			for (PImage img : entry.getValue())
			{
				System.out.print("\t");
				System.out.println(img);
			}
		}
	}
}
