
public class PathObj
{
	private Point pos;
	private PathObj cameFrom;
	private int gScore;
	private int hScore;
	private int fScore;
	
	public PathObj(Point pos, PathObj cameFrom, int gScore, int hScore)
	{
		this.pos = pos;
		this.cameFrom = cameFrom;
		this.gScore = gScore;
		this.hScore = hScore;
		this.fScore = gScore + hScore;
	}
	
	public Point getPos()
	{
		return this.pos;
	}
	
	public PathObj getCameFrom()
	{
		return this.cameFrom;
	}
	
	public void setCameFrom(PathObj obj)
	{
		this.cameFrom = obj;
	}
	
	public int getGScore()
	{
		return this.gScore;
	}
	
	public void setGScore(int newG)
	{
		this.gScore = newG;
	}
	
	public int getHScore()
	{
		return this.hScore;
	}
	
	public int getFScore()
	{
		return this.fScore;
	}
	
	public boolean equals(Object other)
	{
		if (other instanceof PathObj)
		{
			PathObj o = (PathObj)other;
			return this.pos.equals(o.pos);
		}
		return false;
	}
}