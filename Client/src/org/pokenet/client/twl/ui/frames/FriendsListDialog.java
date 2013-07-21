package org.pokenet.client.twl.ui.frames;

import java.util.ArrayList;
import java.util.List;
import org.pokenet.client.GameClient;
import org.pokenet.client.constants.ServerPacket;
import org.pokenet.client.protocol.ClientMessage;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.PopupWindow;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.Widget;

/**
 * Friends List
 * 
 * @author Myth1c
 */
public class FriendsListDialog extends ResizableFrame
{
	private List<String> m_friends = new ArrayList<String>();
	private int m_index;
	private List<String> m_online = new ArrayList<String>();
	private PopUp m_popup;
	private Label[] m_shownFriends = new Label[10];
	private Button m_up, m_down;

	/** Default Constructor */
	public FriendsListDialog()
	{
		m_index = 0;
		initGUI();
	}

	/**
	 * Adds a friend from the list.
	 * 
	 * @param friend The friend to add to the list.
	 */
	public void addFriend(String friend)
	{
		boolean knownFriend = false;
		if(m_friends.contains(friend))
			knownFriend = true;
		if(!knownFriend)
			m_friends.add(friend);
		else
			System.out.println("This friend is already present in your Friendlist!");
		scroll(0);
	}

	/** Initializes the interface */
	public void initGUI()
	{
		setTitle("Friends");
		setSize(170, 180);
		m_up = new Button();
		m_up.setTheme("upbutton");
		m_up.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				scroll(-1);
			}
		});
		m_up.setEnabled(false);

		add(m_up);
		m_down = new Button();
		m_down.setTheme("downbutton");
		if(m_friends.size() <= 10)
			m_down.setEnabled(false);
		m_down.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				scroll(1);
			}
		});

		add(m_down);
		scroll(0);
		setResizableAxis(ResizableAxis.NONE);
	}

	/**
	 * Removes a friend from the list.
	 * 
	 * @param friend The friend to remove from the list.
	 */
	public void removeFriend(String friend)
	{
		for(int i = 0; i < m_friends.size(); i++)
			if(m_friends.get(i).equals(friend))
				m_friends.remove(friend);
		scroll(0);
	}

	/**
	 * Draws the shown friends based on the current scrolling index.
	 * 
	 * @param index The scrolling index.
	 */
	public void scroll(int index)
	{
		int y = -15;
		m_index += index;
		if(m_index == 0)
			m_up.setEnabled(false);
		else
			m_up.setEnabled(true);
		if(m_index + 10 >= m_friends.size())
			m_down.setEnabled(false);
		else
			m_down.setEnabled(true);
		for(int i = 0; i < m_shownFriends.length; i++)
		{
			final int j = i;
			if(m_shownFriends[i] != null)
			{
				removeChild(m_shownFriends[i]);
				m_shownFriends[i] = null;
			}
			try
			{
				m_shownFriends[i] = new Label(m_friends.get(i + m_index));
			}
			catch(Exception e)
			{
				m_shownFriends[i] = new Label();
			}
			if(m_online.contains(m_shownFriends[i].getText()))
				// m_shownFriends[i].setForeground(Color.white); //TODO:
				// else
				// m_shownFriends[i].setForeground(Color.gray); //TODO:
				/* m_shownFriends[i].addMouseListener(new MouseAdapter()
				 * {
				 * @Override
				 * public void mouseEntered(MouseEvent e)
				 * {
				 * super.mouseEntered(e);
				 * m_shownFriends[j].setForeground(new Color(255, 215, 0));
				 * }
				 * @Override
				 * public void mouseExited(MouseEvent e)
				 * {
				 * super.mouseExited(e);
				 * if(m_online.contains(m_shownFriends[j].getText()))
				 * m_shownFriends[j].setForeground(Color.white);
				 * else
				 * m_shownFriends[j].setForeground(Color.gray);
				 * }
				 * @Override
				 * public void mouseReleased(MouseEvent e)
				 * {
				 * if(e.getButton() == 1)
				 * {
				 * if(GameClient.getInstance().getDisplay().containsChild(m_popup))
				 * GameClient.getInstance().getDisplay().remove(m_popup);
				 * m_popup = new PopUp(m_shownFriends[j].getText(), m_online.contains(m_shownFriends[j].getText()));
				 * m_popup.setPosition(e.getAbsoluteX(), e.getAbsoluteY() - 10);
				 * GameClient.getInstance().getDisplay().add(m_popup);
				 * }
				 * }
				 * }); */// TODO:
				add(m_shownFriends[i]);
			y += 15;
			m_shownFriends[i].setPosition(5, y);
		}
	}

	/**
	 * Sets the online/offline status.
	 * 
	 * @param friend The username of the friend.
	 * @param isOnline True if the friend is online, otherwise false.
	 */
	public void setFriendOnline(String friend, boolean online)
	{
		if(online)
		{
			if(!m_online.contains(friend))
				m_online.add(friend);
		}
		else if(m_online.contains(friend))
			m_online.remove(friend);
		scroll(0);
	}

	@Override
	public void layout()
	{
		m_up.setSize(15, 15);
		m_up.setPosition(getWidth() - 15, 0);
		m_down.setSize(15, 15);
		m_down.setPosition(getWidth() - 15, getHeight() - 15 - 48);
	}
}

/**
 * PopUp for right Click.
 * 
 * @author ZombieBear
 */
class PopUp extends Widget
{
	private Label m_name;
	private Button m_remove, m_whisper, m_cancel;
	private PopupWindow popup;

	/**
	 * Default Constructor
	 * 
	 * @param friend The username of the friend.
	 * @param online True if the friend is online, otherwise false.
	 */
	public PopUp(String friend, boolean online,Widget root)
	{
		setPosition(getX() - 1, getY() + 1);
		m_name = new Label(friend);
		/* if(online) m_name.setForeground(Color.white); else m_name.setForeground(Color.grey); */
		m_name.setPosition(0, 0);
		add(m_name);
		m_remove = new Button("Remove");
		m_remove.setSize(100, 25);
		m_remove.setPosition(0, m_name.getY() + m_name.computeTextHeight() + 3);
		add(m_remove);
		m_whisper = new Button("Whisper");
		m_whisper.setSize(100, 25);
		m_whisper.setPosition(0, m_remove.getY() + m_remove.getHeight());
		m_whisper.setEnabled(online);
		add(m_whisper);
		m_cancel = new Button("Cancel");
		m_cancel.setSize(100, 25);
		m_cancel.setPosition(0, m_whisper.getY() + m_cancel.getHeight());
		add(m_cancel);
		setSize(100, 103 + m_name.computeTextHeight());
		setVisible(true);
		popup = new PopupWindow(root);
		popup.setTheme("FriendsListPopup");
		popup.add(this);
		popup.setCloseOnClickedOutside(true);
		popup.setCloseOnEscape(true);
		
		m_remove.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				destroy();
				Runnable yes = new Runnable()
				{
					@Override
					public void run()
					{
						// GameClient.getInstance().getPacketGenerator().writeTcpMessage("30" + m_name.getText());
						ClientMessage message = new ClientMessage(ServerPacket.FRIEND_REMOVE);
						message.addString(m_name.getText());
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
				GameClient.getInstance().getGUIPane().showConfirmationDialog("Are you sure you want to remove " + m_name.getText() + " from your friends?", yes, no);

			}
		});
		m_whisper.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				GameClient.getInstance().getGUIPane().getHUD().getChat().addChat(m_name.getText(), true);
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
	}

	/** Destroys the popup */
	public void destroy()
	{
		// GameClient.getInstance().getDisplay().remove(this); TODO:
	}
}