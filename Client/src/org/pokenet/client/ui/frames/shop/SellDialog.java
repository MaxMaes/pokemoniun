package org.pokenet.client.ui.frames.shop;

import org.pokenet.client.GameClient;
import org.pokenet.client.backend.entity.PlayerItem;
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
		initGUI();
	}

	/**
	 * Displays the selling item gui
	 */
	public void initGUI()
	{
		loadBag();
		sellList = new ListBox<String>(sellModel);
		sellButton = new Button("Sell");
		sellButton.setCanAcceptKeyboardFocus(false);
		sellButton.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				PlayerItem selectedItem = getSelectedItem();
				try
				{
					Runnable yes = new Runnable()
					{
						@Override
						public void run()
						{
							ClientMessage message = new ClientMessage(ServerPacket.BUY_SELL_ITEMS);
							message.addInt(1);
							PlayerItem selectedItem = getSelectedItem();
							message.addInt(selectedItem.getItem().getId());
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
					GameClient.getInstance().getGUIPane()
							.showConfirmationDialog("Are you sure you want to sell " + selectedItem.getItem().getName() + " for $" + selectedItem.getItem().getPrice() / 2 + "?", yes, no);
				}
				catch(Exception e2)
				{
					e2.printStackTrace();
				}
			}
		});

		// Start the UI
		cancelButton = new Button("Cancel");
		cancelButton.setCanAcceptKeyboardFocus(false);
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
	}

	public void loadBag()
	{
		sellModel = new SimpleChangableListModel<>();
		for(int i = 0; i < GameClient.getInstance().getOurPlayer().getItems().size(); i++)
		{
			if(!GameClient.getInstance().getOurPlayer().getItems().get(i).getItem().getCategory().equals("Key"))
			{
				PlayerItem playerItem = GameClient.getInstance().getOurPlayer().getItems().get(i);
				sellModel.addElement(playerItem.getItem().getName() + "    X" + playerItem.getQuantity());
			}
		}
		if(sellList != null)
		{
			int selection = sellList.getSelected();
			sellList.setModel(sellModel);
			sellList.setSelected(selection);
		}
	}

	private PlayerItem getSelectedItem()
	{
		return GameClient.getInstance().getOurPlayer().getItems().get(sellList.getSelected());
	}

	@Override
	public void layout()
	{
		sellList.setSize(getWidth(), 300);
		sellButton.setSize(getWidth(), 35);
		sellButton.setPosition(getInnerX(), getInnerY() + 300);
		cancelButton.setSize(300, 56);
		cancelButton.setPosition(getInnerX(), getInnerY() + 335);
		setSize(300, 391);
	}
}
