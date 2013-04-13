package org.pokenet.client.backend;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.ByteBuffer;
import org.pokenet.client.GameClient;
import org.pokenet.client.constants.Language;
import de.matthiasmann.twl.renderer.DynamicImage;
import de.matthiasmann.twl.renderer.Image;
import de.matthiasmann.twl.renderer.Texture;
import de.matthiasmann.twl.utils.PNGDecoder;

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
	public static Image loadPNG(String path)
	{
		ByteBuffer buffer = null;
		try(InputStream input = new FileInputStream(path))
		{
			PNGDecoder decoder = new PNGDecoder(input);
			buffer = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
			decoder.decode(buffer, decoder.getWidth() * 4, de.matthiasmann.twl.utils.PNGDecoder.Format.RGBA);
			buffer.flip();
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
		DynamicImage dynImage = GameClient.getInstance().getRenderer().createDynamicImage(800, 600);
		dynImage.update(buffer, de.matthiasmann.twl.renderer.DynamicImage.Format.RGBA);
		return dynImage;
	}

	/**
	 * Returns an Image object
	 */
	public static Image loadImage(String path)
	{
		File fl = new File(path);
		Image img = null;
		try
		{
			URL flURL = fl.getAbsoluteFile().toURI().toURL();
			Texture text = GameClient.getInstance().getRenderer().loadTexture(flURL, "RGBA", "linear");
			img = text.getImage(0, 0, text.getWidth(), text.getHeight(), null, false, Texture.Rotation.NONE);
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
		return img;
	}
}
