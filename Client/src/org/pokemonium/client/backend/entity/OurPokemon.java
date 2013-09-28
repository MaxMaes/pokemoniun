package org.pokemonium.client.backend.entity;

import java.io.InputStream;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.loading.LoadingList;
import org.pokemonium.client.backend.FileLoader;
import org.pokemonium.client.backend.entity.Enums.Poketype;

public class OurPokemon extends Pokemon
{
	private String m_ability;
	private int m_atk;
	private Image m_backSprite;
	private int m_def;
	private int m_exp;
	private int m_explvl;
	private int m_explvlup;
	private String m_item;
	private String m_nature;
	private String m_originalTrainer = "";
	private int m_spatk;
	private int m_spdef;
	private int m_speed;

	/**
	 * Default Constructor
	 */
	public OurPokemon()
	{
		super();
	}

	/**
	 * Returns ability
	 * 
	 * @return
	 */
	public String getAbility()
	{
		return m_ability;
	}

	/**
	 * Returns ATK
	 */
	public int getAtk()
	{
		return m_atk;
	}

	/**
	 * Returns back sprite
	 * 
	 * @return
	 */
	public Image getBackSprite()
	{
		if(m_backSprite == null)
			setBackSprite();
		return m_backSprite;
	}

	/**
	 * Returns DEF
	 */
	public int getDef()
	{
		return m_def;
	}

	/**
	 * Returns Exp
	 * 
	 * @return
	 */
	public int getExp()
	{
		return m_exp;
	}

	/**
	 * Gets Exp to current Level
	 */
	public int getExpLvl()
	{
		return m_explvl;
	}

	/**
	 * Gets Exp to the next Level
	 */
	public int getExpLvlUp()
	{
		return m_explvlup;
	}

	public String getHoldItem()
	{
		return m_item;
	}

	/**
	 * Returns nature
	 * 
	 * @return
	 */
	public String getNature()
	{
		return m_nature;
	}

	public String getOriginalTrainer()
	{
		return m_originalTrainer;
	}

	/**
	 * Returns SP.ATK
	 */
	public int getSpatk()
	{
		return m_spatk;
	}

	/**
	 * Returns SP.DEF
	 * 
	 * @return
	 */
	public int getSpdef()
	{
		return m_spdef;
	}

	/**
	 * Returns SPD
	 */
	public int getSpeed()
	{
		return m_speed;
	}

	/**
	 * Generates a pokemon object for the trade display
	 * 
	 * @param info
	 * @return
	 */
	public OurPokemon initTradePokemon(String[] info)
	{
		int sprite = Integer.parseInt(info[0]);
		if(sprite > 389)
			sprite -= 2;
		else
			sprite++;
		/* Set sprite, name, gender and hp */
		setSpriteNumber(sprite);
		setName(info[1]);
		setCurHP(Integer.parseInt(info[5]));
		setGender(Integer.parseInt(info[3]));
		if(info[4].equalsIgnoreCase("0"))
			setShiny(false);
		else
			setShiny(true);
		setMaxHP(Integer.parseInt(info[2]));
		/* Stats */
		setAtk(Integer.parseInt(info[6]));
		setDef(Integer.parseInt(info[7]));
		setSpeed(Integer.parseInt(info[8]));
		setSpatk(Integer.parseInt(info[9]));
		setSpdef(Integer.parseInt(info[10]));
		setType1(Poketype.valueOf(info[11]));
		if(info[12] != null && !info[12].equalsIgnoreCase(""))
			setType2(Poketype.valueOf(info[12]));
		setExp(Integer.parseInt(info[13].substring(0, info[13].indexOf('.'))));
		setLevel(Integer.parseInt(info[14]));
		setAbility(info[15]);
		setNature(info[16]);
		/* Moves */
		String[] moves = new String[4];
		for(int j = 0; j < 4; j++)
			if(j < info.length - 17 && info[j + 17] != null)
				moves[j] = info[j + 17];
			else
				moves[j] = "";
		setMoves(moves);

		return this;
	}

	/**
	 * Sets ability
	 * 
	 * @param ability
	 */
	public void setAbility(String ability)
	{
		m_ability = ability;
	}

	/**
	 * Sets ATK
	 */
	public void setAtk(int atk)
	{
		m_atk = atk;
	}

	/**
	 * Sets DEF
	 * 
	 * @param def
	 */
	public void setDef(int def)
	{
		m_def = def;
	}

	/**
	 * Sets Exp
	 * 
	 * @param exp
	 */
	public void setExp(int exp)
	{
		m_exp = exp;
	}

	/**
	 * Sets the total Exp required to reach current level
	 * 
	 * @param exprmn
	 */
	public void setExpLvl(int exprmn)
	{
		m_explvl = exprmn;
	}

	/**
	 * Sets the total Exp required to reach next level
	 * 
	 * @param exprmn
	 */
	public void setExpLvlUp(int exprmn)
	{
		m_explvlup = exprmn;
	}

	public void setHoldItem(String item)
	{
		m_item = item;
	}

	/**
	 * Sets nature
	 * 
	 * @param nature
	 */
	public void setNature(String nature)
	{
		m_nature = nature;
	}

	public void setOriginalTrainer(String string)
	{
		m_originalTrainer = string;
	}

	/**
	 * Sets SP.ATK
	 */
	public void setSpatk(int spatk)
	{
		m_spatk = spatk;
	}

	/**
	 * Sets SP.DEF
	 * 
	 * @param spdef
	 */
	public void setSpdef(int spdef)
	{
		m_spdef = spdef;
	}

	/**
	 * Sets SPD
	 * 
	 * @param speed
	 */
	public void setSpeed(int speed)
	{
		m_speed = speed;
	}

	/**
	 * Sets the sprite number and loads sprites
	 */
	@Override
	public void setSpriteNumber(int i)
	{
		super.setSpriteNumber(i);
		setBackSprite();
	}

	/**
	 * Sets back sprite
	 */
	private void setBackSprite()
	{
		LoadingList.setDeferredLoading(true);
		try
		{
			String respath = System.getProperty("res.path");
			if(respath == null)
				respath = "";
			InputStream f;
			String path = new String();
			String index, isShiny = new String();

			if(!isShiny())
			{
				isShiny = "normal/";
			}
			else
			{
				isShiny = "shiny/";
			}

			if(getSpriteNumber() <= 9)
			{
				index = "00" + String.valueOf(getSpriteNumber());
			}
			else if(getSpriteNumber() <= 99)
			{
				index = "0" + String.valueOf(getSpriteNumber());
			}
			else
			{
				if(getSpriteNumber() > 389)
				{
					if(getSpriteNumber() < 413)
					{
						index = String.valueOf(getSpriteNumber() - 3);
					}
					else if(getSpriteNumber() < 416)
					{
						index = String.valueOf(413);
					}
					else
					{
						index = String.valueOf(getSpriteNumber() - 5);
					}
				}
				else
					index = String.valueOf(getSpriteNumber());
			}

			int gender;
			if(getGender() == 1)
				gender = 1;
			else
				gender = 0;

			try
			{
				path = respath + "res/pokemon/back/" + isShiny + index + "-" + String.valueOf(gender) + ".png";
				f = FileLoader.loadFile(path);
				m_backSprite = new Image(f, path.toString(), false);
			}
			catch(Exception e)
			{
				if(gender == 3)
				{
					path = respath + "res/pokemon/back/" + isShiny + index + "-1.png";
				}
				else
				{
					path = respath + "res/pokemon/back/" + isShiny + index + "-0.png";
				}
				m_backSprite = new Image(path.toString(), false);
				e.printStackTrace();
			}
		}
		catch(SlickException e)
		{
			e.printStackTrace();
		}
		LoadingList.setDeferredLoading(false);
	}
}
