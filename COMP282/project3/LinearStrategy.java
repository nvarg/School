import java.io.*;
import java.util.*;

public class LinearStrategy implements IRegistrar {

	public LinearStrategy(String filename)
	{
		this.filename = filename;
	}

	public String load(String key) throws IOException {
		byte[] chars = null;
		try(DBLite db = new DBLite(filename))
		{
			chars = db.read();
		}

		String[] dbArray = (new String(chars)).split("\0");
		String value = "";

		for(int i = 0; i < dbArray.length; i+=2)
		{
			if(dbArray[i].equals(key))
			{
				value = dbArray[i+1];
				break;
			}
		}

		return value;
	}

	public void store(String key, String value) throws IOException {
		try(DBLite db = new DBLite(filename))
		{
			ArrayList<String> dbArray = new ArrayList<String>(Arrays.asList((new String (db.read()).split("\0"))));
			if (dbArray.size() == 1) dbArray = new ArrayList<String>();
			if (!this.load(key).equals(""))
			{
				for(int i = 0; i < dbArray.size(); i+=2)
				{
					if(dbArray.get(i).equals(key))
					{
						dbArray.set(i+1, value);
						db.write(String.join("\0", dbArray.toArray(new String[dbArray.size()])));
						break;
					}
				}
			}
			else
			{
				dbArray.add(key);
				dbArray.add(value);
				db.write(String.join("\0", dbArray));
			}
		}
	}

	private String filename = "";
}
