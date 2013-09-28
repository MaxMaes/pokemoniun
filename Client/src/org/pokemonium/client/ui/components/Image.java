package org.pokemonium.client.ui.components;

import org.pokemonium.client.backend.FileLoader;
import de.matthiasmann.twl.GUI;

/**
 * This way I can add images as widgets, probably easier than always overriding paintWidget.
 * 
 * @author Myth1c
 */
public class Image extends de.matthiasmann.twl.Widget
{
	private de.matthiasmann.twl.renderer.Image img;

	/**
	 * Creates an empty image object;
	 */
	public Image()
	{

	}

	/**
	 * Loads the image from the given path. The parameter cannot be null.
	 * It also adjusts the size to the given image.
	 * 
	 * @param path
	 */
	public Image(String path)
	{
		if(path != null)
		{
			img = FileLoader.loadImage(path);
		}
		else
		{
			throw new IllegalArgumentException("Path cannot be null");
		}
		if(img == null)
		{
			throw new IllegalArgumentException("Could not load image from path: " + path);
		}
	}

	/**
	 * Creates a widget image from the given renderer image. The parameter cannot be null.
	 * It also adjusts the size to the given image.
	 * 
	 * @param path
	 */
	public Image(de.matthiasmann.twl.renderer.Image image)
	{
		if(image != null)
		{
			img = image;
		}
		else
		{
			throw new IllegalArgumentException("Image cannot be null");
		}
	}

	/**
	 * Sets the image. Can be null.
	 * It also adjusts the size to the given image.
	 * 
	 * @param image
	 */
	public void setImage(de.matthiasmann.twl.renderer.Image image)
	{
		img = image;
	}

	@Override
	public int getPreferredHeight()
	{
		if(img != null)
		{
			return img.getHeight();
		}
		else
		{
			return 0;
		}
	}

	@Override
	public int getPreferredWidth()
	{
		if(img != null)
		{
			return img.getWidth();
		}
		else
		{
			return 0;
		}
	}

	@Override
	public void layout()
	{
		if(img != null)
		{
			setSize(img.getWidth(), img.getHeight());
		}
	}

	@Override
	public void paintWidget(GUI gui)
	{
		super.paintWidget(gui);
		if(img != null)
		{
			img.draw(getAnimationState(), getX(), getY(), getWidth(), getHeight());
		}
	}

	public de.matthiasmann.twl.renderer.Image getImage()
	{
		return img;
	}
}
