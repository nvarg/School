import java.util.Stack;

public class AVLTree<T extends Comparable<T>> extends BinaryTree<T>
{
	public AVLTree(T item)
	{
		super(item);
	}

	public AVLTree(T item, AVLTree<T> parent)
	{
		super(item);
	}
	public AVLTree(AVLTree<T> other)
	{
		super(other.data);
		leftChild = other.leftChild;
		rightChild = other.rightChild;
	}

	@Override
	public ITree<T> insert(T item)
	{
		ITree<T> node = find(item);
		AVLTree<T> cursor = this;
		Stack<AVLTree<T>> path = new Stack<>();
		while (node == null)
		{
			int compare = item.compareTo(cursor.data);
			if (compare > 0)
			{
				if (cursor.rightChild == null)
				{
					cursor.rightChild = new AVLTree<T>(item);
					node = cursor.rightChild;
				}
				else
				{
					path.push(cursor);
					cursor = (AVLTree<T>) cursor.rightChild;
				}
			}
			else if (compare < 0)
			{
				if (cursor.leftChild == null)
				{
					cursor.leftChild = new AVLTree<T>(item);
					node = cursor.leftChild;
				}
				else
				{
					path.push(cursor);
					cursor = (AVLTree<T>) cursor.leftChild;
				}
			}
		}

		((AVLTree<T>) cursor).rebalance(path);
		return node;
	}

	private ITree<T> rebalance(Stack<AVLTree<T>> path)
	{
		AVLTree<T> cursor = this;

		while(!path.isEmpty())
		{
			cursor = path.pop();
			int balance = cursor.balance();
			if (balance > 1) // right imbalance
			{
				if (cursor.rightChild.balance() < 0) // left subtree
				{
					cursor.rightChild.rotateRight();
				}
				cursor.rotateLeft();
			}
			else if (balance < -1) // left imbalance
			{
				if (cursor.leftChild.balance() > 0) // right subtree
				{
					cursor.leftChild.rotateLeft();
				}
				cursor.rotateRight();
			}
		}

		return cursor;
	}


	@Override
	public ITree<T> rotateLeft()
	{
		AVLTree<T> pivot = (AVLTree<T>) rightChild;
		T temp = pivot.data;
		pivot.data = data;
		data = temp;
		AVLTree<T> X = leftChild == null ? null : (AVLTree<T>)leftChild;
		AVLTree<T> Y = pivot.leftChild == null ? null : (AVLTree<T>)pivot.leftChild;
		AVLTree<T> Z = pivot.rightChild == null ? null : (AVLTree<T>)pivot.rightChild;
		leftChild = pivot;
		rightChild = Z;
		pivot.leftChild = X;
		pivot.rightChild = Y;
		return this;
	}

	@Override
	public ITree<T> rotateRight()
	{
		AVLTree<T> pivot = (AVLTree<T>) leftChild; 
		T temp = pivot.data;
		pivot.data = data;
		data = temp;
		AVLTree<T> X = pivot.leftChild == null ? null : (AVLTree<T>) pivot.leftChild;
		AVLTree<T> Y = pivot.rightChild == null ? null : (AVLTree<T>) pivot.rightChild;
		AVLTree<T> Z = rightChild == null ? null : (AVLTree<T>) rightChild;
		rightChild = pivot;
		leftChild = X;
		pivot.leftChild = Y;
		pivot.rightChild = Z;
		return this;
	}
	

}
