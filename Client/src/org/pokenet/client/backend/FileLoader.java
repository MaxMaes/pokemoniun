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
import org.newdawn.slick.Color;
import org.pokenet.client.GameClient;
import org.pokenet.client.constants.Language;
import de.matthiasmann.twl.renderer.DynamicImage;
import de.matthiasmann.twl.renderer.Image;
import de.matthiasmann.twl.renderer.Texture;

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
	 * @param path
	 *        The path to the wanted file.
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
	 * @param path
	 *        The path to the wanted file.
	 * @return Returns a BufferedReader for a text file
	 */
	public static BufferedReader loadTextFile(String path)
	{
		return new BufferedReader(new InputStreamReader(loadFile(path)));
	}

	/**
	 * Returns an Image object
	 */
	public static Image loadImage(String path)
	{
		File fl = new File(path);
		Image img = null;
		if(!fl.exists())
		{
			return null;
		}
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

	/**
	 * Returns an Image object
	 */
	public static Texture loadImageAsTexture(String path)
	{
		File fl = new File(path);
		Texture text = null;
		try
		{
			URL flURL = fl.getAbsoluteFile().toURI().toURL();
			text = GameClient.getInstance().getRenderer().loadTexture(flURL, "RGBA", "linear");
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
		return text;
	}

	public static Image toTWLImage(org.newdawn.slick.Image image)
	{
		ByteBuffer bb = ByteBuffer.allocateDirect(image.getWidth() * image.getHeight() * 4);

		for(int i = 0; i < image.getHeight(); i++)
		{
			for(int j = 0; j < image.getWidth(); j++)
			{
				Color c = image.getColor(j, i);
				byte alpha = (byte) c.getAlphaByte();
				byte red = (byte) c.getRedByte();
				byte green = (byte) c.getGreenByte();
				byte blue = (byte) c.getBlueByte();

				bb.put(red);
				bb.put(green);
				bb.put(blue);
				bb.put(alpha);
			}
		}
		bb.flip();

		DynamicImage dymage = GameClient.getInstance().getRenderer().createDynamicImage(image.getWidth(), image.getHeight());
		dymage.update(bb, DynamicImage.Format.RGBA);
		return dymage;
	}

	public static ByteBuffer loadImageAsByteBuffer(org.newdawn.slick.Image image)
	{
		ByteBuffer bb = ByteBuffer.allocateDirect(image.getWidth() * image.getHeight() * 4);

		for(int i = 0; i < image.getHeight(); i++)
		{
			for(int j = 0; j < image.getWidth(); j++)
			{
				Color c = image.getColor(j, i);
				byte alpha = (byte) c.getAlphaByte();
				byte red = (byte) c.getRedByte();
				byte green = (byte) c.getGreenByte();
				byte blue = (byte) c.getBlueByte();

				bb.put(red);
				bb.put(green);
				bb.put(blue);
				bb.put(alpha);
			}
		}
		bb.flip();
		return bb;
	}
}
