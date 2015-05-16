import java.util.Random;
import processing.core.*;

public class Main
{
	private final Random rand;
	
	private WorldModel theWorld;
	private WorldView view;
	private Map<String, String> iStore; //initialize later!
	
	private static final boolean RUN_AFTER_LOAD = true;
	private static final String IMAGE_LIST_FILE_NAME = 'imagelist';
	private static final String WORLD_FILE = 'gaia.sav';
	
	private static final int WORLD_WIDTH_SCALE = 2;
	private static final int WORLD_HEIGHT_SCALE = 2;
	
	private static final int SCREEN_COLS = 20;
	private static final int SCREEN_ROWS = 15;
	private static final int TILE_WIDTH = 32;
	private static final int TILE_HEIGHT = 32;
	
	private Background createDefaultBackground(***Image img)
	{
		return new Background(***imageStore.DEFAULT_IMAGE_NAME, img);
	}
	
	private void loadWorld(WorldModel world, Map<String, String> iStore, String filename)
	{
		
	}
	
	public static void main(String args[])
	{
		//Lots to do with images. Will have to change later. Should it be of type Background?
		PImage defaultBackground = createDefaultBackground(getImages(iStore, image_store.DEFAULT_IMAGE_NAME));
		
		world = new WorldModel(SCREEN_ROWS * WORLD_HEIGHT_SCALE, SCREEN_COLS * WORLD_WIDTH_SCALE, defaultBackground);
		
		//View uses floor divide instead of regular /
		view = new WorldView(SCREEN_COLS, SCREEN_ROWS, world, TILE_WIDTH, TILE_HEIGHT);
		
		HashMap<String, String> iStore = loadImages(IMAGE_LIST_FILE_NAME, TILE_WIDTH, TITLE_HEIGHT);
		
		loadWorld(world, iStore, TILE_WIDTH, TILE_HEIGHT);
		
		view.updateView();
		
		rand = new Random();
		
		//From controller
		activityLoop(view, world);
	}
}
