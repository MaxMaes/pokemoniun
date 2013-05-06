package org.pokenet.client.twl.ui;

import org.pokenet.client.GameClient;
import org.pokenet.client.backend.entity.OurPlayer;
import org.pokenet.client.backend.entity.PlayerItem;
import org.pokenet.client.twl.ui.frames.BagDialog;
import org.pokenet.client.twl.ui.frames.BattleFrontierDialog;
import org.pokenet.client.twl.ui.frames.BoatChooserDialog;
import org.pokenet.client.twl.ui.frames.ChatDialog;
import org.pokenet.client.twl.ui.frames.HelpWindow;
import org.pokenet.client.twl.ui.frames.OptionsDialog;
import org.pokenet.client.twl.ui.frames.PlayerInfoDialog;
import org.pokenet.client.twl.ui.frames.PlayerPopupDialog;
import org.pokenet.client.twl.ui.frames.PokedexDialog;
import org.pokenet.client.ui.base.TWLImageButton;
import de.matthiasmann.twl.DesktopArea;
import de.matthiasmann.twl.Widget;

/**
 * Class which controls and contains everything which is rendered over the map and player.
 * 
 * @author Myth1c
 */
public class HUD extends DesktopArea
{
	private Widget topBar;
	private TWLImageButton[] barbuttons; // The buttons in the top bar

	private PokedexDialog pokedex;
	private HelpWindow help;
	private BagDialog bag;
	private OptionsDialog options;
	private PlayerInfoDialog playerinfo;
	private ChatDialog chat;
	private BoatChooserDialog boatChooser;
	private BattleFrontierDialog battlefrontierDialog;
	private PlayerPopupDialog playerPopupDialog;

	public HUD()
	{
		setSize(800, 600);
		setPosition(0, 0);

		pokedex = new PokedexDialog();
		add(pokedex);
		help = new HelpWindow();
		help.setSize(360, 460);
		add(help);
		options = new OptionsDialog();
		add(options);
		chat = new ChatDialog();
		add(chat);

		pokedex.setVisible(false);
		options.setVisible(false);
		help.setVisible(false);
		chat.setVisible(false);
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

	public void toggleBag()
	{
		if(bag == null)
		{
			bag = new BagDialog(GameClient.getInstance().getOurPlayer().getItems())
			{
				@Override
				public void cancelled()
				{
					bag.setVisible(false);
					bag = null;
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
					bag.setVisible(false);
					bag = null;
					/* m_bag = new BigBagDialog();
					 * getDisplay().add(m_bag); */
				}
			};
		}
		else
		{
			bag.setVisible(false);
			bag = null;
		}
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
		}
		else
		{
			hideHUDElements();
			playerinfo = new PlayerInfoDialog();
			playerinfo.setPosition(barbuttons[0].getX(), 67);
			add(playerinfo);
		}
	}

	/**
	 * Shows the boat chooser dialog
	 */
	public void showBoatDialog(String currentLocation)
	{
		if(boatChooser != null)
		{
			removeChild(boatChooser);
		}
		hideHUDElements();
		boatChooser = new BoatChooserDialog(currentLocation);
		add(boatChooser);
		boatChooser.setVisible(true);
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

	public void hideHUDElements()
	{
		pokedex.setVisible(false);
		help.setVisible(false);
		options.setVisible(false);
		bag.setVisible(false);
		if(boatChooser != null)
			boatChooser.setVisible(false);

		hideBattleFrontierDialog();
	}

	public ChatDialog getChat()
	{
		return null;
	}

	public void hideBattleFrontierDialog()
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
	 */
	public void createPlayerPopupDialog(String player)
	{
		playerPopupDialog = new PlayerPopupDialog(player);
		playerPopupDialog.setVisible(false);
		add(playerPopupDialog);
	}

	public PlayerPopupDialog getPlayerPopupDialog()
	{
		return playerPopupDialog;
	}

	public void destroyPlayerPopupDialog()
	{

	}

	public void showPlayerPopupDialogAt(int x, int y)
	{

	}
}
