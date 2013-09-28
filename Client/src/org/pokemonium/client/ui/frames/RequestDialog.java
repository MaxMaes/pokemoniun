package org.pokemonium.client.ui.frames;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.pokemonium.client.GameClient;
import org.pokemonium.client.backend.Translator;
import org.pokemonium.client.constants.ServerPacket;
import org.pokemonium.client.protocol.ClientMessage;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.Widget;

/**
 * Request dialog
 * 
 * @author Myth1c
 */
public class RequestDialog extends ResizableFrame
{
	private List<Widget> m_Widgets = new ArrayList<Widget>();
	private Label m_noOffers;
	private HashMap<String, Button> m_offers = new HashMap<String, Button>();
	private List<String> m_offerUser = new ArrayList<String>();

	/**
	 * Default Constructor
	 */
	public RequestDialog()
	{
		initGUI();
	}

	/**
	 * Initializes the user interface
	 */
	public void initGUI()
	{
		List<String> translated = Translator.translate("_GUI");
		setTitle(translated.get(33));
		m_noOffers = new Label("There are no offers");
		setResizableAxis(ResizableAxis.NONE);
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
			ClientMessage message = new ClientMessage(ServerPacket.REQUEST_ACCEPTED);
			message.addString(m_offerUser.get(userIndex));
			GameClient.getInstance().getSession().send(message);
			m_offers.remove(m_offerUser.get(userIndex));
			m_offerUser.remove(userIndex);
			update();
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
		boolean updated = false;
		if(request.equalsIgnoreCase("trade"))
		{
			// TRADE
			if(!m_offerUser.contains(username))
			{
				m_offerUser.add(username);
				Button trade = new Button("Trade");
				trade.setCanAcceptKeyboardFocus(false);
				m_offers.put(username, trade);
				updated = true;
			}
			GameClient.getInstance().getHUD().getChat().addSystemMessage("*" + username + " sent you a trade request.");
		}
		else if(request.equalsIgnoreCase("battle"))
		{
			if(!m_offerUser.contains(username))
			{
				m_offerUser.add(username);
				Button battle = new Button("Battle");
				battle.setCanAcceptKeyboardFocus(false);
				m_offers.put(username, battle);
				updated = true;
			}
			GameClient.getInstance().getHUD().getChat().addSystemMessage("*" + username + " would like to battle!");
		}
		if(updated)
		{
			update();
		}
	}

	/**
	 * Clears all offers
	 */
	public void clearOffers()
	{
		for(String name : m_offerUser)
		{
			ClientMessage message = new ClientMessage(ServerPacket.REQUEST_DECLINED);
			message.addString(name);
			GameClient.getInstance().getSession().send(message);
		}
		m_offers.clear();
		m_offerUser.clear();
		update();
	}

	/**
	 * And offer was declined
	 * 
	 * @param userIndex
	 */
	public void declineOffer(int userIndex)
	{
		ClientMessage message = new ClientMessage(ServerPacket.REQUEST_DECLINED);
		message.addString(m_offerUser.get(userIndex));
		GameClient.getInstance().getSession().send(message);
		m_offers.remove(m_offerUser.get(userIndex));
		m_offerUser.remove(userIndex);
		update();
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
		}
	}

	public void update()
	{
		GameClient.getInstance().getGUI().invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				for(int i = 0; i < m_Widgets.size(); i++)
				{
					m_Widgets.get(i).removeAllChildren();
					try
					{
						removeChild(m_Widgets.get(i));
					}
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
				removeChild(m_noOffers);
				if(m_offerUser.size() == 0)
				{
					m_Widgets.clear();
					add(m_noOffers);
				}
				else
				{
					m_Widgets.clear();
					for(int i = 0; i < m_offers.size(); i++)
					{
						final int j = i;
						final Label m_label = new Label(m_offerUser.get(i));
						final Button m_offerBtn = m_offers.get(m_offerUser.get(i));
						final Button m_cancel = new Button("Cancel");
						m_cancel.setSize(35, 25);
						m_cancel.setPosition(getInnerX() + getWidth() - 47, getInnerY());
						m_cancel.addCallback(new Runnable()
						{
							@Override
							public void run()
							{
								declineOffer(j);
							}
						});
						m_label.setTheme("requestlabel");
						m_label.adjustSize();
						m_label.setPosition(getInnerX(), getInnerY() + 10 - m_label.computeTextHeight() / 2);
						m_offerBtn.setSize(35, 25);
						m_offerBtn.setPosition(getInnerX() + getWidth() - 92, getInnerY());
						m_offerBtn.addCallback(new Runnable()
						{
							@Override
							public void run()
							{
								acceptOffer(j);
							}
						});
						Widget container = new Widget();
						container.add(m_label);
						container.add(m_offerBtn);
						container.add(m_cancel);
						m_Widgets.add(container);
						add(container);
					}
				}
			}
		});
	}

	@Override
	public void layout()
	{
		setSize(200, 48 + 25 * m_Widgets.size());
		setPosition(getInnerX(), getInnerY());
		m_noOffers.setPosition(getInnerX() + 5, getInnerY() + 30 + (10 - m_noOffers.computeTextHeight() / 2));
		int y = 28;
		for(Widget widget : m_Widgets)
		{
			widget.adjustSize();
			widget.setPosition(0, y);
			y += 25;
		}
	}
}