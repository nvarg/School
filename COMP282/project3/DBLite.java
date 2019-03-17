import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DBLite implements IReadWritable, IRegistrar, AutoCloseable {

	public static enum Strategy { LINEAR, HASHING };

	public DBLite(String filename, Strategy strategy) {
		this.filename = filename;
		this.strategy = createStrategy(strategy, filename);
	}

	public DBLite(String filename) {
		this(filename, Strategy.LINEAR);
	}

	public byte[] read() throws IOException {
		byte[] data = null;
		if ((new File(filename + ".dbl")).exists()){
			data = Files.readAllBytes(Paths.get(filename + ".dbl"));
		} else {
			data = new byte[0];
		}

		return data;
	}

	public String readString() throws IOException {
		return new String(this.read());
	}

	public void write(byte[] value) throws IOException {
		try (FileOutputStream file = new FileOutputStream(filename + ".dbl")) {
        		file.write(value);
		}
	}

	public void write(String value) throws IOException {
		write(value.getBytes());
    	}



	public String load(String key) throws IOException {
		return strategy.load(key);
	}

	public void store(String key, String value) throws IOException {
		strategy.store(key, value);
	}

	private IRegistrar createStrategy(Strategy s, String filename)
	{
		switch(s) {
		case LINEAR: return new LinearStrategy(filename);
		case HASHING: return new HashingStrategy(filename);
		default: return null;
		}
	}

	public void close() {}

	private IRegistrar strategy = null;
	private String filename = "";
}
