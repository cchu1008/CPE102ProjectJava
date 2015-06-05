import java.util.function.LongConsumer;
import java.util.List;
import java.util.LinkedList;
import static java.lang.Math.*;
import processing.core.*;

public abstract class Actor extends Entity
{
	private int actionRate;
	private List<LongConsumer> pendingActions = new LinkedList<LongConsumer>();
	protected List<PathObj> closedSet = new LinkedList<PathObj>();
	protected PathObj target = null;
	
	protected Actor(String name, Point position, PImage img, int actRate)
	{
		super(name, position, img);
		this.actionRate = actRate;
	}
	
	public int getActionRate()
	{
		return this.actionRate;
	}
	
	public void removePendingAction(LongConsumer action)
	{
		this.pendingActions.remove(action);
	}
	
	public void addPendingAction(LongConsumer action)
	{
		this.pendingActions.add(action);
	}
	
	public List<LongConsumer> getPendingActions()
	{
		return this.pendingActions;
	}
	
	public void clearPendingActions()
	{
		this.pendingActions.clear();
	}
	
	public String entityString()
	{
		String initial = super.entityString();
		return String.format("%s %d", initial, this.getActionRate());
	}
	
	public List<PathObj> getClosedSet()
	{
		return this.closedSet;
	}
	
	public PathObj getTarget()
	{
		return this.target;
	}
	
	protected static int sign(int x)
	{
		if (x == 0) return 0;
		return (x > 0) ? 1 : -1;
	}
	
	protected static PathObj findLowFScore(List<PathObj> open)
	{
		PathObj lowest = open.get(0);
		for (PathObj pt : open)
		{
			if (pt.getFScore() < lowest.getFScore())
			{
				lowest = pt;
			}
		}
		return lowest;
	}
	
	protected List<Point> getValidNeighbors(WorldModel world, PathObj current, Point destination)
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
			if (world.withinBounds(pt) && this.isLegal(world, pt, destination))
			{
				fin.add(pt);
			}
		}
		
		return fin;
	}
	
	protected boolean isLegal(WorldModel world, Point pt, Point destination)
	{
		return !world.isOccupied(pt) || pt.equals(destination);
	}
	
	protected static int calculateH(Point beginning, Point end)
	{
		return (abs(beginning.getXCoord() - end.getXCoord()) + abs(beginning.getYCoord() - end.getYCoord()));
	}
	
	public Point nextPosition()
	{
		if (this.target != null)
		{
			PathObj cur = this.target;
			PathObj prev = cur.getCameFrom();
			while (prev != null && !(prev.getPos().equals(this.getPosition())))
			{
				cur = prev;
				prev = cur.getCameFrom();
			}
			return cur.getPos();
		}
		return this.getPosition();
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
				this.target = cur;
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
