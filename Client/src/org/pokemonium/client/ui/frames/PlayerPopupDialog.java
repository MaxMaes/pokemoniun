package org.pokemonium.client.ui.frames;

import org.pokemonium.client.GameClient;
import org.pokemonium.client.constants.ServerPacket;
import org.pokemonium.client.protocol.ClientMessage;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.PopupWindow;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.Widget;

/**
 * Popup for right click on players
 * 
 * @author Myth1c
 */
public class PlayerPopupDialog extends ResizableFrame
{
	private Button m_battle, m_trade, m_addFriend, m_whisper, m_cancel;
	private Label m_name;
	private PopupWindow popup;

	/**
	 * Default Constructor
	 * 
	 * @param player
	 */
	public PlayerPopupDialog(Widget root)
	{
		m_name = new Label("");
		add(m_name);

		m_battle = new Button("Battle");
		m_battle.setCanAcceptKeyboardFocus(false);
		m_battle.setSize(75, 25);
		add(m_battle);

		m_trade = new Button("Trade");
		m_trade.setCanAcceptKeyboardFocus(false);
		m_trade.setSize(75, 25);
		add(m_trade);

		m_whisper = new Button("Whisper");
		m_whisper.setCanAcceptKeyboardFocus(false);
		m_whisper.setSize(75, 25);
		add(m_whisper);

		m_addFriend = new Button("Add Friend");
		m_addFriend.setCanAcceptKeyboardFocus(false);
		m_addFriend.setSize(75, 25);
		add(m_addFriend);

		m_cancel = new Button("Cancel");
		m_cancel.setCanAcceptKeyboardFocus(false);
		m_cancel.setSize(75, 25);
		add(m_cancel);
		setVisible(true);

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

		setTheme("playerpopupdialog");
		popup = new PopupWindow(root);
		popup.setTheme("PlayerPopup");
		popup.add(this);
		popup.adjustSize();
	}

	public void showPopupAt(int x, int y)
	{
		popup.setPosition(x, y);
		popup.openPopup();
	}

	/**
	 * Destroys the popup
	 */
	public void destroy()
	{
		popup.closePopup();
	}

	@Override
	public void layout()
	{
		m_name.adjustSize();
		m_name.setPosition(getX(), getY());
		m_battle.setPosition(getX(), m_name.getY() + m_name.getHeight());
		m_trade.setPosition(getX(), m_battle.getY() + m_battle.getHeight());
		m_whisper.setPosition(getX(), m_trade.getY() + m_trade.getHeight());
		m_addFriend.setPosition(getX(), m_whisper.getY() + m_whisper.getHeight());
		m_cancel.setPosition(getX(), m_addFriend.getY() + m_addFriend.getHeight());
		setSize(86, 128 + m_name.computeTextHeight());
		popup.adjustSize();
	}

	public void setPlayerName(String player)
	{
		m_name.setText(player);
	}
}