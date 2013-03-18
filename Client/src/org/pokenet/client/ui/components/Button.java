/**
 * 
 */
package org.pokenet.client.ui.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.event.EventListenerList;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.gui.GUIContext;

/**
 * @author chappie112
 *
 */
public class Button extends Label
{
	/**constants for the different states */
	public static final int UP = -10;
	public static final int DOWN = -9;
	public static final int HOVER = -8;
	/**constants for the current state */
	private int state = UP;
	/**the image to draw when the mouse hovers over the button */
	private Image hoverImage = null;
	/**the image to draw when the button is pressed down */
	private Image downImage = null;
	/**the image to draw when the button is disabled */
	private Image disabledImage = null;
	/**the image to draw normally */
	private Image image = null;
	/** the action command, initially null */
	private String actionCommand = null;
	private boolean enabled = true;
	protected EventListenerList listenerList = new EventListenerList();
	
	private boolean pressedOutside = false;
	
	public Button(String text)
	{
		this();
		setText(text);
		actionCommand = text;
		fitToText();
		setPosition(0,0);
	}
	
	public Button(Image img)
	{
		this();
		setImage(img);
		setPosition(0,0);
	}
	
	public Button()
	{
		this(true);
		
	}
	
	public Button(boolean updateAppearance)
	{
		super();
		//setRequestFocusEnabled(true);
		//setVerticalPadding(5);
		//setHorizontalPadding(8);
		//setFocusable(true);
		addMouseListener(new ButtonListener());
	}
	
	public void setActionCommand(String t)
	{
		this.actionCommand = t;
	}
	
	public String getActionCommand()
	{
		return actionCommand;
	}
	
	public synchronized void addMouseListener(MouseListener s)
	{
		listenerList.add(MouseListener.class,s);
	}
	
	public synchronized void addActionListener(ActionListener s)
	{
		listenerList.add(ActionListener.class,s);
	}
	
	public synchronized void removeActionListener(ActionListener s)
	{
		listenerList.remove(ActionListener.class,s);
	}
	
	public void setVisible(boolean b)
	{
		if(!isVisible()&&b)
			state= UP;
		super.setVisible(b);
	}
	
	protected Image getImageForState(int state)
	{
		switch(state)
		{
		default:
		case Button.UP:
			return isEnabled() ? getImage() : disabledImage;
		case Button.DOWN:
			return downImage != null ? downImage : getImage();
		case Button.HOVER:
			return hoverImage != null ? hoverImage : getImage();
		}
	}
	
	private boolean isEnabled() 
	{
		return enabled;
	}

	public int getState()
	{
		return state;
	}
	
	public void setEnabled(boolean b)
	{
		if(!b && isEnabled())
			state = UP;
	}
	
	public Image getDisabledImage()
	{
		return disabledImage;
	}
	
	public Image getDownImage()
	{
		return downImage;
	}
	
	public Image getHoverImage()
	{
		return hoverImage;
	}
	
	public Image getImage()
	{
		return image;
	}
	
	public void setDisabledImage(Image img)
	{
		this.disabledImage = img;
	}
	
	public void setDownImage(Image img)
	{
		this.downImage = img;
	}
	
	public void setHoverImage(Image img)
	{
		this.hoverImage = img;
	}
	
	public void setImage(Image img)
	{
		this.image = img;
	}
	
	public void press()
	{
		fireActionPerformed(actionCommand);
	}
	
	public boolean isPressedOutside()
	{
		return pressedOutside;
	}
	
	protected void fireActionPerformed(String command)
	{
		ActionEvent evt = null;
		final ActionListener[] listeners = (ActionListener[])listenerList.getListeners(ActionListener.class);
		for (int i=0; i<listeners.length; i++)
		{
			if (evt==null)
				evt = new ActionEvent(this,i, command);
			listeners[i].actionPerformed(evt);
		}
	}
	
	@Override
	public void render(GUIContext gc, Graphics g)
	{
		System.out.println(this.getState());
		g.translate(g.getWorldClip().getX(), g.getWorldClip().getY());
		g.drawImage(this.getImageForState(this.getState()), this.getX(), this.getY());
		g.translate(-g.getWorldClip().getX(), -g.getWorldClip().getY());
		super.render(gc, g);
	}
	
	protected class ButtonListener extends MouseAdapter
	{
		private boolean inside = false;
		
		public void mousePressed(MouseEvent e)
		{
			System.out.println("pressed");
			if(!isEnabled())
			{
				state = UP;
				return;
			}
			inside = true;
			if(e.getButton()==MouseEvent.BUTTON1)
				state = DOWN;
			pressedOutside = false;
		}
		
		public void mouseDragged(MouseEvent e)
		{
			System.out.println("DRAGGED");
		}
		
		public void mouseEntered(MouseEvent e)
		{
			System.out.println("ENTERED");
			if(!isEnabled())
			{
				state = UP;
				return;
			}
			if(state != DOWN)
				state = HOVER;
			inside = true;
		}
		
		public void mouseReleased(MouseEvent e)
		{
			System.out.println("released");
			if(!isEnabled())
			{
				state = UP;
				return;
			}
			pressedOutside = false;
			if(inside && e.getButton() == MouseEvent.BUTTON1)
			{
				state = HOVER;
				press();
			}
		}
		
		public void mouseExited(MouseEvent e)
		{
			System.out.println("exited");
			if(!isEnabled())
			{
				state = UP;
				return;
			}
			inside = false;
			state = UP;
		}
	}
	
}
