import java.io.*;

public interface IRegistrar {
	public String load(String key) throws IOException;
	public void store(String key, String value) throws IOException;
}
