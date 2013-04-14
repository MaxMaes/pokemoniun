package org.pokenet.client.twl.ui.frames;

import java.util.ArrayList;

import org.newdawn.slick.loading.LoadingList;
import org.pokenet.client.GameClient;
import org.pokenet.client.backend.FileLoader;
import org.pokenet.client.backend.PokedexData;
import org.pokenet.client.twl.ui.base.PokemonLocationIcon;
import org.pokenet.client.twl.ui.base.PokemonLocationIcon.PokedexMap;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.renderer.Image;

/**
 * Pokedex dialog
 * 
 * @author Myth1c
 */
public class PokedexDialog extends ResizableFrame
{
	// Images
	private Image icon_caught = GameClient.getInstance().getTheme().getImage("pokemoncaught");
	private Image icon_location =GameClient.getInstance().getTheme().getImage("pokemonlocationicon");
	private Image map_kantojohto = GameClient.getInstance().getTheme().getImage("map_kantojohto");
	private Image map_hoenn = GameClient.getInstance().getTheme().getImage("map_hoenn");
	private Image map_sinnoh = GameClient.getInstance().getTheme().getImage("map_sinnoh");
	private Image selectionFrame = GameClient.getInstance().getTheme().getImage("pokemonselected");
	private Image[] pokemonIcons = loadPokemonIcons();
	
	//Buttons
	private Button inc1, inc5, inc10, inc50, currIncButton;
	private Button up, down, left, right;
	
	//Labels
	private Label[] loreLabels;
	private Label[] pokemonBiologyLabels;
	private Label[] pokemonCaughtLabels;
	private PokemonLocationIcon[] pokemonLocationLabels;
	private Label[] pokemonNameList;
	private Label pokemonnumber;
	private Label pokemontypes;
	private Label pokedexsprite;
	private Label[] pokemonMoveLabels;
	private Label pokemonname;
	private Label tabname;
	
	//Variables
	private int incrementer = 1;
	private int scrollindex = 0;
	private int selection = 1;
	private int tabindex = 1;
	private int[] trainerPokedex;
	private static final int MAX = 493; // Change this to the amount of pokemon we've got

	public PokedexDialog()
	{
		trainerPokedex = new int[MAX + 1];
		initGUI();
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
				if(getPokemon(first + i) == 0)
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
				if(getPokemon(first + i) > 0)
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
			}
		}

	}

	public int getPokemon(int id)
	{
		return trainerPokedex[id];
	}

	public void initGUI()
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
		pokemonCaughtLabels = new Label[13];
		pokemonLocationLabels = new PokemonLocationIcon[0];
		pokemonMoveLabels = new Label[0];
		pokemonBiologyLabels = new Label[14];

		for(int i = 0; i < 14; i++)
		{
			pokemonBiologyLabels[i] = new Label();
			pokemonBiologyLabels[i].setTheme("label_minismall");
		}

		for(int i = 0; i < 13; i++)
		{
			pokemonNameList[i] = new Label();
			pokemonNameList[i].setTheme("label_small");
			pokemonNameList[i].setPosition(getInnerX() + 322, getInnerY() + 63 + 22 * i);
			pokemonCaughtLabels[i] = new Label();
			pokemonCaughtLabels[i].setPosition(getInnerX() + 299, getInnerY() + 60 + 22 * i);
		}

		updateNameList();

		updatePokemonInfo();

		setSize(500, 363);

		add(pokemonname);
		add(pokemontypes);
		add(pokemonnumber);
		add(tabname);
		for(int i = 0; i < 14; i++)
			add(pokemonBiologyLabels[i]);

		for(int i = 0; i < 13; i++)
		{
			add(pokemonNameList[i]);
			add(pokemonCaughtLabels[i]);
		}
		setDraggable(true);
		setResizableAxis(ResizableAxis.NONE);
	}

	public Image loadImage(String path)
	{
		boolean old = LoadingList.isDeferredLoading();
		LoadingList.setDeferredLoading(false);
		String respath = System.getProperty("res.path");
		if(respath == null)
			respath = "";
		Image i = null;
		i = FileLoader.loadImage(respath + path);
		LoadingList.setDeferredLoading(old);
		return i;
	}

	public Image[] loadPokemonIcons()
	{
		Image[] sprites = new Image[MAX + 1];

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

		if(tabindex == 1)
		{
			tabname.setText("Kanto/Johto");
		}
		else if(tabindex == 2)
		{
			tabname.setText("Hoenn");			
		}
		else if(tabindex == 3)
		{
			tabname.setText("Sinnoh");
		}
		else if(tabindex == 4)
		{
			tabname.setText("Lore");
			for(Label l : loreLabels)
				l.setVisible(true);
		}
		else if(tabindex == 5)
		{
			tabname.setText("Moves");
			for(Label l : pokemonMoveLabels)
				l.setVisible(true);
		}
		else if(tabindex == 6)
		{
			tabname.setText("Biology");
			for(Label l : pokemonBiologyLabels)
				l.setVisible(true);
		}
		tabname.setPosition(getInnerX() + 133 - tabname.getWidth() / 2, left.getY() + 7);
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
		pokemonnumber.setPosition(getInnerX() + 178 - pokemonnumber.getWidth() / 2, pokemonname.getY() - 25);

		if(getPokemon(selection) < 1)
		{
			pokemonname.setText("???");
			pokemonname.setPosition(getInnerX() + 178 - pokemonname.getWidth() / 2, getInnerY() + 75);
			pokemontypes.setText("???");
			pokemontypes.setPosition(getInnerX() + 178 - pokemontypes.getWidth() / 2, pokemonname.getY() + 17);
			for(int i = 0; i < pokemonMoveLabels.length; i++)
				removeChild(pokemonMoveLabels[i]);
			for(int i = 0; i < loreLabels.length; i++)
				removeChild(loreLabels[i]);
			/*for(int i = 0; i < pokemonLocationLabels.length; i++)
				removeChild(pokemonLocationLabels[i]);*/
			for(int i = 0; i < pokemonBiologyLabels.length; i++)
				removeChild(pokemonBiologyLabels[i]);
		}
		else if(getPokemon(selection) >= 1)
		{
			pokemonname.setText(PokedexData.getName(selection));
			pokemonname.setPosition(getInnerX() + 178 - pokemonname.getWidth() / 2, getInnerY() + 75);

			pokemontypes.setText(PokedexData.getTypestring(selection));
			pokemontypes.setPosition(getInnerX() + 178 - pokemontypes.getWidth() / 2, pokemonname.getY() + 17);

			initLocationLabels();
			initMoveLabels();
		}
		if(getPokemon(selection) == 2)
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
	private Image getSprite(int pokenumber, int male)
	{
		String respath = System.getProperty("res.path");
		if(respath == null)
			respath = "";
		LoadingList.setDeferredLoading(true);
		Image i = null;
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
		{
			if(i < 9)
				pokemonBiologyLabels[i].setPosition(getInnerX() + 35 + 15 * (i / 9), getInnerY() + 132 + 11 * i);
			else
				pokemonBiologyLabels[i].setPosition(getInnerX() + 125, getInnerY() + 132 + 11 * (i % 9));
		}

		for(int i = 0; i < pokemonBiologyLabels.length; i++)
			add(pokemonBiologyLabels[i]);
	}

	private void initLocationLabels()
	{
		int day = PokedexData.getLocations(selection)[0].size();
		int night = PokedexData.getLocations(selection)[1].size();
		int fish = PokedexData.getLocations(selection)[2].size();
		int surf = PokedexData.getLocations(selection)[3].size();

		int size = day + night + fish + surf;

		pokemonLocationLabels = new PokemonLocationIcon[size];

		int idx = 0;
		ArrayList<Integer> data = PokedexData.getLocations(selection)[0];
		data.addAll(PokedexData.getLocations(selection)[1]);
		data.addAll(PokedexData.getLocations(selection)[2]);
		data.addAll(PokedexData.getLocations(selection)[3]);
		for(Integer i : data)
		{
			Object[] locationInfo = PokedexData.getLocationInfo(i);
			int mapX = getInnerX() + 33;
			int mapY = getInnerY() + 132;
			int x = (int) ((int) mapX + Integer.parseInt((String) locationInfo[1]) - icon_location.getWidth() / 2);
			int y = (int) ((int) mapY + Integer.parseInt((String) locationInfo[2]) - icon_location.getHeight() / 2);
			int locationid = (Integer) locationInfo[3];
			if(locationid < 92) 
				pokemonLocationLabels[idx] = new PokemonLocationIcon(PokedexMap.MAP_KANTOJOHTO, x, y, icon_location);
			else if(locationid >= 92 && locationid < 161)
				pokemonLocationLabels[idx] = new PokemonLocationIcon(PokedexMap.MAP_HOENN, x, y, icon_location);
			else if(locationid > 170)
				pokemonLocationLabels[idx] = new PokemonLocationIcon(PokedexMap.MAP_SINNOH, x, y, icon_location);
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
			loreLabels[i].setTheme("label_smallmini");
			loreLabels[i].setText(loreString.substring(begin, end));
			loreLabels[i].setPosition(getInnerX() + 34, getInnerY() + 132 + 10 * i);
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
			pokemonMoveLabels[i].setTheme("smallmini");
			if(i < 9)
				pokemonMoveLabels[i].setPosition(getInnerX() + 34 + 15 * (i / 9), getInnerY() + 132 + 10 * i);
			else
				pokemonMoveLabels[i].setPosition(getInnerX() + 135, getInnerY() + 132 + 10 * (i % 9));
			add(pokemonMoveLabels[i]);
		}

		for(int i = 0; i < moveset.length; i += 2)
		{
			pokemonMoveLabels[i / 2].setText(moveset[i] + " " + moveset[i + 1]);
		}

	}

	private void initPokedexSprite()
	{
		pokedexsprite = new Label();
		//pokedexsprite.setImage(pokedexSprite);
		pokedexsprite.setSize(519, 377);
		pokedexsprite.setPosition(0, 0);

		up = new Button();
		down = new Button();
		left = new Button();
		right = new Button();

		inc1 = new Button();
		inc5 = new Button();
		inc10 = new Button();
		inc50 = new Button();

		down.setSize(30, 30);
		down.setTheme("button_down");
		down.setPosition(getInnerX() + pokedexsprite.getWidth() - down.getWidth() - 8, getInnerY() + pokedexsprite.getHeight() - down.getHeight() - 47);
		down.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				if(selection + incrementer <= 493)
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
		up.setSize(30, 30);
		up.setPosition(down.getX(), down.getY() - down.getHeight() - 5);
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
		left.setSize(20, 38);
		left.setPosition(getInnerX() + 32, getInnerY() + pokedexsprite.getHeight() - left.getHeight() - 76);
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
		right.setSize(20, 38);
		right.setPosition(left.getX() + 177, left.getY());
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

		inc1.setTheme("button_incrementer");
		inc1.setText("1");
		inc1.setSize(36, 14);
		inc1.setPosition(down.getX() - 3, getInnerY() + 70);
		currIncButton = inc1;
		currIncButton.setEnabled(false);
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

		inc5.setTheme("button_incrementer");
		inc5.setText("5");
		inc5.setSize(36, 14);
		inc5.setPosition(inc1.getX(), inc1.getY() + inc1.getHeight() + 5);
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

		inc10.setTheme("button_incrementer");
		inc10.setText("10");
		inc10.setSize(36, 14);
		inc10.setPosition(inc5.getX(), inc5.getY() + inc5.getHeight() + 5);
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

		inc50.setTheme("button_incrementer");
		inc50.setText("50");
		inc50.setSize(36, 14);
		inc50.setPosition(inc10.getX(), inc10.getY() + inc10.getHeight() + 5);
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

		add(pokedexsprite);
		add(down);
		add(up);
		add(left);
		add(right);
		add(inc1);
		add(inc5);
		add(inc10);
		add(inc50);
	}

	private void updateNameList()
	{
		fillNameList();
	}
	
	@Override
	public void paintWidget(GUI gui) {	
		//Render the maps
		int mapX = getInnerX() + 33;
		int mapY = getInnerY() + 132;
		if(tabindex == 1) {
			map_kantojohto.draw(getAnimationState(), mapX, mapY);
			for(PokemonLocationIcon icon : pokemonLocationLabels)
				if(icon.getMap() == PokedexMap.MAP_KANTOJOHTO)
					icon.draw(getAnimationState());
		} else if(tabindex == 2) {
			map_hoenn.draw(getAnimationState(), mapX, mapY);
			for(PokemonLocationIcon icon : pokemonLocationLabels)
				if(icon.getMap() == PokedexMap.MAP_HOENN)
					icon.draw(getAnimationState());
		} else if(tabindex == 3) {
			map_sinnoh.draw(getAnimationState(), mapX, mapY);
			for(PokemonLocationIcon icon : pokemonLocationLabels)
				if(icon.getMap() == PokedexMap.MAP_SINNOH)
					icon.draw(getAnimationState());
		}
		
		//Render the selection rectangle around the name
		int selectionX = getInnerX() + 295;
		int selectionY = getInnerY() + 59 + 22 * ((selection - 1) % 13);
		selectionFrame.draw(getAnimationState(), selectionX, selectionY);
		
		//Render the pokemon sprite
		if(getPokemon(selection) > 1) {
			pokemonIcons[selection].draw(getAnimationState(), getInnerX() + 33, getInnerY() + 39);
		}
		
		//Render the pokemon caught icon
		int first = scrollindex * 13 + 1;
		for(int i = 0; i < 13; i++)
			if(getPokemon(first + i) == 2)
				icon_caught.draw(getAnimationState(), getInnerX() + 299, getInnerY() + 60 + 22 * i);
		
	}
}
