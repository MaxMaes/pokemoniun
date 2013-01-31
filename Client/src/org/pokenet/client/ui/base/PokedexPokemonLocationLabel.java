package org.pokenet.client.ui.base;

import mdes.slick.sui.Label;

public class PokedexPokemonLocationLabel extends Label
{
	public enum PokedexMap {
		MAP_KANTOJOHTO,
		MAP_HOENN,
		MAP_SINNOH
	};
	private PokedexMap map;
	
	public PokedexPokemonLocationLabel()
	{

	}
	
	public void setMap(PokedexMap map)
	{
		this.map = map;
	}
	
	public PokedexMap getMap()
	{
		return this.map;
	}
}
