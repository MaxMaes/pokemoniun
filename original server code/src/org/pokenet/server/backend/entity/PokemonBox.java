package org.pokenet.server.backend.entity;

import org.pokenet.server.battle.Pokemon;

/**
 * Represents a Pokemon box.
 * @author shadowkanji
 *
 */
public class PokemonBox {
	private Pokemon[] m_pokemon = new Pokemon[30];
	
	/**
	 * Returns a specific pokemon
	 * @param i
	 * @return
	 */
	public Pokemon getPokemon(int idx) {
		return m_pokemon[idx];
	}
	
	/**
	 * Sets a specific pokemon
	 * @param index
	 * @param p
	 */
	public void setPokemon(int idx, Pokemon pokemon) {
		m_pokemon[idx] = pokemon;
	}
}
