import processing.core.*;
import processing.event.*;

public class Controller
{	
	//Don't need this right now.
	public static Point mouseToTile(Point pos, int tileWidth, int tileHeight)
	{
		return new Point(pos[0] / tileWidth, pos[1] / tileHeight);
	}
	
	//Don't need this right now.
	public static void handleMouseMotion(WorldView view, Event event)
	{
		Point mousePt = mouseToTile(event.pos, view.tileWidth, view.tileHeight);
		view.mouseMove(mousePt);
	}
}