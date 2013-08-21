package org.pokenet.client.backend;

import java.util.HashMap;
import java.util.Map;
import org.newdawn.slick.Color;
import org.pokenet.client.GameClient;
import org.pokenet.client.backend.entity.OurPlayer;
import org.pokenet.client.backend.entity.OurPokemon;
import org.pokenet.client.backend.entity.Pokemon;
import org.pokenet.client.constants.Music;
import org.pokenet.client.twl.ui.BattleDialog;

/**
 * Handles battle events and controls the battle window
 * 
 * @author ZombieBear
 */
public class BattleManager
{
	private static BattleManager m_instance;
	private boolean m_isBattling = false;
	private BattleDialog m_battle;
	private boolean m_canFinish = false;
	private int m_curEnemyIndex;
	private Pokemon m_curEnemyPoke;
	private OurPokemon m_curPoke;
	private int m_curPokeIndex;
	private String m_curTrack;
	private String m_enemy;
	private Pokemon[] m_enemyPokes;
	private boolean m_isWild;
	private OurPokemon[] m_ourPokes;
	private Map<Integer, String> m_ourStatuses = new HashMap<Integer, String>();
	private OurPlayer m_player;
	private BattleTimeLine m_timeLine;

	/**
	 * Default Constructor
	 */
	private BattleManager()
	{
		m_battle = new BattleDialog();
		GameClient.getInstance().getHUD().setBattleDialog(m_battle);
		GameClient.getInstance().getGUIPane().getHUD().add(m_battle);
		m_timeLine = new BattleTimeLine();
		m_battle.setVisible(false);
	}

	/**
	 * Returns the instance
	 * 
	 * @return
	 */
	public static BattleManager getInstance()
	{
		if(m_instance == null)
			m_instance = new BattleManager();
		return m_instance;
	}

	/**
	 * Returns true if a battle is in progress
	 * 
	 * @return true if a battle is in progress
	 */
	public boolean isBattling()
	{
		return m_isBattling;
	}

	public boolean canFinish()
	{
		return m_canFinish;
	}

	/**
	 * Ends the battle
	 */
	public void endBattle()
	{
		BattleManager.getInstance().setFinish(true);
		GameClient.getInstance().getGUIPane().showHUD();
		m_timeLine.endBattle();
		if(m_battle.getControlFrame() != null)
		{
			for(int i = 0; i < m_battle.getControlFrame().getNumChildren(); i++)
				m_battle.getControlFrame().removeChild(i);
			// m_battle.getControlFrame();//TODO
		}
		m_battle.setVisible(false);
		m_isBattling = false;
		if(GameClient.getInstance().getHUD().hasBattlebag())
			GameClient.getInstance().getHUD().removeBattlebag();
		GameClient.getInstance().getGUIPane().removeChild(m_battle);
		while(GameClient.getInstance().getHUD().hasBattleDialog())
			;
		GameClient.getInstance().getSoundPlayer().setTrackByLocation(GameClient.getInstance().getMapMatrix().getCurrentMap().getName());
		if(GameClient.getInstance().getSoundPlayer().m_trackName == Music.PVNPC)
			GameClient.getInstance().getSoundPlayer().setTrack(m_curTrack);
		m_battle = null;
		m_timeLine = null;
	}

	/**
	 * Gets the BattleWindow
	 * 
	 * @return
	 */
	public BattleDialog getBattleWindow()
	{
		return m_battle;
	}

	/**
	 * Returns the active enemy pokemon's index in party
	 * 
	 * @return
	 */
	public int getCurEnemyIndex()
	{
		return m_curEnemyIndex;
	}

	/**
	 * Returns the enemy's active pokemon or the wild pokemon
	 */
	public Pokemon getCurEnemyPoke()
	{
		return m_curEnemyPoke;
	}

	/**
	 * Returns the player's active pokemon
	 */
	public OurPokemon getCurPoke()
	{
		return m_curPoke;
	}

	/**
	 * Returns the active pokemon's index in party
	 * 
	 * @return
	 */
	public int getCurPokeIndex()
	{
		return m_curPokeIndex;
	}

	public String getCurrentTrack()
	{
		return m_curTrack;
	}

	/**
	 * Returns a list of our pokes who are affected by statuses
	 * 
	 * @return a list of our pokes who are affected by statuses
	 */
	public Map<Integer, String> getOurStatuses()
	{
		return m_ourStatuses;
	}

	/**
	 * Returns the TimeLine
	 * 
	 * @return m_timeLine
	 */
	public BattleTimeLine getTimeLine()
	{
		return m_timeLine;
	}

	/**
	 * Returns a boolean determining whether the pokemon is wild
	 * 
	 * @return m_isWild
	 */
	public boolean isWild()
	{
		return m_isWild;
	}

	/**
	 * Requests a move from the player
	 */
	public void requestMoves()
	{
		m_battle.enableMoves();
		m_battle.showAttack();
	}

	public void setBattling(boolean isBattling)
	{
		m_isBattling = isBattling;
	}

	public void setCurrentTrack(String m_curTrack)
	{
		this.m_curTrack = m_curTrack;
	}

	/**
	 * Sets the enemy's name
	 * 
	 * @param name
	 */
	public void setEnemyName(String name)
	{
		m_enemy = name;
	}

	/**
	 * Adds an enemy poke
	 * 
	 * @param index
	 * @param name
	 * @param level
	 * @param gender
	 * @param maxHP
	 * @param curHP
	 * @param spriteNum
	 * @param isShiny
	 */
	public void setEnemyPoke(int index, String name, int level, int gender, int maxHP, int curHP, int spriteNum, boolean isShiny)
	{
		if(curHP != 0)
			m_timeLine.getBattleCanvas().setPokeballImage(index, "normal");
		else
			m_timeLine.getBattleCanvas().setPokeballImage(index, "fainted");

		m_enemyPokes[index] = new Pokemon();
		m_enemyPokes[index].setName(name);
		m_enemyPokes[index].setLevel(level);
		m_enemyPokes[index].setGender(gender);
		m_enemyPokes[index].setMaxHP(maxHP);
		m_enemyPokes[index].setCurHP(curHP);
		m_enemyPokes[index].setShiny(isShiny);
		m_enemyPokes[index].setSpriteNumber(spriteNum);

		if(index + 1 == m_enemyPokes.length)
			setEnemyData();
	}

	public void setFinish(boolean bool)
	{
		m_canFinish = bool;
	}

	/**
	 * Sets wild battle
	 * 
	 * @param m_isWild
	 */
	public void setWild(boolean m_isWild)
	{
		this.m_isWild = m_isWild;
		m_battle.setWild(m_isWild);
	}

	/**
	 * Starts a new BattleWindow and BattleCanvas
	 * 
	 * @param isWild
	 * @param pokeAmount
	 */
	public void startBattle(boolean isWild, int pokeAmount)
	{
		/* boolean was a char with the TcpProtocolHandler system. */
		m_isBattling = true;
		GameClient.getInstance().getHUD().hideHUDElements();
		if(isWild)
			setWild(true);
		else
			setWild(false);
		m_battle.setPosition(272, 40);
		m_battle.showAttack();
		m_timeLine.getBattleCanvas().setVisible(true);
		m_battle.getControlFrame().setVisible(true);
		m_enemyPokes = new Pokemon[pokeAmount];
		getPlayerData();
		m_battle.disableMoves();
		updateMoves();
		updatePokePane();
		m_timeLine.startBattle();
		m_curTrack = GameClient.getInstance().getSoundPlayer().m_trackName;
		System.out.println("Before Battle Music Name:" + m_curTrack);
		// GameClient.getInstance().getHUD().setBattleDialog(m_battle);
		GameClient.getInstance().getHUD().showBattleDialog();
		m_battle.enableMoves();
		GameClient.getInstance().changeTrack(Music.PVNPC);
	}

	/**
	 * Switch a pokemon
	 * 
	 * @param trainer
	 * @param pokeIndex
	 */
	public void switchPoke(int trainer, int pokeIndex)
	{
		if(trainer == 0)
		{
			m_curPoke = GameClient.getInstance().getOurPlayer().getPokemon()[pokeIndex];
			m_curPokeIndex = pokeIndex;
			updateMoves();
			updatePokePane();
			m_timeLine.getBattleCanvas().setPlayerHP(m_curPoke.getMaxHP());
			m_timeLine.getBattleCanvas().setPlayerMaxHP(m_curPoke.getCurHP());
			if(BattleManager.getInstance().getCurPoke().getCurHP() > BattleManager.getInstance().getCurPoke().getMaxHP() / 2)
				m_timeLine.getBattleCanvas().setPlayerHPColor(Color.green);
			else if(BattleManager.getInstance().getCurPoke().getCurHP() < BattleManager.getInstance().getCurPoke().getMaxHP() / 2
					&& BattleManager.getInstance().getCurPoke().getCurHP() > BattleManager.getInstance().getCurPoke().getMaxHP() / 3)
				m_timeLine.getBattleCanvas().setPlayerHPColor(Color.orange);
			else if(BattleManager.getInstance().getCurPoke().getCurHP() < BattleManager.getInstance().getCurPoke().getMaxHP() / 3)
				m_timeLine.getBattleCanvas().setPlayerHPColor(Color.red);
			m_timeLine.getBattleCanvas().initPlayerXPBar();
			// m_timeLine.getBattleCanvas().drawOurPoke();
			// m_timeLine.getBattleCanvas().drawOurInfo();
		}
		else
		{
			m_curEnemyPoke = m_enemyPokes[pokeIndex];
			m_curEnemyIndex = pokeIndex;
			m_timeLine.getBattleCanvas().setEnemyHP(m_curEnemyPoke.getMaxHP());
			m_timeLine.getBattleCanvas().setEnemyMaxHP(m_curEnemyPoke.getCurHP());
			if(BattleManager.getInstance().getCurEnemyPoke().getCurHP() > BattleManager.getInstance().getCurEnemyPoke().getMaxHP() / 2)
				m_timeLine.getBattleCanvas().setEnemyHPColor(Color.green);
			else if(BattleManager.getInstance().getCurEnemyPoke().getCurHP() < BattleManager.getInstance().getCurEnemyPoke().getMaxHP() / 2
					&& BattleManager.getInstance().getCurEnemyPoke().getCurHP() > BattleManager.getInstance().getCurEnemyPoke().getMaxHP() / 3)
				m_timeLine.getBattleCanvas().setEnemyHPColor(Color.orange);
			else if(BattleManager.getInstance().getCurEnemyPoke().getCurHP() < BattleManager.getInstance().getCurEnemyPoke().getMaxHP() / 3)
				m_timeLine.getBattleCanvas().setEnemyHPColor(Color.red);
		}
	}

	/**
	 * Updates moves with the current poke
	 */
	public void updateMoves()
	{
		for(int i = 0; i < 4; i++)
			if(m_curPoke != null && m_curPoke.getMoves()[i] != null && !m_curPoke.getMoves()[i].equals(""))
			{
				m_battle.getMoveButton(i).setText(m_curPoke.getMoves()[i]);
				m_battle.getPPLabel(i).setText(m_curPoke.getMoveCurPP()[i] + "/" + m_curPoke.getMoveMaxPP()[i]);
				m_battle.getMoveTypeLabel(i).setText(m_curPoke.getMoveType(i));

				m_battle.getMoveButton(i).setVisible(true);
				m_battle.getPPLabel(i).setVisible(true);
				m_battle.getMoveTypeLabel(i).setVisible(true);
			}
			else
			{
				m_battle.getMoveButton(i).setText("");
				m_battle.getPPLabel(i).setText("");
				m_battle.getMoveButton(i).setEnabled(false);
			}
	}

	/**
	 * Retrieves a pokemon's moves and updates the BattleWindow
	 * 
	 * @param int pokeIndex
	 */
	public void updateMoves(int pokeIndex)
	{
		for(int i = 0; i < 4; i++)
			if(m_ourPokes[pokeIndex].getMoves()[i] != null)
			{
				m_battle.getMoveButton(i).setText(m_ourPokes[pokeIndex].getMoves()[i]);
				m_battle.getPPLabel(i).setText(m_ourPokes[pokeIndex].getMoveCurPP()[i] + "/" + m_ourPokes[pokeIndex].getMoveMaxPP()[i]);
			}
			else
			{
				m_battle.getMoveButton(i).setText("");
				m_battle.getPPLabel(i).setText("");
			}
	}

	/**
	 * Updates the pokemon pane
	 */
	public void updatePokePane()
	{
		for(int i = 0; i < 6; i++)
			try
			{
				m_battle.getPokeButton(i).setText(m_ourPokes[i].getName());
				m_battle.getPokeInfo(i).setText("Lv: " + m_ourPokes[i].getLevel() + " HP:" + m_ourPokes[i].getCurHP() + "/" + m_ourPokes[i].getMaxHP());
				try
				{
					if(m_ourStatuses.containsKey(i) && m_battle.getStatusIcons().containsKey(m_ourStatuses.get(i)))
						m_battle.getPokeStatus(i).setImage(m_battle.getStatusIcons().get(m_ourStatuses.get(i)));
					else
						m_battle.getPokeStatus(i).setImage(null);
				}
				catch(Exception e2)
				{
				}
				if(m_ourPokes[i].getCurHP() <= 0 || m_curPokeIndex == i || m_ourPokes[i] == null)
					m_battle.getPokeButton(i).setEnabled(false);
				else
					m_battle.getPokeButton(i).setEnabled(true);
			}
			catch(Exception e)
			{
			}
	}

	/**
	 * Retrieves player data
	 */
	private void getPlayerData()
	{
		m_player = GameClient.getInstance().getOurPlayer();
		m_ourPokes = m_player.getPokemon();
		for(int i = 0; i < 6; i++)
			if(m_ourPokes[i].getCurHP() > 0)
			{
				m_curPokeIndex = i;
				m_curPoke = m_ourPokes[i];
				break;
			}
	}

	/**
	 * Sets the enemy's data
	 */
	private void setEnemyData()
	{
		m_curEnemyPoke = m_enemyPokes[0];
		m_curEnemyIndex = 0;
		try
		{
			// m_timeLine.getBattleCanvas().drawEnemyPoke();
			try
			{
				m_timeLine.getBattleCanvas().drawEnemyInfo();
			}
			catch(Exception e)
			{
				m_timeLine.getBattleCanvas().removeChild(m_timeLine.getBattleCanvas().enemyDataBG);
				m_timeLine.getBattleCanvas().removeChild(m_timeLine.getBattleCanvas().enemyNameLabel);
				m_timeLine.getBattleCanvas().removeChild(m_timeLine.getBattleCanvas().enemyLv);
				m_timeLine.getBattleCanvas().removeChild(m_timeLine.getBattleCanvas().enemyGender);
				m_timeLine.getBattleCanvas().drawEnemyInfo();
			}
			m_timeLine.getBattleCanvas().initEnemyHPBar();
			if(m_isWild)
			{
				m_timeLine.getBattleCanvas().hidePokeballs();
				m_timeLine.addSpeech("A wild " + m_curEnemyPoke.getName() + " appeared!");
			}
			else
			{
				m_timeLine.getBattleCanvas().showPokeballs();
				m_timeLine.addSpeech(m_enemy + " sent out " + m_curEnemyPoke.getName());
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void deleteInstance()
	{
		m_instance = null;
	}

}
