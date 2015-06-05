public class Point
{
	private final int xCoord;
	private final int yCoord;
	
	public Point(int x, int y)
	{
		this.xCoord = x;
		this.yCoord = y;
	}
	
	public int getXCoord()
	{
		return this.xCoord;
	}
	
	public int getYCoord()
	{
		return this.yCoord;
	}
	
	public boolean equals(Object other)
	{
		if (other instanceof Point)
		{
			Point o = (Point)other;
			return (this.getXCoord() == o.getXCoord() && this.getYCoord() == o.getYCoord());
		}
		return false;
	}
	
	public Point translate(int dx, int dy)
	{
		return new Point(this.xCoord + dx, this.yCoord + dy);
	}
	
	public String toString()
	{
		return "" + this.xCoord + ", " + this.yCoord;
	}
}
