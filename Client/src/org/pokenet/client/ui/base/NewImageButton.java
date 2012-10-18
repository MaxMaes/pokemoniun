package org.pokenet.client.ui.base;

import mdes.slick.sui.Label;
import mdes.slick.sui.event.ActionEvent;
import mdes.slick.sui.event.ActionListener;
import mdes.slick.sui.event.MouseEvent;
import mdes.slick.sui.event.MouseListener;
import org.newdawn.slick.Image;

/**
 * A custom implementation of an imagebutton, features transparant background.
 * 
 * @author Myth1c
 */
public class NewImageButton extends Label implements MouseListener
{
	private boolean enabled;
	private Image[] images = new Image[3];
	private ActionListener listener;

	public NewImageButton()
	{
		super();
		addMouseListener(this);
	}

	public NewImageButton(Image buttonDown, Image neutral, Image disabled)
	{
		super();
		images[0] = buttonDown;
		images[1] = neutral;
		images[2] = disabled;
		setImage(images[1]);
		addMouseListener(this);
	}

	public void disable()
	{
		setImage(images[2]);
		setSize(images[2].getWidth(), images[2].getHeight());
		enabled = false;
	}

	public void enable()
	{
		setImage(images[1]);
		setSize(images[1].getWidth(), images[1].getHeight());
		enabled = true;
	}

	public boolean enabled()
	{
		return enabled;
	}

	@Override
	public void mouseDragged(MouseEvent arg0)
	{
	}

	@Override
	public void mouseEntered(MouseEvent arg0)
	{
	}

	@Override
	public void mouseExited(MouseEvent arg0)
	{
	}

	@Override
	public void mouseMoved(MouseEvent arg0)
	{
	}

	@Override
	public void mousePressed(MouseEvent arg0)
	{
		if(enabled)
		{
			setImage(images[0]);
			setSize(images[0].getWidth(), images[0].getHeight());
			listener.actionPerformed(new ActionEvent(this, "Down"));
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0)
	{
		if(enabled)
		{
			setImage(images[1]);
			setSize(images[1].getWidth(), images[1].getHeight());
			listener.actionPerformed(new ActionEvent(this, "Up"));
		}
	}

	public void setActionListener(ActionListener listener)
	{
		this.listener = listener;
		enabled = true;
	}

	/**
	 * Sets this button's images
	 * 
	 * @param buttonDown
	 * @param neutral
	 * @param disabled
	 */
	public void setImages(Image buttonDown, Image neutral, Image disabled)
	{
		images[0] = buttonDown;
		images[1] = neutral;
		images[2] = disabled;
		setImage(images[1]);
	}

}
