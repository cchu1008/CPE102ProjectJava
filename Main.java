import java.util.Random;
import processing.core.*;

public class Main
{
	private final Random rand;
	
	private static final boolean RUN_AFTER_LOAD = true;
	private static final String IMAGE_LIST_FILE_NAME = 'imagelist';
	private static final String WORLD_FILE = 'gaia.sav';
	
	private static final int WORLD_WIDTH_SCALE = 2;
	private static final int WORLD_HEIGHT_SCALE = 2;
	
	private static final int SCREEN_WIDTH = 640;
	private static final int SCREEN_HEIGHT = 480;
	private static final int TILE_WIDTH = 32;
	private static final int TILE_HEIGHT = 32;
	
	private Background createDefaultBackground(***Image img)
	{
		return new Background(***imageStore.DEFAULT_IMAGE_NAME, img);
	}
	
	private void loadWorld(WorldModel world, ***iStore, String filename)
	{
		
	}
	
	public static void main(String args[])
	{
		//translate from pygame!
		rand = new Random();//***random.seed()
		//***pygame.init()
		***screen = pygame.display.setMode((SCREEN_WIDTH, SCREEN_HEIGHT));
		***iStore = loadImages(IMAGE_LIST_FILE_NAME, TILE_WIDTH, TITLE_HEIGHT);
		
		//The below use a floor divide instead of a regular /
		int numCols = SCREEN_WIDTH / TILE_WIDTH * WORLD_WIDTH_SCALE;
		int numRows = SCREEN_HEIGHT / TILE_HEIGHT * WORLD_HEIGHT_SCALE;
		
		//Lots to do with images. Will have to change later. Should it be of type Background?
		PImage defaultBackground = createDefaultBackground(getImages(iStore, image_store.DEFAULT_IMAGE_NAME));
		
		WorldModel world = new WorldModel(numRows, numCols, defaultBackground);
		//View uses floor divide instead of regular /
		WorldView view = new WorldView(SCREEN_WIDTH / TILE_WIDTH, SCREEN_HEIGHT / TILE_HEIGHT, screen, world, TILE_WIDTH, TILE_HEIGHT);
		loadWorld(world, iStore, TILE_WIDTH, TILE_HEIGHT);
		
		view.updateView();
		//From controller
		activityLoop(view, world);
	}
}
