package org.pokenet.client.ui;

import java.util.HashMap;
import mdes.slick.sui.Display;
import mdes.slick.sui.Frame;
import mdes.slick.sui.Label;
import mdes.slick.sui.event.ActionEvent;
import mdes.slick.sui.event.ActionListener;
import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import org.pokenet.client.GameClient;
import org.pokenet.client.backend.BattleManager;
import org.pokenet.client.backend.entity.PlayerItem;
import org.pokenet.client.constants.ServerPacket;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.ui.base.ConfirmationDialog;
import org.pokenet.client.ui.base.HUDButtonFactory;
import org.pokenet.client.ui.base.ImageButton;
import org.pokenet.client.ui.frames.BagDialog;
import org.pokenet.client.ui.frames.BigBagDialog;
import org.pokenet.client.ui.frames.ChatDialog;
import org.pokenet.client.ui.frames.FriendListDialog;
import org.pokenet.client.ui.frames.HelpWindow;
import org.pokenet.client.ui.frames.NPCSpeechFrame;
import org.pokenet.client.ui.frames.OptionsDialog;
import org.pokenet.client.ui.frames.PartyInfoDialog;
import org.pokenet.client.ui.frames.PlayerInfoDialog;
import org.pokenet.client.ui.frames.PokeStorageBoxFrame;
import org.pokenet.client.ui.frames.PokedexDialog;
import org.pokenet.client.ui.frames.RequestDialog;
import org.pokenet.client.ui.frames.ShopDialog;
import org.pokenet.client.ui.frames.TownMap;
import org.pokenet.client.ui.frames.TradeDialog;

/**
 * The main ui on screen
 * 
 * @author shadowkanji
 * @author ZombieBear
 */
public class UserInterface extends Frame
{
	private static UserInterface m_instance;

	private static final int UI_WIDTH = 32 * 7;

	private BigBagDialog m_bag;
	private Frame m_bagForm;
	@SuppressWarnings("unused")
	private BattleManager m_battleManager;
	private ImageButton[] m_buttons;
	private ChatDialog m_chat;
	private Display m_display;
	private ConfirmationDialog m_evolveDialog;
	private FriendListDialog m_friendsList;
	private HelpWindow m_helpForm;
	private boolean m_isOption;
	private TownMap m_map;
	private Label m_moneyLabel = new Label();
	private OptionsDialog m_optionsForm;
	private PokedexDialog m_pokedex;
	private RequestDialog m_requestsForm;
	private ShopDialog m_shop;
	private NPCSpeechFrame m_speechFrame;
	private PlayerInfoDialog m_stats;
	private PokeStorageBoxFrame m_storageBox;
	private PartyInfoDialog m_teamInfo;
	private TradeDialog m_trade;

	/**
	 * Default constructor
	 */
	public UserInterface(Display display)
	{
		UserInterface.setInstance(this);
		getContentPane().setX(getContentPane().getX() - 1);
		getContentPane().setY(getContentPane().getY() + 1);
		this.setSize(800, 66);
		this.setLocation(0, -getTitleBar().getHeight());
		setBackground(new Color(0, 0, 0, 75));
		setResizable(false);
		setDraggable(false);
		m_battleManager = BattleManager.getInstance();
		m_display = display;
		m_chat = new ChatDialog();
		m_chat.setLocation(0, GameClient.getInstance().getDisplay().getHeight() - m_chat.getHeight());
		m_display.add(m_chat);
		m_requestsForm = new RequestDialog();
		m_friendsList = new FriendListDialog();
		m_friendsList.setVisible(false);
		m_display.add(m_friendsList);
		m_map = new TownMap();
		m_map.setVisible(false);
		m_display.add(m_map);
		m_pokedex = new PokedexDialog();
		m_pokedex.setVisible(false);
		m_display.add(m_pokedex);
		startButtons();
		m_moneyLabel.setText("$0");
		m_moneyLabel.pack();
		m_moneyLabel.setLocation(m_buttons[m_buttons.length - 1].getX() + 37, 6);
		m_moneyLabel.setVisible(true);
		m_moneyLabel.setFont(GameClient.getInstance().getFontLarge());
		m_moneyLabel.setForeground(new Color(255, 255, 255));
		this.add(m_moneyLabel);
		this.add(GameClient.getInstance().getTimeService());
		GameClient.getInstance().getTimeService().setX(745);
		getTitleBar().setVisible(false);
		m_display.add(this);
	}

	/**
	 * Returns the instance
	 * 
	 * @return the instance
	 */
	public static UserInterface getInstance()
	{
		return m_instance;
	}

	public void disconnect()
	{
		GameClient.getInstance().disconnectRequest();
	}

	/**
	 * Returns the bag dialog
	 * 
	 * @return the bag dialog
	 */
	public BigBagDialog getBag()
	{
		return m_bag;
	}

	/**
	 * Returns the Chat Dialog
	 * 
	 * @return the Chat Dialog
	 */
	public ChatDialog getChat()
	{
		return m_chat;
	}

	/**
	 * Returns the Friends List
	 * 
	 * @return the Friends List
	 */
	public FriendListDialog getFriendsList()
	{
		return m_friendsList;
	}

	/**
	 * Returns the map
	 * 
	 * @return the map
	 */
	public TownMap getMap()
	{
		return m_map;
	}

	/**
	 * Returns the NPC Speech Frame
	 * 
	 * @return the NPC Speech Frame
	 */
	public NPCSpeechFrame getNPCSpeech()
	{
		return m_speechFrame;
	}

	/**
	 * Returns the options form
	 * 
	 * @return the options form
	 */
	public OptionsDialog getOptionPane()
	{
		return m_optionsForm;
	}

	public PokedexDialog getPokedex()
	{
		return m_pokedex;
	}

	/**
	 * Returns the request window
	 * 
	 * @return the request window
	 */
	public RequestDialog getReqWindow()
	{
		return m_requestsForm;
	}

	/**
	 * Returns the shop dialog
	 * 
	 * @return the shop dialog
	 */
	public ShopDialog getShop()
	{
		return m_shop;
	}

	/**
	 * Returns the Storage Box
	 * 
	 * @return the Storage Box
	 */
	public PokeStorageBoxFrame getStorageBox()
	{
		return m_storageBox;
	}

	/**
	 * Returns the trade dialog
	 * 
	 * @return the trade dialog
	 */
	public TradeDialog getTrade()
	{
		return m_trade;
	}

	/**
	 * Hides the HUD
	 * 
	 * @param hide
	 */
	public void hideHUD(boolean hide)
	{
		if(hide)
		{
			hideHUDElements();
			setVisible(false);
		}
		else
			setVisible(true);
	}

	/**
	 * Initializes the Friends List
	 * 
	 * @param friends
	 */
	public void initFriendsList(String[] friends)
	{
		for(int i = 0; i < friends.length; i++)
			m_friendsList.addFriend(friends[i]);
	}

	/**
	 * Returns true if a pane is being shown
	 * 
	 * @return true if a pane is being shown
	 */
	public boolean isOccupied()
	{
		return m_optionsForm != null && m_optionsForm.isVisible() || m_bagForm != null && m_bagForm.isVisible() || m_teamInfo != null && m_teamInfo.isVisible();
	}

	/**
	 * ????
	 * 
	 * @return
	 */
	public boolean isOption()
	{
		return m_isOption;
	}

	/**
	 * Adds a message to its appropriate chat window.
	 * 
	 * @param type The type of chat message.
	 * @param msg The message itself.
	 */
	public void messageReceived(int type, String msg)
	{
		switch(type)
		{
			case 0:
				m_chat.addChatLine(0, msg);
				break;
			case 1:
				// Private Chat
				String[] details = msg.split(",");
				m_chat.addChat(details[0], true);
				m_chat.addWhisperLine(details[0], details[1]);
				break;
			case 2:
				// NPC Speeech
				String[] speech = msg.split(",");
				String result = "";
				for(int i = 0; i < speech.length; i++)
					result += GameClient.getInstance().getMapMatrix().getSpeech(Integer.parseInt(speech[i])) + "/n";
				talkToNPC(result);
				break;
			case 3:
				// Trade npc speech
				talkToNPC(msg);
				break;
			case 4:
				// Server Announcement
				m_chat.addSystemMessage(msg);
				break;
			default:
				m_chat.addChatLine(0, msg);
				break;
		}
	}

	/**
	 * Nulls m_speechFrame
	 */
	public void nullSpeechFrame()
	{
		getDisplay().remove(m_speechFrame);
		m_speechFrame = null;
	}

	/* public void messageReceived(String m) { switch(m.charAt(0)) { case 'n': // NPC Speech String[] speech = m.substring(1).split(","); String result = ""; for(int i = 0; i < speech.length; i++) { result += GameClient.getInstance().getMapMatrix().getSpeech(Integer.parseInt(speech[i])) + "/n"; } talkToNPC(result); break; case 't': // Trade npc speech talkToNPC(m.substring(1)); break; // The following is for in case the chat server is down... case 'l': // Local Chat if(!GameClient.getInstance().chatServerIsActive()) if(m.substring(1).toLowerCase().contains(GameClient.getInstance().getOurPlayer().getUsername().toLowerCase()) && !m.substring(1).contains("<" + GameClient.getInstance().getOurPlayer().getUsername() + ">")) m_chat.addChatLine(0, "!" + m.substring(1)); else m_chat.addChatLine(0, m.substring(1)); break; case 'p': // Private Chat String[] details = m.substring(1).split(","); if(!GameClient.getInstance().chatServerIsActive()) m_chat.addChat(details[0], true); m_chat.addWhisperLine(details[0], details[1]); break; case 's': // Server Announcement if(!GameClient.getInstance().chatServerIsActive()) m_chat.addSystemMessage(m.substring(1)); break; } } */

	/**
	 * Pops up the trade dialog
	 * 
	 * @param pokes
	 * @param player
	 */
	public void openTrade(String player)
	{
		new TradeDialog(player);
	}

	/**
	 * Refreshes PokemonParty HUD
	 */
	public void refreshParty()
	{
		if(m_teamInfo != null)
			m_teamInfo.update(GameClient.getInstance().getOurPlayer().getPokemon());
	}

	/**
	 * Sets all components visible/invisible
	 * 
	 * @param b
	 */
	public void setAllVisible(boolean b)
	{
		setVisible(b);
		m_chat.setVisible(b);
	}

	/**
	 * Starts the HUD buttons
	 */
	public void startButtons()
	{
		m_buttons = new ImageButton[10];

		m_buttons[0] = HUDButtonFactory.getButton("stats");
		m_buttons[0].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				toggleStats();
			}
		});
		m_buttons[0].setToolTipText("Stats");

		m_buttons[1] = HUDButtonFactory.getButton("pokedex");
		m_buttons[1].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				togglePokedex();
			}
		});
		m_buttons[1].setToolTipText("Pokedex");

		m_buttons[2] = HUDButtonFactory.getButton("pokemon");
		m_buttons[2].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				togglePokemon();
			}
		});
		m_buttons[2].setToolTipText("Pokemon");

		m_buttons[3] = HUDButtonFactory.getButton("bag");
		m_buttons[3].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				toggleBag();
			}
		});
		m_buttons[3].setToolTipText("Bag");

		m_buttons[4] = HUDButtonFactory.getButton("map");
		m_buttons[4].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				toggleMap();
			}
		});
		m_buttons[4].setToolTipText("Map");

		m_buttons[5] = HUDButtonFactory.getButton("friends");
		m_buttons[5].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				toggleFriends();
			}
		});
		m_buttons[5].setToolTipText("Friends");

		m_buttons[6] = HUDButtonFactory.getButton("requests");
		m_buttons[6].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				toggleRequests();
			}
		});
		m_buttons[6].setToolTipText("Requests");

		m_buttons[7] = HUDButtonFactory.getButton("options");
		m_buttons[7].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				toggleOptions();
			}
		});
		m_buttons[7].setToolTipText("Options");

		m_buttons[8] = HUDButtonFactory.getButton("help");
		m_buttons[8].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				toggleHelp();
			}
		});
		m_buttons[8].setToolTipText("Help");

		m_buttons[9] = HUDButtonFactory.getButton("disconnect");
		m_buttons[9].addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				disconnect();
			}
		});
		m_buttons[9].setToolTipText("Disconnect");

		for(int i = 0; i < m_buttons.length; i++)
		{
			m_buttons[i].pack();
			getContentPane().add(m_buttons[i]);
			m_buttons[i].setLocation(5 + 32 * i + 5 * i, 7);
		}
	}

	/**
	 * Opens a store
	 * 
	 * @param pokeNums
	 * @param trainer
	 */
	public void startShop(HashMap<Integer, Integer> stock)
	{
		if(m_shop != null)
			/* Attempt to remove previous GUI */
			m_display.remove(m_shop);
		if(!m_display.containsChild(m_shop))
		{
			m_shop = new ShopDialog(stock);
			m_display.add(m_shop);
		}
		else
		{
			/* If previous GUI couldn't be removed, just update it */
			m_shop.updateStock(stock);
			m_shop.setVisible(true);
		}
	}

	/**
	 * Starts a trade
	 * 
	 * @param pokeNums
	 * @param trainer
	 */
	public void startTrade(String trainer)
	{
		m_trade = new TradeDialog(trainer);
		m_display.add(m_trade);
	}

	/**
	 * Closes the store
	 */
	public void stopShop()
	{
		m_display.remove(m_shop);
		m_shop = null;
	}

	/**
	 * Stops a trade
	 */
	public void stopTrade()
	{
		m_display.remove(m_trade);
		m_trade = null;
	}

	/**
	 * Stops the Storage Box
	 */
	public void stopUsingBox()
	{
		getDisplay().remove(m_storageBox);
		m_storageBox = null;
	}

	/**
	 * Starts to talk to an NPC
	 * 
	 * @param speech
	 * @throws SlickException
	 */
	public void talkToNPC(String speech)
	{
		m_speechFrame = new NPCSpeechFrame(speech);
		getDisplay().add(m_speechFrame);
		// if (speech.startsWith("*"))
		// usePokeStorageBox("");
	}

	/**
	 * Toggles the Bag Pane
	 */
	public void toggleBag()
	{
		if(m_bagForm != null)
		{
			getDisplay().remove(m_bagForm);
			hideHUDElements();
		}
		else
		{
			hideHUDElements();
			m_bagForm = new Frame();
			m_bagForm.getContentPane().setX(m_bagForm.getContentPane().getX() - 1);
			m_bagForm.getContentPane().setY(m_bagForm.getContentPane().getY() + 1);
			m_bagForm.setBackground(new Color(0, 0, 0, 70));
			m_bagForm.setResizable(false);
			m_bagForm.setDraggable(false);
			m_bagForm.setTitle("     Bag");
			BagDialog pane = new BagDialog(GameClient.getInstance().getOurPlayer().getItems())
			{
				@Override
				public void cancelled()
				{
					getDisplay().remove(m_bagForm);
					m_bagForm = null;
				}

				@Override
				public void itemClicked(PlayerItem item)
				{
					/* TODO: Implement 'Hotbar' functionality or remove completely */
					/* ClientMessage message = new ClientMessage(); message.Init(-); GameClient.m_Session.Send(message); */
				}

				@Override
				public void loadBag()
				{
					getDisplay().remove(m_bagForm);
					m_bagForm = null;
					m_bag = new BigBagDialog();
					getDisplay().add(m_bag);
				}
			};
			pane.setSize(80, 246);
			pane.pack();

			m_bagForm.getTitleBar().getCloseButton().setVisible(false);
			m_bagForm.getContentPane().add(pane);
			m_bagForm.setSize(pane.getWidth(), pane.getHeight() + m_bagForm.getTitleBar().getHeight());
			getDisplay().add(m_bagForm);
			m_bagForm.setLocation(m_buttons[2].getX(), 67 - getTitleBar().getHeight());
			m_bagForm.setDraggable(false);
		}
	}

	/**
	 * Toggles the Chat Pane
	 */
	public void toggleChat()
	{
		if(m_chat.isVisible())
			m_chat.setVisible(false);
		else
		{
			m_chat.setLocation(0, GameClient.getInstance().getDisplay().getHeight() - m_chat.getHeight());
			m_chat.setVisible(true);
		}
	}

	/**
	 * Toggles the Friends Pane
	 */
	public void toggleFriends()
	{
		if(m_friendsList.isVisible())
		{
			m_friendsList.setVisible(false);
			hideHUDElements();
		}
		else
		{
			hideHUDElements();
			m_friendsList.setLocation(m_buttons[4].getX(), 67 - getTitleBar().getHeight());
			m_friendsList.setVisible(true);
		}
	}

	/**
	 * Toggles the Help Pane
	 */
	public void toggleHelp()
	{
		if(m_helpForm != null)
		{
			getDisplay().remove(m_helpForm);
			hideHUDElements();
		}
		else
		{
			hideHUDElements();
			m_helpForm = new HelpWindow();
			m_helpForm.setWidth(UI_WIDTH);
			m_helpForm.setHeight(300);
			m_helpForm.setLocation(m_buttons[7].getX(), 67 - getTitleBar().getHeight());
			getDisplay().add(m_helpForm);
		}
	}

	/**
	 * Toggles the Map Pane
	 */
	public void toggleMap()
	{
		if(m_map.isVisible())
		{
			m_map.setVisible(false);
			hideHUDElements();
		}
		else
		{
			hideHUDElements();
			m_chat.releaseFocus();
			m_map.setLocation(m_buttons[1].getX(), 67 - getTitleBar().getHeight());
			m_map.setVisible(true);
		}
	}

	/**
	 * Toggles the Options Pane
	 */
	public void toggleOptions()
	{
		if(m_optionsForm != null)
		{
			getDisplay().remove(m_optionsForm);
			hideHUDElements();
		}
		else
		{
			hideHUDElements();
			m_isOption = true;
			m_optionsForm = new OptionsDialog();
			m_optionsForm.setWidth(UI_WIDTH);
			m_optionsForm.setLocation(m_buttons[6].getX(), 67 - getTitleBar().getHeight());
			m_optionsForm.setDraggable(false);
			getDisplay().add(m_optionsForm);
		}
	}

	public void togglePokedex()
	{
		/* TODO: Is this still Todo, if yes. What? */
		if(m_pokedex.isVisible())
		{
			m_pokedex.setVisible(false);
			hideHUDElements();
		}
		else
		{
			hideHUDElements();
			m_pokedex.setLocation(150, 50);
			m_pokedex.setVisible(true);
		}
	}

	/**
	 * Toggles the Pokemon Pane
	 */
	public void togglePokemon()
	{
		if(m_teamInfo != null)
		{
			getDisplay().remove(m_teamInfo);
			hideHUDElements();
		}
		else
		{
			hideHUDElements();
			m_teamInfo = new PartyInfoDialog(GameClient.getInstance().getOurPlayer().getPokemon());
			m_teamInfo.setWidth(175);
			m_teamInfo.setLocation(m_buttons[1].getX(), 67 - getTitleBar().getHeight() * 2);
			m_teamInfo.getTitleBar().setVisible(false);
			m_teamInfo.setDraggable(false);
			getDisplay().add(m_teamInfo);
		}
	}

	/**
	 * Toggles the Request Pane
	 */
	public void toggleRequests()
	{
		if(getDisplay().containsChild(m_requestsForm))
		{
			getDisplay().remove(m_requestsForm);
			hideHUDElements();
		}
		else
		{
			hideHUDElements();
			m_requestsForm.setWidth(UI_WIDTH);
			m_requestsForm.setLocation(m_buttons[4].getX(), 67 - getTitleBar().getHeight());
			m_requestsForm.setDraggable(false);
			getDisplay().add(m_requestsForm);
		}
	}

	/**
	 * Toggles the Player Stats pane
	 */
	public void toggleStats()
	{
		if(m_stats != null)
		{
			getDisplay().remove(m_stats);
			hideHUDElements();
		}
		else
		{
			hideHUDElements();
			m_stats = new PlayerInfoDialog();
			m_stats.setBackground(new Color(0, 0, 0, 70));
			m_stats.setResizable(false);
			m_stats.setDraggable(false);
			m_stats.getTitleBar().setVisible(false);
			m_stats.setLocation(m_buttons[0].getX(), 67 - getTitleBar().getHeight() * 2);
			getDisplay().add(m_stats);
		}
	}

	/**
	 * A pokemon wants to evolve
	 * 
	 * @param pokeIndex
	 */
	public void tryEvolve(int pokeIndex)
	{
		final int index = pokeIndex;
		ActionListener yes = new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// GameClient.getInstance().getPacketGenerator().writeTcpMessage("0C" + index);
				ClientMessage message = new ClientMessage(ServerPacket.EVOLVE);
				message.addInt(index);
				GameClient.getInstance().getSession().send(message);
				GameClient.getInstance().getDisplay().remove(m_evolveDialog);
			}
		};
		ActionListener no = new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// GameClient.getInstance().getPacketGenerator().writeTcpMessage("0B" + index);
				ClientMessage message = new ClientMessage(ServerPacket.DONT_EVOLVE);
				message.addInt(index);
				GameClient.getInstance().getSession().send(message);
				GameClient.getInstance().getDisplay().remove(m_evolveDialog);
			}
		};
		m_evolveDialog = new ConfirmationDialog(GameClient.getInstance().getOurPlayer().getPokemon()[pokeIndex].getName() + " is trying to evolve.", yes, no);
	}

	/**
	 * Updates the data
	 * 
	 * @param p
	 */
	public void update(boolean money)
	{
		if(money)
			try
			{
				m_moneyLabel.setText("$" + String.valueOf(GameClient.getInstance().getOurPlayer().getMoney()));
				m_moneyLabel.pack();
			}
			catch(Exception e)
			{
				System.err.println("Failed to update money");
			}
		else
			try
			{
				if(m_teamInfo != null)
					m_teamInfo.update(GameClient.getInstance().getOurPlayer().getPokemon());
			}
			catch(Exception e)
			{
				System.err.println("Failed to update pokemon data");
			}
	}

	/**
	 * Starts a Storage Box
	 */
	public void useStorageBox(int[] data)
	{
		m_storageBox = new PokeStorageBoxFrame(data);
		getDisplay().add(m_storageBox);
	}

	/**
	 * Hides all HUD elements
	 */
	private void hideHUDElements()
	{
		if(m_display.containsChild(m_requestsForm))
			m_display.remove(m_requestsForm);
		if(m_bagForm != null)
			m_bagForm.setVisible(false);
		m_bagForm = null;
		if(m_teamInfo != null)
			m_teamInfo.setVisible(false);
		m_teamInfo = null;
		if(m_optionsForm != null)
			m_optionsForm.setVisible(false);
		m_optionsForm = null;
		if(m_helpForm != null)
			m_helpForm.setVisible(false);
		m_helpForm = null;
		if(m_map.isVisible())
			m_map.setVisible(false);
		if(m_friendsList.isVisible())
			m_friendsList.setVisible(false);
		if(m_stats != null && m_stats.isVisible())
			m_stats.setVisible(false);
		m_stats = null;
		if(m_pokedex != null && m_pokedex.isVisible())
			m_pokedex.setVisible(false);
	}

	private static void setInstance(UserInterface ui)
	{
		m_instance = ui;
	}
}