package org.pokenet.client.messages.events;

import org.pokenet.client.GameClient;
import org.pokenet.client.Session;
import org.pokenet.client.backend.ItemDatabase;
import org.pokenet.client.messages.MessageEvent;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.protocol.ServerMessage;

public class ShopSellItemEvent implements MessageEvent
{

	@Override
	public void parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		try
		{
			GameClient.getInstance().getHUD().getNPCSpeech().advance();
			GameClient.getInstance().getHUD().getNPCSpeech().advance();
		}
		catch(Exception e)
		{
		}
		GameClient.getInstance().getHUD().talkToNPC("You sold a " + ItemDatabase.getInstance().getItem(Request.readInt()).getName());
		GameClient.getInstance().getHUD().getShop().m_timer.reset();
		GameClient.getInstance().getHUD().getShop().m_timer.resume();
	}
}
