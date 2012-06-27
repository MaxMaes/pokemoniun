package org.pokenet.server.backend.entity;

import org.pokenet.server.ServerProperties;
import org.pokenet.server.battle.Pokemon;

/**
 * Represents a Pokemon box.
 * 
 * @author shadowkanji, XtremeJedi
 * 
 */
public class PokemonBox {
	// We can have maximum 30 Pokemon in a box.
	private final Pokemon[] m_pokemon;

	public PokemonBox() {
		m_pokemon = new Pokemon[ServerProperties.MAX_POKEMON_EACH_BOX];
	}

	/**
	 * Sets a specific pokemon
	 * 
	 * @param index
	 * @param p
	 */
	public void setPokemon(int index, Pokemon p) {
		m_pokemon[index] = p;
	}
	
	/**
	 * Returns a specific pokemon
	 * 
	 * @param i
	 * @return
	 */
	public Pokemon getPokemon(int index) {
		return m_pokemon[index];
	}
}
