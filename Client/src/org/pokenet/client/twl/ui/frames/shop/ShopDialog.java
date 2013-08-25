package org.pokenet.client.twl.ui.frames.shop;

import java.util.HashMap;
import org.pokenet.client.GameClient;
import org.pokenet.client.constants.ServerPacket;
import org.pokenet.client.protocol.ClientMessage;
import de.matthiasmann.twl.Widget;

/**
 * The shop dialog
 * 
 * @author Myth1c/sadhi
 */
public class ShopDialog extends Widget
{
	private HashMap<Integer, Integer> m_stock;
	private Widget m_shopdialog;
	private Widget m_buyShopdialog;
	private Widget m_sellShopdialog;
	private Widget currentScreen;
	private int currentState;

	public final static int SHOPSTATE_MAIN = 0;
	public final static int SHOPSTATE_BUY = 1;
	public final static int SHOPSTATE_SELL = 2;

	/**
	 * Constructor
	 * 
	 * @param stock
	 */
	public ShopDialog(HashMap<Integer, Integer> stock)
	{
		m_stock = stock;
		m_shopdialog = new MainShopDialog(this);
		m_buyShopdialog = new BuyDialog(this, stock);
		m_sellShopdialog = new SellDialog(this);
		setCenter();
		switchUI(SHOPSTATE_MAIN);
	}

	public void cancelled()
	{
		ClientMessage message = new ClientMessage(ServerPacket.BUY_SELL_ITEMS);
		message.addInt(2);
		GameClient.getInstance().getSession().send(message);
		GameClient.getInstance().getGUIPane().getHUD().removeShop();
	}

	public void switchUI(int state)
	{
		removeUI(currentState);
		switch(state)
		{
			case SHOPSTATE_MAIN:
				add(m_shopdialog);
				currentScreen = m_shopdialog;
				break;
			case SHOPSTATE_BUY:
				add(m_buyShopdialog);
				currentScreen = m_buyShopdialog;
				break;
			case SHOPSTATE_SELL:
				add(m_sellShopdialog);
				currentScreen = m_sellShopdialog;
				break;
		}
		currentState = state;
	}

	private void removeUI(int state)
	{
		switch(state)
		{
			case SHOPSTATE_MAIN:
				removeChild(m_shopdialog);
				break;
			case SHOPSTATE_BUY:
				removeChild(m_buyShopdialog);
				break;
			case SHOPSTATE_SELL:
				removeChild(m_sellShopdialog);
				break;
		}
	}

	/**
	 * Centers the frame
	 */
	public void setCenter()
	{
		int height = (int) GameClient.getInstance().getHUD().getHeight();
		int width = (int) GameClient.getInstance().getHUD().getWidth();
		int x = width / 2 - getWidth() / 2;
		int y = height / 2 - getHeight() / 2;
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
