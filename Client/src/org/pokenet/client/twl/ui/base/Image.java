package org.pokenet.client.twl.ui.base;

import org.pokenet.client.backend.FileLoader;
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
	 * Creates an empty image object.
	 * 
	 * @param path
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
			throw new IllegalArgumentException();
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
			throw new IllegalArgumentException();
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
		return img.getHeight();
	}

	@Override
	public int getPreferredWidth()
	{
		return img.getWidth();
	}

	@Override
	public void layout()
	{
		setSize(img.getWidth(), img.getHeight());
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
}
