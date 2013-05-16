package org.pokenet.client.twl.ui;

import org.pokenet.client.GameClient;
import de.matthiasmann.twl.Widget;

/**
 * The loading screen
 * 
 * @author Myth1c
 */
public class LoadingScreen extends Widget
{

	/**
	 * Default constructor
	 */
	public LoadingScreen()
	{
		this.setSize(800, 632);
		this.setPosition(0, -32);

		setVisible(false);
		// setAlwaysOnTop(true);
	}

	@Override
	public void setVisible(boolean visible)
	{
		super.setVisible(visible);
		if(visible)
		{
			GameClient.getInstance().disableKeyRepeat();
		}
		else
		{
			GameClient.getInstance().enableKeyRepeat();
		}
	}
}
