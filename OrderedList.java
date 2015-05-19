import java.util.List;
import java.util.LinkedList;

public class OrderedList
{
	private List<LongConsumer> list;
	
	public OrderedList()
	{
		this.list = new LinkedList<LongConsumer>();
	}
	
	public void insert(LongConsumer item, int ord)
	{
		int size = list.length;
		int idx = 0;
		while (idx < size && list.get(idx).ord < ord)
			idx++;
		
		list.add(idx, new ListItem(item, ord));
	}
	
	public void remove(LongConsumer item)
	{
		int size = list.length;
		int idx = 0;
		
		while (idx <size && list.get(idx).item != item)
			idx++;
		if (idx < size)
			list.remove(item);
	}
	
	public ListItem head()
	{
		if (list.length > 0)
			return list.get(0);
		return None;
	}
	
	public ListItem pop()
	{
		if (list.length > 0)
		{
			ListItem fin = list.get(0);
			list.remove(list.get(0));
			return fin;
		}
	}
}