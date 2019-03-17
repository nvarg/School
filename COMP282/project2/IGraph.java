import java.util.ArrayList;

public interface IGraph<T>
{
	public ArrayList<T> getVertices();
	public ArrayList<ArrayList<Float>> getEdges();

	public void addVertex(T v);
	public void addEdge(T v1, T v2, float w);

	public void removeVertex(T v);
	public void removeEdge(T v1, T v2);
}
