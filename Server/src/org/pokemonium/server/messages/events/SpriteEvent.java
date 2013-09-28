package org.pokemonium.server.messages.events;

import org.pokemonium.server.GameServer;
import org.pokemonium.server.backend.entity.Player;
import org.pokemonium.server.client.Session;
import org.pokemonium.server.messages.MessageEvent;
import org.pokemonium.server.protocol.ClientMessage;
import org.pokemonium.server.protocol.ServerMessage;

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
