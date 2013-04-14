package org.pokenet.client.twl.ui.base;

import de.matthiasmann.twl.renderer.AnimationState;
import de.matthiasmann.twl.renderer.Image;

public class PokemonLocationIcon {
	private Image locationIcon;
	private PokedexMap map;
	private int x;
	private int y;
	
	public enum PokedexMap {
		MAP_KANTOJOHTO,
		MAP_HOENN,
		MAP_SINNOH
	};
	
	public PokemonLocationIcon(PokedexMap m, int xPos, int yPos, Image img) {
		setMap(m);
		x = xPos;
		y = yPos;
		locationIcon = img;
	}
	
	public void draw(AnimationState anim) {
		locationIcon.draw(anim, x, y);
	}

	public PokedexMap getMap() {
		return map;
	}

	public void setMap(PokedexMap map) {
		this.map = map;
	}
}
