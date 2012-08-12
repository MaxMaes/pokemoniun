package org.pokenet.server.backend.entity;

import org.pokenet.server.network.MySqlManager;

public class Pokedex
{
	private int[] m_pokedex = new int[494];
	private MySqlManager m_database;
	private int m_id;
	
	public Pokedex(int id, int[] pokedex)
	{
		m_database = MySqlManager.getInstance();
		m_pokedex = pokedex;
		m_id = id;
	}
	
	/**
	 * Retrieves this players pokedex
	 */
	public int[] getPokedex()
	{
		return this.m_pokedex;
	}
	
	/**
	 * Sets this player pokedex
	 * @param pokedex Value to set
	 */
	public void setPokedex(int[] pokedex)
	{
		this.m_pokedex = pokedex;
	}
	
	/**
	 * Sets that this pokemon has been seen
	 * @param id the id of the pokemon.
	 */
	public void setPokemonSeen(int id)
	{
		this.m_pokedex[id] = 1;
		m_database.query("UPDATE pn_pokedex SET " + "`" + MySqlManager.parseSQL("" + id) + "`" + " = '1' WHERE pokedexid = '" + MySqlManager.parseSQL("" + m_id) + "'");
	}
	
	/**
	 * Sets that this pokemon has been caught
	 * @param id the id of the pokemon.
	 */
	public void setPokemonCaught(int id)
	{
		this.m_pokedex[id] = 2;
		m_database.query("UPDATE pn_pokedex SET " + "`" + MySqlManager.parseSQL("" + id) + "`" + " = '2' WHERE pokedexid = '" + MySqlManager.parseSQL("" + m_id) + "'");
	}
	
	/**
	 * Checks if this pokemon is seen or caught
	 * @param id the id of the pokemon.
	 * @return returns true if seen or caught
	 */
	public boolean isPokemonSeen(int id)
	{
		if(m_pokedex[id] >= 1)
			return true;
		else
			return false;
	}
	
	/**
	 * Checks if this pokemon is caught
	 * @param id the id of the pokemon.
	 * @return returns true if caught
	 */
	public boolean isPokemonCaught(int id)
	{
		if(m_pokedex[id] == 2)
			return true;
		else
			return false;
	}
	
	
}
