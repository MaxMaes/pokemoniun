package org.pokenet.client.ui.base;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.renderer.Image;

/**
 * An ImageButton class based on the TWL Library.
 * 
 * @author Myth1c
 */

public class TWLImageButton extends Button
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
	public TWLImageButton()
	{
		super();
	}

	/**
	 * Creates the imagebutton without an image and with text
	 * 
	 * @param img The image
	 */
	public TWLImageButton(String text)
	{
		super(text);
	}

	/**
	 * Creates the imagebutton with an image and with text
	 * 
	 * @param img The image
	 */
	public TWLImageButton(Image img, String text)
	{
		super(text);
		image = img;
		currImage = image;
	}

	/**
	 * Creates the imagebutton with given image
	 * 
	 * @param img The image
	 */
	public TWLImageButton(Image img)
	{
		super();
		image = img;
		currImage = image;
	}

	/**
	 * Creates the imagebutton with given images
	 * 
	 * @param img The image
	 */
	public TWLImageButton(Image img, Image hover, Image pressed)
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
	}

	/**
	 * Sets the image to be displayed on the button
	 * 
	 * @param img The image to be set
	 */
	public void setImage(Image img)
	{
		image = img;
	}

	/**
	 * Sets the position of the image based on the position of the button
	 * 
	 * @param xPos
	 * @param yPos
	 */
	public void setImagePosition(int xPos, int yPos)
	{
		x = xPos + getInnerX();
		y = yPos + getInnerY();
	}

	/**
	 * Paints the button first and then the image on top of it.
	 */
	@Override
	public void paintWidget(GUI gui)
	{
		super.paintWidget(gui);
		if(alignment == ImageAlignment.CENTER)
			currImage.draw(getAnimationState(), getInnerX() - 4, getInnerY() - 4, currImage.getWidth(), currImage.getHeight());
		else
			currImage.draw(getAnimationState(), x, y, currImage.getWidth(), currImage.getHeight());
	}
}