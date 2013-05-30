package org.pokenet.client.backend;

import de.matthiasmann.twl.renderer.Image;

public class ItemSpriteDatabase
{
	private static final int LASTSPRITE = 806;
	private static Image[] items_24 = new Image[LASTSPRITE + 1];
	private static Image[] items_48 = new Image[LASTSPRITE + 1];
	private static Image tm_24;
	private static Image tm_48;

	public static Image getItemsprite24(int num)
	{
		return items_24[num];
	}

	public static Image getItemsprite48(int num)
	{
		return items_48[num];
	}

	public static Image getTM24()
	{
		return tm_24;
	}

	public static Image getTM48()
	{
		return tm_48;
	}

	public static void loadItemSprites()
	{
		String respath = System.getProperty("res.path");
		if(respath == null)
			respath = "";
		for(int i = 0; i < LASTSPRITE + 1; i++)
		{
			items_24[i] = FileLoader.loadImage(respath + "res/items/24/" + i + ".png");
			items_48[i] = FileLoader.loadImage(respath + "res/items/48/" + i + ".png");
		}
		tm_24 = FileLoader.loadImage(respath + "res/items/24/TM.png");
		tm_48 = FileLoader.loadImage(respath + "res/items/48/TM.png");
	}
}
