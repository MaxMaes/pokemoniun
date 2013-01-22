package org.pokenet.server.messages.events;

import org.pokenet.server.GameServer;
import org.pokenet.server.backend.entity.Player;
import org.pokenet.server.client.Session;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;

public class SpriteEvent implements MessageEvent
{

	public void Parse(Session session, ClientMessage request, ServerMessage message)
	{

		Player p = session.getPlayer();
		int sprite = request.readInt();
		/* Ensure the user buys a visible sprite */
		if(sprite > 0 && !GameServer.getServiceManager().getSpriteList().getUnbuyableSprites().contains(sprite))
			if(p.getMoney() >= 500)
			{
				p.setMoney(p.getMoney() - 500);
				p.updateClientMoney();
				p.setSprite(sprite);
				p.setSpriting(false);
			}

	}
}
