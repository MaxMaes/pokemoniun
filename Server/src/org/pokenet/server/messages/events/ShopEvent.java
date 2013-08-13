package org.pokenet.server.messages.events;

import org.pokenet.server.backend.entity.Player;
import org.pokenet.server.client.Session;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;

public class ShopEvent implements MessageEvent
{

	public void Parse(Session session, ClientMessage request, ServerMessage message)
	{
		Player p = session.getPlayer();
		int i = request.readInt();
		int item;
		switch(i)
		{
			case 0:
				item = request.readInt();
				p.buyItem(item, 1);
				break;
			case 1:
				item = request.readInt();
				p.sellItem(item, 1);
				break;
			case 2:
				p.setShopping(false);
				break;
			default:
				p.setShopping(false);
				break;
		}
	}
}
