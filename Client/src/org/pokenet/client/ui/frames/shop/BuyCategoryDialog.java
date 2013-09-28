package org.pokenet.client.ui.frames.shop;

import java.util.ArrayList;
import java.util.HashMap;
import org.pokenet.client.GameClient;
import org.pokenet.client.backend.FileLoader;
import org.pokenet.client.backend.entity.Item;
import org.pokenet.client.backend.entity.PlayerItem;
import org.pokenet.client.ui.components.ImageButton;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.Widget;

public class BuyCategoryDialog extends Widget
{
	private ShopDialog shopDialog;
	private ImageButton[] categoryButtons;
	private Label[] categoryLabels;
	private Button cancelButton;
	private ArrayList<Integer> itemIDList;

	public BuyCategoryDialog(ShopDialog shop, HashMap<Integer, Integer> stock)
	{
		shopDialog = shop;
		itemIDList = new ArrayList<Integer>();
		for(Integer i : stock.keySet())
		{
			itemIDList.add(i);
		}
		initGUI();
	}

	/**
	 * Displays the item buying GUI
	 */
	public void initGUI()
	{
		categoryButtons = new ImageButton[4];
		categoryButtons[0] = new ImageButton(" ");
		categoryButtons[0].addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				categoryClicked(0);
			}
		});
		add(categoryButtons[0]);

		categoryButtons[1] = new ImageButton(" ");
		categoryButtons[1].addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				categoryClicked(1);
			}
		});
		add(categoryButtons[1]);

		categoryButtons[2] = new ImageButton(" ");
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

		categoryButtons[3] = new ImageButton(" ");
		categoryButtons[3].addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				categoryClicked(3);
			}
		});
		add(categoryButtons[3]);

		GameClient.getInstance().getGUI().invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				String respath = System.getProperty("res.path");
				if(respath == null)
					respath = "";
				categoryButtons[0].setImage(FileLoader.loadImage(respath + "res/ui/shop/pokeball.png")); // TODO: THEME
				categoryButtons[1].setImage(FileLoader.loadImage(respath + "res/ui/shop/potion.png"));  // TODO: THEME
				categoryButtons[2].setImage(FileLoader.loadImage(respath + "res/ui/shop/status.png"));
				categoryButtons[3].setImage(FileLoader.loadImage(respath + "res/ui/shop/field.png"));
			}
		});

		categoryLabels = new Label[4];
		categoryLabels[0] = new Label("Pokeballs");
		categoryLabels[1] = new Label("Potions");
		categoryLabels[2] = new Label("Status Heals");
		categoryLabels[3] = new Label("Field Tools");
		add(categoryLabels[0]);
		add(categoryLabels[1]);
		add(categoryLabels[2]);
		add(categoryLabels[3]);

		cancelButton = new Button("Cancel");
		cancelButton.setCanAcceptKeyboardFocus(false);
		add(cancelButton);
		cancelButton.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				shopDialog.switchUI(ShopDialog.SHOPSTATE_MAIN);
			}
		});

		for(ImageButton imgbtn : categoryButtons)
		{
			imgbtn.setCanAcceptKeyboardFocus(false);
		}
	}

	/**
	 * Called when a category for item to buy is selected
	 * 
	 * @param name
	 */
	public void categoryClicked(int name)
	{
		ArrayList<Item> items = new ArrayList<Item>();
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
				shopDialog.switchToItemBuy(items);
				break;
			case 1:
				for(int i : itemIDList)
				{
					if(PlayerItem.getItem(i).getCategory().equals("Potions") || PlayerItem.getItem(i).getCategory().equalsIgnoreCase("Food"))
						items.add(PlayerItem.getItem(i));
				}
				shopDialog.switchToItemBuy(items);
				break;
			case 2:
				for(int i : itemIDList)
				{
					if(PlayerItem.getItem(i).getCategory().equals("Medicine") || PlayerItem.getItem(i).getCategory().equalsIgnoreCase("Vitamins"))
						items.add(PlayerItem.getItem(i));
				}
				shopDialog.switchToItemBuy(items);
				break;
			case 3:
				for(int i : itemIDList)
				{
					if(PlayerItem.getItem(i).getCategory().equals("Field") || PlayerItem.getItem(i).getCategory().equals("TM") || PlayerItem.getItem(i).getCategory().equalsIgnoreCase("Held"))
						items.add(PlayerItem.getItem(i));
				}
				shopDialog.switchToItemBuy(items);
				break;
		}
	}

	@Override
	public void layout()
	{
		categoryButtons[0].setSize(150, 160);
		categoryButtons[0].setPosition(getInnerX(), getInnerY());
		categoryButtons[1].setSize(150, 160);
		categoryButtons[1].setPosition(getInnerX() + 151, getInnerY());
		categoryButtons[2].setSize(150, 160);
		categoryButtons[2].setPosition(getInnerX(), getInnerY() + 161);
		categoryButtons[3].setSize(150, 160);
		categoryButtons[3].setPosition(getInnerX() + 151, getInnerY() + 161);

		// TODO: Center label in button
		categoryLabels[0].setPosition(getInnerX() + 40, getInnerY() + 140);
		categoryLabels[0].setSize(GameClient.getInstance().getFontSmall().getWidth(categoryLabels[0].getText()), GameClient.getInstance().getFontSmall().getHeight(categoryLabels[0].getText()));
		categoryLabels[1].setPosition(getInnerX() + 191, getInnerY() + 140);
		categoryLabels[1].setSize(GameClient.getInstance().getFontSmall().getWidth(categoryLabels[1].getText()), GameClient.getInstance().getFontSmall().getHeight(categoryLabels[1].getText()));
		categoryLabels[2].setPosition(getInnerX() + 40, getInnerY() + 300);
		categoryLabels[2].setSize(GameClient.getInstance().getFontSmall().getWidth(categoryLabels[2].getText()), GameClient.getInstance().getFontSmall().getHeight(categoryLabels[2].getText()));
		categoryLabels[3].setPosition(getInnerX() + 191, getInnerY() + 300);
		categoryLabels[3].setSize(GameClient.getInstance().getFontSmall().getWidth(categoryLabels[3].getText()), GameClient.getInstance().getFontSmall().getHeight(categoryLabels[3].getText()));
		cancelButton.setSize(301, 50);
		cancelButton.setPosition(getInnerX(), categoryButtons[2].getInnerY() + categoryButtons[2].getHeight() + 1);
		setSize(300, 372);
	}
}