package org.pokenet.client.ui.frames;

import org.pokenet.client.GameClient;
import org.pokenet.client.backend.ItemDatabase;
import org.pokenet.client.constants.ServerPacket;
import org.pokenet.client.protocol.ClientMessage;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.TextArea;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.textarea.SimpleTextAreaModel;

/**
 * Class which displays and controls the dialog for traveling via boat.
 * 
 * @author Sadhi
 */

public class KurtDialog extends ResizableFrame
{
	private String pokeball, item;
	private int max, quantity, price;
	private TextArea dialogText;
	private SimpleTextAreaModel textModel;
	// private Button
	private Widget pane;
	private Button buy1, buy5, buy10, buyMax, cancel;

	/**
	 * @param pokeball. the pokeball you get if you pay
	 * @param item. the name of the apricorn required
	 * @param max. max represends the maximum ammount you can buy.
	 * @param price. the price of one pokeball
	 */
	public KurtDialog(String pokeball, String item, int max, int price)
	{
		setTheme("chooserDialog");
		this.pokeball = pokeball;
		this.item = item;
		this.max = max;
		this.price = price;
		quantity = 0;
		initUse();
		setTitle("Please choose how many you want to buy");
		// setVisible(false);
		setResizableAxis(ResizableAxis.NONE);
		setDraggable(true);
		// setVisible(false);
		add(pane);
	}

	private void initUse()
	{
		pane = new Widget();
		pane.setTheme("content");

		String text = "1 " + pokeball + " = $" + price + " + 1 " + item + "\nMax => " + max + " " + pokeball + "s = $" + (max * price) + " " + max + " " + item + "s.";
		textModel = new SimpleTextAreaModel(text);
		dialogText = new TextArea(textModel);
		pane.add(dialogText);

		buy1 = new Button("  1  ");
		buy1.setTheme("button");
		pane.add(buy1);
		buy5 = new Button("  5  ");
		buy5.setTheme("button");
		pane.add(buy5);
		buy10 = new Button(" 10 ");
		buy10.setTheme("button");
		pane.add(buy10);
		buyMax = new Button("Max");
		buyMax.setTheme("button");
		pane.add(buyMax);

		if(max == 1)
			buyMax.setEnabled(false);
		if(max < 5)
			buy5.setEnabled(false);
		if(max < 10)
			buy10.setEnabled(false);

		cancel = new Button("Cancel");
		cancel.setTheme("button");
		pane.add(cancel);

		cancel.addCallback(new Runnable()
		{
			public void run()
			{
				// stop shopping
				ClientMessage msg = new ClientMessage(ServerPacket.BUY_SELL_ITEMS);
				msg.addInt(2);
				GameClient.getInstance().getSession().send(msg);
				GameClient.getInstance().getHUD().removeKurtDialog();
			}
		});
		buy1.addCallback(new Runnable()
		{
			public void run()
			{
				quantity = 1;
				String note = "Are you sure you wish to buy " + quantity + " " + pokeball + "?";
				Runnable yes = new Runnable()
				{
					@Override
					public void run()
					{
						// notify that you bought the pokeballs
						GameClient.getInstance().getHUD().removeKurtDialog();
						ClientMessage message = new ClientMessage(ServerPacket.BUY_SELL_ITEMS);
						message.addInt(3);
						message.addInt(ItemDatabase.getInstance().getItem(pokeball).getId());
						message.addInt(quantity);
						GameClient.getInstance().getSession().send(message);

						// stop shopping
						ClientMessage msg = new ClientMessage(ServerPacket.BUY_SELL_ITEMS);
						msg.addInt(2);
						GameClient.getInstance().getSession().send(msg);
						GameClient.getInstance().getGUIPane().hideConfirmationDialog();
					}
				};
				Runnable no = new Runnable()
				{
					public void run()
					{
						GameClient.getInstance().getGUIPane().hideConfirmationDialog();
					}
				};
				GameClient.getInstance().getGUIPane().showConfirmationDialog(note, yes, no);
			}
		});
		buy5.addCallback(new Runnable()
		{
			public void run()
			{
				quantity = 5;
				String note = "Are you sure you wish to buy " + quantity + " " + pokeball + "s?";
				Runnable yes = new Runnable()
				{
					@Override
					public void run()
					{
						GameClient.getInstance().getHUD().removeKurtDialog();
						ClientMessage message = new ClientMessage(ServerPacket.BUY_SELL_ITEMS);
						message.addInt(3);
						message.addInt(ItemDatabase.getInstance().getItem(pokeball).getId());
						message.addInt(quantity);
						GameClient.getInstance().getSession().send(message);

						// stop shopping
						ClientMessage msg = new ClientMessage(ServerPacket.BUY_SELL_ITEMS);
						msg.addInt(2);
						GameClient.getInstance().getSession().send(msg);
						GameClient.getInstance().getGUIPane().hideConfirmationDialog();
					}
				};
				Runnable no = new Runnable()
				{
					public void run()
					{
						GameClient.getInstance().getGUIPane().hideConfirmationDialog();
					}
				};
				GameClient.getInstance().getGUIPane().showConfirmationDialog(note, yes, no);
			}
		});
		buy10.addCallback(new Runnable()
		{
			public void run()
			{
				quantity = 10;
				String note = "Are you sure you wish to buy " + quantity + " " + pokeball + "s?";
				Runnable yes = new Runnable()
				{
					@Override
					public void run()
					{
						GameClient.getInstance().getHUD().removeKurtDialog();
						ClientMessage message = new ClientMessage(ServerPacket.BUY_SELL_ITEMS);
						message.addInt(3);
						message.addInt(ItemDatabase.getInstance().getItem(pokeball).getId());
						message.addInt(quantity);
						GameClient.getInstance().getSession().send(message);

						// stop shopping
						ClientMessage msg = new ClientMessage(ServerPacket.BUY_SELL_ITEMS);
						msg.addInt(2);
						GameClient.getInstance().getSession().send(msg);
						GameClient.getInstance().getGUIPane().hideConfirmationDialog();
					}
				};
				Runnable no = new Runnable()
				{
					public void run()
					{
						GameClient.getInstance().getGUIPane().hideConfirmationDialog();
					}
				};
				GameClient.getInstance().getGUIPane().showConfirmationDialog(note, yes, no);
			}
		});
		buyMax.addCallback(new Runnable()
		{
			public void run()
			{
				quantity = max;
				String note = "Are you sure you wish to buy " + quantity + " " + pokeball + "s?";
				Runnable yes = new Runnable()
				{
					@Override
					public void run()
					{
						GameClient.getInstance().getHUD().removeKurtDialog();
						ClientMessage message = new ClientMessage(ServerPacket.BUY_SELL_ITEMS);
						message.addInt(3);
						message.addInt(ItemDatabase.getInstance().getItem(pokeball).getId());
						message.addInt(quantity);
						GameClient.getInstance().getSession().send(message);

						// stop shopping
						ClientMessage msg = new ClientMessage(ServerPacket.BUY_SELL_ITEMS);
						msg.addInt(2);
						GameClient.getInstance().getSession().send(msg);
						GameClient.getInstance().getGUIPane().hideConfirmationDialog();
					}
				};
				Runnable no = new Runnable()
				{
					public void run()
					{
						GameClient.getInstance().getGUIPane().hideConfirmationDialog();
					}
				};
				GameClient.getInstance().getGUIPane().showConfirmationDialog(note, yes, no);
			}
		});
		layout();
	}

	@Override
	public void layout()
	{
		setSize(250, 134);
		setPosition(300, 150);

		pane.setSize(250, 130);
		pane.setPosition(getInnerX() + 2, getInnerY());

		dialogText.setMaxSize(250, 130);
		dialogText.adjustSize();
		dialogText.setPosition((getInnerX()), getInnerY() + 25);

		buy1.setSize(50, 20);
		buy1.setPosition(getInnerX() + 15, getInnerY() + 70);

		buy5.setSize(50, 20);
		buy5.setPosition(getInnerX() + 80, getInnerY() + 70);

		buy10.setSize(50, 20);
		buy10.setPosition(getInnerX() + 145, getInnerY() + 70);

		buyMax.setSize(70, 20);
		buyMax.setPosition(getInnerX() + 25, getInnerY() + 100);

		cancel.setSize(70, 20);
		cancel.setPosition(getInnerX() + 150, getInnerY() + 100);
	}
}
