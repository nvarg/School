import java.util.ArrayList;
import java.lang.IllegalArgumentException;
import java.util.Stack;

public class Graph<T> implements IGraph<T>, IConnected<T>
{
	public ArrayList<T> getVertices()
	{
		return (ArrayList<T>) vertecies.clone();
	}

	public ArrayList<ArrayList<Float>> getEdges()
	{
		return (ArrayList<ArrayList<Float>>) edges.clone();
	}

	public void addVertex(T v)
	{
		if (vertecies.contains(v)) return;

		vertecies.add(v);

		ArrayList<Float> row = new ArrayList<Float>();
		edges.add(row);

		for(int i = 1; i<vertecies.size(); i++)
		{
			row.add(0.0f);
		}

		for(ArrayList<Float> col : edges)
		{
			col.add(0.0f);
		}
	}

	public void addEdge(T v1, T v2, float w)
	{
		if (!(vertecies.contains(v1) && vertecies.contains(v2)))
				throw new IllegalArgumentException("One of the vertecies in this edge does not exist");
		if (v2 == v1) return;

		int row = vertecies.indexOf(v1);
		int col = vertecies.indexOf(v2);

		edges.get(min(col, row)).set(max(col, row), w);
	}

	private int min(int v1, int v2)
	{
		return v1 < v2 ? v1 : v2;
	}

	private int max(int v1, int v2)
	{
		return v1 > v2 ? v1 : v2;
	}

	public void removeVertex(T v)
	{
		if (!vertecies.contains(v)) return;

		int index = vertecies.indexOf(v);

		vertecies.remove(index);
		edges.remove(index);
		for(ArrayList<Float> col : edges)
		{
			col.remove(index);
		}
	}

	public void removeEdge(T v1, T v2)
	{
		addEdge(v1, v2, 0);
	}


	public ArrayList<T> componentOf(T v)
	{
		ArrayList<Boolean> visited = new ArrayList<Boolean>();
		for (int i = 0; i < vertecies.size(); i++)
		{
			visited.add(false);
		}

		ArrayList<T> connected = new ArrayList<T>();

		int start = vertecies.indexOf(v);
		if (start == -1) return connected;

		Stack<Integer> stack = new Stack<Integer>();

		stack.add(start);
		connected.add(vertecies.get(start));
		visited.set(start, true);

		while(!stack.empty())
		{
			int row = stack.pop();
			for (int col = 0; col < vertecies.size(); col++)
			{
				if (edges.get(min(row,col)).get(max(row,col)) != 0 && !visited.get(col))
				{
					stack.add(col);
					connected.add(vertecies.get(col));
					visited.set(col, true);
				}

			}
		}

		return connected;
	}

	private ArrayList<T> vertecies = new ArrayList<T>();
	private ArrayList<ArrayList<Float>> edges = new ArrayList<ArrayList<Float>>();
}
