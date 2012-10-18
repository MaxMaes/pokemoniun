/* Credits to Cobe AKA Makarov from RaGEZONE! */
package org.pokenet.server;

import java.io.FileInputStream;
import java.util.Properties;

public class Configuration
{
	private Properties Config;

	public Configuration()
	{
		Config = new Properties();
	}

	public int __Size()
	{
		return Config.size();
	}

	public String GrabValue(String key)
	{
		return Config.getProperty(key);
	}

	public boolean Load(String File)
	{
		try
		{
			Config.load(new FileInputStream(File));
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
}