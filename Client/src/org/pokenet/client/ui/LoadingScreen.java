package org.pokenet.client.ui;

import mdes.slick.sui.Frame;
import mdes.slick.sui.Label;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.pokenet.client.GameClient;

/**
 * The loading screen
 * 
 * @author shadowkanji
 */
public class LoadingScreen extends Frame
{

	/**
	 * Default constructor
	 */
	public LoadingScreen()
	{
		getContentPane().setX(getContentPane().getX() - 1);
		getContentPane().setY(getContentPane().getY() + 1);
		String respath = System.getProperty("res.path");
		if(respath == null)
			respath = "";
		try
		{
			this.setSize(800, 632);
			setBackground(new Color(0, 0, 0, 50));
			this.setLocation(0, -32);
			setResizable(false);
			getTitleBar().setVisible(false);

			Label m_bg = new Label(new Image(respath + "res/ui/loading.png", false));
			m_bg.pack();
			m_bg.setLocation(400 - m_bg.getWidth() / 2, 300 - m_bg.getHeight() / 2);
			m_bg.setVisible(true);
			this.add(m_bg);

			setVisible(false);
			// setAlwaysOnTop(true);
		}
		catch(SlickException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void setVisible(boolean visible)
	{
		super.setVisible(visible);
		if(visible)
		{
			GameClient.disableKeyRepeat();
		}
		else
		{
			GameClient.enableKeyRepeat();
		}
	}
}
