package org.pokenet.client.twl.ui.frames.shop;

import org.pokenet.client.GameClient;
import org.pokenet.client.backend.ItemDatabase;
import org.pokenet.client.constants.ServerPacket;
import org.pokenet.client.protocol.ClientMessage;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.ListBox;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.model.SimpleChangableListModel;

public class SellDialog extends Widget
{
	private ShopDialog shopDialog;

	private SimpleChangableListModel<String> sellModel;
	private ListBox<String> sellList;
	private Button sellButton;
	private Button cancelButton;

	public SellDialog(ShopDialog shop)
	{
		shopDialog = shop;
	}

	/**
	 * Displays the selling item gui
	 */
	public void sellGUI()
	{
		sellModel = new SimpleChangableListModel<>();
		for(int i = 0; i < GameClient.getInstance().getOurPlayer().getItems().size(); i++)
		{
			if(!GameClient.getInstance().getOurPlayer().getItems().get(i).getItem().getCategory().equals("Key"))
				sellModel.addElement(GameClient.getInstance().getOurPlayer().getItems().get(i).getItem().getName());
		}
		sellList = new ListBox<String>(sellModel);
		sellButton = new Button("Sell");
		sellButton.setSize(getWidth(), 35);
		sellButton.setPosition(0, cancelButton.getY() - 35);
		sellButton.addCallback(new Runnable()
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
							message.addInt(ItemDatabase.getInstance().getItem(sellModel.getEntry(sellList.getSelected())).getId());
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
									"Are you sure you want to sell " + sellModel.getEntry(sellList.getSelected()) + " for $"
											+ ItemDatabase.getInstance().getItem(sellModel.getEntry(sellList.getSelected())).getPrice() / 2 + "?", yes, no);
				}
				catch(Exception e2)
				{
					e2.printStackTrace();
				}
			}
		});
		sellList.setSize(getWidth(), sellButton.getY());
		// Start the UI
		cancelButton = new Button("Cancel");
		cancelButton.setSize(300, 56);
		cancelButton.setPosition(0, 321);
		cancelButton.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				shopDialog.switchUI(ShopDialog.SHOPSTATE_MAIN);
			}
		});
		add(cancelButton);
		add(sellList);
		add(sellButton);
		// int height = (int) GameClient.getInstance().getGUI().getHeight();
		// int width = (int) GameClient.getInstance().getGUI().getWidth();
		// int x = width / 2 - 130;
		// int y = height / 2 - 238;
		// m_shopdialog.setSize(259, 475);
		// m_shopdialog.setPosition(x, y);
		// m_shopdialog.setVisible(false);
	}
}
