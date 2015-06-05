import processing.core.*;
import java.util.List;
import java.util.LinkedList;

public class Birdie extends Animated
{
	public static final String ID_KEY = "birdie";
	
	private Point spawnPoint;
	
	public Birdie(Point position, List<PImage> imageList, int actRate, int animRate)
	{
		super(ID_KEY, position, imageList, actRate, animRate);
		this.spawnPoint = position;
	}
	
	public Point getSpawnPoint()
	{
		return this.spawnPoint;
	}
	
	protected boolean isLegal(WorldModel world, Point pt, Point destination)
	{
		return !world.isBirdieAt(pt);
	}
}
