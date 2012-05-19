package org.pokenet.client.backend;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.pokenet.client.GameClient;

/**
 * A simple file loader to make our lives easier
 * 
 * @author ZombieBear
 */
public class FileLoader
{
	/**
	 * Loads a file as an InputStream.
	 * 
	 * @param path The path to the wanted file.
	 * @return Returns an InputStream of a file.
	 * @throws FileNotFoundException 
	 */
	public static InputStream loadFile(String path)
	{
		try
		{
			return new FileInputStream(path);
		}
		catch(FileNotFoundException fnfe)
		{
			System.out.println("Path to the wanted file: " + path);
			if(path.contains("language") && !path.contains("english"))
				GameClient.setLanguage("english");
		}
		return null;
	}

	/**
	 * Loads a text file and gets it ready for parsing.
	 * 
	 * @param path The path to the wanted file.
	 * @return Returns a BufferedReader for a text file
	 * @throws FileNotFoundException
	 */
	public static BufferedReader loadTextFile(String path)
	{
		return new BufferedReader(new InputStreamReader(loadFile(path)));
	}
}
