package org.pokenet.client.backend;

import java.util.ArrayList;
import java.util.HashMap;
import de.matthiasmann.twl.renderer.Image;
import de.matthiasmann.twl.renderer.Texture;

public class PokemonSpriteDatabase
{
	public static int MALE = 0;
	public static int FEMALE = 1;
	private static int spriteamount = 494;
	private static Image[][] front = new Image[2][spriteamount];
	private static Image[][] front_shiney = new Image[2][spriteamount];
	private static Image[][] back = new Image[2][spriteamount];
	private static Image[][] back_shiney = new Image[2][spriteamount];
	private static Image[] icons = new Image[spriteamount];
	private static String respath;
	private static ArrayList<HashMap<String, Image[]>> overworldnormal;
	private static ArrayList<HashMap<String, Image[]>> overworldshiny;

	public static void Init()
	{
		respath = System.getProperty("res.path");
		if(respath == null)
			respath = "";

		overworldnormal = new ArrayList<HashMap<String, Image[]>>();
		overworldshiny = new ArrayList<HashMap<String, Image[]>>();
	}

	/**
	 * Returns an array with images, based on a direction.
	 * 
	 * @param key UP, DOWN, LEFT, RIGHT
	 * @param pokenum number of the pokemon
	 * @return
	 */
	public static Image[] getOverworldNormal(String key, int pokenum)
	{
		return overworldnormal.get(pokenum).get(key);
	}

	/**
	 * Returns an array with images, based on a direction.
	 * 
	 * @param key UP, DOWN, LEFT, RIGHT
	 * @param pokenum number of the pokemon
	 * @return
	 */
	public static Image[] getOverworldShiny(String key, int pokenum)
	{
		return overworldshiny.get(pokenum).get(key);
	}

	public static Image getIcon(int pokenum)
	{
		if(icons[pokenum] == null)
		{
			loadSpriteWithNumber(pokenum);
		}
		return icons[pokenum];
	}

	public static Image getNormalBack(int gender, int pokenum)
	{
		if(back[gender][pokenum] == null)
		{
			loadSpriteWithNumber(pokenum);
		}
		return back[gender][pokenum];
	}

	public static Image getShineyBack(int gender, int pokenum)
	{
		if(back_shiney[gender][pokenum] == null)
		{
			loadSpriteWithNumber(pokenum);
		}
		return back_shiney[gender][pokenum];
	}

	public static Image getNormalFront(int gender, int pokenum)
	{
		if(front[gender][pokenum] == null)
		{
			loadSpriteWithNumber(pokenum);
		}
		return front[gender][pokenum];
	}

	public static Image getShineyFront(int gender, int pokenum)
	{
		if(front_shiney[gender][pokenum] == null)
		{
			loadSpriteWithNumber(pokenum);
		}
		return front_shiney[gender][pokenum];
	}

	public static void loadAllPokemonSprites()
	{
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
			loadSpritesheet(pokenum);
		}
		System.out.println("Done loading pokemon sprites.");
	}

	private static void loadSpriteWithNumber(int pokenum)
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
		loadSpritesheet(pokenum);
	}

	private static void loadSpritesheet(int pokenum)
	{
		Texture normalsheet = FileLoader.loadImageAsTexture(respath + "res/pokemon/overworld/normal/" + pokenum + ".png");
		Texture shinysheet = FileLoader.loadImageAsTexture(respath + "res/pokemon/overworld/shiny/" + pokenum + ".png");
		int spriteWidth = 32;
		int spriteHeight = 32;
		int x = 0;
		int y = 0;

		HashMap<String, Image[]> normalmap = new HashMap<String, Image[]>();
		HashMap<String, Image[]> shinymap = new HashMap<String, Image[]>();
		Image[] normal = new Image[2];
		Image[] shiny = new Image[2];
		// UP
		for(int i = 0; i < 2; i++)
		{
			y = (i % 2) * 32;
			normal[i] = normalsheet.getImage(x, y, spriteWidth, spriteHeight, null, false, Texture.Rotation.NONE);
			shiny[i] = shinysheet.getImage(x, y, spriteWidth, spriteHeight, null, false, Texture.Rotation.NONE);
		}
		normalmap.put("UP", normal);
		shinymap.put("UP", shiny);
		normal = new Image[2];
		shiny = new Image[2];

		x = 0;
		// DOWN
		for(int i = 0; i < 2; i++)
		{
			y = 64 + (i % 2) * 32;
			normalsheet.getImage(x, y, spriteWidth, spriteHeight, null, false, Texture.Rotation.NONE);
		}
		normalmap.put("DOWN", normal);
		shinymap.put("DOWN", shiny);
		normal = new Image[2];
		shiny = new Image[2];

		x = 32;
		// LEFT
		for(int i = 0; i < 2; i++)
		{
			y = (i % 2) * 32;
			normalsheet.getImage(x, y, spriteWidth, spriteHeight, null, false, Texture.Rotation.NONE);
		}
		normalmap.put("LEFT", normal);
		shinymap.put("LEFT", shiny);
		normal = new Image[2];
		shiny = new Image[2];

		// RIGHT
		for(int i = 0; i < 2; i++)
		{
			y = 64 + (i % 2) * 32;
			normalsheet.getImage(x, y, spriteWidth, spriteHeight, null, false, Texture.Rotation.NONE);
		}
		normalmap.put("RIGHT", normal);
		shinymap.put("RIGHT", shiny);
		overworldnormal.add(normalmap);
		overworldshiny.add(shinymap);
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
			index = String.valueOf(pokenum);
		}
		return index;
	}
}
