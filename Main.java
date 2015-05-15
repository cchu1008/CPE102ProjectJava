import processing.core.*;

public class Main extends PApplet
{
	private boolean RUN_AFTER_LOAD = true;
	private String IMAGE_LIST_FILE_NAME = 'imagelist';
	private String WORLD_FILE = 'gaia.sav';
	
	private int WORLD_WIDTH_SCALE = 2;
	private int WORLD_HEIGHT_SCALE = 2;
	
	private int SCREEN_WIDTH = 640;
	private int SCREEN_HEIGHT = 480;
	private int TILE_WIDTH = 32;
	private int TILE_HEIGHT = 32;
	
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
		***random.seed()
		***pygame.init()
		***screen = pygame.display.setMode((SCREEN_WIDTH, SCREEN_HEIGHT));
		***iStore = loadImages(IMAGE_LIST_FILE_NAME, TILE_WIDTH, TITLE_HEIGHT);
		
		//The below use a floor divide instead of a regular /
		public int numCols = SCREEN_WIDTH / TILE_WIDTH * WORLD_WIDTH_SCALE;
		public int numRows = SCREEN_HEIGHT / TILe_HEIGHT * WORLD_HEIGHT_SCALE;
		
		//Lots to do with images. Will have to change later. Should it be of type Background?
		Image defaultBackground = createDefaultBackground(getImages(iStore, image_store.DEFAULT_IMAGE_NAME));
		
		WorldModel world = new WorldModel(numRows, numCols, defaultBackground);
		//View uses floor divide instead of regular /
		WorldView view = new WorldView(SCREEN_WIDTH / TILE_WIDTH, SCREEN_HEIGHT / TILE_HEIGHT, screen, world, TILE_WIDTH, TILE_HEIGHT);
		loadWorld(world, iStore, TILE_WIDTH, TILE_HEIGHT);
		
		view.updateView();
		//From controller
		activityLoop(view, world);
	}
}
