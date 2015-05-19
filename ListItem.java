import java.util.function.LongConsumer;
import processing.core.*;

public class ListItem
{
	private LongConsumer action;
	private long time;
	
	public ListItem(LongConsumer action, long time)
	{
		this.action = action;
		this.time = time;
	}
	
	public LongConsumer getAction()
	{
		return this.action;
	}
	
	public long getTime()
	{
		return this.time;
	}
	
	public boolean equals(Object b)
	{
		if (b instanceof ListItem)
		{
			ListItem c = (ListItem)b;
			return this.action == c.getAction() && this.time == c.getTime();
		}
		return false;
	}
}