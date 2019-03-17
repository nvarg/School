import java.io.*;
import java.util.*;

public class HashingStrategy implements IRegistrar {

	public HashingStrategy(String filename)
	{
		this(filename, 2);
	}

	public HashingStrategy(String filename, int max)
	{
		this.filename = filename;
		this.lastModified = 0;
		this.current = 0;
		this.max = max;
		this.keys = new String[max];
		this.values = new String[max];
	}

	public String load(String key) throws IOException {
		if (lastModified != lastModified(filename)) reloadFile();

		for(int i = hash(key) % max; keys[i] != null; i = (i + 1) % max)
		{
			if(keys[i].equals(key))
				return values[i];
		}

		return "";
	}

	public void store(String key, String value) throws IOException {
		if (lastModified != lastModified(filename)) reloadFile();

		put(key, value);

		String[] tempKeys = Arrays.stream(keys)
			.filter(s -> s != null && !s.equals(""))
			.toArray(String[]::new);
		String[] tempValue = Arrays.stream(values)
			.filter(s -> s != null)
			.toArray(String[]::new);

		String storeKeys = "";
		String storeValue = "";
		for(int j = 0; j < tempKeys.length; j ++)
		{
			storeKeys += tempKeys[j] + "," + storeValue.length() + "\0";
			storeValue += "\0" + tempValue[j];
		}

		try(DBLite db = new DBLite(filename))
		{
			db.write(storeKeys + storeValue);
			lastModified = lastModified(filename);
		}
	}

	private void put(String key, String value)
	{
		if (current >= max/2) resize(2*max);

		int i;
		for (i = hash(key) % max; keys[i] != null; i = (i + 1) % max) {
			if (keys[i].equals(key)) {
				values[i] = value;
				break;
			}
		}

		keys[i] = key;
		values[i] = value;
		current++;
	}

	private void resize(int capacity)
	{
		HashingStrategy temp = new HashingStrategy(filename, capacity);
		for (int i = 0; i < max; i++)
		{
			if (keys[i] != null && keys[i] != "") {
				temp.put(keys[i], values[i]);
			}
		}

		keys = temp.keys;
		values = temp.values;
		max = temp.max;
	}

	private void reloadFile() throws IOException
	{
		String[] unhashedPairs = headerString().split("\0");
		String dataString = dataString();

		for(String pair : unhashedPairs)
		{
			String[] splitPair = pair.split(",");
			int beg = Integer.valueOf(splitPair[1]);
			int end = dataString.indexOf("\0", beg);
			String value = dataString.substring(beg, end == -1 ? dataString.length() : end);
			put(splitPair[0], value);
		}
	}

	static private int hash(String key) {
		int h = 0;
		for (int i = key.length() - 1; i >= 0; --i)
			h += (int) key.charAt(i);
		return h;
	}

	static private long lastModified(String filename)
	{
		File file = new File(filename + ".dbl");
		return file.lastModified();
	}

	private String dataString() throws IOException
	{
		String dataString = "";
		try(DBLite db = new DBLite(filename))
		{
			dataString = new String(db.read());
		}
		int headerEnd = dataString.indexOf("\0\0");
		headerEnd = headerEnd > 0 ? headerEnd : -2;
		dataString = dataString.substring(headerEnd + 2, dataString.length());
		return dataString;
	}

	private String headerString() throws IOException
	{
		String headerString = "";
		try(DBLite db = new DBLite(filename))
		{
			headerString = new String(db.read());
		}
		int headerEnd = headerString.indexOf("\0\0");
		headerEnd = headerEnd > 0 ? headerEnd : 0;
		headerString = headerString.substring(0, headerEnd);
		return headerString;
	}

	private String filename = "";
	private long lastModified = 0;
	private int current = 0;
	private int max = 4;
	private String[] keys = new String[max];
	private String[] values = new String[max];
}
