package org.pokenet.client.twl.ui;

import org.pokenet.client.GameClient;
import org.pokenet.client.ui.base.TWLImageButton;
import de.matthiasmann.twl.renderer.Font;
import de.matthiasmann.twl.renderer.Image;

public class BattleButtonFactory
{
	private static Font font;
	private static Image normal;
	private static Image normalDown;
	private static Image small;
	private static Image smallDown;

	static
	{
		String respath = System.getProperty("res.path");
		if(respath == null)
			respath = "";
		font = GameClient.getInstance().getTheme().getFont("battlebuttonfont");

		normal = GameClient.getInstance().getTheme().getImage("battlebutton_background");
		normalDown = GameClient.getInstance().getTheme().getImage("battlebutton_background_pressed");
		small = GameClient.getInstance().getTheme().getImage("battlebutton_small_background");
		smallDown = GameClient.getInstance().getTheme().getImage("battlebutton_small_background_pressed");
	}

	public static TWLImageButton getButton(String text)
	{
		TWLImageButton out = new TWLImageButton(normal, normal, normalDown);
		out.setFont(font);
		out.setText(text);
		return out;
	}

	public static TWLImageButton getSmallButton(String text)
	{
		TWLImageButton out = new TWLImageButton(small, small, smallDown);
		out.setFont(font);
		out.setText(text);
		return out;
	}
}
