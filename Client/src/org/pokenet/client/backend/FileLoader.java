package org.pokenet.client.backend;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;

import org.pokenet.client.GameClient;
import org.pokenet.client.constants.Language;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

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
	 */
	public static InputStream loadFile(String path)
	{
		FileInputStream inputStream = null;
		try
		{
			inputStream = new FileInputStream(path);
		}
		catch(FileNotFoundException fnfe)
		{
			if(path.contains("language") && !path.contains(Language.ENGLISH))
				GameClient.getInstance().setLanguage(Language.ENGLISH);
		}
		return inputStream;
	}

	/**
	 * Loads a text file and gets it ready for parsing.
	 * 
	 * @param path The path to the wanted file.
	 * @return Returns a BufferedReader for a text file
	 */
	public static BufferedReader loadTextFile(String path)
	{
		return new BufferedReader(new InputStreamReader(loadFile(path)));
	}
	
	/**
	 * Decodes a PNG file using TWL's PNG decoder
	 */
	public static ByteBuffer loadPNG(String path)
	{
		InputStream input = null;
		ByteBuffer buffer = null;
		try {
			input = new FileInputStream(path);
			PNGDecoder decoder = new PNGDecoder(input);

			buffer = ByteBuffer.allocateDirect(4*decoder.getWidth()*decoder.getHeight());
			decoder.decode(buffer, decoder.getWidth()*4, Format.RGBA);
			buffer.flip();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

		return buffer;
	}
}
