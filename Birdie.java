import processing.core.*;
import java.util.List;
import java.util.LinkedList;

public class Birdie extends Animated
{
	public static final String ID_KEY = "birdie";
	
	public Birdie(Point position, List<PImage> imageList, int actRate, int animRate)
	{
		super(ID_KEY, position, imageList, actRate, animRate);
	}
	
	protected static List<Point> getValidNeighbors(WorldModel world, PathObj current, Point destination)
	{
		Point pos = current.getPos();
		List<Point> fin = new LinkedList<Point>();
		
		Point[] run = new Point[]{
			new Point(pos.getXCoord() - 1, pos.getYCoord()),
			new Point(pos.getXCoord(), pos.getYCoord() - 1),
			new Point(pos.getXCoord(), pos.getYCoord() + 1),
			new Point(pos.getXCoord() + 1, pos.getYCoord())
		};
		
		for (Point pt : run)
		{
			if (world.withinBounds(pt) && !world.isBirdieAt(pt))
			{
				fin.add(pt);
			}
		}
		
		return fin;
	}
	
	public void buildPath(WorldModel world, Point destination)
	{
		this.closedSet = new LinkedList<PathObj>();
		List<PathObj> openSet = new LinkedList<PathObj>();
		
		Point position = this.getPosition();
		int hScore = calculateH(position, destination);
		
		openSet.add(new PathObj(position, null, 0, hScore));
		
		while (openSet.size() != 0)
		{
			PathObj cur = findLowFScore(openSet);
			
			if (cur.getPos().equals(destination))
			{
				this.target = cur.getCameFrom();
				return;
			}
			
			openSet.remove(cur);
			closedSet.add(cur);
			
			List<Point> neighborNodes = this.getValidNeighbors(world, cur, destination);
			for (Point node : neighborNodes)
			{
				PathObj neighbor = new PathObj(node, cur, cur.getGScore() + 1, calculateH(node, destination));
				if (closedSet.contains(neighbor))
					continue;
				
				if (!(openSet.contains(neighbor)))
				{
					openSet.add(0, neighbor);
				}
			}
		}
	}
}
