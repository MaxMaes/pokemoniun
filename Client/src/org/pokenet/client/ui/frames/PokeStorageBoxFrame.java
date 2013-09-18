package org.pokenet.client.ui.frames;

import org.newdawn.slick.SlickException;
import org.pokenet.client.GameClient;
import org.pokenet.client.backend.PokemonSpriteDatabase;
import org.pokenet.client.constants.ServerPacket;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.ui.components.ImageButton;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.ComboBox;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.model.SimpleChangableListModel;

/**
 * Storage Box
 * 
 * @author ZombieBear / sadhi
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
	private TeamForBox teamPanel;
	private Widget pane;

	/**
	 * Default constructor
	 * 
	 * @param boxIndex
	 * @param pokes
	 * @throws SlickException
	 */
	public PokeStorageBoxFrame(int[] pokes, Widget root)
	{
		pane = new Widget();
		pane.setTheme("content");
		pane.setSize(250, 130);
		pane.setPosition(0, 0);
		add(pane);

		setPosition(getX() - 1, getY() + 1);
		setTheme("pokemonstoragebox");
		m_pokeNums = pokes;
		m_boxIndex = 0;
		m_boxNum = m_boxIndex + 1;

		initGUI();

		setSize(370, 225);
		setPosition(400 - getWidth() / 2, 200 - getHeight() / 2);
		setTitle("Box Number " + String.valueOf(m_boxNum));
		setResizableAxis(ResizableAxis.NONE);
		setVisible(true);

		// partybox
		teamPanel = new TeamForBox(this);
		GameClient.getInstance().getHUD().add(teamPanel);
		teamPanel.setPosition(getWidth() / 2 - teamPanel.getWidth() / 2, getHeight() / 2 - teamPanel.getHeight() / 2);
		teamPanel.setVisible(false);
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

	protected ImageButton[] getButtons()
	{
		return m_buttons;
	}

	protected int[] getPokeNums()
	{
		return m_pokeNums;
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
	public void initGUI()
	{
		int buttonX = 15;
		int buttonY = 37;
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
				buttonX += 45;
				buttonCount += 1;
			}
			buttonX = 15;
			buttonY += 37;
		}

		for(int i = 0; i <= 29; i++)
			pane.add(m_buttons[i]);
		m_switchPoke = new Button();
		m_close = new Button();
		boxmodel = new SimpleChangableListModel<String>();
		m_changeBox = new ComboBox<String>(boxmodel);
		m_release = new Button();

		m_switchPoke.setText("Switch");
		m_switchPoke.setPosition(290, 74);
		m_switchPoke.setSize(70, 20);
		m_switchPoke.setEnabled(false);
		m_switchPoke.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				// setVisible(false);
				teamPanel.startPartySwitch(m_boxNum, m_buttonChosen);

				// GameClient.getInstance().getHUD().add(teamPanel);
				// teamPanel.setPosition(getWidth() / 2 - teamPanel.getWidth() / 2, getHeight() / 2 - teamPanel.getHeight() / 2);
				// teamPanel.setVisible(true);
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

		m_changeBox.setSize(70, 20);
		m_changeBox.setPosition(m_switchPoke.getX(), 37);
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
		m_release.setSize(70, 20);
		m_release.setPosition(m_switchPoke.getX(), 111);
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
		m_close.setSize(70, 20);
		m_close.setPosition(m_switchPoke.getX(), 148);
		m_close.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				setVisible(false);
				teamPanel.setVisible(false);
				ClientMessage finishBoxing = new ClientMessage(ServerPacket.FINISH_BOX_INTERACTION);
				GameClient.getInstance().getSession().send(finishBoxing);
				GameClient.getInstance().getGUIPane().getHUD().removeBoxDialog();
				GameClient.getInstance().getHUD().removeChild(teamPanel);
				GameClient.getInstance().getOurPlayer().setBoxing(false);
			}
		});
		pane.add(m_switchPoke);
		pane.add(m_close);
		pane.add(m_changeBox);
		pane.add(m_release);
		loadImages();
	}

	/**
	 * Loads pokemon images in buttons
	 */
	public void loadImages()
	{
		GameClient.getInstance().getGUI().invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				for(int i = 0; i <= 29; i++)
				{
					m_buttons[i].setImage(null);
					if(m_pokeNums[i] >= 0)
					{
						int p = 0;
						if(m_pokeNums[i] + 1 > 493)
							p = m_pokeNums[i] - 3;
						else
							p = m_pokeNums[i] + 1;

						m_buttons[i].setImage(PokemonSpriteDatabase.getIcon(p));
					}
				}
			}
		});
	}

	/**
	 * return the team panel
	 * 
	 * @return
	 */
	public TeamForBox getTeamPanel()
	{
		return teamPanel;
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
