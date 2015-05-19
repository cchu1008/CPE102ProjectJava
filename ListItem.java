public class ListItem
{
	private Action item;
	private int ord;
	
	public ListItem(Action item, int ord)
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