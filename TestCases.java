import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import java.util.List;
import java.util.ArrayList;
import org.junit.Test;
import org.junit.Before;
import processing.core.*;

public class TestCases
{
	private static final double DELTA = 0.00001;
	
	private static List<PImage> minerImgs = new ArrayList<PImage>();
	
	private static final Point pointy = new Point(10, 16);
	
	private static final Miner Harrison = new Miner(new Point(1, 11), minerImgs, 2, 0, 1, 1);
	private static final Miner Courtney = new Miner(new Point(10, 8), minerImgs, 2, 2, 1, 1);
	
	private static final Background back = new Background();
	
	private static final WorldModel theWorld = new WorldModel(16, 16, back);
	
	private static final Blacksmith smithy = new Blacksmith(new Point(3, 8));
	
	private static final Obstacle obby = new Obstacle(new Point(4, 4));
	
	private static final OreBlob blobby = new OreBlob(new Point(9, 6));
	
	private static final Ore oreby = new Ore(new Point(13, 31));
	
	private static final Vein veiny = new Vein(new Point(9, 6), 3);
	
	@Test
	public void testPoint()
	{
		assertEquals(10, pointy.getXCoord());
		assertEquals(16, pointy.getYCoord());
	}
	
	@Test
	public void testMiners()
	{
		assertEquals("miner", Courtney.getName());
		assertEquals("miner", Harrison.getName());
		
		assertEquals(10, Courtney.getPosition().getXCoord());
		assertEquals(8, Courtney.getPosition().getYCoord());
		
		assertEquals(1, Harrison.getPosition().getXCoord());
		assertEquals(11, Harrison.getPosition().getYCoord());
		
		assertEquals(2, Courtney.getResourceLimit());
		assertEquals(2, Courtney.getResourceCount());
		assertTrue(Courtney.isFull());
		
		assertEquals(2, Harrison.getResourceLimit());
		assertEquals(0, Harrison.getResourceCount());
		assertFalse(Harrison.isFull());
		
		Harrison.incrementResourceCount();
		assertEquals(1, Harrison.getResourceCount());
		Harrison.incrementResourceCount();
		assertEquals(2, Harrison.getResourceCount());
		assertTrue(Harrison.isFull());
		
		Courtney.setResourceCount(0);
		assertEquals(0, Courtney.getResourceCount());
		assertFalse(Courtney.isFull());
		
		assertEquals("miner 1 11 2 2", Harrison.entityString());
		assertEquals("miner 10 8 2 0", Courtney.entityString());
	}
	
	@Test
	public void testBlacksmith()
	{
		assertEquals("blacksmith", smithy.getName());
		
		assertEquals(3, smithy.getPosition().getXCoord());
		assertEquals(8, smithy.getPosition().getYCoord());
		
		assertEquals("blacksmith 3 8", smithy.entityString());
	}
	
	@Test
	public void testObstacle()
	{
		assertEquals("obstacle", obby.getName());
		
		assertEquals(4, obby.getPosition().getXCoord());
		assertEquals(4, obby.getPosition().getYCoord());
		
		assertEquals("obstacle 4 4", obby.entityString());
	}
	
	@Test
	public void testOreBlob()
	{
		assertEquals("oreblob", blobby.getName());
		
		assertEquals(9, blobby.getPosition().getXCoord());
		assertEquals(6, blobby.getPosition().getYCoord());
		
		assertEquals("oreblob 9 6", blobby.entityString());
	}
	
	@Test
	public void testOre()
	{
		assertEquals("ore", oreby.getName());
		
		assertEquals(13, oreby.getPosition().getXCoord());
		assertEquals(31, oreby.getPosition().getYCoord());
		
		assertEquals("ore 13 31", oreby.entityString());
	}
	
	@Test
	public void testVein()
	{
		assertEquals("vein", veiny.getName());
		
		assertEquals(9, veiny.getPosition().getXCoord());
		assertEquals(6, veiny.getPosition().getYCoord());
		
		assertEquals(3, veiny.getResourceDistance());
		
		assertEquals("vein 9 6 3", veiny.entityString());
	}
	
	@Test
	public void testBackground()
	{
		assertEquals("background", back.getName());
	}
	
	@Test
	public void testWorldModel()
	{
		assertEquals(16, theWorld.getWidth());
		assertEquals(16, theWorld.getHeight());
		
		assertEquals(0, theWorld.getEntities().size());
		
		assertEquals(false, theWorld.isOccupied(new Point(5, 5)));
		
		assertEquals(null, theWorld.getTileOccupant(new Point(5, 5)));
		
		assertEquals(true, theWorld.withinBounds(new Point(6, 10)));
		
		theWorld.addEntity(Harrison);
		assertEquals(Harrison, theWorld.getTileOccupant(new Point(1, 11)));
		
		assertEquals(1, theWorld.getEntities().size());
		
		theWorld.moveEntity(Harrison, new Point(14, 7));
		assertEquals(Harrison, theWorld.getTileOccupant(new Point(14, 7)));
		
		theWorld.removeEntity(Harrison);
		assertEquals(null, theWorld.getTileOccupant(new Point(14, 7)));
		
		Harrison.setPosition(new Point(1, 11));
		theWorld.addEntity(Harrison);
		assertEquals(Harrison, theWorld.getTileOccupant(new Point(1, 11)));
		
		assertEquals(null, theWorld.findNearest(new Point(4, 10), OreBlob.class));
		
		theWorld.addEntity(blobby);
		assertEquals(2, theWorld.getEntities().size());
		
		theWorld.addEntity(oreby);
		assertEquals(2, theWorld.getEntities().size());
		
		assertEquals(blobby, theWorld.findNearest(new Point(4, 10), OreBlob.class));
		
		assertEquals(Harrison, theWorld.findNearest(new Point(4, 10), Miner.class));
		
		assertEquals(5.0D, theWorld.distance(Harrison.getPosition(), new Point(4, 7)), DELTA);
	}
}