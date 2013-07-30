package org.pokenet.client.twl.ui.frames;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.loading.LoadingList;
import org.pokenet.client.GameClient;
import org.pokenet.client.backend.FileLoader;
import org.pokenet.client.backend.entity.Pokemon;
import org.pokenet.client.constants.ServerPacket;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.twl.ui.base.ImageButton;
import org.pokenet.client.twl.ui.base.ProgressBar;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.ComboBox;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.PopupWindow;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.model.SimpleChangableListModel;

/**
 * Storage Box
 * 
 * @author ZombieBear
 */
public class PokeStorageBoxFrame extends ResizableFrame
{
	private int m_boxNum, m_boxIndex;
	private int m_buttonChosen = 0;
	private ImageButton[] m_buttons = new ImageButton[30];
	private ComboBox<String> m_changeBox;
	private SimpleChangableListModel<String> boxmodel;
	private int[] m_pokeNums = new int[30];
	private Button m_switchPoke, m_close, m_release;

	/**
	 * Default constructor
	 * 
	 * @param boxIndex
	 * @param pokes
	 * @throws SlickException
	 */
	public PokeStorageBoxFrame(int[] pokes, Widget root)
	{
		setPosition(getX() - 1, getY() + 1);

		m_pokeNums = pokes;
		m_boxIndex = 0;
		m_boxNum = m_boxIndex + 1;

		initGUI(root);

		setSize(231, 248);
		setPosition(400 - getWidth() / 2, 300 - getHeight() / 2);
		setTitle("Box Number " + String.valueOf(m_boxNum));
		setResizableAxis(ResizableAxis.NONE);
		setVisible(true);
	}

	/**
	 * Changes the box
	 * 
	 * @param boxNum
	 */
	public void changeBox(int[] pokes)
	{
		m_pokeNums = pokes;
		loadImages();
		enableButtons();
	}

	/**
	 * Disables all buttons
	 */
	public void disableButtons()
	{
		// for(int i = 0; i <= 29; i++)
		// {
		// m_buttons[i].setEnabled(false);
		m_switchPoke.setEnabled(false);
		// m_close.setEnabled(false);
		// m_changeBox.setEnabled(false);
		m_release.setEnabled(false);
		// }
	}

	/**
	 * Enables all buttons
	 */
	public void enableButtons()
	{
		for(int i = 0; i <= 29; i++)
		{
			m_buttons[i].setEnabled(true);
			m_switchPoke.setEnabled(true);
			m_close.setEnabled(true);
			m_changeBox.setEnabled(true);
			m_release.setEnabled(true);
		}
	}

	/**
	 * Initializes the interface
	 */
	public void initGUI(final Widget root)
	{
		int buttonX = 7;
		int buttonY = 5;
		int buttonCount = 0;

		for(int i = 0; i <= 29; i++)
		{
			m_buttons[i] = new ImageButton();
			final int j = i;
			m_buttons[i].addCallback(new Runnable()
			{
				@Override
				public void run()
				{
					setChoice(j);
				}
			});
			m_buttons[i].setSize(32, 32);
		}

		for(int row = 0; row < 5; row++)
		{
			for(int column = 0; column < 6; column++)
			{
				m_buttons[buttonCount].setPosition(buttonX, buttonY);
				buttonX += 37;
				buttonCount += 1;
			}
			buttonX = 7;
			buttonY += 37;
		}

		for(int i = 0; i <= 29; i++)
			add(m_buttons[i]);
		m_switchPoke = new Button();
		m_close = new Button();
		boxmodel = new SimpleChangableListModel<String>();
		m_changeBox = new ComboBox<String>(boxmodel);
		m_release = new Button();

		m_switchPoke.setText("Switch");
		m_switchPoke.setPosition(5, 192);
		m_switchPoke.setEnabled(false);
		m_switchPoke.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				setVisible(false);
				TeamForBox teamPanel = new TeamForBox(m_boxNum, m_buttonChosen, root);
				add(teamPanel);
				teamPanel.setPosition(getWidth() / 2 - teamPanel.getWidth() / 2, getHeight() / 2 - teamPanel.getHeight() / 2);
			}
		});

		boxmodel.addElement("Box 1");
		boxmodel.addElement("Box 2");
		boxmodel.addElement("Box 3");
		boxmodel.addElement("Box 4");
		boxmodel.addElement("Box 5");
		boxmodel.addElement("Box 6");
		boxmodel.addElement("Box 7");
		boxmodel.addElement("Box 8");
		boxmodel.addElement("Box 9");

		m_changeBox.setSize(55, 15);
		m_changeBox.setPosition(m_switchPoke.getX() + m_switchPoke.getWidth(), 197);
		m_changeBox.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				m_boxIndex = m_changeBox.getSelected();
				m_boxNum = m_boxIndex + 1;
				disableButtons();
				ClientMessage message = new ClientMessage(ServerPacket.REQUEST_INFO_BOX_NUMBER);
				message.addInt(m_boxIndex);
				GameClient.getInstance().getSession().send(message);
				setTitle("Box Number " + String.valueOf(m_boxNum));
			}
		});
		m_release.setText("Release");
		m_release.setPosition(m_changeBox.getX() + m_changeBox.getWidth(), 192);
		m_release.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				setVisible(false);

				Runnable yes = new Runnable()
				{
					@Override
					public void run()
					{
						GameClient.getInstance().getGUIPane().hideConfirmationDialog();
						ClientMessage message = new ClientMessage(ServerPacket.RELEASE_POKEMON);
						message.addInt(m_boxIndex);
						message.addInt(m_buttonChosen);
						GameClient.getInstance().getSession().send(message);
						ClientMessage finishBoxing = new ClientMessage(ServerPacket.FINISH_BOX_INTERACTION);
						GameClient.getInstance().getSession().send(finishBoxing);
						GameClient.getInstance().getGUIPane().getHUD().removeBoxDialog();
					}
				};

				Runnable no = new Runnable()
				{
					@Override
					public void run()
					{
						GameClient.getInstance().getGUIPane().hideConfirmationDialog();
						ClientMessage finishBoxing = new ClientMessage(ServerPacket.FINISH_BOX_INTERACTION);
						GameClient.getInstance().getSession().send(finishBoxing);
						GameClient.getInstance().getGUIPane().getHUD().removeBoxDialog();
					}
				};
				GameClient.getInstance().getGUIPane().showConfirmationDialog("Are you sure you want to release your Pokemon?", yes, no);
			}
		});
		m_release.setEnabled(false);

		m_close.setText("Bye");
		m_close.setPosition(m_release.getX() + m_release.getWidth(), 192);
		m_close.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				setVisible(false);
				ClientMessage finishBoxing = new ClientMessage(ServerPacket.FINISH_BOX_INTERACTION);
				GameClient.getInstance().getSession().send(finishBoxing);
				GameClient.getInstance().getGUIPane().getHUD().removeBoxDialog();
			}
		});
		add(m_switchPoke);
		add(m_close);
		add(m_changeBox);
		add(m_release);
		loadImages();
	}

	/**
	 * Loads pokemon images in buttons
	 */
	public void loadImages()
	{
		LoadingList.setDeferredLoading(true);
		for(int i = 0; i <= 29; i++)
		{
			m_buttons[i].setImage(null);
			if(m_pokeNums[i] >= 0)
			{
				m_buttons[i].setImage(FileLoader.loadImage(Pokemon.getIconPathByIndex(m_pokeNums[i] + 1)));
			}
		}
		LoadingList.setDeferredLoading(false);
	}

	/**
	 * Toggles the chosen button and untoggles the others
	 * 
	 * @param x
	 */
	public void setChoice(int x)
	{
		untoggleButtons();
		m_buttons[x].setEnabled(false);
		m_switchPoke.setEnabled(true);
		m_release.setEnabled(true);
		m_buttonChosen = x;
		m_boxIndex = x;
	}

	/**
	 * Untoggles all buttons
	 */
	public void untoggleButtons()
	{
		for(int i = 0; i <= 29; i++)
			m_buttons[i].setEnabled(true);
	}
}

/**
 * Team panel for storage purposes
 * 
 * @author ZombieBear
 */
class TeamForBox extends Widget
{
	private int m_teamIndex = 0, m_boxNumber = 0, m_boxIndex = 0;
	private Button m_accept = new Button();
	private Button m_cancel = new Button();
	private ProgressBar[] m_hp = new ProgressBar[6];
	private Label[] m_level = new Label[6];
	private ImageButton[] m_pokeIcon = new ImageButton[6];
	private Label[] m_pokeName = new Label[6];
	private Widget[] m_pokes = new Widget[6];
	private PopupWindow popup;
	/**
	 * Default Constractor
	 * 
	 * @param boxNum
	 * @param boxInd
	 */
	public TeamForBox(int boxNum, int boxInd, Widget root)
	{
		m_boxNumber = boxNum;
		m_boxIndex = boxInd;
		loadPokes();
		initGUI(root);
		setVisible(true);
	}

	/**
	 * Initializes the interface
	 */
	public void initGUI(Widget root)
	{
		int y = 0;
		for(int i = 0; i < 6; i++)
		{
			m_pokes[i] = new Widget();
			m_pokes[i].setSize(170, 42);
			m_pokes[i].setVisible(true);
			m_pokes[i].setPosition(0, y);

			y += 41;
			add(m_pokes[i]);
			try
			{
				m_pokes[i].add(m_pokeIcon[i]);
				m_pokeIcon[i].setPosition(2, 3);
				m_pokes[i].add(m_pokeName[i]);
				m_pokeName[i].setPosition(40, 5);
				m_pokes[i].add(m_level[i]);
				m_level[i].setPosition(m_pokeName[i].getX() + m_pokeName[i].getWidth() + 10, 5);
				m_hp[i].setSize(114, 10);
				m_hp[i].setPosition(40, m_pokeName[i].getY() + m_pokeName[i].getHeight() + 5);
				m_pokes[i].add(m_hp[i]);
			}
			catch(NullPointerException e)
			{
				e.printStackTrace();
			}
		}
		m_accept.setSize(80, 30);
		m_accept.setPosition(3, 245);
		m_accept.setText("Accept");
		m_accept.setEnabled(false);
		m_accept.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				switchPokes(m_boxNumber, m_boxIndex, m_teamIndex);
				// GameClient.getInstance().getPacketGenerator().writeTcpMessage("1A");
				ClientMessage finishBoxing = new ClientMessage(ServerPacket.FINISH_BOX_INTERACTION);
				GameClient.getInstance().getSession().send(finishBoxing);
				GameClient.getInstance().getGUIPane().getHUD().removeBoxDialog();
				setVisible(false);
			}
		});
		add(m_accept);
		m_cancel.setSize(80, 30);
		m_cancel.setPosition(86, 245);
		m_cancel.setText("Cancel");
		m_cancel.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				// GameClient.getInstance().getPacketGenerator().writeTcpMessage("1A");
				ClientMessage finishBoxing = new ClientMessage(ServerPacket.FINISH_BOX_INTERACTION);
				GameClient.getInstance().getSession().send(finishBoxing);
				GameClient.getInstance().getGUIPane().getHUD().removeBoxDialog();
				setVisible(false);
			}
		});
		add(m_cancel);
		setSize(170, 302);
		popup = new PopupWindow(root);
		popup.setTheme("PokeStorageBoxPopup");
		popup.add(this);
		popup.adjustSize();
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

		ClientMessage finishBoxing = new ClientMessage(ServerPacket.FINISH_BOX_INTERACTION);
		GameClient.getInstance().getSession().send(finishBoxing);
	}
}