package org.pokenet.client.ui.frames;

import org.pokenet.client.GameClient;
import org.pokenet.client.constants.ServerPacket;
import org.pokenet.client.protocol.ClientMessage;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.PopupWindow;
import de.matthiasmann.twl.Widget;

/**
 * Popup for right click on players
 * 
 * @author Myth1c
 */
public class PlayerPopupDialog extends Widget
{
	private Button m_battle, m_trade, m_addFriend, m_whisper, m_cancel;
	private Label m_name;
	private PopupWindow popup;

	/**
	 * Default Constructor
	 * 
	 * @param player
	 */
	public PlayerPopupDialog(String player, Widget root)
	{
		m_name = new Label(player);
		m_name.setPosition(0, 0);
		add(m_name);

		m_battle = new Button("Battle");
		m_battle.setSize(100, 25);
		add(m_battle);

		m_trade = new Button("Trade");
		m_trade.setSize(100, 25);
		add(m_trade);

		m_whisper = new Button("Whisper");
		m_whisper.setSize(100, 25);
		add(m_whisper);

		m_addFriend = new Button("Add Friend");
		m_addFriend.setSize(100, 25);
		add(m_addFriend);

		m_cancel = new Button("Cancel");
		m_cancel.setSize(100, 25);
		add(m_cancel);
		setVisible(true);
		// setAlwaysOnTop(true); TODO: CHAPPIE MAGIC :D

		m_battle.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				ClientMessage message = new ClientMessage(ServerPacket.REQUEST_BATTLE);
				message.addString(m_name.getText());
				GameClient.getInstance().getSession().send(message);
				destroy();
			}
		});
		m_trade.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				ClientMessage message = new ClientMessage(ServerPacket.REQUEST_TRADE);
				message.addString(m_name.getText());
				GameClient.getInstance().getSession().send(message);
				destroy();
			}
		});
		m_whisper.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				GameClient.getInstance().getHUD().getChat().addChatChannel(m_name.getText(), true);
				destroy();
			}
		});
		m_addFriend.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				ClientMessage message = new ClientMessage(ServerPacket.FRIEND_ADD);
				message.addString(m_name.getText());
				GameClient.getInstance().getSession().send(message);
				GameClient.getInstance().getHUD().getFriendsList().addFriend(m_name.getText());
				GameClient.getInstance().getHUD().getFriendsList().setFriendOnline(m_name.getText(), true);
				destroy();
			}
		});
		m_cancel.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				destroy();
			}
		});

		popup = new PopupWindow(root);
		popup.setTheme("PlayerPopup");
		popup.add(this);
		popup.adjustSize();
	}

	/**
	 * Destroys the popup
	 */
	public void destroy()
	{
		popup.destroy();
	}

	@Override
	public void layout()
	{
		m_battle.setPosition(0, m_name.getY() + m_name.getHeight() + 3);
		m_trade.setPosition(0, m_battle.getY() + 25);
		m_whisper.setPosition(0, m_trade.getY() + 25);
		m_addFriend.setPosition(0, m_whisper.getY() + 25);
		m_cancel.setPosition(0, m_addFriend.getY() + 25);
		setSize(100, 150 + m_name.computeTextHeight());
		popup.adjustSize();
	}
}