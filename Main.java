import java.util.Random;
import java.util.Map;
import java.util.List;
import processing.core.*;
import processing.core.PApplet;

public class Main extends PApplet
{
	public static final Random RANDOMIZER = new Random();
	
	private WorldModel world;
	private WorldView view;
	private Map<String, List<PImage>> imageList; //initialize later!
	
	private static final boolean RUN_AFTER_LOAD = true;
	private static final String IMAGE_LIST_FILE_NAME = "imagelist";
	private static final String WORLD_FILE = "gaia.sav";
	
	private static final int WORLD_WIDTH_SCALE = 2;
	private static final int WORLD_HEIGHT_SCALE = 2;
	
	private static final int SCREEN_COLS = 20;
	private static final int SCREEN_ROWS = 15;
	private static final int TILE_WIDTH = 32;
	private static final int TILE_HEIGHT = 32;
	
	public void setup()
	{
		//size(SCREEN_COLS, SCREEN_ROWS);
		
		this.imageList = ImageStore.loadImages(IMAGE_LIST_FILE_NAME, TILE_WIDTH, TILE_HEIGHT);
		
		Background defaultBackground = new Background("default", this.imageList.get("grass").get(0));
		
		this.world = new WorldModel(SCREEN_ROWS * WORLD_HEIGHT_SCALE, SCREEN_COLS * WORLD_WIDTH_SCALE, defaultBackground);
		
		this.view = new WorldView(SCREEN_COLS, SCREEN_ROWS, world, TILE_WIDTH, TILE_HEIGHT);
		
		SaveLoad.loadWorld(world, imageList, WORLD_FILE, true);
		
		//view.updateView();
	}
	
	public void keyPressed()
	{/*
		int xDelta = 0;
		int yDelta = 0;
		int[] diff = new int[2];
		
		if (key == UP) yDelta -= 1;
		if (key == DOWN) yDelta += 1;
		if (key == LEFT) xDelta -= 1;
		if (key == RIGHT) xDelta += 1;
		
		diff[0] = xDelta;
		diff[1] = yDelta;
		
		view.updateView(diff);*/
	}
	
	public void draw()
	{
		
	}
	
	public static void main(String args[])
	{
		PApplet.main("Main");
	}
}
