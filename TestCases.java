import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import org.junit.Test;
import org.junit.Before;

public class TestCases
{
	private static final Point point = new Point(10, 16);
	
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
		assertEqual(10, point.getXCoord());
		assertEqual(16, point.getYCoord());
	}
	
	@Test
	public void testMiners()
	{
		
		assertEquals(10, Courtney.getPosition().getXCoord());
		assertEquals(8, Courtney.getPosition().getYCoord());
		
		assertEquals(1, Harrison.getPosition().getXCoord());
		assertEquals(11, Harrison.getPosition().getYCoord());
		
		assertEquals(2, Courtney.getResourceLimit());
		assertEquals(2, Courtney.getResourceCount());
		assertTrue(Courney.isFull());
		
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
		
		assertEquals("unknown", Harrison.entityString(false));
		assertEquals("miner miner_10_8 10 8 2", Courtney.entityString(false));
	}
	
	@test
	public void testBlacksmith()
	{
		assertEquals(3, smithy.getPosition().getXCoord());
		assertEquals(8, smithy.getPosition().getYCoord());
		
		assertEquals(10, smithy.getResourceLimit());
		assertEquals(3, smithy.getResourceCount());
		assertEquals(1, smithy.getResourceDistance());
		
		smithy.incrementResourceCount();
		assertEquals(4, smithy.getResourceCount());
		for (int i = 4; i < 10; i++)
		{
			smithy.incrementResourceCount();
		}
		assertTrue(smithy.isFull());
		
		smithy.setResourceCount(7);
		assertFalse(smithy.isFull());
		assertEquals(7, smithy.getResourceCount());
	}
	
	
}