import java.lang.Comparable;
import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;


public class BinaryTree<T extends Comparable<T>> implements ITree<T>, ITraversable<T>, IMeasurable, IRotatable<T> {

	BinaryTree(T item)
	{
		data = item;
	}

	// ITree interface

	public T getItem()
	{
		return data;
	}
	public ITree<T> find(T item)
	{
		int compare = item.compareTo(data);
		ITree<T> node = null;
		if (compare == 0) {
			node = this;
		}
		else if (rightChild != null && compare > 0) {
			node = rightChild.find(item);
		}
		else if (leftChild != null && compare < 0) {
			node = leftChild.find(item);
		}
		return node;
	}

	public ITree<T> insert(T item)
	{
		ITree<T> node = find(item); 
		if (node != null)
		{
			return node;
		}
		int compare = item.compareTo(data);
		if (compare > 0)
		{
			if (rightChild == null)
			{
				rightChild = new BinaryTree<T>(item);
				node = rightChild;
			}
			else
			{
				node = rightChild.insert(item);
			}
		}
		else if (compare < 0)
		{
			if (leftChild == null)
			{
				leftChild = new BinaryTree<T>(item);
				node = leftChild;
			}
			else
			{
				node = leftChild.insert(item);
			}
		}
		return node;
	}

	// ITraversable interface

	public ArrayList<T> nlr()
	{
		ArrayList<T> list = new ArrayList<T>();

		list.add(data);
		if (leftChild != null) list.addAll(leftChild.nlr());
		if (rightChild != null) list.addAll(rightChild.nlr());
		
		return list;
	}
	
	public ArrayList<T> lnr()
	{
		ArrayList<T> list = new ArrayList<T>();

		if (leftChild != null) list.addAll(leftChild.lnr());
		list.add(data);
		if (rightChild != null)	list.addAll(rightChild.lnr());
		
		return list;
	}
	
	public ArrayList<T> lrn()
	{
		ArrayList<T> list = new ArrayList<T>();

		if (leftChild != null) list.addAll(leftChild.lrn());
		if (rightChild != null) list.addAll(rightChild.lrn());
		list.add(data);

		return list;
	}
	
	public ArrayList<T> bfs()
	{
		ArrayList<T> list = new ArrayList<T>();
		Queue<BinaryTree<T>> queue = new LinkedList<BinaryTree<T>>();
		queue.add(this);
		while (!queue.isEmpty())
		{
			BinaryTree<T> node = queue.remove();
			list.add(node.getItem());
			if (node.leftChild != null) queue.add(node.leftChild);
			if (node.rightChild != null) queue.add(node.rightChild);
		}
		return list;
	}

	// IMeasurable interface

	public int size()
	{
		return nlr().size();
	}

	public int height()
	{
		return depth(this) - 1;
	}

	protected int depth(BinaryTree<T> node)
	{
		int depth = 0;
		if (node != null)
		{
			int left_depth = depth(node.leftChild);
			int right_depth = depth(node.rightChild);

			depth = left_depth > right_depth ? left_depth+1 : right_depth+1;
		}
		return depth;
	}

	protected int balance()
	{
		return depth(rightChild) - depth(leftChild);
	}
	
	// IRotatable

	public ITree<T> rotateLeft()
	{
		BinaryTree<T> pivot = rightChild;
		rightChild = pivot.leftChild;
		pivot.leftChild = this;
		return pivot;
	}

	public ITree<T> rotateRight()
	{
		BinaryTree<T> pivot = leftChild;
		leftChild = pivot.rightChild;
		pivot.rightChild = this;
		return pivot;
	}

	// Data

	protected T data;
	protected BinaryTree<T> rightChild = null;
	protected BinaryTree<T> leftChild = null;
}
