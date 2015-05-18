import java.util.Random;
import processing.core.*;

public class Main extends PApplet
{
	private final Random rand;
	
	private WorldModel theWorld;
	private WorldView view;
	private Map<String, List<PImage>> iStore; //initialize later!
	
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
	
	public void setup()
	{
		this.rand = new Random();
		
		Background defaultBackground = createDefaultBackground(getImages(iStore, image_store.DEFAULT_IMAGE_NAME));
		
		this.world = new WorldModel(SCREEN_ROWS * WORLD_HEIGHT_SCALE, SCREEN_COLS * WORLD_WIDTH_SCALE, defaultBackground);
		
		this.view = new WorldView(SCREEN_COLS, SCREEN_ROWS, world, TILE_WIDTH, TILE_HEIGHT);
		
		this.iStore = ImageStore.loadImages(IMAGE_LIST_FILE_NAME, TILE_WIDTH, TITLE_HEIGHT);
		
		SaveLoad.loadWorld(world, iStore, WORLD_FILE, true);
		
	}
	
	public void draw()
	{
		view.updateView();
		
		//From controller
		activityLoop(view, world);
	}
	
	public static void main(String args[])
	{
		PApplet.main("Main");
	}
}
