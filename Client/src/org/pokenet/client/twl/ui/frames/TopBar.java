package org.pokenet.client.twl.ui.frames;

import org.pokenet.client.GameClient;
import org.pokenet.client.twl.ui.base.ImageButton;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.theme.ThemeManager;

public class TopBar extends Widget
{
	private ImageButton[] barbuttons;
	private Label moneyLabel;
	private Label timeLabel;

	public TopBar()
	{
		initTopbar();
		setPosition(0, 0);
		setSize(800, 47);
	}

	private void initTopbar()
	{
		ThemeManager theme = GameClient.getInstance().getTheme();
		barbuttons = new ImageButton[10];
		barbuttons[0] = new ImageButton(theme.getImage("topbar_stats"), theme.getImage("topbar_stats_hover"), theme.getImage("topbar_stats_pressed"));
		barbuttons[1] = new ImageButton(theme.getImage("topbar_pokedex"), theme.getImage("topbar_pokedex_hover"), theme.getImage("topbar_pokedex_pressed"));
		barbuttons[2] = new ImageButton(theme.getImage("topbar_pokemon"), theme.getImage("topbar_pokemon_hover"), theme.getImage("topbar_pokemon_pressed"));
		barbuttons[3] = new ImageButton(theme.getImage("topbar_bag"), theme.getImage("topbar_bag_hover"), theme.getImage("topbar_bag_pressed"));
		barbuttons[4] = new ImageButton(theme.getImage("topbar_map"), theme.getImage("topbar_map_hover"), theme.getImage("topbar_map_pressed"));
		barbuttons[5] = new ImageButton(theme.getImage("topbar_friends"), theme.getImage("topbar_friends_hover"), theme.getImage("topbar_friends_pressed"));
		barbuttons[6] = new ImageButton(theme.getImage("topbar_requests"), theme.getImage("topbar_requests_hover"), theme.getImage("topbar_requests_pressed"));
		barbuttons[7] = new ImageButton(theme.getImage("topbar_options"), theme.getImage("topbar_options_hover"), theme.getImage("topbar_options_pressed"));
		barbuttons[8] = new ImageButton(theme.getImage("topbar_help"), theme.getImage("topbar_help_hover"), theme.getImage("topbar_help_pressed"));
		barbuttons[9] = new ImageButton(theme.getImage("topbar_disconnect"), theme.getImage("topbar_disconnect_hover"), theme.getImage("topbar_disconnect_pressed"));

		barbuttons[0].setTooltipContent("Player stats");
		barbuttons[1].setTooltipContent("Pokedex");
		barbuttons[2].setTooltipContent("Party");
		barbuttons[3].setTooltipContent("Bag");
		barbuttons[4].setTooltipContent("World map");
		barbuttons[5].setTooltipContent("Friends");
		barbuttons[6].setTooltipContent("Requests");
		barbuttons[7].setTooltipContent("Options");
		barbuttons[8].setTooltipContent("Help");
		barbuttons[9].setTooltipContent("Disconnect");

		barbuttons[0].addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				GameClient.getInstance().getHUD().togglePlayerStats();
			}
		});
		barbuttons[1].addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				GameClient.getInstance().getHUD().togglePokedex();
			}
		});
		barbuttons[2].addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				GameClient.getInstance().getHUD().togglePokemon();
			}
		});
		barbuttons[3].addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				GameClient.getInstance().getHUD().toggleBag();
			}
		});
		barbuttons[4].addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				GameClient.getInstance().getHUD().toggleMap();
			}
		});
		barbuttons[5].addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				GameClient.getInstance().getHUD().toggleFriends();
			}
		});
		barbuttons[6].addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				GameClient.getInstance().getHUD().toggleRequests();
			}
		});
		barbuttons[7].addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				GameClient.getInstance().getHUD().toggleOptions();
			}
		});
		barbuttons[8].addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				GameClient.getInstance().getHUD().toggleHelp();
			}
		});
		barbuttons[9].addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				GameClient.getInstance().getHUD().disconnect();
			}
		});

		for(int i = 0; i < barbuttons.length; i++)
		{
			barbuttons[i].setImagePosition(-2, 1);
			barbuttons[i].setSize(28, 35);
			barbuttons[i].setPosition(5 + 35 * i + 5 * i, 7);
			add(barbuttons[i]);
		}
	}

	public ImageButton[] getBarButtons()
	{
		return barbuttons;
	}

	public ImageButton getBarButton(int i)
	{
		return barbuttons[i];
	}

	public Label getMoneyLabel()
	{
		return moneyLabel;
	}
}
