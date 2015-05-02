import static java.lang.Math.abs;

public abstract class Entity
{
	private String name;
	private Point position;
	
	protected Entity(String name, Point position)
	{
		this.name = name;
		this.position = position;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public Point getPosition()
	{
		return this.position;
	}
	
	public void setPosition(Point position)
	{
		this.position = position;
	}
	
	protected static boolean adjacent(Point p1, Point p2)
	{
		return ((p1.getXCoord() == p2.getXCoord() && abs(p1.getYCoord() - p2.getYCoord()) == 1)
				|| (p1.getYCoord() == p2.getYCoord() && abs(p1.getXCoord() - p2.getXCoord()) == 1));
	}
	
	protected int sign(int x)
	{
		if (x == 0) return 0;
		return (x > 0) ? 1 : -1;
	}
	
	public String entityString()
	{
		return String.format("%s %d %d", this.getName(), this.getPosition().getXCoord(), this.getPosition().getYCoord());
	}
}
