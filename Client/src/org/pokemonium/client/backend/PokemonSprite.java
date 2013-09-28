package org.pokemonium.client.backend;

import java.util.HashMap;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;
import org.pokemonium.client.backend.entity.Player.Direction;

/**
 * Handles overworld pokemon sprites
 * 
 * @author sadhi
 */
public class PokemonSprite
{
	private HashMap<Integer, SpriteSheet> normalSpriteSheets;

	/**
	 * Initialises the database of pokemon sprites
	 */
	public PokemonSprite()
	{

		normalSpriteSheets = new HashMap<Integer, SpriteSheet>();
		try
		{
			String location;
			String respath = System.getProperty("res.path");
			if(respath == null)
				respath = "";
			Image temp;
			Image[] imgArray = new Image[493];
			SpriteSheet ss = null;
			/* WARNING: Change 224 to the amount of sprites we have in client the load bar only works when we don't make a new SpriteSheet ie. ss = new SpriteSheet(temp, 41, 51); needs to be commented out in order for the load bar to work. */
			for(int i = 0; i < 493; i++)
				try
				{
					location = respath + "res/pokemon/overworld/normal/" + String.valueOf(i + 1) + ".png";
					temp = new Image(location);
					imgArray[i] = temp;
					ss = new SpriteSheet(temp, 32, 32);

					normalSpriteSheets.put(i, ss);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public PokemonSprite(Image[] imgArray1)
	{
		normalSpriteSheets = new HashMap<Integer, SpriteSheet>();
		for(int i = 0; i < 493; i++)
			normalSpriteSheets.put(i, new SpriteSheet(imgArray1[i], 32, 32));
	}

	/**
	 * Returns the requested sprite
	 * 
	 * @param dir
	 * @param isLeftFoot
	 * @param sprite
	 * @return
	 */
	public Image getPokemonSprite(Direction dir, boolean isLeftFoot, int sprite)
	{
		SpriteSheet sheet = normalSpriteSheets.get(sprite);

		// if (isMoving) {
		if(isLeftFoot)
			switch(dir)
			{
				case Up:
					return sheet.getSprite(0, 0);
				case Down:
					return sheet.getSprite(0, 2);
				case Left:
					return sheet.getSprite(0, 3);
				case Right:
					return sheet.getSprite(0, 1);
			}
		else
			switch(dir)
			{
				case Up:
					return sheet.getSprite(1, 0);
				case Down:
					return sheet.getSprite(1, 2);
				case Left:
					return sheet.getSprite(1, 3);
				case Right:
					return sheet.getSprite(1, 1);
			}

		return null;
	}
}
