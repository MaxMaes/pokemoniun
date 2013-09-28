package org.pokemonium.client.ui.frames.shop;

import java.util.ArrayList;
import java.util.HashMap;
import org.pokemonium.client.GameClient;
import org.pokemonium.client.backend.entity.Item;
import de.matthiasmann.twl.Widget;

/**
 * The shop dialog
 * 
 * @author Myth1c/sadhi
 */
public class ShopDialog extends Widget
{
	private HashMap<Integer, Integer> stock;
	private MainShopDialog m_shopdialog;
	private BuyDialog m_buyShopdialog;
	private SellDialog m_sellShopdialog;
	private BuyCategoryDialog buycategory;
	private Widget currentScreen;
	private int currentState;

	public final static int SHOPSTATE_NONE = -1;
	public final static int SHOPSTATE_MAIN = 0;
	public final static int SHOPSTATE_BUY = 1;
	public final static int SHOPSTATE_SELL = 2;
	public final static int BUYSTATE_ITEM = 3;

	/**
	 * Constructor
	 * 
	 * @param stock
	 */
	public ShopDialog(HashMap<Integer, Integer> stockMap)
	{
		stock = stockMap;
		m_shopdialog = new MainShopDialog(this);
		m_shopdialog.validateLayout();
		m_sellShopdialog = new SellDialog(this);
		m_sellShopdialog.validateLayout();
		buycategory = new BuyCategoryDialog(this, stock);
		buycategory.validateLayout();
		switchUI(SHOPSTATE_MAIN);
	}

	/**
	 * Switches the shop to the given state.
	 * 
	 * @param state DO NOT PASS BUYSTATE_ITEM. This value should only be passed from the switchToItemBuy function because it reinitialises that screen.
	 */
	public void switchUI(int state)
	{
		if(currentState != SHOPSTATE_NONE)
		{
			removeUI(currentState);
		}
		switch(state)
		{
			case SHOPSTATE_MAIN:
				add(m_shopdialog);
				currentScreen = m_shopdialog;
				break;
			case SHOPSTATE_BUY:
				add(buycategory);
				currentScreen = buycategory;
				break;
			case SHOPSTATE_SELL:
				add(m_sellShopdialog);
				currentScreen = m_sellShopdialog;
				break;
			case BUYSTATE_ITEM:
				add(m_buyShopdialog);
				currentScreen = m_buyShopdialog;
		}
		currentState = state;
		currentScreen.validateLayout();
	}

	public void refreshSell()
	{
		if(currentScreen == m_sellShopdialog)
		{
			m_sellShopdialog.loadBag();
		}
	}

	public void switchToItemBuy(ArrayList<Item> items)
	{
		removeUI(currentState);
		m_buyShopdialog = new BuyDialog(this, stock, items);
		switchUI(BUYSTATE_ITEM);
	}

	private void removeUI(int state)
	{
		switch(state)
		{
			case SHOPSTATE_MAIN:
				removeChild(m_shopdialog);
				break;
			case SHOPSTATE_BUY:
				removeChild(buycategory);
				break;
			case SHOPSTATE_SELL:
				removeChild(m_sellShopdialog);
				break;
			case BUYSTATE_ITEM:
				removeChild(m_buyShopdialog);
		}
	}

	/**
	 * Centers the frame
	 */
	public void setCenter()
	{
		int screenHeight = (int) GameClient.getInstance().getHUD().getHeight();
		int screenWidth = (int) GameClient.getInstance().getHUD().getWidth();
		int ourWidth = getWidth();
		int ourHeight = getHeight();
		int x = screenWidth / 2 - ourWidth / 2;
		int y = screenHeight / 2 - ourHeight / 2;
		setPosition(x, y);
	}

	@Override
	public void layout()
	{
		currentScreen.setPosition(getInnerX(), getInnerY());
		setSize(currentScreen.getWidth(), currentScreen.getHeight());
		setCenter();
	}
}
