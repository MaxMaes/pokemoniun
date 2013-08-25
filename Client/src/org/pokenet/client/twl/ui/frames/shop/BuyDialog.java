package org.pokenet.client.twl.ui.frames.shop;

import java.util.ArrayList;
import java.util.HashMap;
import org.pokenet.client.GameClient;
import org.pokenet.client.backend.FileLoader;
import org.pokenet.client.backend.entity.Item;
import org.pokenet.client.backend.entity.PlayerItem;
import org.pokenet.client.constants.ServerPacket;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.twl.ui.base.Image;
import org.pokenet.client.twl.ui.base.ImageButton;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.Widget;

public class BuyDialog extends Widget
{
	private ImageButton[] categoryButtons;
	private Label[] categoryLabels;
	private ShopDialog shopDialog;
	private ArrayList<Item> items;

	private Button[] itemButtons;
	private Label[] itemLabels;
	private Image[] itemSprites;
	private Image[] itemStockSprites;
	private ImageButton[] m_categoryButtons;
	private Label[] m_categoryLabels;
	private Button cancelButton;

	private ArrayList<Integer> itemIDList; // ArrayList of all itemID's that this shop has
	private HashMap<Integer, Integer> shopStock; // Hashmap with <ItemID, Stock amount>

	public BuyDialog(ShopDialog shop, HashMap<Integer, Integer> stock)
	{
		shopDialog = shop;
		shopStock = stock;
		itemIDList = new ArrayList<Integer>();
		for(Integer i : stock.keySet())
		{
			itemIDList.add(i);
		}
	}

	/**
	 * Called when a category for item to buy is selected
	 * 
	 * @param name
	 */
	public void categoryClicked(int name)
	{
		items = new ArrayList<Item>();
		switch(name)
		{
			case 0:
				for(int i : itemIDList)
				{
					if(PlayerItem.getItem(i).getCategory().equals("Pokeball"))
					{
						items.add(PlayerItem.getItem(i));
					}
				}
				initItems();
				break;
			case 1:
				for(int i : itemIDList)
				{
					{
						if(PlayerItem.getItem(i).getCategory().equals("Potions") || PlayerItem.getItem(i).getCategory().equalsIgnoreCase("Food"))
							items.add(PlayerItem.getItem(i));
					}
				}
				initItems();
				break;
			case 2:
				for(int i : itemIDList)
				{
					{
						if(PlayerItem.getItem(i).getCategory().equals("Medicine") || PlayerItem.getItem(i).getCategory().equalsIgnoreCase("Vitamins"))
							items.add(PlayerItem.getItem(i));
					}
				}
				initItems();
				break;
			case 3:
				for(int i : itemIDList)
				{
					{
						if(PlayerItem.getItem(i).getCategory().equals("Field") || PlayerItem.getItem(i).getCategory().equals("TM") || PlayerItem.getItem(i).getCategory().equalsIgnoreCase("Held"))
							items.add(PlayerItem.getItem(i));
					}
				}
				initItems();
				break;
		}
	}

	private void initItems()
	{
		for(int i = 0; i < m_categoryButtons.length; i++)
		{
			removeChild(m_categoryButtons[i]);
		}
		for(int i = 0; i < m_categoryLabels.length; i++)
		{
			removeChild(m_categoryLabels[i]);
		}
		itemButtons = new Button[items.size()];
		itemSprites = new Image[items.size()];
		itemLabels = new Label[items.size()];
		itemStockSprites = new Image[items.size()];
		for(int i = 0; i < items.size(); i++)
		{
			final int itemChosen = items.get(i).getId();
			itemButtons[i] = new Button("");
			itemButtons[i].setSize(300, 50);
			if(i > 0)
				itemButtons[i].setPosition(0, itemButtons[i - 1].getY());
			else
				itemButtons[i].setPosition(0, 0);
			itemButtons[i].addCallback(new Runnable()
			{
				@Override
				public void run()
				{
					itemClicked(itemChosen);
				}
			});
			add(itemButtons[i]);
			String respath = System.getProperty("res.path");
			if(respath == null)
				respath = "";
			try
			{
				if(items.get(i).getCategory().equals("TM"))
				{
					itemSprites[i] = new Image(respath + "res/items/24/TM.png"); // TODO: Theme.
				}
				else
				{
					itemSprites[i] = new Image(respath + "res/items/24/" + items.get(i).getId() + ".png");
				}
				itemSprites[i].setSize(32, 32);
				if(i > 0)
				{
					itemSprites[i].setPosition(0, itemSprites[i - 1].getY());
				}
				else
				{
					itemSprites[i].setPosition(0, 12);
				}
				add(itemSprites[i]);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}

			try
			{
				String stock = "empty";
				if(shopStock.get(items.get(i).getId()) >= 100)
				{
					stock = "full";
				}
				else if(shopStock.get(items.get(i).getId()) < 100 && shopStock.get(items.get(i).getId()) >= 60)
				{
					stock = "half";
				}
				else if(shopStock.get(items.get(i).getId()) < 60 && shopStock.get(items.get(i).getId()) >= 30)
				{
					stock = "halfempty";
				}
				itemStockSprites[i] = new Image(respath + "res/ui/shop/" + stock + ".png");
				itemStockSprites[i].setSize(32, 32);
				if(i > 0)
				{
					itemStockSprites[i].setPosition(260, itemStockSprites[i - 1].getY());
				}
				else
				{
					itemStockSprites[i].setPosition(260, 12);
				}
				add(itemStockSprites[i]);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			itemLabels[i] = new Label(items.get(i).getName() + " - $" + items.get(i).getPrice());
			itemLabels[i].setSize(GameClient.getInstance().getFontSmall().getWidth(itemLabels[i].getText()), GameClient.getInstance().getFontSmall().getHeight(itemLabels[i].getText()));
			if(i > 0)
				itemLabels[i].setPosition(30, itemLabels[i - 1].getY());
			else
				itemLabels[i].setPosition(30, 20);
			itemButtons[i].addCallback(new Runnable()
			{
				@Override
				public void run()
				{
					itemClicked(itemChosen);
				}
			});
			add(itemLabels[i]);
		}

		cancelButton = new Button("Cancel");
		cancelButton.setSize(300, 40);
		cancelButton.setPosition(0, 336);
		cancelButton.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				for(int i = 0; i < itemButtons.length; i++)
					removeChild(itemButtons[i]);
				for(int i = 0; i < itemSprites.length; i++)
					removeChild(itemSprites[i]);
				for(int i = 0; i < itemLabels.length; i++)
					removeChild(itemLabels[i]);
				for(int i = 0; i < itemStockSprites.length; i++)
					removeChild(itemStockSprites[i]);
				removeChild(cancelButton);
				cancelButton = null;

				// re-add the buy ui
				for(int i = 0; i < m_categoryButtons.length; i++)
					add(m_categoryButtons[i]);
				for(int i = 0; i < m_categoryLabels.length; i++)
					add(m_categoryLabels[i]);
				cancelButton = new Button("Cancel");
				cancelButton.setSize(300, 56);
				cancelButton.setPosition(0, 321);
				add(cancelButton);
				cancelButton.addCallback(new Runnable()
				{
					@Override
					public void run()
					{
						shopDialog.switchUI(ShopDialog.SHOPSTATE_MAIN);
					}
				});
			}
		});
		add(cancelButton);
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

	public void itemClicked(int itemid)
	{
		// GameClient.getInstance().getPacketGenerator().writeTcpMessage("0F" + itemid + ",1");
		ClientMessage message = new ClientMessage(ServerPacket.BUY_SELL_ITEMS);
		message.addInt(0);
		message.addInt(itemid);
		GameClient.getInstance().getSession().send(message);
	}

	/**
	 * Displays the item buying GUI
	 */
	public void buyGUI()
	{
		setTheme("shopdialog");
		categoryButtons = new ImageButton[4];
		categoryLabels = new Label[4];
		categoryButtons[0] = new ImageButton(" ");
		String respath = System.getProperty("res.path");
		if(respath == null)
			respath = "";
		categoryButtons[0].setImage(FileLoader.loadImage(respath + "res/ui/shop/pokeball.png")); // TODO: THEME
		categoryButtons[0].setSize(150, 160);
		categoryButtons[0].setPosition(0, 0);
		categoryButtons[0].addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				categoryClicked(0);
			}
		});
		add(categoryButtons[0]);
		categoryLabels[0] = new Label("Pokeballs");
		categoryLabels[0].setPosition(20, 140);
		categoryLabels[0].setSize(GameClient.getInstance().getFontSmall().getWidth(categoryLabels[0].getText()), GameClient.getInstance().getFontSmall().getHeight(categoryLabels[0].getText()));
		add(categoryLabels[0]);
		categoryButtons[1] = new ImageButton(" ");
		categoryButtons[1].setImage(FileLoader.loadImage(respath + "res/ui/shop/potion.png"));  // TODO: THEME
		categoryButtons[1].setSize(150, 160);
		categoryButtons[1].setPosition(151, 0);
		categoryButtons[1].addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				categoryClicked(1);
			}
		});
		add(categoryButtons[1]);

		categoryLabels[1] = new Label("Potions");
		categoryLabels[1].setPosition(171, 140);
		categoryLabels[1].setSize(GameClient.getInstance().getFontSmall().getWidth(categoryLabels[1].getText()), GameClient.getInstance().getFontSmall().getHeight(categoryLabels[1].getText()));
		add(categoryLabels[1]);

		categoryButtons[2] = new ImageButton(" ");
		categoryButtons[2].setImage(FileLoader.loadImage(respath + "res/ui/shop/status.png"));
		categoryButtons[2].setSize(150, 160);
		categoryButtons[2].setPosition(0, 161);
		categoryButtons[2].setEnabled(true);
		categoryButtons[2].addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				categoryClicked(2);
			}
		});
		add(categoryButtons[2]);

		categoryLabels[2] = new Label("Status Heals");
		categoryLabels[2].setPosition(20, 300);
		categoryLabels[2].setSize(GameClient.getInstance().getFontSmall().getWidth(categoryLabels[2].getText()), GameClient.getInstance().getFontSmall().getHeight(categoryLabels[2].getText()));
		add(categoryLabels[2]);

		categoryButtons[3] = new ImageButton(" ");
		try
		{
			categoryButtons[3].setImage(FileLoader.loadImage(respath + "res/ui/shop/field.png"));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		categoryButtons[3].setSize(150, 160);
		categoryButtons[3].setPosition(151, 161);
		categoryButtons[3].addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				categoryClicked(3);
			}
		});
		add(categoryButtons[3]);

		categoryLabels[3] = new Label("Field Tools");
		categoryLabels[3].setPosition(171, 300);
		categoryLabels[3].setSize(GameClient.getInstance().getFontSmall().getWidth(categoryLabels[3].getText()), GameClient.getInstance().getFontSmall().getHeight(categoryLabels[3].getText()));
		add(categoryLabels[3]);

		cancelButton = new Button("Cancel");
		cancelButton.setSize(300, 56);
		cancelButton.setPosition(0, 321);
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
		add(cancelButton);

		cancelButton.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				shopDialog.switchUI(ShopDialog.SHOPSTATE_MAIN);
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
}
