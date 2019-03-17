import java.io.*;

public interface IReadWritable {
	public byte[] read() throws IOException;
	public void write(byte[] value) throws IOException;
	public void write(String value) throws IOException;
}
