import java.util.List;
import java.util.LinkedList;
import java.util.function.*;

public class OrderedList
{
	private List<LongConsumer> list;
	
	public OrderedList()
	{
		this.list = new LinkedList<LongConsumer>();
	}
	
	public void insert(LongConsumer item, int ord)
	{
		int size = list.size();
		int idx = 0;
		while (idx < size && list.get(idx).ord < ord)
			idx++;
		
		list.add(idx, new ListItem(item, ord));
	}
	
	public void remove(LongConsumer item)
	{
		int size = list.size();
		int idx = 0;
		
		while (idx <size && list.get(idx).item != item)
			idx++;
		if (idx < size)
			list.remove(item);
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
	
	private class ListItem
	{
		private LongConsumer item;
		private int ord;
		
		public ListItem(LongConsumer item, int ord)
		{
			this.item = item;
			this.ord = ord;
		}
		
		public boolean equals(Object b)
		{
			if (b instanceof ListItem)
			{
				ListItem c = (ListItem)b;
				return this.item == c.item && this.ord == c.ord;
			}
		}
	}
}