import java.util.List;
import java.util.LinkedList;
import java.util.function.*;

public class OrderedList
{
	private List<ListItem> list;
	
	public OrderedList()
	{
		this.list = new LinkedList<ListItem>();
	}
	
	public void insert(LongConsumer action, long time)
	{
		int size = list.size();
		int idx = 0;
		while (idx < size && list.get(idx).getTime() < time)
			idx++;
		
		list.add(idx, new ListItem(action, time));
	}
	
	public void remove(LongConsumer action)
	{
		int size = list.size();
		int idx = 0;
		
		while (idx <size && list.get(idx).getAction() != action)
			idx++;
		if (idx < size)
			list.remove(idx);
	}
	
	public ListItem head()
	{
		if (list.size() > 0)
			return list.get(0);
		return null;
	}
	
	public ListItem pop()
	{
		if (list.size() > 0)
		{
			ListItem fin = list.get(0);
			list.remove(list.get(0));
			return fin;
		}
		return null;
	}
}