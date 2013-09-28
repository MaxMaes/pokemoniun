package org.pokemonium.client.backend;

import de.matthiasmann.twl.renderer.Image;

public class ItemSpriteDatabase
{
	private static final int LASTSPRITE = 806;
	private static Image[] items_24 = new Image[LASTSPRITE + 1];
	private static Image[] items_48 = new Image[LASTSPRITE + 1];
	private static Image tm_24;
	private static Image tm_48;
	private static String respath;

	public static void Init()
	{
		respath = System.getProperty("res.path");
		if(respath == null)
			respath = "";
	}

	public static Image getItemsprite24(int num)
	{
		if(items_24[num] == null)
		{
			loadItemSpriteWithNumber(num);
		}
		return items_24[num];
	}

	public static Image getItemsprite48(int num)
	{
		if(items_48[num] == null)
		{
			loadItemSpriteWithNumber(num);
		}
		return items_48[num];
	}

	public static Image getTM24()
	{
		if(tm_24 == null)
		{
			loadTMSprite();
		}
		return tm_24;
	}

	public static Image getTM48()
	{
		if(tm_48 == null)
		{
			loadTMSprite();
		}
		return tm_48;
	}

	public static void loadAllItemSprites()
	{
		for(int idx = 0; idx < LASTSPRITE + 1; idx++)
		{
			items_24[idx] = FileLoader.loadImage(respath + "res/items/24/" + idx + ".png");
			items_48[idx] = FileLoader.loadImage(respath + "res/items/48/" + idx + ".png");
		}
	}

	private static void loadItemSpriteWithNumber(int num)
	{
		items_24[num] = FileLoader.loadImage(respath + "res/items/24/" + num + ".png");
		items_48[num] = FileLoader.loadImage(respath + "res/items/48/" + num + ".png");
	}

	private static void loadTMSprite()
	{
		tm_24 = FileLoader.loadImage(respath + "res/items/24/TM.png");
		tm_48 = FileLoader.loadImage(respath + "res/items/48/TM.png");
	}
}
