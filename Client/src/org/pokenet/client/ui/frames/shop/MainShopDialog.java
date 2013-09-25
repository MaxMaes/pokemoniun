package org.pokenet.client.ui.frames.shop;

import org.pokenet.client.GameClient;
import org.pokenet.client.constants.ServerPacket;
import org.pokenet.client.protocol.ClientMessage;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.Widget;

public class MainShopDialog extends Widget
{
	private Button gotoBuyButton;
	private Button gotoSellButton;
	private Button cancelButton;
	private ShopDialog shopDialog;

	public MainShopDialog(ShopDialog shop)
	{
		shopDialog = shop;
		initGUI();
	}

	/**
	 * Initialises the gui when first opened
	 */
	public void initGUI()
	{
		setTheme("mainscreen");
		gotoBuyButton = new Button("Buy");
		gotoBuyButton.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				shopDialog.switchUI(ShopDialog.SHOPSTATE_BUY);
			}
		});
		add(gotoBuyButton);

		gotoSellButton = new Button("Sell");
		gotoSellButton.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				shopDialog.switchUI(ShopDialog.SHOPSTATE_SELL);
			}
		});
		add(gotoSellButton);

		cancelButton = new Button("Cancel");
		cancelButton.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				cancelled();
			}
		});
		add(cancelButton);
		setVisible(true);
	}

	public void cancelled()
	{
		ClientMessage message = new ClientMessage(ServerPacket.BUY_SELL_ITEMS);
		message.addInt(2);
		GameClient.getInstance().getSession().send(message);
		GameClient.getInstance().getGUIPane().getHUD().removeShop();
	}

	@Override
	public void layout()
	{
		gotoBuyButton.setPosition(getInnerX(), getInnerY());
		gotoBuyButton.setSize(150, 320);
		gotoSellButton.setPosition(getInnerX() + 151, getInnerY());
		gotoSellButton.setSize(150, 320);
		cancelButton.setSize(300, 56);
		cancelButton.setPosition(getInnerX(), getInnerY() + 321);

		setSize(300, 376);
	}
}
