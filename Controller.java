import processing.core.*;
import processing.event.*;

public class Controller
{
	private static final int KEY_DELAY = 400;
	private static final int KEY_INTERVAL = 100;
	
	private static final int TIMER_FREQUENCY = 100;
	
	public static int[] onKeydown(String code)
	{
		int xDelta = 0;
		int yDelta = 0;
		int[] diff = new int[2];
		
		if (code == 'UP') yDelta -= 1;
		if (code == 'DOWN') yDelta += 1;
		if (code == 'LEFT') xDelta -= 1;
		if (code == 'RIGHT') xDelta += 1;
		
		diff[0] = xDelta;
		diff[1] = yDelta;
		
		return diff;
	}
	
	//Don't need this right now.
	public static Point mouseToTile(Point pos, int tileWidth, int tileHeight)
	{
		return new Point(pos[0] / tileWidth, pos[1] / tileHeight);
	}
	
	public static void handleTimerEvent(WorldModel world, WorldView view)
	{
		List<Rectangle> rects = world.updateOnTime(millis());
		view.updateViewTiles(rects);
	}
	
	//Don't need this right now.
	public static void handleMouseMotion(WorldView view, Event event)
	{
		Point mousePt = mouseToTile(event.pos, view.tileWidth, view.tileHeight);
		view.mouseMove(mousePt);
	}
	
	public static void handleKeydown(WorldView view, String event)
	{
		int[] viewDelta = onKeydown(event);
		view.updateView(viewDelta);
	}
	
	//Needs to be changed to Java!
	public static void activityLoop(WorldView view, WorldModel world)
	{
		//Figure out how to change the below into Java. The event appears in the event queue every
		//TIMER_FREQUENCY milliseconds.
		pygame.time.set_timer(pygame.USEREVENT, TIMER_FREQUENCY);
		
		//This whole section could do with some work.
		while (true)
		{
			//Translate to java/Processing later
			for (String event : pygame.event.get())
			{
				if (event == pygame.USEREVENT)
					handleTimerEvent(world, view);
				//for later
				else if (event == pygame.MOUSEMOTION)
					handleMouseMotion(view, event);
				else if (event == pygame.KEYDOWN)
					handleKeydown(view, event);
			}
		}
	}
}