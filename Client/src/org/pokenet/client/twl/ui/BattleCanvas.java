package org.pokenet.client.twl.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.lwjgl.util.Timer;
import org.newdawn.slick.Color;
import org.pokenet.client.GameClient;
import org.pokenet.client.backend.BattleManager;
import org.pokenet.client.backend.PokemonSpriteDatabase;
import org.pokenet.client.twl.ui.base.Image;
import org.pokenet.client.twl.ui.base.ProgressBar;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.Widget;

/**
 * Canvas for drawing the battle and it's animations. This is the topside of the battleFrame
 * 
 * @author Myth1c
 */
public class BattleCanvas extends Widget
{
	// Images
	private Image enemyDataBG;
	private Image enemyHPBar;
	private Image playerDataBG;
	private Image playerHPBar;

	private Image genderFemale;
	private Image genderMale;

	private Image enemyGender;
	private Image playerGender;

	private Image enemyStatus;
	private Image playerStatus;

	private Image enemyPokeSprite;
	private Image playerPokeSprite;

	private ProgressBar enemyHP;

	private Label enemyLv;
	private Label enemyNameLabel;

	private List<Image> m_enemyPokeballs = new ArrayList<Image>();
	// Image Loading stuff
	private String m_path = "res/battle/";
	private HashMap<String, Image> m_pokeballIcons = new HashMap<String, Image>();
	private HashMap<String, Image> m_statusIcons = new HashMap<String, Image>();
	private Timer mTimer = new Timer();

	private ProgressBar playerHP;

	private Label playerLv;
	private Label playerNameLabel;
	private ProgressBar playerXP, playerXPEND;

	private de.matthiasmann.twl.renderer.Image hp_high = GameClient.getInstance().getTheme().getImage("hpbar_high");
	private de.matthiasmann.twl.renderer.Image hp_middle = GameClient.getInstance().getTheme().getImage("hpbar_middle");
	private de.matthiasmann.twl.renderer.Image hp_low = GameClient.getInstance().getTheme().getImage("hpbar_low");

	/**
	 * Default constructor
	 */
	public BattleCanvas()
	{
		String respath = System.getProperty("res.path");
		if(respath == null)
			respath = "";
		m_path = respath + m_path;
		setSize(257, 144);
		setVisible(true);
		loadImages();
		startPokeballs();
		loadStatusIcons();
	}

	/**
	 * Draws the background
	 */
	public void drawBackground()
	{
		String respath = System.getProperty("res.path");
		if(respath == null || respath.equals("null"))
			respath = "";

	}

	/**
	 * Draw our enemy poke's information
	 */
	public void drawEnemyInfo()
	{
		// display enemy's data
		enemyDataBG.setPosition(0, 0);

		enemyNameLabel.setText(BattleManager.getInstance().getCurEnemyPoke().getName());
		enemyNameLabel.setTheme("label_enemyname");
		enemyNameLabel.setSize(GameClient.getInstance().getFontSmall().getWidth(enemyNameLabel.getText()), GameClient.getInstance().getFontSmall().getHeight(enemyNameLabel.getText()));
		enemyNameLabel.setPosition(enemyDataBG.getX() + 15, enemyDataBG.getY() + 7);

		if(BattleManager.getInstance().getCurEnemyPoke().getGender() == 0)
		{
			enemyGender = genderFemale;
			enemyGender.setPosition(enemyNameLabel.getX() + GameClient.getInstance().getFontSmall().getWidth(enemyNameLabel.getText()), enemyNameLabel.getY());
		}
		else if(BattleManager.getInstance().getCurEnemyPoke().getGender() == 1)
		{
			enemyGender = genderMale;
			enemyGender.setPosition(enemyNameLabel.getX() + GameClient.getInstance().getFontSmall().getWidth(enemyNameLabel.getText()), enemyNameLabel.getY());
		}

		enemyLv.setText("Lv: " + BattleManager.getInstance().getCurEnemyPoke().getLevel());
		enemyLv.setTheme("label_enemy_level");
		enemyLv.setSize(GameClient.getInstance().getFontSmall().getWidth(enemyLv.getText()), GameClient.getInstance().getFontSmall().getHeight(enemyLv.getText()));
		enemyLv.setPosition(enemyDataBG.getX() + 105, enemyDataBG.getY() + 7);

		enemyHPBar.setPosition(15, 25);
		add(enemyDataBG);
		add(enemyNameLabel);
		add(enemyLv);
		add(enemyGender);
		// initEnemyHPBar();
	}

	/**
	 * Draws the enemy's Pokemon
	 */
	public void drawEnemyPoke()
	{
		// enemyPokeSprite = new Image(BattleManager.getInstance().getCurEnemyPoke().getSprite());
		if(BattleManager.getInstance().getCurEnemyPoke().isShiny())
		{
			enemyPokeSprite = new Image(
					PokemonSpriteDatabase.getShineyFront(BattleManager.getInstance().getCurEnemyPoke().getGender(), BattleManager.getInstance().getCurEnemyPoke().getSpriteNumber()));
		}
		else
		{
			enemyPokeSprite = new Image(
					PokemonSpriteDatabase.getNormalFront(BattleManager.getInstance().getCurEnemyPoke().getGender(), BattleManager.getInstance().getCurEnemyPoke().getSpriteNumber()));// FileLoader.toTWLImage(BattleManager.getInstance().getCurPoke().getBackSprite()));
		}
		enemyPokeSprite.setPosition(150, 21);
		add(enemyPokeSprite);
	}

	/**
	 * Draw our poke's information
	 */
	public void drawOurInfo()
	{
		// display player's data
		playerDataBG.setPosition(90, 98);
		// System.out.println(getInnerX() + ", " + getInnerY());

		playerNameLabel.setTheme("label_playername");
		playerNameLabel.setText(BattleManager.getInstance().getCurPoke().getName());
		playerNameLabel.setSize(GameClient.getInstance().getFontSmall().getWidth(playerNameLabel.getText()), GameClient.getInstance().getFontSmall().getHeight(playerNameLabel.getText()));
		playerNameLabel.setPosition(playerDataBG.getX() + 30, playerDataBG.getY() + 7);

		if(BattleManager.getInstance().getCurPoke().getGender() == 0)
		{
			playerGender = genderFemale;
			playerGender.setPosition(playerNameLabel.getX() + GameClient.getInstance().getFontSmall().getWidth(playerNameLabel.getText()) + 3, playerNameLabel.getY());
		}
		else if(BattleManager.getInstance().getCurPoke().getGender() == 1)
		{
			playerGender = genderMale;
			playerGender.setPosition(playerNameLabel.getX() + GameClient.getInstance().getFontSmall().getWidth(playerNameLabel.getText()) + 3, playerNameLabel.getY());
		}

		playerLv.setText("Lv:" + BattleManager.getInstance().getCurPoke().getLevel());
		playerLv.setTheme("label_playerlevel");
		playerLv.setSize(GameClient.getInstance().getFontSmall().getWidth(playerLv.getText()), GameClient.getInstance().getFontSmall().getHeight(playerLv.getText()));
		playerLv.setPosition(210, playerDataBG.getY() + 7);

		add(playerDataBG);
		add(playerNameLabel);
		add(playerLv);
		add(playerGender);
		// initPlayerHPBar();
		// initPlayerXPBar();
	}

	/**
	 * Draws our Pokemon
	 */
	public void drawOurPoke()
	{
		if(BattleManager.getInstance().getCurPoke().isShiny())
			playerPokeSprite = new Image(PokemonSpriteDatabase.getShineyBack(BattleManager.getInstance().getCurPoke().getGender(), BattleManager.getInstance().getCurPoke().getSpriteNumber()));
		else
			playerPokeSprite = new Image(PokemonSpriteDatabase.getNormalBack(BattleManager.getInstance().getCurPoke().getGender(), BattleManager.getInstance().getCurPoke().getSpriteNumber()));// FileLoader.toTWLImage(BattleManager.getInstance().getCurPoke().getBackSprite()));
		playerPokeSprite.setPosition(15, 70);
		add(playerPokeSprite);
	}

	/**
	 * Hides pokeballs
	 */
	public void hidePokeballs()
	{
		for(int i = 0; i < 6; i++)
		{
			m_enemyPokeballs.set(i, m_pokeballIcons.get("empty"));
		}
	}

	/**
	 * Starts the enemy HP Bar
	 */
	public void initEnemyHPBar()
	{
		// show enemy hp bar
		enemyHP = new ProgressBar(0, BattleManager.getInstance().getCurEnemyPoke().getMaxHP());
		enemyHP.setSize(72, 5);
		enemyHP.setValue(BattleManager.getInstance().getCurEnemyPoke().getCurHP());
		System.out.println("Current HP " + BattleManager.getInstance().getCurEnemyPoke().getCurHP());
		if(BattleManager.getInstance().getCurEnemyPoke().getCurHP() > BattleManager.getInstance().getCurEnemyPoke().getMaxHP() / 2)
		{
			setEnemyHPColor(Color.green);
		}
		else if(BattleManager.getInstance().getCurEnemyPoke().getCurHP() < BattleManager.getInstance().getCurEnemyPoke().getMaxHP() / 2
				&& BattleManager.getInstance().getCurEnemyPoke().getCurHP() > BattleManager.getInstance().getCurEnemyPoke().getMaxHP() / 3)
		{
			setEnemyHPColor(Color.orange);
		}
		else if(BattleManager.getInstance().getCurEnemyPoke().getCurHP() < BattleManager.getInstance().getCurEnemyPoke().getMaxHP() / 3)
		{
			setEnemyHPColor(Color.red);
		}

		enemyHP.setPosition(36, 27);
		add(enemyHPBar);
		add(enemyHP);
	}

	/**
	 * Starts the player's HP bar
	 */
	public void initPlayerHPBar()
	{
		// show hp bar
		playerHP = new ProgressBar(0, BattleManager.getInstance().getCurPoke().getMaxHP());
		playerHP.setSize(72, 5);
		playerHP.setValue(BattleManager.getInstance().getCurPoke().getCurHP());
		if(BattleManager.getInstance().getCurPoke().getCurHP() > BattleManager.getInstance().getCurPoke().getMaxHP() / 2)
		{
			setPlayerHPColor(Color.green);
		}
		else if(BattleManager.getInstance().getCurPoke().getCurHP() < BattleManager.getInstance().getCurPoke().getMaxHP() / 2
				&& BattleManager.getInstance().getCurPoke().getCurHP() > BattleManager.getInstance().getCurPoke().getMaxHP() / 3)
		{
			setPlayerHPColor(Color.orange);
		}
		else if(BattleManager.getInstance().getCurPoke().getCurHP() < BattleManager.getInstance().getCurPoke().getMaxHP() / 3)
		{
			setPlayerHPColor(Color.red);
		}

		playerHP.setPosition(170, 125);
		add(playerHP);
	}

	/**
	 * Starts the player's XP bar
	 */
	public void initPlayerXPBar()
	{
		float max = BattleManager.getInstance().getCurPoke().getExpLvlUp();
		float min = BattleManager.getInstance().getCurPoke().getExpLvl();
		// float val = BattleManager.getInstance().getCurPoke().getExp();
		float testVal = (max - min) / 101;
		int xpMax = (int) (max - testVal * 1.0);
		int xpENDMin = (int) (max - testVal * 8.0);

		if(playerXP == null)
		{
			/* playerXP = new ProgressBar((int)BattleManager.getInstance().getCurPoke().getExpLvl(), (int)BattleManager.getInstance().getCurPoke().getExpLvlUp(), true); */
			playerXP = new ProgressBar(BattleManager.getInstance().getCurPoke().getExpLvl(), xpMax, true);
			playerXP.setTheme("expbar");
			playerXPEND = new ProgressBar(xpENDMin, max, true);
			playerXPEND.setTheme("expbar");
		}
		else
		{
			playerXP.setMinimum(BattleManager.getInstance().getCurPoke().getExpLvl());
			playerXP.setMaximum(xpMax);
			playerXPEND.setMinimum(xpENDMin);
			playerXPEND.setMaximum((int) max);
		}
		playerXP.setSize(99, 4);
		playerXPEND.setSize(8, 9);

		updatePlayerXP(BattleManager.getInstance().getCurPoke().getExp());

		playerXP.setPosition(145, 132);
		playerXPEND.setPosition(143, 125);

		playerHPBar.setPosition(135, 120);
		add(playerXPEND);
		add(playerXP);
		add(playerHPBar);
	}

	/**
	 * Loads images that can't be loading on startBattle()
	 */
	public void loadImages()
	{
		enemyDataBG = new Image(GameClient.getInstance().getTheme().getImage("battle_playerdata_background"));
		playerDataBG = new Image(GameClient.getInstance().getTheme().getImage("battle_enemydata_background"));

		enemyHPBar = new Image(GameClient.getInstance().getTheme().getImage("enemyhp"));
		playerHPBar = new Image(GameClient.getInstance().getTheme().getImage("playerhp"));
		// Gender Image Load
		genderFemale = new Image(GameClient.getInstance().getTheme().getImage("gender_female"));
		genderMale = new Image(GameClient.getInstance().getTheme().getImage("gender_male"));

		m_pokeballIcons.put("empty", new Image(GameClient.getInstance().getTheme().getImage("pokeball_empty")));
		m_pokeballIcons.put("normal", new Image(GameClient.getInstance().getTheme().getImage("pokeball_normal")));
		m_pokeballIcons.put("status", new Image(GameClient.getInstance().getTheme().getImage("pokeball_status")));
		m_pokeballIcons.put("fainted", new Image(GameClient.getInstance().getTheme().getImage("pokeball_fainted")));

		// playerXPBar.setSize(101, 4);
	}

	/**
	 * Loads the status icons
	 */
	public void loadStatusIcons()
	{
		m_statusIcons.put("poison", new Image(GameClient.getInstance().getTheme().getImage("status_psn")));
		m_statusIcons.put("sleep", new Image(GameClient.getInstance().getTheme().getImage("status_slp")));
		m_statusIcons.put("freeze", new Image(GameClient.getInstance().getTheme().getImage("status_frz")));
		m_statusIcons.put("burn", new Image(GameClient.getInstance().getTheme().getImage("status_brn")));
		m_statusIcons.put("paralysis", new Image(GameClient.getInstance().getTheme().getImage("status_par")));
	}

	/**
	 * Centers the battle window
	 */
	public void positionCanvas()
	{
		float y = BattleManager.getInstance().getBattleWindow().getY() + 48;
		float x = BattleManager.getInstance().getBattleWindow().getX() + 1;
		setPosition(Math.round(x), Math.round(y));
	}

	/**
	 * Sets the image for the pokeballs
	 * 
	 * @param i
	 * @param key
	 */
	public void setPokeballImage(int i, String key)
	{
		m_enemyPokeballs.set(i, m_pokeballIcons.get(key));
	}

	/**
	 * Sets the status image
	 * 
	 * @param trainer
	 * @param status
	 */
	public void setStatus(int trainer, String status)
	{
		if(trainer == 0)
		{
			// The player's pokemon
			if(status.equalsIgnoreCase("poison") || status.equalsIgnoreCase("freeze") || status.equalsIgnoreCase("burn") || status.equalsIgnoreCase("paralysis") || status.equalsIgnoreCase("sleep"))
			{
				BattleManager.getInstance().getOurStatuses().put(BattleManager.getInstance().getCurPokeIndex(), status);
				playerStatus = m_statusIcons.get(status.toLowerCase());
				playerStatus.setPosition(playerNameLabel.getX() + getInnerX(), 125 + getInnerY());
			}
			else if(status.equalsIgnoreCase("normal"))
			{
				BattleManager.getInstance().getOurStatuses().remove(BattleManager.getInstance().getCurPokeIndex());
				playerStatus = null;
			}
		}
		else // The enemy's pokemon
		if(status.equalsIgnoreCase("poison") || status.equalsIgnoreCase("freeze") || status.equalsIgnoreCase("burn") || status.equalsIgnoreCase("paralysis") || status.equalsIgnoreCase("sleep"))
		{
			enemyStatus = m_statusIcons.get(status.toLowerCase());
			enemyStatus.setPosition(105 + getInnerX(), 40 + getInnerY());

		}
		else if(status.equalsIgnoreCase("normal"))
			enemyStatus = null;
	}

	/**
	 * Shows pokeballs
	 */
	public void showPokeballs()
	{
		/* for(Label l : m_enemyPokeballs)
		 * if(!containsChild(l))
		 * add(l); */
	}

	/**
	 * Starts a battle
	 */
	public void startBattle()
	{
		initComponents();
		positionCanvas();
		// drawBackground();
		drawOurPoke();
		drawOurInfo();
		initPlayerHPBar();
		initPlayerXPBar();
	}

	/**
	 * Initializes the pokeballs for trainer battles
	 */
	public void startPokeballs()
	{
		m_enemyPokeballs.clear();
		for(int i = 0; i < 6; i++)
		{
			m_enemyPokeballs.add(new Image());
			m_enemyPokeballs.set(i, m_pokeballIcons.get("empty"));
			m_enemyPokeballs.get(i).setPosition((125 + 14 * i + i * 5) + getInnerX(), 3 + getInnerY());
		}
	}

	/**
	 * Stops the canvas
	 */
	public void stop()
	{
		try
		{
			removeAllChildren();
			hidePokeballs();
		}
		catch(Exception e)
		{
			System.err.println(e);
		}
		playerHP = null;
		playerXP = null;
		enemyHP = null;
		playerNameLabel = null;
		enemyNameLabel = null;
		playerDataBG = null;
		enemyDataBG = null;
		playerLv = null;
		enemyLv = null;
		playerStatus = null;
		enemyStatus = null;
		enemyGender = null;
		playerGender = null;
	}

	/**
	 * Updates the HP bar for the opponent's poke
	 * 
	 * @param newValue
	 */
	public void updateEnemyHP(int newValue)
	{
		boolean healing = false;
		int currentHP = (int) enemyHP.getValue();
		if(newValue > currentHP)
			healing = true;
		while(currentHP != newValue)
		{
			while(mTimer.getTime() <= 0.05)
				Timer.tick();
			if(healing)
				currentHP += 1.00f;
			else
				currentHP -= 1.00f;
			enemyHP.setValue(currentHP);

			if(BattleManager.getInstance().getCurEnemyPoke().getCurHP() > BattleManager.getInstance().getCurEnemyPoke().getMaxHP() / 2)
			{
				setEnemyHPColor(Color.green);
			}
			else if(BattleManager.getInstance().getCurEnemyPoke().getCurHP() < BattleManager.getInstance().getCurEnemyPoke().getMaxHP() / 2
					&& BattleManager.getInstance().getCurEnemyPoke().getCurHP() > BattleManager.getInstance().getCurEnemyPoke().getMaxHP() / 3)
			{
				setEnemyHPColor(Color.orange);
			}
			else if(BattleManager.getInstance().getCurEnemyPoke().getCurHP() < BattleManager.getInstance().getCurEnemyPoke().getMaxHP() / 3)
			{
				setEnemyHPColor(Color.red);
			}
			mTimer.reset();
		}
	}

	/**
	 * Updates the HP bar for the player's poke
	 * 
	 * @param newValue
	 */
	public void updatePlayerHP(int newValue)
	{
		boolean healing = false;
		int currentHP = (int) playerHP.getValue();

		if(newValue > currentHP)
			healing = true;

		while(currentHP != newValue)
		{
			while(mTimer.getTime() <= 0.05)
				Timer.tick();
			if(healing)
				currentHP += 1.00f;
			else
				currentHP -= 1.00f;
			playerHP.setValue(currentHP);
			// playerHP.setText(BattleManager.getInstance().getCurPoke().getCurHP() + "/" + BattleManager.getInstance().getCurPoke().getMaxHP());

			if(BattleManager.getInstance().getCurPoke().getCurHP() > BattleManager.getInstance().getCurPoke().getMaxHP() / 2)
			{
				setPlayerHPColor(Color.green);
			}
			else if(BattleManager.getInstance().getCurPoke().getCurHP() < BattleManager.getInstance().getCurPoke().getMaxHP() / 2
					&& BattleManager.getInstance().getCurPoke().getCurHP() > BattleManager.getInstance().getCurPoke().getMaxHP() / 3)
			{
				setPlayerHPColor(Color.orange);
			}
			else if(BattleManager.getInstance().getCurPoke().getCurHP() < BattleManager.getInstance().getCurPoke().getMaxHP() / 3)
			{
				setPlayerHPColor(Color.red);
			}
			mTimer.reset();
		}
	}

	/**
	 * Updates the XP bar for the player's poke
	 * 
	 * @param newValue
	 */
	public void updatePlayerXP(int newValue)
	{
		playerXP.setValue(newValue);
		playerXPEND.setValue(newValue);
	}

	private void initComponents()
	{
		playerHP = new ProgressBar(0, 0);
		enemyHP = new ProgressBar(0, 0);
		playerXPEND = new ProgressBar(0, 0, true);
		playerNameLabel = new Label();
		enemyNameLabel = new Label();
		// End Gender
		playerLv = new Label();
		enemyLv = new Label();
	}

	public void setPlayerHP(float hp)
	{
		playerHP.setValue(hp);
	}

	public void setPlayerMaxHP(int maxHP)
	{
		playerHP.setMaximum(maxHP);
	}

	public void setPlayerHPColor(Color c)
	{
		if(c == Color.green)
		{
			playerHP.setProgressImage(hp_high);
		}
		else if(c == Color.orange)
		{
			playerHP.setProgressImage(hp_middle);
		}
		else if(c == Color.red)
		{
			playerHP.setProgressImage(hp_low);
		}
	}

	public void setEnemyHP(float hp)
	{
		enemyHP.setValue(hp);
	}

	public void setEnemyMaxHP(int maxHP)
	{
		enemyHP.setMaximum(maxHP);
	}

	public void setEnemyHPColor(Color c)
	{
		if(c == Color.green)
		{
			enemyHP.setProgressImage(hp_high);
		}
		else if(c == Color.orange)
		{
			enemyHP.setProgressImage(hp_middle);
		}
		else if(c == Color.red)
		{
			enemyHP.setProgressImage(hp_low);
		}
	}

}
