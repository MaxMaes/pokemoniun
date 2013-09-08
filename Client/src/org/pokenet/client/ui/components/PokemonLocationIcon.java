package org.pokenet.client.ui.components;

public class PokemonLocationIcon extends org.pokenet.client.ui.components.Image
{
	private PokedexMap map;
	private int x, y;

	public enum PokedexMap
	{
		MAP_KANTOJOHTO, MAP_HOENN, MAP_SINNOH
	};

	public PokemonLocationIcon(PokedexMap m, int x, int y, de.matthiasmann.twl.renderer.Image img)
	{
		super(img);
		this.x = x;
		this.y = y;
		setMap(m);
	}

	public PokedexMap getMap()
	{
		return map;
	}

	public void setMap(PokedexMap map)
	{
		this.map = map;
	}

	public void applyPosition(int innerX, int innerY)
	{
		setPosition(x + innerX, y + innerY);
	}
}
