import java.util.function.LongConsumer;
import java.util.List;
import java.util.LinkedList;
import processing.core.*;

public abstract class Actor extends Entity
{
	private int actionRate;
	private List<LongConsumer> pendingActions = new LinkedList<LongConsumer>();
	
	protected Actor(String name, Point position, PImage img, int actRate)
	{
		super(name, position, img);
		this.actionRate = actRate;
	}
	
	public int getActionRate()
	{
		return this.actionRate;
	}
	
	public void removePendingAction(LongConsumer action)
	{
		this.pendingActions.remove(action);
	}
	
	public void addPendingAction(LongConsumer action)
	{
		this.pendingActions.add(action);
	}
	
	public List<LongConsumer> getPendingActions()
	{
		return this.pendingActions;
	}
	
	public void clearPendingActions()
	{
		this.pendingActions.clear();
	}
	
	public String entityString()
	{
		String initial = super.entityString();
		return String.format("%s %d", initial, this.getActionRate());
	}
}
