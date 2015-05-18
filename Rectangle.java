public class Rectangle
{
	private int xCoord;
	private int yCoord;
	private int width;
	private int height;
	
	public Rectangle(int x, int y, int width, int height)
	{
		this.xCoord = x;
		this.yCoord = y;
		this.width = width;
		this.height = height;
	}
	
	public int getX()
	{
		return this.xCoord;
	}
	
	public int getY()
	{
		return this.yCoord;
	}
	
	public int getWidth()
	{
		return this.width;
	}
	
	public int getHeight()
	{
		return this.height;
	}
	
	public boolean collidepoint(int objX, int objY)
	{
		return (((objX <= (this.getX() + this.getWidth())) && (objX >= this.getX())) && ((objY <= this.getY() + this.getHeight()) && (objY >= this.getY())));
	}
	
	public boolean equals(Object other)
	{
		if (other instanceof Rectangle)
		{
			Rectangle o = (Rectangle)other;
			return (this.getX() == o.getX() && this.getY() == o.getY() && this.getWidth() == o.getWidth() && this.getHeight() == o.getHeight());
		}
		return false;
	}
}