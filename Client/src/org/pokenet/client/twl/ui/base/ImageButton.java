package org.pokenet.client.twl.ui.base;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.renderer.Image;

/**
 * An ImageButton class based on the TWL Library.
 * 
 * @author Myth1c
 */

public class ImageButton extends Button
{
	private Image image;
	private Image pressedImage;
	private Image hoverImage;

	private Image currImage;

	private int x;
	private int y;
	private ImageAlignment alignment = ImageAlignment.CENTER;

	public static enum ImageAlignment
	{
		CENTER, CUSTOM
	};

	/**
	 * Creates the imagebutton without an image
	 * 
	 * @param img The image
	 */
	public ImageButton()
	{
		super();
	}

	/**
	 * Creates the imagebutton without an image and with text
	 * 
	 * @param img The image
	 */
	public ImageButton(String text)
	{
		super(text);
	}

	/**
	 * Creates the imagebutton with an image and with text
	 * 
	 * @param img The image
	 */
	public ImageButton(Image img, String text)
	{
		super(text);
		image = img;
		currImage = image;
		setSize(currImage.getWidth(), currImage.getHeight());
	}

	/**
	 * Creates the imagebutton with given image
	 * 
	 * @param img The image
	 */
	public ImageButton(Image img)
	{
		super();
		image = img;
		currImage = image;
		setSize(currImage.getWidth(), currImage.getHeight());
	}

	/**
	 * Creates the imagebutton with given images
	 * 
	 * @param img The image
	 */
	public ImageButton(Image img, Image hover, Image pressed)
	{
		super();
		image = img;
		hoverImage = hover;
		pressedImage = pressed;
		currImage = image;
		getModel().addStateCallback(new Runnable()
		{
			@Override
			public void run()
			{
				if(getModel().isHover())
				{
					currImage = hoverImage;
				}
				else if(getModel().isPressed())
				{
					currImage = pressedImage;
				}
				else
				{
					currImage = image;
				}
			}
		});
		setSize(currImage.getWidth(), currImage.getHeight());
	}

	/**
	 * Sets the image to be displayed on the button
	 * 
	 * @param img The image to be set
	 */
	public void setImage(Image img)
	{
		image = img;
		currImage = image;
	}

	/**
	 * Sets the position of the image based on the position of the button.
	 * 
	 * @param xPos
	 * @param yPos
	 */
	public void setImagePosition(int xPos, int yPos)
	{
		x = xPos;
		y = yPos;
		alignment = ImageAlignment.CUSTOM;
	}

	/**
	 * Paints the button first and then the image on top of it.
	 */
	@Override
	public void paintWidget(GUI gui)
	{
		super.paintWidget(gui);
		if(currImage != null)
		{
			if(alignment == ImageAlignment.CENTER)
			{
				currImage.draw(getAnimationState(), getInnerX() + (getWidth() / 2) - (currImage.getWidth() / 2) - 5, getInnerY() + (getHeight() / 2) - (currImage.getHeight() / 2),
						currImage.getWidth(), currImage.getHeight());
			}
			else
			{
				currImage.draw(getAnimationState(), x + getInnerX(), y + getInnerY(), currImage.getWidth(), currImage.getHeight());
			}
		}
	}
}