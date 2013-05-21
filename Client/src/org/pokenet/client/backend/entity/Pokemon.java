package org.pokenet.client.backend.entity;

import org.pokenet.client.backend.PokemonSpriteDatabase;
import de.matthiasmann.twl.renderer.Image;

public class Pokemon
{
	public static final int MALE = 1;
	public static final int FEMALE = 0;

	static final long serialVersionUID = 1;

	private int m_gender;

	private Image m_icon;
	// level and types
	private int m_level;

	// stats
	private int m_maxHP, m_curHP;

	private int[] m_movecurPP = new int[4];
	private int[] m_movemaxPP = new int[4];
	// moves and pp
	private String[] m_moves = new String[4];
	private String[] m_movetypes = new String[4];
	// name and species data
	private String m_name;

	private String m_nick;
	private boolean m_shiny;

	private Enums.Pokenum m_species;
	// load sprite and icon
	private Image m_sprite;
	private int m_spriteNum;
	// load trainer data
	private int m_trainerID;

	private Enums.Poketype m_type1, m_type2;

	/**
	 * Ststic method to get the file path for a pokemon's icon by it's index number
	 * 
	 * @param i
	 * @return
	 */
	public static String getIconPathByIndex(int i)
	{
		String path = new String();
		String index = new String();
		String respath = System.getProperty("res.path");
		if(respath == null)
			respath = "";

		if(i < 10)
			index = "00" + String.valueOf(i);
		else if(i < 100)
			index = "0" + String.valueOf(i);
		else if(i > 389)
		{
			if(i < 438)
				index = String.valueOf(i - 3);
			else
				index = String.valueOf(i - 5);
		}
		else
			index = String.valueOf(i);

		path = respath + "res/pokemon/icons/" + index + ".gif";
		return path;
	}

	/**
	 * Returns current HP
	 * 
	 * @return
	 */
	public int getCurHP()
	{
		return m_curHP;
	}

	/**
	 * Returns gender
	 * 
	 * @return
	 */
	public int getGender()
	{
		return m_gender;
	}

	/**
	 * Returns the icon
	 * 
	 * @return
	 */
	public Image getIcon()
	{
		return m_icon;
	}

	/**
	 * Returns level
	 * 
	 * @return
	 */
	public int getLevel()
	{
		return m_level;
	}

	/**
	 * Returns max HP
	 * 
	 * @return
	 */
	public int getMaxHP()
	{
		return m_maxHP;
	}

	/**
	 * Returns current PP for moves
	 * 
	 * @return
	 */
	public int[] getMoveCurPP()
	{
		return m_movecurPP;
	}

	/**
	 * Returns maximum PP for moves
	 * 
	 * @return
	 */
	public int[] getMoveMaxPP()
	{
		return m_movemaxPP;
	}

	/**
	 * Returns moves
	 * 
	 * @return
	 */
	public String[] getMoves()
	{
		return m_moves;
	}

	/**
	 * Gets the type for the specified move
	 * 
	 * @param move The move to be returned
	 */
	public String getMoveType(int move)
	{
		return m_movetypes[move];
	}

	/**
	 * Returns name
	 * 
	 * @return
	 */
	public String getName()
	{
		return m_name;
	}

	/**
	 * Returns nickname
	 * 
	 * @return
	 */
	public String getNick()
	{
		return m_nick;
	}

	/**
	 * Returns species
	 * 
	 * @return
	 */
	public Enums.Pokenum getSpecies()
	{
		return m_species;
	}

	/**
	 * Returns the sprite
	 * 
	 * @return
	 */
	public Image getSprite()
	{
		return m_sprite;
	}

	/**
	 * Returns the sprite number
	 * 
	 * @return
	 */
	public int getSpriteNumber()
	{
		return m_spriteNum;
	}

	/**
	 * Returns trainer ID
	 * 
	 * @return
	 */
	public int getTrainerID()
	{
		return m_trainerID;
	}

	/**
	 * Returns type 1
	 * 
	 * @return
	 */
	public Enums.Poketype getType1()
	{
		return m_type1;
	}

	/**
	 * Returns type 2
	 * 
	 * @return
	 */
	public Enums.Poketype getType2()
	{
		return m_type2;
	}

	/**
	 * Returns whether or not a pokemon is shiny
	 * 
	 * @return
	 */
	public boolean isShiny()
	{
		return m_shiny;
	}

	/**
	 * Set current HP
	 * 
	 * @param curHP
	 */
	public void setCurHP(int curHP)
	{
		m_curHP = curHP;
	}

	/**
	 * Sets gender
	 * 
	 * @param gender
	 */
	public void setGender(int gender)
	{
		if(gender == 0 || gender == 2)
		{
			m_gender = FEMALE;
		}
		else
		{
			m_gender = MALE;
		}
	}

	/**
	 * Sets level
	 * 
	 * @param level
	 */
	public void setLevel(int level)
	{
		m_level = level;
	}

	/**
	 * Sets max HP
	 * 
	 * @param maxHP
	 */
	public void setMaxHP(int maxHP)
	{
		m_maxHP = maxHP;
	}

	/**
	 * Sets current PP for a specified move
	 * 
	 * @param move
	 * @param pp
	 */
	public void setMoveCurPP(int move, int pp)
	{
		m_movecurPP[move] = pp;
	}

	/**
	 * Sets current PP for moves
	 * 
	 * @param movecurPP
	 */
	public void setMoveCurPP(int[] movecurPP)
	{
		m_movecurPP = movecurPP;
	}

	/**
	 * Sets maximum PP of a move
	 * 
	 * @param move
	 * @param pp
	 */
	public void setMoveMaxPP(int move, int pp)
	{
		m_movemaxPP[move] = pp;
	}

	/**
	 * Sets maximum PP for moves
	 * 
	 * @param movemaxPP
	 */
	public void setMoveMaxPP(int[] movemaxPP)
	{
		m_movemaxPP = movemaxPP;
	}

	/**
	 * Sets a specific move
	 * 
	 * @param index
	 * @param move
	 */
	public void setMoves(int index, String move)
	{
		m_moves[index] = move;
	}

	/**
	 * Sets moves
	 * 
	 * @param moves
	 */
	public void setMoves(String[] moves)
	{
		m_moves = moves;
	}

	/**
	 * Sets the type for the specified move
	 * 
	 * @param move The move to be returned
	 */
	public void setMoveTypes(String[] movetypes)
	{
		m_movetypes = movetypes;
	}

	/**
	 * Sets name
	 * 
	 * @param name
	 */
	public void setName(String name)
	{
		m_name = name;
	}

	/**
	 * Sets nickname
	 */
	public void setNick(String nick)
	{
		m_nick = nick;
	}

	/**
	 * Sets whether a pokemon is shiny
	 * 
	 * @param shiny
	 */
	public void setShiny(boolean shiny)
	{
		m_shiny = shiny;
	}

	/**
	 * Sets species
	 * 
	 * @param species
	 */
	public void setSpecies(Enums.Pokenum species)
	{
		m_species = species;
	}

	/**
	 * Sets sprite number and loads sprites
	 * 
	 * @param x
	 * @return
	 */
	public void setSpriteNumber(int x)
	{
		m_spriteNum = x;
		try
		{
			setSprite();
		}
		catch(Exception e)
		{
			setSprite();
		}
		try
		{
			setIcon();
		}
		catch(Exception e)
		{
			setIcon();
		}
	}

	/**
	 * Sets Trainer ID
	 * 
	 * @param trainerID
	 */
	public void setTrainerID(int trainerID)
	{
		m_trainerID = trainerID;
	}

	/**
	 * Sets type 1
	 * 
	 * @param type1
	 */
	public void setType1(Enums.Poketype type1)
	{
		m_type1 = type1;
	}

	/**
	 * Sets type 2
	 * 
	 * @param type2
	 */
	public void setType2(Enums.Poketype type2)
	{
		m_type2 = type2;
	}

	/**
	 * Loads the icon
	 */
	private void setIcon()
	{
		m_icon = PokemonSpriteDatabase.getIcon(m_spriteNum);
	}

	/**
	 * Loads the sprite
	 */
	private void setSprite()
	{
		if(isShiny())
		{
			m_sprite = PokemonSpriteDatabase.getShineyBack(m_gender, m_spriteNum);
		}
		else
		{
			m_sprite = PokemonSpriteDatabase.getNormalBack(m_gender, m_spriteNum);
		}
	}
}
