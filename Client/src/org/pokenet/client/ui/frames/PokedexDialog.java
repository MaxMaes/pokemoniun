package org.pokenet.client.ui.frames;

import java.io.InputStream;
import java.util.ArrayList;
import mdes.slick.sui.Frame;
import mdes.slick.sui.Label;
import mdes.slick.sui.event.ActionEvent;
import mdes.slick.sui.event.ActionListener;
import org.newdawn.slick.Color;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.loading.LoadingList;
import org.pokenet.client.GameClient;
import org.pokenet.client.backend.FileLoader;
import org.pokenet.client.backend.PokedexData;
import org.pokenet.client.ui.base.NewImageButton;

/**
 * Pokedex dialog
 * 
 * @author Myth1c
 */
public class PokedexDialog extends Frame
{
	private static final int MAX = 493; // Change this to the amount of pokemon we've got
	private Image buttonDownSprite = loadImage("res/ui/pokedex/button_down.png");
	private Image buttonLeftSprite = loadImage("res/ui/pokedex/button_left.png");
	private Image buttonRightSprite = loadImage("res/ui/pokedex/button_right.png");
	private Image buttonUpSprite = loadImage("res/ui/pokedex/button_up.png");
	// Images
	private Image caughtIcon = loadImage("res/ui/pokedex/pokemoncaught.png");
	private NewImageButton currIncButton;

	private NewImageButton inc1, inc5, inc10, inc50;
	private int incrementer = 1;

	private Image incrementerPressedSprite = loadImage("res/ui/pokedex/incrementer_pressed.png");
	private Image incrementerSprite = loadImage("res/ui/pokedex/incrementer.png");
	private Image locationIcon = loadImage("res/ui/pokedex/pokemonlocationicon.png");
	private Label[] loreLabels;
	private Label map;
	private Image mapIcon = loadImage("res/ui/pokedex/kanto_johto_small.png");
	// Image labels
	private Label pokedexsprite;
	private Image pokedexSprite = loadImage("res/ui/pokedex/pokedex.png");

	private Label[] pokemonBiologyLabels;
	private Label[] pokemonCaughtLabels;
	private Image[] pokemonIcons = loadPokemonIcons();
	private Label[] pokemonLocationLabels;
	private Label[] pokemonMoveLabels;
	// Text labels
	private Label pokemonname;

	private Label[] pokemonNameList;
	private Label pokemonnumber;
	private Label pokemonSelectionFrame;
	private Label pokemonsprite;
	private Label pokemontypes;
	private int scrollindex = 0;
	private int selection = 1;
	private Image selectionFrame = loadImage("res/ui/pokedex/pokemonselected.png");
	private int tabindex = 1;
	private Label tabname;
	private int[] trainerPokedex;
	// UI Buttons
	private NewImageButton up, down, left, right;

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
			pokemonCaughtLabels[i].setVisible(false);
			if(first + i > MAX)
			{
				pokemonNameList[i].setText("");
				pokemonNameList[i].pack();
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
					pokemonNameList[i].pack();
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
					pokemonNameList[i].pack();
					if(getPokemon(first + i) == 2)
						pokemonCaughtLabels[i].setVisible(true);
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
		pokemonname.setFont(GameClient.getPokedexFontMedium());

		pokemontypes = new Label();
		pokemontypes.setFont(GameClient.getPokedexFontSmall());

		pokemonnumber = new Label();
		pokemonnumber.setFont(GameClient.getPokedexFontLarge());

		pokemonsprite = new Label();
		pokemonsprite.setSize(80, 80);
		pokemonsprite.setLocation(pokedexsprite.getX() + 33, pokedexsprite.getY() + 39);

		map = new Label();
		map.setImage(mapIcon);

		map.pack();
		map.setLocation(pokedexsprite.getX() + 33, pokedexsprite.getY() + 132);
		map.setVisible(false);

		tabname = new Label();
		tabname.setFont(GameClient.getPokedexFontLarge());

		pokemonSelectionFrame = new Label();
		pokemonSelectionFrame.setImage(selectionFrame);

		loreLabels = new Label[0];
		pokemonNameList = new Label[13];
		pokemonCaughtLabels = new Label[13];
		pokemonLocationLabels = new Label[0];
		pokemonMoveLabels = new Label[0];
		pokemonBiologyLabels = new Label[14];

		for(int i = 0; i < 14; i++)
		{
			pokemonBiologyLabels[i] = new Label();
			pokemonBiologyLabels[i].setFont(GameClient.getPokedexFontBetweenSmallAndMini());
		}

		for(int i = 0; i < 13; i++)
		{
			pokemonNameList[i] = new Label();
			pokemonNameList[i].setFont(GameClient.getPokedexFontSmall());
			pokemonNameList[i].setLocation(pokedexsprite.getX() + 322, pokedexsprite.getY() + 63 + 22 * i);
			pokemonCaughtLabels[i] = new Label();
			pokemonCaughtLabels[i].setImage(caughtIcon);
			pokemonCaughtLabels[i].pack();
			pokemonCaughtLabels[i].setLocation(pokedexsprite.getX() + 299, pokedexsprite.getY() + 60 + 22 * i);
		}

		updateNameList();

		updatePokemonInfo();

		setBackground(new Color(0, 0, 0, 0));
		getTitleBar().setVisible(false);
		setBorderRendered(false);
		setSize(519, 397 + getTitleBar().getHeight());

		add(pokemonname);
		add(pokemontypes);
		add(pokemonnumber);
		add(pokemonsprite);
		add(tabname);
		add(map);
		add(pokemonSelectionFrame);
		for(int i = 0; i < 14; i++)
			add(pokemonBiologyLabels[i]);

		for(int i = 0; i < 13; i++)
		{
			add(pokemonNameList[i]);
			add(pokemonCaughtLabels[i]);
		}

		setResizable(false);
	}

	public Image loadImage(String path)
	{
		boolean old = LoadingList.isDeferredLoading();
		LoadingList.setDeferredLoading(false);
		String respath = System.getProperty("res.path");
		if(respath == null)
			respath = "";
		Image i = null;
		try
		{
			i = new Image(FileLoader.loadFile(respath + path), path, false);
		}
		catch(SlickException e)
		{
			e.printStackTrace();
		}
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
		map.setVisible(false);
		for(Label l : pokemonLocationLabels)
			l.setVisible(false);
		for(Label l : loreLabels)
			l.setVisible(false);
		for(Label l : pokemonMoveLabels)
			l.setVisible(false);
		for(Label l : pokemonBiologyLabels)
			l.setVisible(false);

		if(tabindex == 1)
		{
			tabname.setText("Location");
			for(Label l : pokemonLocationLabels)
				l.setVisible(true);
			map.setVisible(true);
		}

		if(tabindex == 2)
		{
			tabname.setText("Lore");
			for(Label l : loreLabels)
				l.setVisible(true);
		}
		if(tabindex == 3)
		{
			tabname.setText("Moves");
			for(Label l : pokemonMoveLabels)
				l.setVisible(true);
		}
		else if(tabindex == 4)
		{
			tabname.setText("Biology");
			for(Label l : pokemonBiologyLabels)
				l.setVisible(true);
		}
		tabname.pack();
		tabname.setLocation(pokedexsprite.getX() + 133 - tabname.getWidth() / 2, left.getY() + 7);
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
		pokemonnumber.pack();
		pokemonnumber.setLocation(pokedexsprite.getX() + 178 - pokemonnumber.getWidth() / 2, pokemonname.getY() - 25);

		if(getPokemon(selection) < 1)
		{
			pokemonname.setText("???");
			pokemonname.pack();
			pokemonname.setLocation(pokedexsprite.getX() + 178 - pokemonname.getWidth() / 2, pokedexsprite.getY() + 75);
			pokemontypes.setText("???");
			pokemontypes.pack();
			pokemontypes.setLocation(pokedexsprite.getX() + 178 - pokemontypes.getWidth() / 2, pokemonname.getY() + 17);
			remove(pokemonsprite);
			for(int i = 0; i < pokemonMoveLabels.length; i++)
				remove(pokemonMoveLabels[i]);
			for(int i = 0; i < loreLabels.length; i++)
				remove(loreLabels[i]);
			for(int i = 0; i < pokemonLocationLabels.length; i++)
				remove(pokemonLocationLabels[i]);
			for(int i = 0; i < pokemonBiologyLabels.length; i++)
				remove(pokemonBiologyLabels[i]);
		}
		else if(getPokemon(selection) >= 1)
		{
			pokemonname.setText(PokedexData.getName(selection));
			pokemonname.pack();
			pokemonname.setLocation(pokedexsprite.getX() + 178 - pokemonname.getWidth() / 2, pokedexsprite.getY() + 75);

			pokemontypes.setText(PokedexData.getTypestring(selection));
			pokemontypes.pack();
			pokemontypes.setLocation(pokedexsprite.getX() + 178 - pokemontypes.getWidth() / 2, pokemonname.getY() + 17);

			pokemonsprite.setImage(pokemonIcons[selection]);
			pokemonsprite.setSize(80, 80);
			pokemonsprite.setLocation(pokedexsprite.getX() + 33, pokedexsprite.getY() + 39);
			add(pokemonsprite);
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
		try
		{
			InputStream f;
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
				f = FileLoader.loadFile(path);
				i = new Image(f, path.toString(), false);
			}
			catch(Exception e)
			{
				if(pathGender == 3)
					pathGender = 2;
				else
					pathGender = 3;
				path = respath + "res/pokemon/front/normal/" + index + "-" + pathGender + ".png";
				i = new Image(path.toString(), false);
				e.printStackTrace();
			}
		}
		catch(SlickException e)
		{
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
				pokemonBiologyLabels[i].setLocation(pokedexsprite.getX() + 35 + 15 * (i / 9), pokedexsprite.getY() + 132 + 11 * i);
			else
				pokemonBiologyLabels[i].setLocation(pokedexsprite.getX() + 125, pokedexsprite.getY() + 132 + 11 * (i % 9));
			pokemonBiologyLabels[i].pack();
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

		for(int i = 0; i < pokemonLocationLabels.length; i++)
			remove(pokemonLocationLabels[i]);
		pokemonLocationLabels = new Label[size];
		for(int i = 0; i < pokemonLocationLabels.length; i++)
		{
			pokemonLocationLabels[i] = new Label();
			pokemonLocationLabels[i].setImage(locationIcon);
			pokemonLocationLabels[i].pack();
			add(pokemonLocationLabels[i]);
		}

		int idx = 0;
		ArrayList<Integer> data = PokedexData.getLocations(selection)[0];
		data.addAll(PokedexData.getLocations(selection)[1]);
		data.addAll(PokedexData.getLocations(selection)[2]);
		data.addAll(PokedexData.getLocations(selection)[3]);
		for(Integer i : data)
		{
			Object[] locationInfo = PokedexData.getLocationInfo(i);
			int x = (int) ((int) map.getX() + Integer.parseInt((String) locationInfo[1]) - pokemonLocationLabels[idx].getWidth() / 2);
			int y = (int) ((int) map.getY() + Integer.parseInt((String) locationInfo[2]) - pokemonLocationLabels[idx].getHeight() / 2);
			pokemonLocationLabels[idx].setLocation(x, y);
			idx++;
		}
	}

	private void initLoreLabels()
	{
		for(int i = 0; i < loreLabels.length; i++)
			remove(loreLabels[i]);
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
			loreLabels[i].setFont(GameClient.getPokedexFontBetweenSmallAndMini());
			loreLabels[i].setText(loreString.substring(begin, end));
			loreLabels[i].pack();
			loreLabels[i].setLocation(pokedexsprite.getX() + 34, pokedexsprite.getY() + 132 + 10 * i);
			add(loreLabels[i]);
		}
	}

	private void initMoveLabels()
	{
		for(int i = 0; i < pokemonMoveLabels.length; i++)
			remove(pokemonMoveLabels[i]);
		String moveline = PokedexData.getMoves(selection);
		String[] moveset = moveline.split(",");

		pokemonMoveLabels = new Label[moveset.length / 2];
		for(int i = 0; i < pokemonMoveLabels.length; i++)
		{
			pokemonMoveLabels[i] = new Label();
			pokemonMoveLabels[i].setFont(GameClient.getPokedexFontBetweenSmallAndMini());
			if(i < 9)
				pokemonMoveLabels[i].setLocation(pokedexsprite.getX() + 34 + 15 * (i / 9), pokedexsprite.getY() + 132 + 10 * i);
			else
				pokemonMoveLabels[i].setLocation(pokedexsprite.getX() + 135, pokedexsprite.getY() + 132 + 10 * (i % 9));
			add(pokemonMoveLabels[i]);
		}

		for(int i = 0; i < moveset.length; i += 2)
		{
			pokemonMoveLabels[i / 2].setText(moveset[i] + " " + moveset[i + 1]);
			pokemonMoveLabels[i / 2].pack();
		}

	}

	private void initPokedexSprite()
	{
		pokedexsprite = new Label();
		pokedexsprite.setImage(pokedexSprite);
		pokedexsprite.setSize(519, 377);
		pokedexsprite.setLocation(0, 0);

		up = new NewImageButton();
		down = new NewImageButton();
		left = new NewImageButton();
		right = new NewImageButton();

		inc1 = new NewImageButton();
		inc5 = new NewImageButton();
		inc10 = new NewImageButton();
		inc50 = new NewImageButton();

		down.setSize(30, 30);
		down.setImages(buttonDownSprite, buttonDownSprite, buttonDownSprite);
		down.setForeground(Color.white);
		down.setLocation(pokedexsprite.getX() + pokedexsprite.getWidth() - buttonDownSprite.getWidth() - 8, pokedexsprite.getY() + pokedexsprite.getHeight() - buttonDownSprite.getHeight() - 47);
		down.setActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				if(arg0.getActionCommand().equals("Up"))
					if(selection + incrementer <= 493)
					{
						selection += incrementer;
						if((selection - 1) / 13 != scrollindex)
						{
							scrollindex = (selection - 1) / 13;
							updateNameList();
						}
						updatePokemonInfo();
						updateSelectionFrame();
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
						updateSelectionFrame();
					}
			}
		});

		up.setImages(buttonUpSprite, buttonUpSprite, buttonUpSprite);
		up.setSize(30, 30);
		up.setForeground(Color.white);
		up.setLocation(down.getX(), down.getY() - down.getHeight() - 5);
		up.setActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				if(arg0.getActionCommand().equals("Up"))
					if(selection - incrementer >= 1)
					{
						selection -= incrementer;
						if((selection - 1) / 13 != scrollindex)
						{
							scrollindex = (selection - 1) / 13;
							updateNameList();
						}
						updatePokemonInfo();
						updateSelectionFrame();
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
						updateSelectionFrame();
					}
			}
		});

		left.setImages(buttonLeftSprite, buttonLeftSprite, buttonLeftSprite);
		left.setSize(20, 38);
		left.setLocation(pokedexsprite.getX() + 32, pokedexsprite.getY() + pokedexsprite.getHeight() - buttonLeftSprite.getHeight() - 76);
		left.setForeground(Color.white);
		left.setActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				if(arg0.getActionCommand().equals("Up"))
				{
					tabindex--;
					if(tabindex < 1)
						tabindex = 4;

					updateInfoTab();
				}
			}
		});

		right.setImages(buttonRightSprite, buttonRightSprite, buttonRightSprite);
		right.setSize(20, 38);
		right.setLocation(left.getX() + 177, left.getY());
		right.setForeground(Color.white);
		right.setActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				if(arg0.getActionCommand().equals("Up"))
				{
					tabindex++;
					if(tabindex > 4)
						tabindex = 1;

					updateInfoTab();
				}
			}
		});

		inc1.setImages(incrementerPressedSprite, incrementerSprite, incrementerPressedSprite);
		inc1.setFont(GameClient.getPokedexFontMini());
		inc1.setText("1");
		inc1.setSize(36, 14);
		inc1.setLocation(down.getX() - 3, pokedexsprite.getY() + 70);
		inc1.setForeground(Color.white);
		currIncButton = inc1;
		currIncButton.disable();
		inc1.setActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				if(inc1.enabled())
				{
					currIncButton.enable();
					currIncButton = inc1;
					inc1.disable();
					incrementer = 1;
				}
			}
		});

		inc5.setImages(incrementerPressedSprite, incrementerSprite, incrementerPressedSprite);
		inc5.setFont(GameClient.getPokedexFontMini());
		inc5.setText("5");
		inc5.setSize(36, 14);
		inc5.setLocation(inc1.getX(), inc1.getY() + inc1.getHeight() + 5);
		inc5.setForeground(Color.white);
		inc5.setActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				if(inc5.enabled())
				{
					currIncButton.enable();
					currIncButton = inc5;
					inc5.disable();
					incrementer = 5;
				}
			}
		});

		inc10.setImages(incrementerPressedSprite, incrementerSprite, incrementerPressedSprite);
		inc10.setFont(GameClient.getPokedexFontMini());
		inc10.setText("10");
		inc10.setSize(36, 14);
		inc10.setLocation(inc5.getX(), inc5.getY() + inc5.getHeight() + 5);
		inc10.setForeground(Color.white);
		inc10.setActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				if(inc10.enabled())
				{
					currIncButton.enable();
					currIncButton = inc10;
					inc10.disable();
					incrementer = 10;
				}
			}
		});

		inc50.setImages(incrementerPressedSprite, incrementerSprite, incrementerPressedSprite);
		inc50.setFont(GameClient.getPokedexFontMini());
		inc50.setText("50");
		inc50.setSize(36, 14);
		inc50.setLocation(inc10.getX(), inc10.getY() + inc10.getHeight() + 5);
		inc50.setForeground(Color.white);
		inc50.setActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				if(inc50.enabled())
				{
					currIncButton.enable();
					currIncButton = inc50;
					inc50.disable();
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
		updateSelectionFrame();
	}

	private void updateSelectionFrame()
	{
		pokemonSelectionFrame.setLocation(pokedexsprite.getX() + 295, pokedexsprite.getY() + 59 + 22 * ((selection - 1) % 13));
		pokemonSelectionFrame.pack();
	}
}
