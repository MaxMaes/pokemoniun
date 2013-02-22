package org.pokenet.client.backend;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.pokenet.client.GameClient;
import org.pokenet.client.constants.Language;

public class Translator
{
	private static Translator m_instance;

	/**
	 * Returns the instance of translator
	 * 
	 * @return Instance of the translator.
	 **/
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

	/**
	 * Returns a list of translated text
	 * 
	 * @param The requested file.
	 * @return The translated version of the requested file
	 **/
	public List<String> translateText(String filename)
	{
		List<String> translated = new ArrayList<String>();
		String respath = System.getProperty("res.path");
		if(respath == null)
			respath = "";
		try
		{
			String path = respath + "res/language/" + GameClient.getInstance().getLanguage() + "/UI/" + filename + ".txt";
			InputStream in = new FileInputStream(path);
			BufferedReader f = new BufferedReader(new InputStreamReader(in));
			Scanner reader = new Scanner(f);
			while(reader.hasNextLine())
				translated.add(reader.nextLine().replaceAll("/n", "\n"));
		}
		catch(FileNotFoundException fnfe)
		{
			// This should not happen anymore, but to be 9000% safe we keep it.
			GameClient.getInstance().log("INFO: This language is not available yet, reverting to English!");
			GameClient.getInstance().setLanguage(Language.ENGLISH);
		}
		finally
		{
			try
			{
				InputStream in = new FileInputStream(respath + "res/language/" + GameClient.getInstance().getLanguage() + "/UI/" + filename + ".txt");
				BufferedReader f = new BufferedReader(new InputStreamReader(in));
				Scanner reader = new Scanner(f);
				while(reader.hasNextLine())
					translated.add(reader.nextLine().replaceAll("/n", "\n"));
			}
			catch(FileNotFoundException fnfe)
			{
				fnfe.printStackTrace();
			}
		}
		return translated;
	}
}
