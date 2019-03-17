import java.lang.Comparable;
import java.util.ArrayList;

public class Tree<T extends Comparable<T>> implements ITree<T> {

	Tree(T item)
	{
		data = item;
		children = new ArrayList<ITree<T>>();
	}

	public T getItem()
	{
		return data;
	}

	public ITree<T> find(T item)
	{
		ITree<T> node = null;
		if (data.compareTo(data) == 0) return this;
		for (int i = 0; i < children.size(); i++)
		{
			if ( children.get(i).getItem().compareTo(item) == 0)
			{
				node = children.get(i);
				break;
			}
			else
			{
				node = children.get(i).find(item);
			}
		}
		return node;
	}

	public ITree<T> insert(T item)
	{
		ITree<T> new_child = new Tree<T>(item);
		children.add(new_child);
		return new_child;
	}

	protected T data;
	protected ArrayList<ITree<T>> children;
}
