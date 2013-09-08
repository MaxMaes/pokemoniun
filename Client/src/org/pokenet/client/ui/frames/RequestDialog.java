package org.pokenet.client.ui.frames;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.pokenet.client.GameClient;
import org.pokenet.client.backend.Translator;
import org.pokenet.client.constants.ServerPacket;
import org.pokenet.client.protocol.ClientMessage;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.Widget;

/**
 * Request dialog
 * 
 * @author ZombieBear
 */
public class RequestDialog extends ResizableFrame
{
	private List<Widget> m_Widgets = new ArrayList<Widget>();
	private Label m_noOffers = new Label("There are no offers");
	private HashMap<String, Button> m_offers = new HashMap<String, Button>();
	private List<String> m_offerUser = new ArrayList<String>();
	private boolean m_update = false;

	/**
	 * Default Constructor
	 */
	public RequestDialog()
	{
		initGUI();
	}

	/**
	 * An offer was accepted
	 * 
	 * @param userIndex
	 */
	public void acceptOffer(int userIndex)
	{
		if(m_offerUser != null && m_offerUser.size() > 0)
		{
			// GameClient.getInstance().getPacketGenerator().writeTcpMessage("15" + m_offerUser.get(userIndex));
			ClientMessage message = new ClientMessage(ServerPacket.REQUEST_ACCEPTED);
			message.addString(m_offerUser.get(userIndex));
			GameClient.getInstance().getSession().send(message);
			m_offers.remove(m_offerUser.get(userIndex));
			m_offerUser.remove(userIndex);
			m_update = true;
		}
	}

	/**
	 * Adds a request
	 * 
	 * @param username
	 * @param request
	 */
	public void addRequest(final String username, String request)
	{
		if(request.equalsIgnoreCase("trade"))
		{
			// TRADE
			if(!m_offerUser.contains(username))
			{
				m_offerUser.add(username);
				m_offers.put(username, new Button("Trade"));
				m_update = true;
			}
			GameClient.getInstance().getHUD().getChat().addSystemMessage("*" + username + " sent you a trade request.");
		}
		else if(request.equalsIgnoreCase("battle"))
		{
			if(!m_offerUser.contains(username))
			{
				m_offerUser.add(username);
				m_offers.put(username, new Button("Battle"));
				m_update = true;
			}
			GameClient.getInstance().getHUD().getChat().addSystemMessage("*" + username + " would like to battle!");
		}
	}

	/**
	 * Clears all offers
	 */
	public void clearOffers()
	{
		for(String name : m_offerUser)
		{
			// GameClient.getInstance().getPacketGenerator().writeTcpMessage("16" + name);
			ClientMessage message = new ClientMessage(ServerPacket.REQUEST_DECLINED);
			message.addString(name);
			GameClient.getInstance().getSession().send(message);
			m_offers.remove(name);
			m_offerUser.remove(name);
			m_update = true;
		}
	}

	/**
	 * And offer was declined
	 * 
	 * @param userIndex
	 */
	public void declineOffer(int userIndex)
	{
		// GameClient.getInstance().getPacketGenerator().writeTcpMessage("16" + m_offerUser.get(userIndex));
		ClientMessage message = new ClientMessage(ServerPacket.REQUEST_DECLINED);
		message.addString(m_offerUser.get(userIndex));
		GameClient.getInstance().getSession().send(message);
		m_offers.remove(m_offerUser.get(userIndex));
		m_offerUser.remove(userIndex);
		m_update = true;
	}

	/**
	 * Initializes the user interface
	 */
	public void initGUI()
	{
		setPosition(getX() - 1, getY() + 1);
		List<String> translated = Translator.translate("_GUI");
		setTitle(translated.get(33));
		setSize(getWidth(), 48 + 25);
		m_noOffers.setPosition(getX(), (10 - m_noOffers.computeTextHeight() / 2));
		add(m_noOffers);
		setResizableAxis(ResizableAxis.NONE);
	}

	/**
	 * Removes an offer
	 * 
	 * @param username
	 */
	public void removeOffer(String username)
	{
		if(m_offerUser.contains(username))
		{
			m_offers.remove(username);
			m_offerUser.remove(username);
			m_update = true;
		}
	}

	/* TODO: do.
	 * @Override
	 * public void update(GUIContext Widget, int delta)
	 * {
	 * super.update(Widget, delta);
	 * if(isVisible())
	 * if(m_update)
	 * {
	 * m_update = false;
	 * for(int i = 0; i < m_Widgets.size(); i++)
	 * {
	 * m_Widgets.get(i).removeAll();
	 * try
	 * {
	 * remove(m_Widgets.get(i));
	 * }
	 * catch(Exception e)
	 * {
	 * }
	 * }
	 * if(m_offerUser.size() == 0)
	 * {
	 * setHeight(getTitleBar().getHeight() + 25);
	 * m_Widgets.clear();
	 * add(m_noOffers);
	 * }
	 * else
	 * {
	 * int y = 0;
	 * if(containsChild(m_noOffers))
	 * remove(m_noOffers);
	 * setHeight(getTitleBar().getHeight() + 25 * m_offers.size());
	 * m_Widgets.clear();
	 * for(int i = 0; i < m_offers.size(); i++)
	 * {
	 * final int j = i;
	 * final Label m_label = new Label(m_offerUser.get(i));
	 * final Button m_offerBtn = m_offers.get(m_offerUser.get(i));
	 * final Button m_cancel = new Button("Cancel");
	 * m_cancel.setHeight(25);
	 * m_cancel.setWidth(45);
	 * m_cancel.addActionListener(new ActionListener()
	 * {
	 * @Override
	 * public void actionPerformed(ActionEvent e)
	 * {
	 * declineOffer(j);
	 * }
	 * });
	 * m_label.setFont(GameClient.getInstance().getFontSmall());
	 * m_label.setForeground(Color.white);
	 * m_label.pack();
	 * m_label.setY(10 - m_label.getTextHeight() / 2);
	 * m_offerBtn.setHeight(25);
	 * m_offerBtn.setX(getWidth() - 92);
	 * m_offerBtn.setWidth(45);
	 * m_offerBtn.addCallback(new Runnable()
	 * {
	 * @Override
	 * public void run()
	 * {
	 * acceptOffer(j);
	 * }
	 * });
	 * m_Widgets.add(new Widget());
	 * m_Widgets.get(i).setSize(getWidth(), 25);
	 * m_Widgets.get(i).setPosition(0, y);
	 * m_Widgets.get(i).add(m_label);
	 * m_Widgets.get(i).add(m_offerBtn);
	 * m_Widgets.get(i).add(m_cancel);
	 * m_cancel.setX(getWidth() - 47);
	 * add(m_Widgets.get(i));
	 * y += 25;
	 * }
	 * }
	 * }
	 * } */
}