import java.util.ArrayList;

public interface ITraversable<T> {
	public ArrayList<T> nlr(); // Pre-order
	public ArrayList<T> lnr(); // In-order
	public ArrayList<T> lrn(); // Post-order
	public ArrayList<T> bfs(); // Breadth-first
}
