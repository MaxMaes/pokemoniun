package org.pokenet.client.messages.events;

import org.pokenet.client.GameClient;
import org.pokenet.client.Session;
import org.pokenet.client.backend.ItemDatabase;
import org.pokenet.client.messages.MessageEvent;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.protocol.ServerMessage;

public class ShopBuyItemEvent implements MessageEvent
{

	@Override
	public void Parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		try
		{
			GameClient.getInstance().getUi().getNPCSpeech().advance();
			GameClient.getInstance().getUi().getNPCSpeech().advance();
		}
		catch(Exception e)
		{
		}
		GameClient.getInstance().getUi().talkToNPC("You bought a " + ItemDatabase.getInstance().getItem(Request.readInt()).getName());
		GameClient.getInstance().getUi().getShop().m_timer.reset();
		GameClient.getInstance().getUi().getShop().m_timer.resume();
	}
}
