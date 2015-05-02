import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import org.junit.Test;
import org.junit.Before;

public class TestCases
{
	private static final Point pointy = new Point(10, 16);
	
	private static final Miner Harrison = new Miner(new Point(1, 11), 2, 0);
	private static final Miner Courtney = new Miner(new Point(10, 8), 2, 2);
	
	private static final Background back = new Background();
	
	private static final WorldModel theWorld = new WorldModel(16, 16, back);
	
	private static final Blacksmith smithy = new Blacksmith(new Point(3, 8), 10, 3, 1);
	
	private static final Obstacle obby = new Obstacle(new Point(4, 4));
	
	private static final OreBlob blobby;
	
	private static final Ore oreby;
	
	private static final Vein veiny;
	
	@test
	public void testPoint()
	{
		assertEqual(10, pointy.getXCoord());
		assertEqual(16, pointy.getYCoord());
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
		
		//assertEquals("unknown", Harrison.entityString());
		//assertEquals("miner miner_10_8 10 8 2", Courtney.entityString());
	}
	
	@Test
	public void testBlacksmith()
	{
		assertEquals("blacksmith", smithy.getName());
		
		assertEquals(3, smithy.getPosition().getXCoord());
		assertEquals(8, smithy.getPosition().getYCoord());
		
		assertEquals(10, smithy.getResourceLimit());
		assertEquals(3, smithy.getResourceCount());
		assertEquals(1, smithy.getResourceDistance());
		
		smithy.incrementResourceCount();
		assertEquals(4, smithy.getResourceCount());
		while (smithy.getResourceCount() < 10)
		{
			smithy.incrementResourceCount();
		}
		assertTrue(smithy.isFull());
		
		smithy.setResourceCount(7);
		assertFalse(smithy.isFull());
		assertEquals(7, smithy.getResourceCount());
	}
	
	@Test
	public void testObstacle()
	{
		assertEquals("obstacle", obby.getName());
	}
}