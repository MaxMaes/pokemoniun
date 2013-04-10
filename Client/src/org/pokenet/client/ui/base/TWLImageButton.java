package org.pokenet.client.ui.base;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.renderer.AnimationState;
import de.matthiasmann.twl.renderer.Image;
import de.matthiasmann.twl.renderer.lwjgl.LWJGLRenderer;

/**
 * An ImageButton class based on the TWL Library.
 * @author Myth1c
 *
 */

public class TWLImageButton extends Button {
	private Image image;
	
	/**
	 * Creates the imagebutton with given image
	 * @param img The image
	 */
	public TWLImageButton(Image img) {
		
		image = img;
	}
	
	/**
	 * Sets the image to be displayed on the button
	 * @param img The image to be set
	 */
	public void setImage(Image img)	{
		image = img;
	}
	
	/**
	 * Paints the button first and then the image on top of it.
	 */
	@Override
	public void paintWidget(GUI gui) {
		super.paintWidget(gui);
		image.draw(getAnimationState(), getInnerX()-4, getInnerY()-4, image.getWidth(), image.getHeight());
	}
}