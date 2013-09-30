package org.pokemonium.client.ui.frames;

import java.util.ArrayList;
import org.newdawn.slick.loading.LoadingList;
import org.pokemonium.client.GameClient;
import org.pokemonium.client.backend.FileLoader;
import org.pokemonium.client.backend.PokedexData;
import org.pokemonium.client.backend.PokemonSpriteDatabase;
import org.pokemonium.client.ui.components.Image;
import org.pokemonium.client.ui.components.PokemonLocationIcon;
import org.pokemonium.client.ui.components.PokemonLocationIcon.PokedexMap;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.ResizableFrame;

/**
 * Pokedex dialog
 * 
 * @author Myth1c
 */
public class PokedexDialog extends ResizableFrame
{
	// Images
	private de.matthiasmann.twl.renderer.Image icon_caught = GameClient.getInstance().getTheme().getImage("pokemoncaught");
	private de.matthiasmann.twl.renderer.Image icon_location = GameClient.getInstance().getTheme().getImage("pokemonlocation");

	// Image widgets
	private Image map_kantojohto = new Image(GameClient.getInstance().getTheme().getImage("map_kantojohto"));
	private Image map_hoenn = new Image(GameClient.getInstance().getTheme().getImage("map_hoenn"));
	private Image map_sinnoh = new Image(GameClient.getInstance().getTheme().getImage("map_sinnoh"));
	private Image selectionFrame = new Image(GameClient.getInstance().getTheme().getImage("pokemonselection"));
	private Image pokemonIcon;

	// Buttons
	private Button inc1, inc5, inc10, inc50, currIncButton;
	private Button up, down, left, right;

	// Labels
	private Label[] loreLabels;
	private Label[] pokemonBiologyLabels;
	private Image[] pokemonCaughtIcons;
	private PokemonLocationIcon[] pokemonLocationLabels;
	private Label[] pokemonNameList;
	private Label pokemonnumber;
	private Label pokemontypes;
	private Label[] pokemonMoveLabels;
	private Label pokemonname;
	private Label tabname;

	// Variables
	private int incrementer = 1;
	private int scrollindex = 0;
	private int selection = 1;
	private int tabindex = 1;
	private int[] trainerPokedex;
	private static final int MAX = 493; // Change this to the amount of pokemon we've got
	private boolean initialized = false;

	public PokedexDialog()
	{
		trainerPokedex = new int[MAX + 1];
		// initGUI();
	}

	public void fillNameList()
	{
		int first = scrollindex * 13 + 1;

		for(int i = 0; i < 13; i++)
		{
			if(first + i > MAX)
			{
				pokemonNameList[i].setText("");
			}
			else
			{
				if(first + i == 490)
				{
					System.out.println("");
				}
				if(getPokemon(first + i) == PokedexData.POKEMON_UNKNOWN)
				{
					String number = new String();
					if(first + i < 10)
						number = number + "00" + (first + i);
					else if(first + i < 100)
						number = number + "0" + (first + i);
					else
						number = "" + (first + i);

					pokemonNameList[i].setText("#" + number + "                ???");
				}
				if(getPokemon(first + i) > PokedexData.POKEMON_UNKNOWN)
				{
					String number = new String();
					if(first + i < 10)
						number = number + "00" + (first + i);
					else if(first + i < 100)
						number = number + "0" + (first + i);
					else
						number = "" + (first + i);

					pokemonNameList[i].setText("#" + number + " " + PokedexData.getName(first + i));
				}
				if(getPokemon(first + i) == PokedexData.POKEMON_CAUGHT)
				{
					pokemonCaughtIcons[i].setVisible(true);
				}
				else
				{
					pokemonCaughtIcons[i].setVisible(false);
				}
			}
		}

	}

	/**
	 * Returns the pokedex state of this pokemon
	 * 
	 * @param id
	 * @return Pokedex
	 */
	public int getPokemon(int id)
	{
		return trainerPokedex[id];
	}

	public void initGUI()
	{
		if(!initialized)
		{
			// This one has to be initialized fist, since it's the 'background'
			initPokedexSprite();

			pokemonname = new Label();
			pokemonname.setTheme("label_medium");

			pokemontypes = new Label();
			pokemontypes.setTheme("label_small");

			pokemonnumber = new Label();
			pokemonnumber.setTheme("label_large");

			tabname = new Label();
			tabname.setTheme("label_large");

			loreLabels = new Label[0];
			pokemonNameList = new Label[13];
			pokemonCaughtIcons = new Image[13];
			pokemonLocationLabels = new PokemonLocationIcon[0];
			pokemonMoveLabels = new Label[0];
			pokemonBiologyLabels = new Label[14];

			for(int i = 0; i < 14; i++)
			{
				pokemonBiologyLabels[i] = new Label();
				pokemonBiologyLabels[i].setTheme("label_minismall");
				add(pokemonBiologyLabels[i]);
			}

			for(int i = 0; i < 13; i++)
			{
				pokemonNameList[i] = new Label();
				pokemonNameList[i].setTheme("label_small");
				pokemonCaughtIcons[i] = new Image(icon_caught);
			}

			add(pokemonname);
			add(pokemontypes);
			add(pokemonnumber);
			add(tabname);
			add(map_kantojohto);
			add(map_hoenn);
			add(map_sinnoh);
			add(selectionFrame);

			for(int i = 0; i < 13; i++)
			{
				add(pokemonNameList[i]);
				add(pokemonCaughtIcons[i]);
			}
			setBackgroundDraggable(true);
			setResizableAxis(ResizableAxis.NONE);
			initialized = true;
		}

		updateNameList();
		updatePokemonInfo();
	}

	public de.matthiasmann.twl.renderer.Image loadImage(String path)
	{
		String respath = System.getProperty("res.path");
		if(respath == null)
			respath = "";
		de.matthiasmann.twl.renderer.Image i = null;
		i = FileLoader.loadImage(respath + path);
		return i;
	}

	public de.matthiasmann.twl.renderer.Image[] loadPokemonIcons()
	{
		de.matthiasmann.twl.renderer.Image[] sprites = new de.matthiasmann.twl.renderer.Image[MAX + 1];

		for(int i = 1; i < MAX + 1; i++)
			sprites[i] = getSprite(i, 2);

		return sprites;
	}

	/**
	 * Sets frame (in)visible and sets the pokedex data.
	 */
	@Override
	public void setVisible(boolean toSet)
	{
		if(toSet)
		{
			trainerPokedex = GameClient.getInstance().getOurPlayer().getPokedex();
			updateNameList();
			updatePokemonInfo();
			updateInfoTab();
		}
		super.setVisible(toSet);
	}

	public void updateInfoTab()
	{
		for(Label l : loreLabels)
			l.setVisible(false);
		for(Label l : pokemonMoveLabels)
			l.setVisible(false);
		for(Label l : pokemonBiologyLabels)
			l.setVisible(false);
		for(PokemonLocationIcon icon : pokemonLocationLabels)
			icon.setVisible(false);

		map_kantojohto.setVisible(false);
		map_hoenn.setVisible(false);
		map_sinnoh.setVisible(false);

		if(tabindex == 1)
		{
			tabname.setText("Kanto/Johto");
			map_kantojohto.setVisible(true);
			for(PokemonLocationIcon icon : pokemonLocationLabels)
			{
				if(icon.getMap() == PokedexMap.MAP_KANTOJOHTO)
				{
					icon.setVisible(true);
				}
			}
		}
		else if(tabindex == 2)
		{
			tabname.setText("Hoenn");
			map_hoenn.setVisible(true);
			for(PokemonLocationIcon icon : pokemonLocationLabels)
			{
				if(icon.getMap() == PokedexMap.MAP_HOENN)
				{
					icon.setVisible(true);
				}
			}
		}
		else if(tabindex == 3)
		{
			tabname.setText("Sinnoh");
			map_sinnoh.setVisible(true);
			for(PokemonLocationIcon icon : pokemonLocationLabels)
			{
				if(icon.getMap() == PokedexMap.MAP_SINNOH)
				{
					icon.setVisible(true);
				}
			}
		}
		else if(tabindex == 4)
		{
			tabname.setText("Lore");
			if(getPokemon(selection) == PokedexData.POKEMON_CAUGHT)
			{
				for(Label l : loreLabels)
					l.setVisible(true);
			}
		}
		else if(tabindex == 5)
		{
			tabname.setText("Moves");
			if(getPokemon(selection) >= PokedexData.POKEMON_SEEN)
			{
				for(Label l : pokemonMoveLabels)
					l.setVisible(true);
			}
		}
		else if(tabindex == 6)
		{
			tabname.setText("Biology");
			if(getPokemon(selection) == PokedexData.POKEMON_CAUGHT)
			{
				for(Label l : pokemonBiologyLabels)
					l.setVisible(true);
			}
		}
	}

	public void updatePokemonInfo()
	{
		String number = "#";
		if(selection < 10)
			number = number + "00" + selection;
		else if(selection < 100)
			number = number + "0" + selection;
		else
			number = "#" + selection;

		pokemonnumber.setText(number);

		if(getPokemon(selection) < PokedexData.POKEMON_SEEN)
		{
			pokemonname.setText("???");
			pokemontypes.setText("???");

			for(int i = 0; i < pokemonMoveLabels.length; i++)
				removeChild(pokemonMoveLabels[i]);
			for(int i = 0; i < loreLabels.length; i++)
				removeChild(loreLabels[i]);
			for(int i = 0; i < pokemonBiologyLabels.length; i++)
				pokemonBiologyLabels[i].setVisible(false);

			if(pokemonIcon != null)
			{
				pokemonIcon.setVisible(false);
			}
		}
		else if(getPokemon(selection) >= PokedexData.POKEMON_SEEN)
		{
			pokemonname.setText(PokedexData.getName(selection));
			pokemontypes.setText(PokedexData.getTypestring(selection));

			initLocationLabels();
			initMoveLabels();
			removeChild(pokemonIcon);
			if(selection == 29 || selection == 30 || selection == 31) // female only pokemon
			{
				pokemonIcon = new Image(PokemonSpriteDatabase.getNormalFront(PokemonSpriteDatabase.FEMALE, selection));
			}
			else
			{
				pokemonIcon = new Image(PokemonSpriteDatabase.getNormalFront(PokemonSpriteDatabase.MALE, selection));
			}
			add(pokemonIcon);
		}
		if(getPokemon(selection) == PokedexData.POKEMON_CAUGHT)
		{
			initLoreLabels();
			initBiologyLabels();
		}
		updateInfoTab();
	}

	/**
	 * Gets the pokemons sprite
	 * 
	 * @param pokenumber pokemons pokedex number
	 * @param male 2=male, 3=female
	 * @return
	 */
	private de.matthiasmann.twl.renderer.Image getSprite(int pokenumber, int male)
	{
		String respath = System.getProperty("res.path");
		if(respath == null)
			respath = "";
		LoadingList.setDeferredLoading(true);
		de.matthiasmann.twl.renderer.Image i = null;
		String path = new String();
		String index = new String();

		if(pokenumber < 10)
			index = "00" + String.valueOf(pokenumber);
		else if(pokenumber < 100)
			index = "0" + String.valueOf(pokenumber);
		else
			index = String.valueOf(pokenumber);

		int pathGender;
		if(male != 2)
			pathGender = 3;
		else
			pathGender = 2;

		try
		{
			path = respath + "res/pokemon/front/normal/" + index + "-" + pathGender + ".png";
			i = FileLoader.loadImage(path);
		}
		catch(Exception e)
		{
			if(pathGender == 3)
				pathGender = 2;
			else
				pathGender = 3;
			path = respath + "res/pokemon/front/normal/" + index + "-" + pathGender + ".png";
			i = FileLoader.loadImage(path.toString());
			e.printStackTrace();
		}
		return i;
	}

	private void initBiologyLabels()
	{
		String height = PokedexData.getHeight(selection);
		String weight = PokedexData.getWeight(selection);
		String color = PokedexData.getColor(selection);
		String habitat = PokedexData.getHabitat(selection);
		String abilities = PokedexData.getAbilities(selection);
		String baseStats = PokedexData.getBaseStats(selection);
		String rareness = PokedexData.getRareness(selection);
		String baseExp = PokedexData.getBaseEXP(selection);
		String happiness = PokedexData.getHappiness(selection);
		String growthrate = PokedexData.getGrowthRate(selection);
		String evyield = PokedexData.getEffortPoints(selection);
		String genderrate = PokedexData.getGenderRate(selection);
		String compatibility = PokedexData.getCompatibility(selection);
		String stepstohatch = PokedexData.getStepsToHatch(selection);

		pokemonBiologyLabels[0].setText("Weight: " + height);
		pokemonBiologyLabels[1].setText("Height: " + weight);
		pokemonBiologyLabels[2].setText("Color: " + color);
		pokemonBiologyLabels[7].setText("Habitat: " + habitat);
		pokemonBiologyLabels[4].setText("Abilities: " + abilities);
		pokemonBiologyLabels[5].setText("Base stats: " + baseStats);
		pokemonBiologyLabels[10].setText("Base EXP: " + baseExp);
		pokemonBiologyLabels[3].setText("Happiness: " + happiness);
		pokemonBiologyLabels[8].setText("Growthrate: " + growthrate);
		pokemonBiologyLabels[9].setText("Ev's: " + evyield);
		pokemonBiologyLabels[6].setText("Genderrate: " + genderrate);
		pokemonBiologyLabels[11].setText("Rareness: " + rareness);
		pokemonBiologyLabels[12].setText("Steps to hatch: " + stepstohatch);
		pokemonBiologyLabels[13].setText("Compatibility: " + compatibility);

		for(int i = 0; i < pokemonBiologyLabels.length; i++)
			pokemonBiologyLabels[i].setVisible(true);
	}

	private void initLocationLabels()
	{
		int day = PokedexData.getLocations(selection)[0].size();
		int night = PokedexData.getLocations(selection)[1].size();
		int fish = PokedexData.getLocations(selection)[2].size();
		int surf = PokedexData.getLocations(selection)[3].size();

		int size = day + night + fish + surf;

		for(PokemonLocationIcon icon : pokemonLocationLabels)
		{
			removeChild(icon);
		}

		pokemonLocationLabels = new PokemonLocationIcon[size];

		int idx = 0;
		ArrayList<Integer> data = PokedexData.getLocations(selection)[0];
		data.addAll(PokedexData.getLocations(selection)[1]);
		data.addAll(PokedexData.getLocations(selection)[2]);
		data.addAll(PokedexData.getLocations(selection)[3]);
		for(Integer i : data)
		{
			Object[] locationInfo = PokedexData.getLocationInfo(i);
			int mapX = 33;
			int mapY = 132;
			int x = (int) ((int) mapX + Integer.parseInt((String) locationInfo[1]) - icon_location.getWidth() / 2);
			int y = (int) ((int) mapY + Integer.parseInt((String) locationInfo[2]) - icon_location.getHeight() / 2);
			int locationid = (Integer) locationInfo[3];
			if(locationid < 92)
				pokemonLocationLabels[idx] = new PokemonLocationIcon(PokedexMap.MAP_KANTOJOHTO, x, y, icon_location);
			else if(locationid >= 92 && locationid < 161)
				pokemonLocationLabels[idx] = new PokemonLocationIcon(PokedexMap.MAP_HOENN, x, y, icon_location);
			else if(locationid > 170)
				pokemonLocationLabels[idx] = new PokemonLocationIcon(PokedexMap.MAP_SINNOH, x, y, icon_location);
			add(pokemonLocationLabels[idx]);
			pokemonLocationLabels[idx].setVisible(true);
			idx++;
		}
	}

	private void initLoreLabels()
	{
		for(int i = 0; i < loreLabels.length; i++)
			removeChild(loreLabels[i]);
		int charsPerLine = 35;
		String loreString = PokedexData.getStory(selection);
		int loreLength = loreString.length();
		int lines = loreLength / charsPerLine + 1;

		loreLabels = new Label[lines];
		for(int i = 0; i < loreLabels.length; i++)
		{
			loreLabels[i] = new Label();
			int begin = i * charsPerLine;
			int end = (i + 1) * charsPerLine;
			if(end > loreString.length())
				end = loreString.length();
			loreLabels[i].setTheme("label_minismall");
			loreLabels[i].setText(loreString.substring(begin, end));
			add(loreLabels[i]);
		}
	}

	private void initMoveLabels()
	{
		for(int i = 0; i < pokemonMoveLabels.length; i++)
			removeChild(pokemonMoveLabels[i]);
		String moveline = PokedexData.getMoves(selection);
		String[] moveset = moveline.split(",");

		pokemonMoveLabels = new Label[moveset.length / 2];
		for(int i = 0; i < pokemonMoveLabels.length; i++)
		{
			pokemonMoveLabels[i] = new Label();
			pokemonMoveLabels[i].setTheme("label_minismall");

			add(pokemonMoveLabels[i]);
		}

		for(int i = 0; i < moveset.length; i += 2)
		{
			pokemonMoveLabels[i / 2].setText(moveset[i] + " " + moveset[i + 1]);
		}

	}

	private void initPokedexSprite()
	{
		up = new Button();
		down = new Button();
		left = new Button();
		right = new Button();

		down.setTheme("button_down");
		down.setCanAcceptKeyboardFocus(false);
		down.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				if(selection + incrementer <= MAX)
				{
					selection += incrementer;
					if((selection - 1) / 13 != scrollindex)
					{
						scrollindex = (selection - 1) / 13;
						updateNameList();
					}
					updatePokemonInfo();
				}
				else
				{
					selection = 493;
					if((selection - 1) / 13 != scrollindex)
					{
						scrollindex = (selection - 1) / 13;
						updateNameList();
					}
					updatePokemonInfo();
				}
			}
		});

		up.setTheme("button_up");
		up.setCanAcceptKeyboardFocus(false);
		up.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				if(selection - incrementer >= 1)
				{
					selection -= incrementer;
					if((selection - 1) / 13 != scrollindex)
					{
						scrollindex = (selection - 1) / 13;
						updateNameList();
					}
					updatePokemonInfo();
				}
				else
				{
					selection = 1;
					if((selection - 1) / 13 != scrollindex)
					{
						scrollindex = (selection - 1) / 13;
						updateNameList();
					}
					updatePokemonInfo();
				}
			}
		});

		left.setTheme("button_left");
		left.setCanAcceptKeyboardFocus(false);
		left.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				tabindex--;
				if(tabindex < 1)
					tabindex = 6;

				updateInfoTab();
			}
		});

		right.setTheme("button_right");
		right.setCanAcceptKeyboardFocus(false);
		right.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				tabindex++;
				if(tabindex > 6)
					tabindex = 1;

				updateInfoTab();
			}
		});

		inc1 = new Button("1");
		inc1.setTheme("button_incrementer");
		inc1.setCanAcceptKeyboardFocus(false);
		inc1.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				if(inc1.isEnabled())
				{
					currIncButton.setEnabled(true);
					currIncButton = inc1;
					inc1.setEnabled(false);
					incrementer = 1;
				}
			}
		});

		inc5 = new Button("5");
		inc5.setCanAcceptKeyboardFocus(false);
		inc5.setTheme("button_incrementer");
		inc5.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				if(inc5.isEnabled())
				{
					currIncButton.setEnabled(true);
					currIncButton = inc5;
					inc5.setEnabled(false);
					incrementer = 5;
				}
			}
		});

		inc10 = new Button("10");
		inc10.setTheme("button_incrementer");
		inc10.setCanAcceptKeyboardFocus(false);
		inc10.setTheme("button_incrementer");
		inc10.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				if(inc10.isEnabled())
				{
					currIncButton.setEnabled(true);
					currIncButton = inc10;
					inc10.setEnabled(false);
					incrementer = 10;
				}
			}
		});

		inc50 = new Button("50");
		inc50.setTheme("button_incrementer");
		inc50.setCanAcceptKeyboardFocus(false);
		inc50.setTheme("button_incrementer");
		inc50.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				if(inc50.isEnabled())
				{
					currIncButton.setEnabled(true);
					currIncButton = inc50;
					inc50.setEnabled(false);
					incrementer = 50;
				}
			}
		});

		add(down);
		add(up);
		add(left);
		add(right);
		add(inc1);
		add(inc5);
		add(inc10);
		add(inc50);

		currIncButton = inc1;
		currIncButton.setEnabled(false);
	}

	private void updateNameList()
	{
		fillNameList();
	}

	@Override
	public void layout()
	{
		super.layout();
		if(initialized)
		{
			down.setSize(29, 26);
			down.setPosition(getInnerX() + getWidth() - down.getWidth() - 8, getInnerY() + getHeight() - down.getHeight() - 47);
			up.setSize(29, 26);
			up.setPosition(down.getX(), down.getY() - down.getHeight() - 5);
			left.setSize(20, 35);
			left.setPosition(getInnerX() + 32, getInnerY() + getHeight() - left.getHeight() - 76);
			right.setSize(20, 35);
			right.setPosition(left.getX() + 177, left.getY());

			inc1.setSize(36, 14);
			inc1.setPosition(down.getX() - 3, getInnerY() + 70);
			inc5.setSize(36, 14);
			inc5.setPosition(inc1.getX(), inc1.getY() + inc1.getHeight() + 5);
			inc10.setSize(36, 14);
			inc10.setPosition(inc5.getX(), inc5.getY() + inc5.getHeight() + 5);
			inc50.setSize(36, 14);
			inc50.setPosition(inc10.getX(), inc10.getY() + inc10.getHeight() + 5);

			tabname.adjustSize();
			tabname.setPosition(getInnerX() + 133 - tabname.getWidth() / 2, left.getInnerY() + 5);

			pokemonname.adjustSize();
			pokemonname.setPosition(getInnerX() + 178 - pokemonname.getWidth() / 2, getInnerY() + 75);
			pokemonnumber.adjustSize();
			pokemonnumber.setPosition(getInnerX() + 178 - pokemonnumber.getWidth() / 2, pokemonname.getInnerY() - 25);
			pokemontypes.adjustSize();
			pokemontypes.setPosition(getInnerX() + 178 - pokemontypes.getWidth() / 2, pokemonname.getInnerY() + 17);

			if(loreLabels != null)
			{
				for(int i = 0; i < loreLabels.length; i++)
				{
					loreLabels[i].adjustSize();
					loreLabels[i].setPosition(getInnerX() + 36, getInnerY() + 136 + 10 * i);
				}
			}
			if(pokemonBiologyLabels != null)
			{
				for(int i = 0; i < pokemonBiologyLabels.length; i++)
				{
					if(i < 9)
					{
						pokemonBiologyLabels[i].adjustSize();
						pokemonBiologyLabels[i].setPosition(getInnerX() + 35 + 15 * (i / 9), getInnerY() + 136 + 10 * i);
					}
					else
					{
						pokemonBiologyLabels[i].adjustSize();
						pokemonBiologyLabels[i].setPosition(getInnerX() + 125, getInnerY() + 136 + 10 * (i % 9));
					}
				}
			}
			if(pokemonMoveLabels != null)
			{
				for(int i = 0; i < pokemonMoveLabels.length; i++)
				{
					if(i < 9)
					{
						pokemonMoveLabels[i].adjustSize();
						pokemonMoveLabels[i].setPosition(getInnerX() + 34 + 15 * (i / 9), getInnerY() + 136 + 10 * i);
					}
					else
					{
						pokemonMoveLabels[i].adjustSize();
						pokemonMoveLabels[i].setPosition(getInnerX() + 135, getInnerY() + 136 + 10 * (i % 9));
					}
				}
			}

			int mapX = getInnerX() + 33;
			int mapY = getInnerY() + 132;
			map_kantojohto.setPosition(mapX, mapY);
			map_hoenn.setPosition(mapX, mapY);
			map_sinnoh.setPosition(mapX, mapY);

			int selectionX = getInnerX() + 295;
			int selectionY = getInnerY() + 59 + 22 * ((selection - 1) % 13);
			selectionFrame.setPosition(selectionX, selectionY);

			if(pokemonIcon != null)
			{
				pokemonIcon.setPosition(getInnerX() + 33, getInnerY() + 39);
			}

			for(PokemonLocationIcon icon : pokemonLocationLabels)
			{
				icon.applyPosition(getInnerX(), getInnerY());
			}

			for(int i = 0; i < pokemonCaughtIcons.length; i++)
			{
				pokemonCaughtIcons[i].setPosition(getInnerX() + 299, getInnerY() + 60 + 22 * i);
			}

			for(int i = 0; i < 13; i++)
			{
				pokemonNameList[i].setPosition(getInnerX() + 322, getInnerY() + 60 + 22 * i);
				pokemonNameList[i].adjustSize();
			}

			setSize(519, 377);
		}
	}
}
