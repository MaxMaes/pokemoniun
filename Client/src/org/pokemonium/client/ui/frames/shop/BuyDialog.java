package org.pokemonium.client.ui.frames.shop;

import java.util.ArrayList;
import java.util.HashMap;
import org.pokemonium.client.GameClient;
import org.pokemonium.client.backend.entity.Item;
import org.pokemonium.client.constants.ServerPacket;
import org.pokemonium.client.constants.ShopInteraction;
import org.pokemonium.client.protocol.ClientMessage;
import org.pokemonium.client.ui.components.Image;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.Widget;

public class BuyDialog extends Widget
{
	private ShopDialog shopDialog;
	private ArrayList<Item> items;

	private Button cancelButton;

	private ArrayList<Integer> itemIDList; // ArrayList of all itemID's that this shop has
	private HashMap<Integer, Integer> shopStock; // Hashmap with <ItemID, Stock amount>

	private Button[] itemButtons;
	private Image[] itemSprites;
	private Label[] itemLabels;
	private Image[] itemStockSprites;

	public BuyDialog(ShopDialog shop, HashMap<Integer, Integer> stock, ArrayList<Item> itemList)
	{
		shopDialog = shop;
		shopStock = stock;
		items = itemList;
		itemIDList = new ArrayList<Integer>();
		for(Integer i : stock.keySet())
		{
			itemIDList.add(i);
		}
		initGUI();
	}

	private void initGUI()
	{
		itemButtons = new Button[items.size()];
		itemSprites = new Image[items.size()];
		itemLabels = new Label[items.size()];
		itemStockSprites = new Image[items.size()];
		for(int i = 0; i < items.size(); i++)
		{
			final int itemChosen = items.get(i).getId();
			itemButtons[i] = new Button("");
			itemButtons[i].setCanAcceptKeyboardFocus(false);
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

				add(itemStockSprites[i]);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			itemLabels[i] = new Label(items.get(i).getName() + " - $" + items.get(i).getPrice());

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
		cancelButton.setCanAcceptKeyboardFocus(false);
		cancelButton.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				shopDialog.switchUI(ShopDialog.SHOPSTATE_BUY);
			}
		});
		add(cancelButton);
	}

	public void itemClicked(int itemid)
	{
		ClientMessage message = new ClientMessage(ServerPacket.SHOPPING);
		message.addInt(ShopInteraction.BUY_ITEM);
		message.addInt(itemid);
		GameClient.getInstance().getSession().send(message);
	}

	@Override
	public void layout()
	{
		try
		{
			for(int i = 0; i < items.size(); i++)
			{
				itemButtons[i].setSize(300, 50);
				if(i > 0)
				{
					itemButtons[i].setPosition(getInnerX(), getInnerY() + itemButtons[i].getHeight());
				}
				else
				{
					itemButtons[i].setPosition(getInnerX(), getInnerY());
				}

				if(i > 0)
				{
					itemSprites[i].setPosition(getInnerX(), getInnerY() + itemButtons[i - 1].getHeight() + 12);
				}
				else
				{
					itemSprites[i].setPosition(getInnerX(), getInnerY() + 12);
				}

				if(i > 0)
				{
					itemStockSprites[i].setPosition(getInnerX() + 260, getInnerY() + itemButtons[i - 1].getHeight() + 12);
				}
				else
				{
					itemStockSprites[i].setPosition(getInnerX() + 260, getInnerY() + 12);
				}
				itemLabels[i].setSize(itemLabels[i].computeTextWidth(), itemLabels[i].computeTextHeight());
				if(i > 0)
				{
					itemLabels[i].setPosition(getInnerX() + 30, getInnerY() + itemButtons[i - 1].getHeight() + 20);
				}
				else
				{
					itemLabels[i].setPosition(getInnerX() + 30, getInnerY() + 20);
				}
			}
		}
		catch(Exception e)
		{
			System.err.println("there were no items in this shop category");
		}
		cancelButton.setSize(300, 40);
		cancelButton.setPosition(getInnerX(), getInnerY() + 336);

		setSize(300, getInnerY() + 336 - 72);
	}
}
