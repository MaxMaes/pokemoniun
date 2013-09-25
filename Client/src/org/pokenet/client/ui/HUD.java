package org.pokenet.client.ui;

import java.util.HashMap;
import org.newdawn.slick.SlickException;
import org.pokenet.client.GameClient;
import org.pokenet.client.backend.entity.OurPlayer;
import org.pokenet.client.backend.entity.OurPokemon;
import org.pokenet.client.constants.ServerPacket;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.ui.frames.BattleBag;
import org.pokenet.client.ui.frames.BattleFrontierDialog;
import org.pokenet.client.ui.frames.BigBagDialog;
import org.pokenet.client.ui.frames.BoatChooserDialog;
import org.pokenet.client.ui.frames.ChatDialog;
import org.pokenet.client.ui.frames.FriendsListDialog;
import org.pokenet.client.ui.frames.HelpWindow;
import org.pokenet.client.ui.frames.KurtDialog;
import org.pokenet.client.ui.frames.MoveLearningDialog;
import org.pokenet.client.ui.frames.MoveRelearnDialog;
import org.pokenet.client.ui.frames.OptionsDialog;
import org.pokenet.client.ui.frames.PartyInfoDialog;
import org.pokenet.client.ui.frames.PlayerInfoDialog;
import org.pokenet.client.ui.frames.PlayerPopupDialog;
import org.pokenet.client.ui.frames.PokeStorageBoxFrame;
import org.pokenet.client.ui.frames.PokedexDialog;
import org.pokenet.client.ui.frames.PokemonInfoDialog;
import org.pokenet.client.ui.frames.RequestDialog;
import org.pokenet.client.ui.frames.SpriteChooserDialog;
import org.pokenet.client.ui.frames.TopBar;
import org.pokenet.client.ui.frames.TownMap;
import org.pokenet.client.ui.frames.TradeDialog;
import org.pokenet.client.ui.frames.TrainChooserDialog;
import org.pokenet.client.ui.frames.battle.BattleDialog;
import org.pokenet.client.ui.frames.shop.ShopDialog;
import org.pokenet.client.ui.frames.speechframes.BattleSpeechFrame;
import org.pokenet.client.ui.frames.speechframes.NPCSpeechFrame;
import de.matthiasmann.twl.DesktopArea;

/**
 * Class which controls and contains everything which is rendered over the map and player.
 * 
 * @author Myth1c
 */
public class HUD extends DesktopArea
{
	private TopBar topbar;
	private PokedexDialog pokedex;
	private HelpWindow help;
	private OptionsDialog options;
	private PlayerInfoDialog playerinfo;
	private ChatDialog chat;
	private BoatChooserDialog boatChooser;
	private BattleFrontierDialog battlefrontierDialog;
	private PlayerPopupDialog playerPopupDialog;
	private TrainChooserDialog trainChooserDialog;
	private FriendsListDialog friends;
	private PartyInfoDialog partyInfo;
	private TownMap map;
	private RequestDialog requests;
	private ShopDialog shop;
	private MoveLearningDialog moveLearning;
	private NPCSpeechFrame npcSpeech;
	private BigBagDialog bigBag;
	private BattleBag battleBag;
	private BattleDialog battleDialog;
	private BattleSpeechFrame battleSpeechFrame;
	private SpriteChooserDialog spriteChooser;
	private TradeDialog tradeDialog;
	private PokeStorageBoxFrame boxDialog;
	private MoveRelearnDialog relearnDialog;
	private KurtDialog kurtDialog;
	private PokemonInfoDialog pokemonInfoDialogs[] = new PokemonInfoDialog[6];

	public HUD()
	{
		topbar = new TopBar();
		add(topbar);

		friends = new FriendsListDialog();
		add(friends);
		pokedex = new PokedexDialog();
		add(pokedex);
		help = new HelpWindow();
		add(help);
		options = new OptionsDialog();
		add(options);
		chat = new ChatDialog();
		add(chat);
		requests = new RequestDialog();
		add(requests);
		map = new TownMap(this);
		add(map);

		pokedex.setVisible(false);
		options.setVisible(false);
		help.setVisible(false);
		chat.setVisible(false);
		requests.setVisible(false);
		map.setVisible(false);
		friends.setVisible(false);
	}

	@Override
	public void layout()
	{
		setSize(800, 600);
		setPosition(0, 0);
	}

	/**
	 * Shows the boat chooser dialog
	 */
	public void showBoatDialog(String currentLocation)
	{
		hideHUDElements();
		if(boatChooser != null)
		{
			removeChild(boatChooser);
		}
		boatChooser = new BoatChooserDialog(currentLocation);
		add(boatChooser);
		boatChooser.setVisible(true);
	}

	/**
	 * Shows the train chooser dialog
	 */
	public void showTrainDialog(String currentLocation)
	{
		hideHUDElements();
		if(trainChooserDialog != null)
		{
			removeChild(trainChooserDialog);
		}
		trainChooserDialog = new TrainChooserDialog(currentLocation, GameClient.getInstance().getOurPlayer());
		add(trainChooserDialog);
		trainChooserDialog.setVisible(true);
	}

	/**
	 * Toggles the Player Stats pane
	 */
	public void togglePlayerStats()
	{
		hideHUDElements();
		if(playerinfo != null)
		{
			removeChild(playerinfo);
			playerinfo = null;
		}
		else
		{
			hideHUDElements();
			playerinfo = new PlayerInfoDialog();
			playerinfo.setPosition(topbar.getBarButton(0).getX(), 47);
			add(playerinfo);
		}
	}

	public void togglePokedex()
	{
		if(pokedex.isVisible())
		{
			pokedex.setVisible(false);
		}
		else
		{
			hideHUDElements();
			pokedex.setPosition(150, 50);
			pokedex.setVisible(true);
		}
	}

	public void toggleChat()
	{
		if(chat.isVisible())
		{
			chat.setVisible(false);
		}
		else
		{
			hideHUDElements();
			chat.setVisible(true);
			chat.invalidateLayout();
		}
	}

	public void togglePokemon()
	{
		if(partyInfo != null)
		{
			removeChild(partyInfo);
			partyInfo = null;
			hideHUDElements();
		}
		else
		{
			hideHUDElements();
			partyInfo = new PartyInfoDialog(GameClient.getInstance().getOurPlayer().getPokemon());
			partyInfo.setPosition(topbar.getBarButton(1).getInnerX(), topbar.getBarButton(1).getInnerY() + topbar.getBarButton(1).getHeight());
			add(partyInfo);
		}
	}

	public void toggleBag()
	{
		if(bigBag != null)
		{
			removeChild(bigBag);
			bigBag = null;
			hideHUDElements();
		}
		else
		{
			hideHUDElements();
			bigBag = new BigBagDialog(this);
			add(bigBag);
		}
	}

	public void toggleMap()
	{
		if(map.isVisible())
		{
			map.setVisible(false);
		}
		else
		{
			hideHUDElements();
			map.setPosition(50, 50);
			map.setVisible(true);
		}
	}

	public void toggleFriends()
	{
		if(friends.isVisible())
		{
			friends.setVisible(false);
		}
		else
		{
			hideHUDElements();
			friends.setPosition(150, 50);
			friends.setVisible(true);
		}
	}

	public void toggleRequests()
	{
		if(requests.isVisible())
		{
			requests.setVisible(false);
		}
		else
		{
			hideHUDElements();
			requests.setPosition(150, 50);
			requests.setVisible(true);
		}
	}

	public void toggleOptions()
	{
		if(options.isVisible())
		{
			options.setVisible(false);
		}
		else
		{
			hideHUDElements();
			options.setPosition(150, 50);
			options.setVisible(true);
		}
	}

	public void toggleHelp()
	{
		if(help.isVisible())
		{
			help.setVisible(false);
		}
		else
		{
			hideHUDElements();
			help.setPosition(150, 50);
			help.setVisible(true);
		}
	}

	public void disconnect()
	{
		GameClient.getInstance().disconnectRequest();
	}

	public void hideHUDElements()
	{
		pokedex.setVisible(false);
		help.setVisible(false);
		options.setVisible(false);
		pokedex.setVisible(false);
		if(boatChooser != null)
			boatChooser.setVisible(false);
		if(trainChooserDialog != null)
			trainChooserDialog.setVisible(false);
		if(playerinfo != null)
			playerinfo.setVisible(false);
		if(requests != null)
			requests.setVisible(false);
		if(friends != null)
			friends.setVisible(false);

		removeBattleFrontierDialog();
	}

	public void removeBattleFrontierDialog()
	{
		if(battlefrontierDialog != null)
		{
			battlefrontierDialog.setVisible(false);
			removeChild(battlefrontierDialog);
			battlefrontierDialog = null;
		}
	}

	public void showBattleFrontierDialog(String battle, OurPlayer p)
	{
		hideHUDElements();
		battlefrontierDialog = new BattleFrontierDialog(battle, p);
		battlefrontierDialog.setVisible(true);
		add(battlefrontierDialog);
	}

	/**
	 * functions for player popup
	 * 
	 * @param y
	 * @param x
	 */
	public void showPlayerPopupDialog(String player, int x, int y)
	{
		if(playerPopupDialog != null)
			removeChild(playerPopupDialog);
		playerPopupDialog = new PlayerPopupDialog(this);

		playerPopupDialog.setPlayerName(player);
		playerPopupDialog.showPopupAt(x, y);
	}

	public void hidePlayerPopupDialog()
	{
		playerPopupDialog.destroy();
	}

	public void showShop(HashMap<Integer, Integer> stock)
	{
		shop = new ShopDialog(stock);
		add(shop);
	}

	public void removeShop()
	{
		removeChild(shop);
		shop = null;
		removeNPCSpeechFrame();
	}

	public boolean hasShop()
	{
		if(shop != null)
			return true;
		return false;
	}

	public void showMoveLearning()
	{
		moveLearning = new MoveLearningDialog(this);
		add(moveLearning);
	}

	public void removeMoveLearning()
	{
		removeChild(moveLearning);
		moveLearning = null;
	}

	public boolean hasMoveLearning()
	{
		if(moveLearning != null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Creates a new trainchooser dialog with given parameters.
	 * 
	 * @param travel
	 * @param p
	 */
	public void showTrainchooser(String travel, OurPlayer p)
	{
		trainChooserDialog = new TrainChooserDialog(travel, p);
		add(trainChooserDialog);
	}

	/**
	 * Removes the train chooser and nulls it's variable.
	 */
	public void removeTrainchooser()
	{
		removeChild(trainChooserDialog);
		trainChooserDialog = null;
	}

	/**
	 * Checks if the HUD has a trainchooser present.
	 * 
	 * @return
	 */
	public boolean hasTrainchooser()
	{
		if(trainChooserDialog != null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Creates a new BattleDialog dialog with given parameters.
	 */
	public void showBattleDialog()
	{
		if(battleDialog != null)
		{
			GameClient.getInstance().getGUI().invokeLater(new Runnable()
			{
				@Override
				public void run()
				{
					removeChild(battleDialog);
				}
			});
		}

		battleDialog = new BattleDialog();
		battleDialog.setPosition(272, 100);

		GameClient.getInstance().getGUI().invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				add(battleDialog);
			}
		});
	}

	/**
	 * Removes the BattleDialog and nulls it's variable.
	 */
	public void removeBattleDialog()
	{
		removeChild(battleDialog);
		battleDialog = null;
	}

	/**
	 * Checks if the HUD has a BattleDialog present.
	 * 
	 * @return
	 */
	public boolean hasBattleDialog()
	{
		if(battleDialog != null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Creates a new Battlebag dialog with given parameters.
	 * 
	 * @param travel
	 * @param p
	 */
	public void showBattlebag()
	{
		battleBag = new BattleBag(this);
		add(battleBag);
	}

	/**
	 * Removes the Battlebag and nulls it's variable.
	 */
	public void removeBattlebag()
	{
		removeChild(battleBag);
		battleBag = null;
	}

	/**
	 * Checks if the HUD has a Battlebag present.
	 * 
	 * @return
	 */
	public boolean hasBattlebag()
	{
		if(battleBag != null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public void showSpritechooser()
	{
		spriteChooser = new SpriteChooserDialog();
		add(spriteChooser);
	}

	/**
	 * Removes the BattleDialog and nulls it's variable.
	 */
	public void removeSpritechooser()
	{
		removeChild(spriteChooser);
		spriteChooser = null;
	}

	/**
	 * Checks if the HUD has a BattleDialog present.
	 * 
	 * @return
	 */
	public boolean hasSpritechooser()
	{
		if(spriteChooser != null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public void showTradeDialog(final String trainername)
	{
		GameClient.getInstance().getGUI().invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				tradeDialog = new TradeDialog(trainername);
				add(tradeDialog);
			}
		});
	}

	/**
	 * Removes the BattleDialog and nulls it's variable.
	 */
	public void removeTradeDialog()
	{
		removeChild(tradeDialog);
		tradeDialog = null;
	}

	/**
	 * Checks if the HUD has a BattleDialog present.
	 * 
	 * @return
	 */
	public boolean hasTradeDialog()
	{
		if(tradeDialog != null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public void showRequestDialog()
	{
		requests = new RequestDialog();
		add(requests);
	}

	/**
	 * Removes the RequestDialog and nulls it's variable.
	 */
	public void removeRequestDialog()
	{
		removeChild(requests);
		requests = null;
	}

	/**
	 * Checks if the HUD has a RequestDialog present.
	 * 
	 * @return
	 */
	public boolean hasRequestDialog()
	{
		if(requests != null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public void showBoxDialog(int[] pokes)
	{
		boxDialog = new PokeStorageBoxFrame(pokes, this);
		add(boxDialog);
	}

	/**
	 * Removes the BattleDialog and nulls it's variable.
	 */
	public void removeBoxDialog()
	{
		removeChild(boxDialog);
		boxDialog = null;
	}

	public void showBattleSpeechFrame()
	{
		if(battleSpeechFrame != null)
		{
			removeChild(this.battleSpeechFrame);
		}

		battleSpeechFrame = new BattleSpeechFrame();
		add(battleSpeechFrame);
	}

	public void removeBattleSpeechFrame()
	{
		removeChild(battleSpeechFrame);
		battleSpeechFrame = null;
	}

	public boolean hasBattleSpeechFrame()
	{
		return (battleSpeechFrame != null) ? true : false;
	}

	/**
	 * Checks if the HUD has a BattleDialog present.
	 * 
	 * @return
	 */
	public boolean hasBoxDialog()
	{
		if(boxDialog != null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Creates a new friendslist dialog and adds it to the UI.
	 * 
	 * @param split
	 */
	public void showFriendsList(String[] split)
	{
		friends = new FriendsListDialog();
		add(friends);
	}

	public void removeNPCSpeechFrame()
	{
		if(npcSpeech != null)
		{
			removeChild(npcSpeech);
		}
		npcSpeech = null;
	}

	public void hideChat()
	{
		chat.setVisible(false);
	}

	public void showChat()
	{
		chat.setVisible(true);
	}

	public TownMap getMap()
	{
		return map;
	}

	public ShopDialog getShop()
	{
		return shop;
	}

	public BigBagDialog getBag()
	{
		return bigBag;
	}

	public PokedexDialog getPokedex()
	{
		return pokedex;
	}

	public HelpWindow getHelp()
	{
		return help;
	}

	public OptionsDialog getOptions()
	{
		return options;
	}

	public ChatDialog getChat()
	{
		return chat;
	}

	public PlayerPopupDialog getPlayerPopupDialog()
	{
		return playerPopupDialog;
	}

	public FriendsListDialog getFriendsList()
	{
		return friends;
	}

	public BattleDialog getBattleDialog()
	{
		return battleDialog;
	}

	public RequestDialog getRequestDialog()
	{
		return requests;
	}

	public NPCSpeechFrame getNPCSpeech()
	{
		return npcSpeech;
	}

	public BattleSpeechFrame getBattleSpeechFrame()
	{
		return battleSpeechFrame;
	}

	public PokeStorageBoxFrame getBoxDialog()
	{
		return boxDialog;
	}

	public TradeDialog getTradeDialog()
	{
		return tradeDialog;
	}

	public TopBar getTopBar()
	{
		return topbar;
	}

	/**
	 * Starts to talk to an NPC
	 * 
	 * @param speech
	 * @throws SlickException
	 */
	public void talkToNPC(String speech)
	{
		npcSpeech = new NPCSpeechFrame(speech);
		add(npcSpeech);
	}

	public void refreshParty()
	{
		if(partyInfo != null)
			partyInfo.update(GameClient.getInstance().getOurPlayer().getPokemon());
	}

	/**
	 * Updates the data
	 * 
	 * @param p
	 */
	public void update(boolean money)
	{
		if(money)
		{
			try
			{
				topbar.getMoneyLabel().setText("$" + String.valueOf(GameClient.getInstance().getOurPlayer().getMoney()));
			}
			catch(Exception e)
			{
				System.err.println("Failed to update money");
			}
		}
		else
		{
			try
			{
				if(partyInfo != null)
					partyInfo.update(GameClient.getInstance().getOurPlayer().getPokemon());
			}
			catch(Exception e)
			{
				System.err.println("Failed to update pokemon data");
			}
		}
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
				// Global chat
				getChat().addLine(msg);
				break;
			case 1:
				// Private Chat
				String[] details = msg.split(",");
				getChat().addChatChannel(details[0], true);
				getChat().addWhisperLine(details[0], details[1]);
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
				getChat().addSystemMessage(msg);
				break;
			default:
				getChat().addLine(msg);
				break;
		}
	}

	/**
	 * Initializes the Friends List DATAWISE
	 * 
	 * @param friends
	 */
	public void initFriendsList(String[] list)
	{
		for(int i = 0; i < list.length; i++)
		{
			friends.addFriend(list[i]);
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
		Runnable yes = new Runnable()
		{
			@Override
			public void run()
			{
				ClientMessage message = new ClientMessage(ServerPacket.EVOLVE);
				message.addInt(index);
				GameClient.getInstance().getSession().send(message);
				GameClient.getInstance().getGUIPane().hideConfirmationDialog();
			}
		};
		Runnable no = new Runnable()
		{
			@Override
			public void run()
			{
				ClientMessage message = new ClientMessage(ServerPacket.DONT_EVOLVE);
				message.addInt(index);
				GameClient.getInstance().getSession().send(message);
				GameClient.getInstance().getGUIPane().hideConfirmationDialog();
			}
		};
		GameClient.getInstance().getGUIPane().showConfirmationDialog(GameClient.getInstance().getOurPlayer().getPokemon()[pokeIndex].getName() + " is trying to evolve.", yes, no);
	}

	public boolean hasNPCSpeechFrame()
	{
		if(npcSpeech != null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public MoveRelearnDialog getRelearnDialog()
	{
		return relearnDialog;
	}

	public void showRelearnDialog(String price)
	{
		if(relearnDialog == null)
			relearnDialog = new MoveRelearnDialog(price);
		add(relearnDialog);
	}

	public void removeRelearnDialog()
	{
		removeChild(relearnDialog);
		relearnDialog = null;
	}

	public void showKurtDialog(String pokeball, String item, int max, int price)
	{
		if(kurtDialog == null)
			kurtDialog = new KurtDialog(pokeball, item, max, price);
		add(kurtDialog);
	}

	public void removeKurtDialog()
	{
		removeChild(kurtDialog);
		kurtDialog = null;
	}

	/**
	 * @return the pokemonInfoDialogs
	 */
	public PokemonInfoDialog[] getPokemonInfoDialogs()
	{
		return pokemonInfoDialogs;
	}

	/**
	 * @param i, the index of the party pokemon
	 */
	public void showPokemonInfoDialogs(int i, OurPokemon p)
	{
		if(pokemonInfoDialogs[i] == null)
		{
			pokemonInfoDialogs[i] = new PokemonInfoDialog(p, i);
			pokemonInfoDialogs[i].setPosition(200, 50);
			add(pokemonInfoDialogs[i]);
		}
		pokemonInfoDialogs[i].setVisible(true);
	}

	public void removePokemonInfoDialog(int i)
	{
		removeChild(pokemonInfoDialogs[i]);
		pokemonInfoDialogs[i] = null;
	}
}
