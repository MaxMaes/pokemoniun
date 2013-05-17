package org.pokenet.client.backend;

import de.matthiasmann.twl.renderer.Image;

public class PokemonSpriteDatabase
{
	public static int MALE;
	public static int FEMALE;
	private static int spriteamount = 494;
	private static Image[][] front = new Image[2][spriteamount];
	private static Image[][] front_shiney = new Image[2][spriteamount];
	private static Image[][] back = new Image[2][spriteamount];
	private static Image[][] back_shiney = new Image[2][spriteamount];
	private static Image[] icons = new Image[spriteamount];
	private static String respath;

	public static Image getIcon(int pokenum)
	{
		return icons[pokenum];
	}

	public static Image getNormalBack(int gender, int pokenum)
	{
		return back[gender][pokenum];
	}

	public static Image getShineyBack(int gender, int pokenum)
	{
		return back_shiney[gender][pokenum];
	}

	public static Image getNormalFront(int gender, int pokenum)
	{
		return front[gender][pokenum];
	}

	public static Image getShineyFrontk(int gender, int pokenum)
	{
		return front_shiney[gender][pokenum];
	}

	public static void loadPokemonSprites()
	{
		respath = System.getProperty("res.path");
		if(respath == null)
			respath = "";

		System.out.println("Filling pokemon sprites database.");
		for(int pokenum = 1; pokenum < spriteamount; pokenum++)
		{
			if(pokenum != 29 && pokenum != 30 && pokenum != 31)
			{
				loadMaleSprites(pokenum);
			}
			if(pokenum != 32 && pokenum != 33 && pokenum != 34)
			{
				loadFemaleSprites(pokenum);
			}
			loadIcon(pokenum);
		}
		System.out.println("Done loading pokemon sprites.");
	}

	private static void loadMaleSprites(int pokenum)
	{
		front_shiney[MALE][pokenum] = FileLoader.loadImage(respath + "res/pokemon/front/shiny/" + getSpriteIndex(pokenum) + "-3.png");
		front[MALE][pokenum] = FileLoader.loadImage(respath + "res/pokemon/front/normal/" + getSpriteIndex(pokenum) + "-3.png");
		back_shiney[MALE][pokenum] = FileLoader.loadImage(respath + "res/pokemon/back/shiny/" + getSpriteIndex(pokenum) + "-1.png");
		back[MALE][pokenum] = FileLoader.loadImage(respath + "res/pokemon/back/normal/" + getSpriteIndex(pokenum) + "-1.png");

	}

	private static void loadFemaleSprites(int pokenum)
	{
		front_shiney[FEMALE][pokenum] = FileLoader.loadImage(respath + "res/pokemon/front/shiny/" + getSpriteIndex(pokenum) + "-2.png");
		front[FEMALE][pokenum] = FileLoader.loadImage(respath + "res/pokemon/front/normal/" + getSpriteIndex(pokenum) + "-2.png");
		back_shiney[FEMALE][pokenum] = FileLoader.loadImage(respath + "res/pokemon/back/shiny/" + getSpriteIndex(pokenum) + "-0.png");
		back[FEMALE][pokenum] = FileLoader.loadImage(respath + "res/pokemon/back/normal/" + getSpriteIndex(pokenum) + "-0.png");
	}

	private static void loadIcon(int pokenum)
	{
		icons[pokenum] = FileLoader.loadImage(respath + "res/pokemon/icons/" + getSpriteIndex(pokenum) + ".png");
	}

	private static String getSpriteIndex(int pokenum)
	{
		String index = "";
		if(pokenum < 10)
		{
			index = "00" + String.valueOf(pokenum);
		}
		else if(pokenum < 100)
		{
			index = "0" + String.valueOf(pokenum);
		}
		else
		{
			if(pokenum > 389)
			{
				if(pokenum < 413)
				{
					index = String.valueOf(pokenum - 3);
				}
				else if(pokenum < 416)
				{
					index = String.valueOf(413);
				}
				else
				{
					index = String.valueOf(pokenum - 5);
				}
			}
			else
			{
				index = String.valueOf(pokenum);
			}
		}
		return index;
	}
}
