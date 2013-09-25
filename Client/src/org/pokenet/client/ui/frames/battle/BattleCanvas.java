package org.pokenet.client.ui.frames.battle;

import java.util.ArrayList;
import java.util.HashMap;
import org.lwjgl.util.Timer;
import org.newdawn.slick.Color;
import org.pokenet.client.GameClient;
import org.pokenet.client.backend.BattleManager;
import org.pokenet.client.backend.PokemonSpriteDatabase;
import org.pokenet.client.ui.components.Image;
import org.pokenet.client.ui.components.ProgressBar;
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
	public Image enemyDataBG;
	private Image enemyHPBar;
	private Image playerDataBG;
	private Image playerHPBar;

	private Image genderFemale;
	private Image genderMale;

	public Image enemyGender;
	private Image playerGender;

	private Image enemyStatus;
	private Image playerStatus;

	private Image enemyPokeSprite;
	private Image playerPokeSprite;

	private ProgressBar enemyHP;

	public Label enemyLv;
	public Label enemyNameLabel;

	private ArrayList<Image> m_enemyPokeballs = new ArrayList<Image>();
	// Image Loading stuff
	private String m_path = "res/battle/";
	private HashMap<String, de.matthiasmann.twl.renderer.Image> m_pokeballIcons = new HashMap<String, de.matthiasmann.twl.renderer.Image>();
	private HashMap<String, de.matthiasmann.twl.renderer.Image> m_statusIcons = new HashMap<String, de.matthiasmann.twl.renderer.Image>();
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
		setVisible(true);
		initComponents();
	}

	/**
	 * Initialises the components for the battlecanvas
	 */
	private void initComponents()
	{
		loadImages();
		initPlayerComponents();
		initEnemyComponents();
	}

	/**
	 * Initialises the components for the enemy
	 */
	private void initEnemyComponents()
	{
		enemyGender = new Image();
		enemyStatus = new Image();
		enemyPokeSprite = new Image();
		enemyLv = new Label();
		enemyLv.setTheme("label_enemy_level");
		enemyNameLabel = new Label();
		enemyNameLabel.setTheme("label_enemyname");
		enemyHP = new ProgressBar(0, 0);
		add(enemyDataBG);
		add(enemyStatus);
		add(enemyLv);
		add(enemyNameLabel);
		add(enemyHP);
		add(enemyHPBar);
		add(enemyPokeSprite);
		initEnemyPokeballs();
	}

	/**
	 * Initialises the components for the player
	 */
	private void initPlayerComponents()
	{
		playerGender = new Image();
		playerStatus = new Image();
		playerPokeSprite = new Image();
		playerLv = new Label();
		playerLv.setTheme("label_playerlevel");
		playerNameLabel = new Label();
		playerNameLabel.setTheme("label_playername");
		playerHP = new ProgressBar(0, 0);
		playerXP = new ProgressBar(0, 0, true);
		playerXP.setTheme("expbar");
		playerXPEND = new ProgressBar(0, 0, true);
		playerXPEND.setTheme("expbar");
		add(playerDataBG);
		add(playerGender);
		add(playerStatus);
		add(playerLv);
		add(playerNameLabel);
		add(playerHP);
		add(playerXP);
		add(playerHPBar);
		add(playerXPEND);
		add(playerPokeSprite);
	}

	/**
	 * Initializes the pokeballs for trainer battles with all empty pokeballs.
	 */
	private void initEnemyPokeballs()
	{
		m_enemyPokeballs = new ArrayList<>();
		for(int i = 0; i < 6; i++)
		{
			Image img = new Image(m_pokeballIcons.get("empty"));
			add(img);
			m_enemyPokeballs.add(img);
		}
	}

	/**
	 * Loads images from the theme.
	 */
	private void loadImages()
	{
		enemyDataBG = new Image(GameClient.getInstance().getTheme().getImage("battle_playerdata_background"));
		playerDataBG = new Image(GameClient.getInstance().getTheme().getImage("battle_enemydata_background"));

		enemyHPBar = new Image(GameClient.getInstance().getTheme().getImage("enemyhp"));
		playerHPBar = new Image(GameClient.getInstance().getTheme().getImage("playerhp"));
		// Gender Image Load
		genderFemale = new Image(GameClient.getInstance().getTheme().getImage("gender_female"));
		genderMale = new Image(GameClient.getInstance().getTheme().getImage("gender_male"));

		m_pokeballIcons.put("empty", GameClient.getInstance().getTheme().getImage("pokeball_empty"));
		m_pokeballIcons.put("normal", GameClient.getInstance().getTheme().getImage("pokeball_normal"));
		m_pokeballIcons.put("status", GameClient.getInstance().getTheme().getImage("pokeball_status"));
		m_pokeballIcons.put("fainted", GameClient.getInstance().getTheme().getImage("pokeball_fainted"));
	}

	/**
	 * Draw our enemy poke's information
	 */
	public void setEnemyInfo()
	{
		// display enemy's data
		enemyNameLabel.setText(BattleManager.getInstance().getCurEnemyPoke().getName());

		if(BattleManager.getInstance().getCurEnemyPoke().getGender() == 0)
		{
			enemyGender = genderFemale;
		}
		else if(BattleManager.getInstance().getCurEnemyPoke().getGender() == 1)
		{
			enemyGender = genderMale;
		}

		enemyLv.setText("Lv: " + BattleManager.getInstance().getCurEnemyPoke().getLevel());
		setEnemyPokeSprite();
	}

	/**
	 * Sets the enemy's Pokemon sprite
	 */
	public void setEnemyPokeSprite()
	{
		GameClient.getInstance().getGUI().invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				if(BattleManager.getInstance().getCurEnemyPoke().isShiny())
				{
					enemyPokeSprite.setImage(PokemonSpriteDatabase.getShineyFront(BattleManager.getInstance().getCurEnemyPoke().getGender(), BattleManager.getInstance().getCurEnemyPoke()
							.getSpriteNumber()));
				}
				else
				{
					enemyPokeSprite.setImage(PokemonSpriteDatabase.getNormalFront(BattleManager.getInstance().getCurEnemyPoke().getGender(), BattleManager.getInstance().getCurEnemyPoke()
							.getSpriteNumber()));
				}
			}
		});
	}

	/**
	 * Draw our poke's information
	 */
	public void setPlayerInfo()
	{
		// display player's data
		playerNameLabel.setText(BattleManager.getInstance().getCurPoke().getName());

		if(BattleManager.getInstance().getCurPoke().getGender() == 0)
		{
			playerGender = genderFemale;
		}
		else if(BattleManager.getInstance().getCurPoke().getGender() == 1)
		{
			playerGender = genderMale;
		}

		playerLv.setText("Lv:" + BattleManager.getInstance().getCurPoke().getLevel());
		setPlayerPokemonSprite();
	}

	/**
	 * Draws our Pokemon
	 */
	public void setPlayerPokemonSprite()
	{
		GameClient.getInstance().getGUI().invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				if(BattleManager.getInstance().getCurPoke().isShiny())
				{
					playerPokeSprite.setImage(PokemonSpriteDatabase.getShineyBack(BattleManager.getInstance().getCurPoke().getGender(), BattleManager.getInstance().getCurPoke().getSpriteNumber()));
				}
				else
				{
					playerPokeSprite.setImage(PokemonSpriteDatabase.getNormalBack(BattleManager.getInstance().getCurPoke().getGender(), BattleManager.getInstance().getCurPoke().getSpriteNumber()));// FileLoader.toTWLImage(BattleManager.getInstance().getCurPoke().getBackSprite()));
				}
			}
		});
	}

	/**
	 * Starts the enemy HP Bar
	 */
	public void initEnemyHPBar()
	{
		// show enemy hp bar
		enemyHP.setMinimum(0);
		enemyHP.setMaximum(BattleManager.getInstance().getCurEnemyPoke().getMaxHP());
		enemyHP.setValue(BattleManager.getInstance().getCurEnemyPoke().getCurHP());
		updateEnemyHPBarColor();
	}

	/**
	 * Starts the player's HP bar
	 */
	public void initPlayerHPBar()
	{
		// show hp bar
		playerHP.setMinimum(0);
		playerHP.setMaximum(BattleManager.getInstance().getCurPoke().getMaxHP());
		playerHP.setValue(BattleManager.getInstance().getCurPoke().getCurHP());
		updatePlayerHPBarColor();
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

		playerXP.setMinimum(BattleManager.getInstance().getCurPoke().getExpLvl());
		playerXP.setMaximum(xpMax);
		playerXPEND.setMinimum(xpENDMin);
		playerXPEND.setMaximum((int) max);
		updatePlayerXPValue(BattleManager.getInstance().getCurPoke().getExp());
	}

	/**
	 * Loads the status icons
	 */
	public void loadStatusIcons()
	{
		m_statusIcons.put("poison", GameClient.getInstance().getTheme().getImage("status_psn"));
		m_statusIcons.put("sleep", GameClient.getInstance().getTheme().getImage("status_slp"));
		m_statusIcons.put("freeze", GameClient.getInstance().getTheme().getImage("status_frz"));
		m_statusIcons.put("burn", GameClient.getInstance().getTheme().getImage("status_brn"));
		m_statusIcons.put("paralysis", GameClient.getInstance().getTheme().getImage("status_par"));
	}

	/**
	 * Sets the image for the pokeballs
	 * 
	 * @param i
	 * @param key
	 */
	public void setEnemyPokeballImage(int i, String key)
	{
		m_enemyPokeballs.get(i).setImage(m_pokeballIcons.get(key));
	}

	/**
	 * Shows pokeballs
	 */
	public void showPokeballs()
	{
		for(Image img : m_enemyPokeballs)
		{
			img.setVisible(true);
		}
	}

	public void hidePokeballs()
	{
		for(Image img : m_enemyPokeballs)
		{
			img.setVisible(false);
		}
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
				playerStatus.setImage(m_statusIcons.get(status.toLowerCase()));
			}
			else if(status.equalsIgnoreCase("normal"))
			{
				BattleManager.getInstance().getOurStatuses().remove(BattleManager.getInstance().getCurPokeIndex());
				playerStatus.setImage(null);
			}
		}
		else if(status.equalsIgnoreCase("poison") || status.equalsIgnoreCase("freeze") || status.equalsIgnoreCase("burn") || status.equalsIgnoreCase("paralysis") || status.equalsIgnoreCase("sleep"))
		{
			enemyStatus.setImage(m_statusIcons.get(status.toLowerCase()));

		}
		else if(status.equalsIgnoreCase("normal"))
		{
			enemyStatus = null;
		}
	}

	/**
	 * Starts a battle
	 */
	public void startBattle()
	{
		setPlayerInfo();
		initPlayerHPBar();
		initPlayerXPBar();
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
		{
			healing = true;
		}
		while(currentHP != newValue)
		{
			while(mTimer.getTime() <= 0.05)
			{
				Timer.tick();
			}
			if(healing)
			{
				currentHP += 1.00f;
			}
			else
			{
				currentHP -= 1.00f;
			}
			enemyHP.setValue(currentHP);
			updateEnemyHPBarColor();
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
		{
			healing = true;
		}

		while(currentHP != newValue)
		{
			while(mTimer.getTime() <= 0.05)
			{
				Timer.tick();
			}
			if(healing)
			{
				currentHP += 1.00f;
			}
			else
			{
				currentHP -= 1.00f;
			}
			playerHP.setValue(currentHP);
			updatePlayerHPBarColor();

			mTimer.reset();
		}
	}

	/**
	 * Updates the XP bar for the player's poke
	 * 
	 * @param newValue
	 */
	public void updatePlayerXPValue(int newValue)
	{
		playerXP.setValue(newValue);
		playerXPEND.setValue(newValue);
	}

	public void setPlayerHP(float hp)
	{
		playerHP.setValue(hp);
	}

	public void setPlayerMaxHP(int maxHP)
	{
		playerHP.setMaximum(maxHP);
	}

	public void updatePlayerHPBarColor()
	{
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
	}

	public void updateEnemyHPBarColor()
	{
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

	public void update()
	{

	}

	@Override
	public void layout()
	{
		enemyDataBG.setPosition(getInnerX(), getInnerY());
		enemyHPBar.setPosition(getInnerX() + 15, getInnerY() + 25);
		enemyNameLabel.setSize(enemyNameLabel.computeTextWidth(), enemyNameLabel.computeTextHeight());
		enemyNameLabel.setPosition(enemyDataBG.getX() + 15, enemyDataBG.getY() + 7);
		enemyGender.setPosition(enemyNameLabel.getX() + GameClient.getInstance().getFontSmall().getWidth(enemyNameLabel.getText()), enemyNameLabel.getY());
		enemyLv.setSize(GameClient.getInstance().getFontSmall().getWidth(enemyLv.getText()), GameClient.getInstance().getFontSmall().getHeight(enemyLv.getText()));
		enemyLv.setPosition(enemyDataBG.getX() + 105, enemyDataBG.getY() + 7);
		enemyStatus.setPosition(105 + getInnerX(), 40 + getInnerY());
		enemyHP.setPosition(getInnerX() + 37, getInnerY() + 27);
		enemyHP.setSize(72, 5);

		playerDataBG.setPosition(getInnerX() + 90, getInnerY() + 98);
		playerNameLabel.setSize(GameClient.getInstance().getFontSmall().getWidth(playerNameLabel.getText()), GameClient.getInstance().getFontSmall().getHeight(playerNameLabel.getText()));
		playerNameLabel.setPosition(playerDataBG.getX() + 30, playerDataBG.getY() + 7);
		playerLv.setSize(GameClient.getInstance().getFontSmall().getWidth(playerLv.getText()), GameClient.getInstance().getFontSmall().getHeight(playerLv.getText()));
		playerLv.setPosition(getInnerX() + 210, playerDataBG.getY() + 7);
		playerGender.setPosition(playerNameLabel.getX() + GameClient.getInstance().getFontSmall().getWidth(playerNameLabel.getText()) + 3, playerNameLabel.getY());
		playerXP.setSize(99, 4);
		playerXPEND.setSize(8, 9);
		playerXP.setPosition(getInnerX() + 145, getInnerY() + 132);
		playerXPEND.setPosition(getInnerX() + 143, getInnerY() + 125);
		playerHPBar.setPosition(getInnerX() + 135, getInnerY() + 120);
		playerHP.setSize(72, 5);
		playerHP.setPosition(getInnerX() + 170, getInnerY() + 125);
		playerStatus.setPosition(playerNameLabel.getX() + getInnerX(), 125 + getInnerY());

		for(int i = 0; i < 6; i++)
		{
			m_enemyPokeballs.get(i).setPosition((125 + 14 * i + i * 5) + getInnerX(), 3 + getInnerY());
		}

		enemyPokeSprite.setPosition(getInnerX() + 150, getInnerY() + 21);
		playerPokeSprite.setPosition(getInnerX() + 15, getInnerY() + 70);
	}
}
