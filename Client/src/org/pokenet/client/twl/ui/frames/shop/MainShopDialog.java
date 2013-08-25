package org.pokenet.client.twl.ui.frames.shop;

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
		setTheme("shopdialog");
		gotoBuyButton = new Button("Buy");
		gotoBuyButton.setPosition(0, 0);
		gotoBuyButton.setSize(150, 320);
		gotoBuyButton.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				if(m_buyCancel == null)
				{
					state = STATE_BUY;
					removeMain();
					buyGUI();
				}
				else
				{
					state = STATE_BUY;
					removeMain();
					switchUI();
				}
			}
		});
		add(gotoBuyButton);
		gotoSellButton = new Button("Sell");
		gotoSellButton.setPosition(151, 0);
		gotoSellButton.setSize(150, 320);
		gotoSellButton.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				if(m_sellCancel == null)
				{
					state = STATE_SELL;
					removeMain();
					sellGUI();
				}
				else
				{
					state = STATE_SELL;
					removeMain();
					switchUI();
				}
				// sellGUI();
			}
		});
		add(gotoSellButton);
		cancelButton = new Button("Cancel");
		cancelButton.setSize(300, 56);
		cancelButton.setPosition(0, 321);
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
}
