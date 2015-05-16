import processing.core.*;
import processing.event.*;

public class Controller
{
	private static final int KEY_DELAY = 400;
	private static final int KEY_INTERVAL = 100;
	
	private static final int TIMER_FREQUENCY = 100;
	
	public int[] onKeydown(Event event)
	{
		int xDelta = 0;
		int yDelta = 0;
		int[] diff = new int[2];
		
		if (event.key == UP) yDelta -= 1;
		if (event.key == DOWN) yDelta += 1;
		if (event.key == LEFT) xDelta -= 1;
		if (event.key == RIGHT) xDelta += 1;
		
		diff[0] = xDelta;
		diff[1] = yDelta;
		
		return diff;
	}
	
	//Don't need this rigt now.
	public Point mouseToTile(Point pos, int tileWidth, int tileHeight)
	{
		return new Point(pos[0] / tileWidth, pos[1] / tileHeight);
	}
	
	public void handleTimerEvent(WorldModel world, WorldView view)
	{
		List<Rectangle> rects = world.updateOnTime(millis());
		view.updateViewTiles(rects);
	}
	
	//Don't need this right now.
	public void handleMouseMotion(WorldView view, Event event)
	{
		Point mousePt = mouseToTile(event.pos, view.tileWidth, view.tileHeight);
		view.mouseMove(mousePt);
	}
	
	public void handleKeydown(WorldView view, Event event)
	{
		int[] viewDelta = onKeydown(event);
		view.updateView(viewDelta);
	}
	
	//All that's left here is activityLoop(view, world).
}