package org.pokenet.client.twl.ui.frames;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.lwjgl.util.Timer;
import org.newdawn.slick.loading.LoadingList;
import org.pokenet.client.GameClient;
import org.pokenet.client.backend.FileLoader;
import org.pokenet.client.backend.ItemDatabase;
import org.pokenet.client.backend.entity.Item;
import org.pokenet.client.backend.entity.PlayerItem;
import org.pokenet.client.constants.ServerPacket;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.twl.ui.base.Image;
import org.pokenet.client.twl.ui.base.ImageButton;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.ListBox;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.model.SimpleChangableListModel;

/**
 * The shop dialog
 * 
 * @author Myth1c/sadhi
 */
public class ShopDialog extends Widget
{
	public Timer m_timer;
	private Button m_buy;
	private Button m_cancel;
	private Button m_buyCancel;
	private Button m_sellCancel;
	private ImageButton[] m_categoryButtons;
	private Label[] m_categoryLabels;
	private Button[] m_itemButtons;
	private Label[] m_itemLabels;
	private Image[] m_itemPics;
	private Image[] m_itemStockPics;
	private List<Integer> m_merch = new ArrayList<Integer>();
	private Button m_sell;
	private Button[] m_sellButton;
	private ListBox<String> m_sellList;
	private SimpleChangableListModel<String> sellmodel;
	private HashMap<Integer, Integer> m_stock;
	List<Item> m_items;
	private Widget m_shopdialog;
	// private Widget m_buyShopdialog;
	// private Widget m_sellShopdialog;
	private int state;

	/**
	 * Constructor
	 * 
	 * @param stock
	 */
	public ShopDialog(HashMap<Integer, Integer> stock)
	{
		for(Integer i : stock.keySet())
			m_merch.add(i);
		m_stock = stock;
		m_timer = new Timer();
		m_timer.pause();
		// setTitle("PokeMart");
		// setResizableAxis(ResizableAxis.NONE);
		// setPosition(getX() - 1, getY() + 1);
		initGUI();
		// buyGUI();
		// sellGUI();
		setCenter();
		state = 0;
	}

	/**
	 * Displays the item buying GUI
	 */
	public void buyGUI()
	{
		// m_shopdialog = new Widget();
		// m_shopdialog.setTheme("shopdialog");
		m_categoryButtons = new ImageButton[4];
		m_categoryLabels = new Label[4];
		m_categoryButtons[0] = new ImageButton(" ");
		String respath = System.getProperty("res.path");
		if(respath == null)
			respath = "";
		m_categoryButtons[0].setImage(FileLoader.loadImage(respath + "res/ui/shop/pokeball.png")); // TODO: THEME
		m_categoryButtons[0].setSize(150, 160);
		m_categoryButtons[0].setPosition(0, 0);
		m_categoryButtons[0].addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				categoryClicked(0);
			}
		});
		m_shopdialog.add(m_categoryButtons[0]);
		m_categoryLabels[0] = new Label("Pokeballs");
		m_categoryLabels[0].setPosition(20, 140);
		m_categoryLabels[0].setSize(GameClient.getInstance().getFontSmall().getWidth(m_categoryLabels[0].getText()), GameClient.getInstance().getFontSmall().getHeight(m_categoryLabels[0].getText()));
		m_shopdialog.add(m_categoryLabels[0]);
		m_categoryButtons[1] = new ImageButton(" ");
		m_categoryButtons[1].setImage(FileLoader.loadImage(respath + "res/ui/shop/potion.png"));  // TODO: THEME
		m_categoryButtons[1].setSize(150, 160);
		m_categoryButtons[1].setPosition(151, 0);
		m_categoryButtons[1].addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				categoryClicked(1);
			}
		});
		m_shopdialog.add(m_categoryButtons[1]);

		m_categoryLabels[1] = new Label("Potions");
		m_categoryLabels[1].setPosition(171, 140);
		m_categoryLabels[1].setSize(GameClient.getInstance().getFontSmall().getWidth(m_categoryLabels[1].getText()), GameClient.getInstance().getFontSmall().getHeight(m_categoryLabels[1].getText()));
		m_shopdialog.add(m_categoryLabels[1]);

		m_categoryButtons[2] = new ImageButton(" ");
		m_categoryButtons[2].setImage(FileLoader.loadImage(respath + "res/ui/shop/status.png"));
		m_categoryButtons[2].setSize(150, 160);
		m_categoryButtons[2].setPosition(0, 161);
		m_categoryButtons[2].setEnabled(true);
		m_categoryButtons[2].addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				categoryClicked(2);
			}
		});
		m_shopdialog.add(m_categoryButtons[2]);

		m_categoryLabels[2] = new Label("Status Heals");
		m_categoryLabels[2].setPosition(20, 300);
		m_categoryLabels[2].setSize(GameClient.getInstance().getFontSmall().getWidth(m_categoryLabels[2].getText()), GameClient.getInstance().getFontSmall().getHeight(m_categoryLabels[2].getText()));
		m_shopdialog.add(m_categoryLabels[2]);

		m_categoryButtons[3] = new ImageButton(" ");
		LoadingList.setDeferredLoading(true);
		try
		{
			m_categoryButtons[3].setImage(FileLoader.loadImage(respath + "res/ui/shop/field.png"));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		LoadingList.setDeferredLoading(false);
		m_categoryButtons[3].setSize(150, 160);
		m_categoryButtons[3].setPosition(151, 161);
		m_categoryButtons[3].addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				categoryClicked(3);
			}
		});
		m_shopdialog.add(m_categoryButtons[3]);

		m_categoryLabels[3] = new Label("Field Tools");
		m_categoryLabels[3].setPosition(171, 300);
		m_categoryLabels[3].setSize(GameClient.getInstance().getFontSmall().getWidth(m_categoryLabels[3].getText()), GameClient.getInstance().getFontSmall().getHeight(m_categoryLabels[3].getText()));
		m_shopdialog.add(m_categoryLabels[3]);

		m_buyCancel = new Button("Cancel");
		m_buyCancel.setSize(300, 56);
		m_buyCancel.setPosition(0, 321);
		// m_cancel.addCallback(new Runnable()
		// {
		// @Override
		// public void run()
		// {
		// for(int i = 0; i < m_categoryLabels.length; i++)
		// removeChild(m_categoryLabels[i]);
		// for(int i = 0; i < m_categoryButtons.length; i++)
		// removeChild(m_categoryButtons[i]);
		// removeChild(m_cancel);
		// initGUI();
		// }
		// });
		m_shopdialog.add(m_buyCancel);

		m_buyCancel.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				removeBuy();
				state = 0;
				switchUI();
			}
		});

		// m_shopdialog.setVisible(false);
		// int height = (int) GameClient.getInstance().getGUI().getHeight();
		// int width = (int) GameClient.getInstance().getGUI().getWidth();
		// int x = width / 2 - 130;
		// int y = height / 2 - 238;
		// m_shopdialog.setSize(259, 475);
		// m_shopdialog.setPosition(x, y);
	}

	public void cancelled()
	{
		// GameClient.getInstance().getPacketGenerator().writeTcpMessage("11");
		ClientMessage message = new ClientMessage(ServerPacket.BUY_SELL_ITEMS);
		message.addInt(2);
		GameClient.getInstance().getSession().send(message);
		removeMain();
		m_shopdialog = null;
		GameClient.getInstance().getGUIPane().getHUD().removeShop();
	}

	/**
	 * Called when a category for item to buy is selected
	 * 
	 * @param name
	 */
	public void categoryClicked(int name)
	{
		m_items = new ArrayList<Item>();
		switch(name)
		{
			case 0:
				for(int i : m_merch)
					if(PlayerItem.getItem(i).getCategory().equals("Pokeball"))
						m_items.add(PlayerItem.getItem(i));
				initItems();
				break;
			case 1:
				for(int i : m_merch)
					if(PlayerItem.getItem(i).getCategory().equals("Potions") || PlayerItem.getItem(i).getCategory().equalsIgnoreCase("Food"))
						m_items.add(PlayerItem.getItem(i));
				initItems();
				break;
			case 2:
				for(int i : m_merch)
					if(PlayerItem.getItem(i).getCategory().equals("Medicine") || PlayerItem.getItem(i).getCategory().equalsIgnoreCase("Vitamins"))
						m_items.add(PlayerItem.getItem(i));
				initItems();
				break;
			case 3:
				for(int i : m_merch)
					if(PlayerItem.getItem(i).getCategory().equals("Field") || PlayerItem.getItem(i).getCategory().equals("TM") || PlayerItem.getItem(i).getCategory().equalsIgnoreCase("Held"))
						m_items.add(PlayerItem.getItem(i));
				initItems();
				break;
		}
	}

	/**
	 * Initialises the gui when first opened
	 */
	public void initGUI()
	{
		m_shopdialog = this;
		m_shopdialog.setTheme("shopdialog");
		m_buy = new Button("Buy");
		m_buy.setPosition(0, 0);
		m_buy.setSize(150, 320);
		m_buy.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				if(m_buyCancel == null)
				{
					state = 1;
					removeMain();
					buyGUI();
				}
				else
				{
					state = 1;
					removeMain();
					switchUI();
				}
				// buyGUI();
			}
		});
		m_shopdialog.add(m_buy);
		m_sell = new Button("Sell");
		m_sell.setPosition(151, 0);
		m_sell.setSize(150, 320);
		m_sell.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				if(m_sellCancel == null)
				{
					state = 2;
					removeMain();
					sellGUI();
				}
				else
				{
					state = 2;
					removeMain();
					switchUI();
				}
				// sellGUI();
			}
		});
		m_shopdialog.add(m_sell);
		m_cancel = new Button("Cancel");
		m_cancel.setSize(300, 56);
		m_cancel.setPosition(0, 321);
		m_cancel.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				cancelled();
			}
		});
		m_shopdialog.add(m_cancel);
		m_shopdialog.setVisible(true);
	}

	public void itemClicked(int itemid)
	{
		// GameClient.getInstance().getPacketGenerator().writeTcpMessage("0F" + itemid + ",1");
		ClientMessage message = new ClientMessage(ServerPacket.BUY_SELL_ITEMS);
		message.addInt(0);
		message.addInt(itemid);
		GameClient.getInstance().getSession().send(message);
	}

	public void pack()
	{

	}

	/**
	 * Displays the selling item gui
	 */
	public void sellGUI()
	{
		// // m_cancel.setVisible(false);
		// m_shopdialog = new Widget();
		// m_shopdialog.setTheme("shopdialog");
		sellmodel = new SimpleChangableListModel<>();
		for(int i = 0; i < GameClient.getInstance().getOurPlayer().getItems().size(); i++)
		{
			if(!GameClient.getInstance().getOurPlayer().getItems().get(i).getItem().getCategory().equals("Key"))
				sellmodel.addElement(GameClient.getInstance().getOurPlayer().getItems().get(i).getItem().getName());
		}
		m_sellList = new ListBox<String>(sellmodel);
		m_sellButton = new Button[1];
		m_sellButton[0] = new Button("Sell");
		m_sellButton[0].setSize(getWidth(), 35);
		m_sellButton[0].setPosition(0, m_cancel.getY() - 35);
		m_sellButton[0].addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					Runnable yes = new Runnable()
					{
						@Override
						public void run()
						{
							// GameClient.getInstance().getPacketGenerator().writeTcpMessage("10" + ItemDatabase.getInstance().getItem(m_sellList.getSelectedName()).getId() + ",");
							ClientMessage message = new ClientMessage(ServerPacket.BUY_SELL_ITEMS);
							message.addInt(1);
							message.addInt(ItemDatabase.getInstance().getItem(sellmodel.getEntry(m_sellList.getSelected())).getId());
							GameClient.getInstance().getSession().send(message);
							GameClient.getInstance().getGUIPane().hideConfirmationDialog();
						}
					};
					Runnable no = new Runnable()
					{
						@Override
						public void run()
						{
							GameClient.getInstance().getGUIPane().hideConfirmationDialog();
						}
					};
					GameClient
							.getInstance()
							.getGUIPane()
							.showConfirmationDialog(
									"Are you sure you want to sell " + sellmodel.getEntry(m_sellList.getSelected()) + " for $"
											+ ItemDatabase.getInstance().getItem(sellmodel.getEntry(m_sellList.getSelected())).getPrice() / 2 + "?", yes, no);
				}
				catch(Exception e2)
				{
					e2.printStackTrace();
				}
			}
		});
		m_sellList.setSize(getWidth(), m_sellButton[0].getY());
		// Start the UI
		m_sellCancel = new Button("Cancel");
		m_sellCancel.setSize(300, 56);
		m_sellCancel.setPosition(0, 321);
		m_sellCancel.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				removeSell();
				state = 0;
				switchUI();
			}
		});
		m_shopdialog.add(m_sellCancel);
		m_shopdialog.add(m_sellList);
		m_shopdialog.add(m_sellButton[0]);
		// int height = (int) GameClient.getInstance().getGUI().getHeight();
		// int width = (int) GameClient.getInstance().getGUI().getWidth();
		// int x = width / 2 - 130;
		// int y = height / 2 - 238;
		// m_shopdialog.setSize(259, 475);
		// m_shopdialog.setPosition(x, y);
		// m_shopdialog.setVisible(false);
	}

	/**
	 * Centers the frame
	 */
	public void setCenter()
	{
		int height = (int) GameClient.getInstance().getGUI().getHeight();
		int width = (int) GameClient.getInstance().getGUI().getWidth();
		int x = width / 2 - 130;
		int y = height / 2 - 238;
		setSize(310, 375);
		setPosition(x, y);
	}

	/* TODO: Derp
	 * @Override
	 * public void update(GUIContext container, int delta)
	 * {
	 * Timer.tick();
	 * if(m_timer.getTime() >= 3)
	 * try
	 * {
	 * GameClient.getInstance().getUi().getNPCSpeech().advance();
	 * m_timer.pause();
	 * }
	 * catch(Exception e)
	 * {
	 * }
	 * } */

	/**
	 * Updates the stock
	 * 
	 * @param stock
	 */
	public void updateStock(HashMap<Integer, Integer> stock)
	{
		m_stock = stock;
		initGUI();
	}

	private void initItems()
	{
		setCenter();
		for(int i = 0; i < m_categoryButtons.length; i++)
			removeChild(m_categoryButtons[i]);
		for(int i = 0; i < m_categoryLabels.length; i++)
			removeChild(m_categoryLabels[i]);
		removeChild(m_buyCancel);
		m_itemButtons = new Button[m_items.size()];
		m_itemPics = new Image[m_items.size()];
		m_itemLabels = new Label[m_items.size()];
		m_itemStockPics = new Image[m_items.size()];
		for(int i = 0; i < m_items.size(); i++)
		{
			final int itemChosen = m_items.get(i).getId();
			m_itemButtons[i] = new Button("");
			m_itemButtons[i].setSize(300, 50);
			if(i > 0)
				m_itemButtons[i].setPosition(0, m_itemButtons[i - 1].getY());
			else
				m_itemButtons[i].setPosition(0, 0);
			m_itemButtons[i].addCallback(new Runnable()
			{
				@Override
				public void run()
				{
					itemClicked(itemChosen);
				}
			});
			m_shopdialog.add(m_itemButtons[i]);
			String respath = System.getProperty("res.path");
			if(respath == null)
				respath = "";
			try
			{
				if(m_items.get(i).getCategory().equals("TM"))
				{
					m_itemPics[i] = new Image(respath + "res/items/24/TM.png"); // TODO: Theme.
				}
				else
				{
					m_itemPics[i] = new Image(respath + "res/items/24/" + m_items.get(i).getId() + ".png");
				}
				m_itemPics[i].setSize(32, 32);
				if(i > 0)
					m_itemPics[i].setPosition(0, m_itemPics[i - 1].getY());
				else
					m_itemPics[i].setPosition(0, 12);
				m_shopdialog.add(m_itemPics[i]);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}

			try
			{
				LoadingList.setDeferredLoading(true);
				String stock = "empty";
				if(m_stock.get(m_items.get(i).getId()) >= 100)
					stock = "full";
				else if(m_stock.get(m_items.get(i).getId()) < 100 && m_stock.get(m_items.get(i).getId()) >= 60)
					stock = "half";
				else if(m_stock.get(m_items.get(i).getId()) < 60 && m_stock.get(m_items.get(i).getId()) >= 30)
					stock = "halfempty";
				m_itemStockPics[i] = new Image(respath + "res/ui/shop/" + stock + ".png");
				LoadingList.setDeferredLoading(false);
				m_itemStockPics[i].setSize(32, 32);
				if(i > 0)
					m_itemStockPics[i].setPosition(260, m_itemStockPics[i - 1].getY());
				else
					m_itemStockPics[i].setPosition(260, 12);
				m_shopdialog.add(m_itemStockPics[i]);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			m_itemLabels[i] = new Label(m_items.get(i).getName() + " - $" + m_items.get(i).getPrice());
			m_itemLabels[i].setSize(GameClient.getInstance().getFontSmall().getWidth(m_itemLabels[i].getText()), GameClient.getInstance().getFontSmall().getHeight(m_itemLabels[i].getText()));
			if(i > 0)
				m_itemLabels[i].setPosition(30, m_itemLabels[i - 1].getY());
			else
				m_itemLabels[i].setPosition(30, 20);
			m_itemButtons[i].addCallback(new Runnable()
			{
				@Override
				public void run()
				{
					itemClicked(itemChosen);
				}
			});
			m_shopdialog.add(m_itemLabels[i]);
		}

		m_buyCancel = new Button("Cancel");
		m_buyCancel.setSize(300, 40);
		m_buyCancel.setPosition(0, 336);
		m_buyCancel.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				for(int i = 0; i < m_itemButtons.length; i++)
					removeChild(m_itemButtons[i]);
				for(int i = 0; i < m_itemPics.length; i++)
					removeChild(m_itemPics[i]);
				for(int i = 0; i < m_itemLabels.length; i++)
					removeChild(m_itemLabels[i]);
				for(int i = 0; i < m_itemStockPics.length; i++)
					removeChild(m_itemStockPics[i]);
				removeChild(m_buyCancel);
				m_buyCancel = null;

				// re-add the buy ui
				for(int i = 0; i < m_categoryButtons.length; i++)
					m_shopdialog.add(m_categoryButtons[i]);
				for(int i = 0; i < m_categoryLabels.length; i++)
					m_shopdialog.add(m_categoryLabels[i]);
				m_buyCancel = new Button("Cancel");
				m_buyCancel.setSize(300, 56);
				m_buyCancel.setPosition(0, 321);
				m_shopdialog.add(m_buyCancel);
				m_buyCancel.addCallback(new Runnable()
				{
					@Override
					public void run()
					{
						removeBuy();
						state = 0;
						switchUI();
					}
				});
			}
		});
		m_shopdialog.add(m_buyCancel);
		// setTitle("Potions");
		// setResizableAxis(ResizableAxis.NONE);
		// setSize(300, 400);
		// pack();
		// for (int i = 0; i < m_itemButtons.length; i++) {
		// if (i > 0)
		// m_itemButtons[i].setPosition(0,m_itemButtons[i-1].getY() + m_itemButtons[i-1].getHeight());
		// m_itemButtons[i].setSize(getWidth(),(getHeight() - 60)/ m_itemButtons.length);
		// }
	}

	private void switchUI()
	{
		switch(state)
		{
			case 0:
				addMain();
				break;
			case 1:
				addBuy();
				break;
			case 2:
				addSell();
				break;
		}
	}

	private void removeMain()
	{
		removeChild(m_buy);
		removeChild(m_sell);
		removeChild(m_cancel);
	}

	private void removeBuy()
	{
		for(int i = 0; i < m_categoryButtons.length; i++)
			removeChild(m_categoryButtons[i]);
		for(int i = 0; i < m_categoryLabels.length; i++)
			removeChild(m_categoryLabels[i]);
		removeChild(m_buyCancel);
	}

	private void removeSell()
	{
		removeChild(m_sellCancel);
		removeChild(m_sellList);
		removeChild(m_sellButton[0]);
	}

	private void addMain()
	{
		m_shopdialog.add(m_buy);
		m_shopdialog.add(m_sell);
		m_shopdialog.add(m_cancel);
	}

	private void addBuy()
	{
		for(int i = 0; i < m_categoryButtons.length; i++)
			m_shopdialog.add(m_categoryButtons[i]);
		for(int i = 0; i < m_categoryLabels.length; i++)
			m_shopdialog.add(m_categoryLabels[i]);
		m_shopdialog.add(m_buyCancel);
	}

	private void addSell()
	{
		m_shopdialog.add(m_sellCancel);
		m_shopdialog.add(m_sellList);
		m_shopdialog.add(m_sellButton[0]);
	}
}
