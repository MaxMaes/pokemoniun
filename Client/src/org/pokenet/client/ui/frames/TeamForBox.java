package org.pokenet.client.ui.frames;

import org.newdawn.slick.loading.LoadingList;
import org.pokenet.client.GameClient;
import org.pokenet.client.backend.FileLoader;
import org.pokenet.client.backend.entity.Pokemon;
import org.pokenet.client.constants.ServerPacket;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.ui.components.ImageButton;
import org.pokenet.client.ui.components.ProgressBar;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.Widget;

/**
 * Team panel for storage purposes
 * 
 * @author ZombieBear / sadhi
 */
public class TeamForBox extends ResizableFrame
{
	private int m_teamIndex = 0, m_boxNumber = 0, m_boxIndex = 0;
	private Button m_accept = new Button();
	// private Button m_cancel = new Button();
	private Widget parent;
	private ProgressBar[] m_hp = new ProgressBar[6];
	private Label[] m_level = new Label[6];
	private ImageButton[] m_pokeIcon = new ImageButton[6];
	private Label[] m_pokeName = new Label[6];
	private Widget[] m_pokes = new Widget[6];
	// private PopupWindow popup;
	private Widget pane;

	/**
	 * Default Constractor
	 * 
	 * @param boxNum
	 * @param boxInd
	 */
	public TeamForBox(Widget root)
	{
		pane = new Widget();
		pane.setTheme("content");
		pane.setSize(170, 302);
		pane.setPosition(0, 0);
		add(pane);
		parent = root;
		setTheme("teambox");
		loadPokes();
		initGUI(parent);
		// setVisible(true);
	}

	/**
	 * start swapping
	 */
	public void startPartySwitch(int boxNum, int boxInd)
	{
		setVisible(true);
		m_boxNumber = boxNum;
		m_boxIndex = boxInd;
		setPosition(GameClient.getInstance().getHUD().getWidth() / 2 - (parent.getWidth() + getWidth()) / 2 + parent.getWidth(), 200 - getHeight() / 2);
		parent.setPosition(GameClient.getInstance().getHUD().getWidth() / 2 - (parent.getWidth() + getWidth()) / 2, 200 - getHeight() / 2);
		m_accept.setEnabled(true);
		for(int i = 0; i < 6; i++)
			m_pokeIcon[i].setEnabled(true);
	}

	/**
	 * Initializes the interface
	 */
	public void initGUI(Widget root)
	{
		int y = 23;
		for(int i = 0; i < 6; i++)
		{
			m_pokes[i] = new Widget();
			m_pokes[i].setSize(170, 42);
			m_pokes[i].setVisible(true);
			m_pokes[i].setPosition(0, y);

			y += 41;
			pane.add(m_pokes[i]);
			try
			{
				m_pokeIcon[i].setPosition(2, 3);
				// m_pokeIcon[i].setSize(20, 20);
				m_pokes[i].add(m_pokeIcon[i]);
				m_pokeName[i].setPosition(40, 5);
				m_pokeName[i].setSize(20, 10);
				m_pokes[i].add(m_pokeName[i]);
				m_level[i].setPosition(120 + 10, 5);
				m_level[i].setSize(20, 10);
				m_pokes[i].add(m_level[i]);
				m_hp[i].setSize(114, 10);
				m_hp[i].setPosition(40, 15);
				m_pokes[i].add(m_hp[i]);
			}
			catch(NullPointerException e)
			{
				e.printStackTrace();
			}
		}
		m_accept.setSize(60, 30);
		m_accept.setPosition(3, 268);
		m_accept.setText("Accept");
		m_accept.setEnabled(false);
		m_accept.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				switchPokes(m_boxNumber, m_boxIndex, m_teamIndex);
				for(int i = 0; i < 6; i++)
					m_pokeIcon[i].setEnabled(false);
				m_accept.setEnabled(false);
			}
		});
		pane.add(m_accept);

		setSize(170, 302);
	}

	/**
	 * Loads the necessary data
	 */
	public void loadPokes()
	{
		LoadingList.setDeferredLoading(true);

		for(int i = 0; i < 6; i++)
		{
			m_pokeIcon[i] = new ImageButton();
			m_pokeName[i] = new Label();
			m_level[i] = new Label();
			m_hp[i] = new ProgressBar(0, 0);
			m_hp[i].setProgressImage(GameClient.getInstance().getTheme().getImage("hpbar_high"));
			final int j = i;
			m_pokeIcon[i].addCallback(new Runnable()
			{
				@Override
				public void run()
				{
					setChoice(j);
				}
			});
			m_pokeIcon[i].setSize(32, 32);
			try
			{
				if(GameClient.getInstance().getOurPlayer().getPokemon()[i] != null)
				{
					m_level[i].setText("Lv: " + String.valueOf(GameClient.getInstance().getOurPlayer().getPokemon()[i].getLevel()));
					m_pokeName[i].setText(GameClient.getInstance().getOurPlayer().getPokemon()[i].getName());
					m_pokeIcon[i].setImage(GameClient.getInstance().getOurPlayer().getPokemon()[i].getIcon());
					m_hp[i].setMaximum(GameClient.getInstance().getOurPlayer().getPokemon()[i].getMaxHP());
					m_hp[i].setProgressImage(GameClient.getInstance().getTheme().getImage("hpbar_high"));
					m_hp[i].setValue(GameClient.getInstance().getOurPlayer().getPokemon()[i].getCurHP());
					if(GameClient.getInstance().getOurPlayer().getPokemon()[i].getCurHP() > GameClient.getInstance().getOurPlayer().getPokemon()[i].getMaxHP() / 2)
						m_hp[i].setProgressImage(GameClient.getInstance().getTheme().getImage("hpbar_high"));
					else if(GameClient.getInstance().getOurPlayer().getPokemon()[i].getCurHP() < GameClient.getInstance().getOurPlayer().getPokemon()[i].getMaxHP() / 2
							&& GameClient.getInstance().getOurPlayer().getPokemon()[i].getCurHP() > GameClient.getInstance().getOurPlayer().getPokemon()[i].getMaxHP() / 3)
						m_hp[i].setProgressImage(GameClient.getInstance().getTheme().getImage("hpbar_middle"));
					else if(GameClient.getInstance().getOurPlayer().getPokemon()[i].getCurHP() < GameClient.getInstance().getOurPlayer().getPokemon()[i].getMaxHP() / 3)
						m_hp[i].setProgressImage(GameClient.getInstance().getTheme().getImage("hpbar_low"));
					m_pokeIcon[i].setImage(GameClient.getInstance().getOurPlayer().getPokemon()[i].getIcon());
					m_pokeIcon[i].setSize(32, 32);
					m_pokeName[i].setText(GameClient.getInstance().getOurPlayer().getPokemon()[i].getName());
					m_level[i].setText("Lv: " + String.valueOf(GameClient.getInstance().getOurPlayer().getPokemon()[i].getLevel()));
				}
				else
					m_hp[i].setVisible(false);
			}
			catch(NullPointerException e)
			{
				e.printStackTrace();
			}
		}
		LoadingList.setDeferredLoading(false);
	}

	/**
	 * reloads the pokemon
	 * TODO: box pokemon doesnt update yet
	 */
	public void reloadPokemon()
	{
		// party
		m_hp[m_teamIndex].setProgressImage(GameClient.getInstance().getTheme().getImage("hpbar_high"));
		// m_pokeIcon[m_teamIndex].setSize(32, 32);
		try
		{
			if(GameClient.getInstance().getOurPlayer().getPokemon()[m_teamIndex] != null)
			{
				m_level[m_teamIndex].setText("Lv: " + String.valueOf(GameClient.getInstance().getOurPlayer().getPokemon()[m_teamIndex].getLevel()));
				m_pokeName[m_teamIndex].setText(GameClient.getInstance().getOurPlayer().getPokemon()[m_teamIndex].getName());
				m_pokeIcon[m_teamIndex].setImage(GameClient.getInstance().getOurPlayer().getPokemon()[m_teamIndex].getIcon());
				m_hp[m_teamIndex].setMaximum(GameClient.getInstance().getOurPlayer().getPokemon()[m_teamIndex].getMaxHP());
				m_hp[m_teamIndex].setProgressImage(GameClient.getInstance().getTheme().getImage("hpbar_high"));
				m_hp[m_teamIndex].setValue(GameClient.getInstance().getOurPlayer().getPokemon()[m_teamIndex].getCurHP());
				if(GameClient.getInstance().getOurPlayer().getPokemon()[m_teamIndex].getCurHP() > GameClient.getInstance().getOurPlayer().getPokemon()[m_teamIndex].getMaxHP() / 2)
					m_hp[m_teamIndex].setProgressImage(GameClient.getInstance().getTheme().getImage("hpbar_high"));
				else if(GameClient.getInstance().getOurPlayer().getPokemon()[m_teamIndex].getCurHP() < GameClient.getInstance().getOurPlayer().getPokemon()[m_teamIndex].getMaxHP() / 2
						&& GameClient.getInstance().getOurPlayer().getPokemon()[m_teamIndex].getCurHP() > GameClient.getInstance().getOurPlayer().getPokemon()[m_teamIndex].getMaxHP() / 3)
					m_hp[m_teamIndex].setProgressImage(GameClient.getInstance().getTheme().getImage("hpbar_middle"));
				else if(GameClient.getInstance().getOurPlayer().getPokemon()[m_teamIndex].getCurHP() < GameClient.getInstance().getOurPlayer().getPokemon()[m_teamIndex].getMaxHP() / 3)
					m_hp[m_teamIndex].setProgressImage(GameClient.getInstance().getTheme().getImage("hpbar_low"));
				m_pokeIcon[m_teamIndex].setImage(GameClient.getInstance().getOurPlayer().getPokemon()[m_teamIndex].getIcon());
				m_pokeIcon[m_teamIndex].setSize(32, 32);
				m_pokeName[m_teamIndex].setText(GameClient.getInstance().getOurPlayer().getPokemon()[m_teamIndex].getName());
				m_level[m_teamIndex].setText("Lv: " + String.valueOf(GameClient.getInstance().getOurPlayer().getPokemon()[m_teamIndex].getLevel()));
			}
			else
				m_hp[m_teamIndex].setVisible(false);
		}
		catch(NullPointerException e)
		{
			e.printStackTrace();
		}

		// box
		((PokeStorageBoxFrame) parent).getButtons()[m_boxIndex].setImage(null);
		if(((PokeStorageBoxFrame) parent).getPokeNums()[m_boxIndex] >= 0)
		{
			((PokeStorageBoxFrame) parent).getButtons()[m_boxIndex].setImage(FileLoader.loadImage(Pokemon.getIconPathByIndex(((PokeStorageBoxFrame) parent).getPokeNums()[m_boxIndex] + 1)));
		}
	}

	/**
	 * Sets the choice
	 * 
	 * @param x
	 */
	public void setChoice(int x)
	{
		for(int i = 0; i < 6; i++)
			m_pokeIcon[i].setEnabled(true);
		m_pokeIcon[x].setEnabled(false);
		m_accept.setEnabled(true);
		m_teamIndex = x;
	}

	/**
	 * Performs the switch
	 * 
	 * @param boxNum
	 * @param boxIndex
	 * @param teamIndex
	 */
	public void switchPokes(int boxNum, int boxIndex, int teamIndex)
	{
		ClientMessage message = new ClientMessage(ServerPacket.SWAP_POKEMON_FROM_BOX);
		message.addInt(boxNum - 1);
		message.addInt(boxIndex);
		message.addInt(teamIndex);
		GameClient.getInstance().getSession().send(message);

		// reloadPokemon();
		/* only quit boxing if the player selects it in the box */
		// ClientMessage finishBoxing = new ClientMessage(ServerPacket.FINISH_BOX_INTERACTION);
		// GameClient.getInstance().getSession().send(finishBoxing);
	}
}
