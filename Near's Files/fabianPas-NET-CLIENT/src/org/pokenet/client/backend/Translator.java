package org.pokenet.client.backend;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.pokenet.client.GameClient;

public class Translator
{
	private static Translator m_instance;

	/**
	 * Returns a list of translated text
	 * 
	 * @param filename
	 * @return
	 */
	public List<String> translateText(String filename)
	{
		List<String> translated = new ArrayList<String>();
		String respath = System.getProperty("res.path");
		if(respath == null)
			respath = "";
		try
		{
			String path = respath + "res/language/" + GameClient.getLanguage() + "/UI/" + filename + ".txt";
			InputStream in = new FileInputStream(path);
			if(in != null)
			{
				BufferedReader f = new BufferedReader(new InputStreamReader(in));
				Scanner reader = new Scanner(f);
				while(reader.hasNextLine())
				{
					translated.add(reader.nextLine().replaceAll("/n", "\n"));
				}
				reader.close();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return translated;
	}

	/**
	 * Returns the instance of translator
	 * 
	 * @return
	 */
	public static Translator getInstance()
	{
		if(m_instance == null)
			m_instance = new Translator();
		return m_instance;
	}

	public static List<String> translate(String filename)
	{
		return Translator.getInstance().translateText(filename);
	}
}
