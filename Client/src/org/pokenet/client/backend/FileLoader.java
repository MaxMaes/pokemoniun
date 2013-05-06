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

	public static de.matthiasmann.twl.renderer.Image toTWLImage(org.newdawn.slick.Image image, boolean hasAlpha)
	{
		// conver the image into a byte buffer by reading each pixel in turn
		int len = 4 * image.getWidth() * image.getHeight();
		if(!hasAlpha)
		{
			len = 3 * image.getWidth() * image.getHeight();
		}

		ByteBuffer out = ByteBuffer.allocate(len);
		org.newdawn.slick.Color c;

		for(int y = image.getHeight() - 1; y >= 0; y--)
		{
			for(int x = 0; x < image.getWidth(); x++)
			{
				c = image.getColor(x, y);

				out.put((byte) (c.r * 255.0f));
				out.put((byte) (c.g * 255.0f));
				out.put((byte) (c.b * 255.0f));
				if(hasAlpha)
				{
					out.put((byte) (c.a * 255.0f));
				}
			}
		}
		DynamicImage dynImage = GameClient.getInstance().getRenderer().createDynamicImage(image.getWidth(), image.getHeight());
		dynImage.update(out, de.matthiasmann.twl.renderer.DynamicImage.Format.RGBA);
		return dynImage;
	}
}
