import java.util.List;
import processing.core.*;

public class OreBlob extends Animated
{
	public static final String ID_KEY = "";
	
	public OreBlob(Point position, List<PImage> images)
	{
		super(ID_KEY, position, images, 0, 0);
	}
	
	public Point blobNextPosition(WorldModel world, Point destPt)
	{
		int horiz = sign(destPt.getXCoord() - this.getPosition().getXCoord());
		Point newPt = new Point(this.getPosition().getXCoord() + horiz, this.getPosition().getYCoord());
		
		if (horiz == 0 || (world.isOccupied(newPt) && !(world.getTileOccupant(newPt) instanceof Ore)))
		{
			int vert = sign(destPt.getYCoord() - this.getPosition().getYCoord());
			newPt = new Point(this.getPosition().getXCoord(), this.getPosition().getYCoord() + vert);
			
			if (vert == 0 || (world.isOccupied(newPt) && (!(world.getTileOccupant(newPt) instanceof Ore))))
			{
				newPt = new Point(this.getPosition().getXCoord(), this.getPosition().getYCoord());
			}
		}
		return newPt;
	}
}
