package org.pokemonium.server.messages.events;

import org.pokemonium.server.backend.entity.Player;
import org.pokemonium.server.client.Session;
import org.pokemonium.server.constants.ClientPacket;
import org.pokemonium.server.messages.MessageEvent;
import org.pokemonium.server.protocol.ClientMessage;
import org.pokemonium.server.protocol.ServerMessage;

public class DropItemEvent implements MessageEvent
{

	public void Parse(Session session, ClientMessage request, ServerMessage message)
	{
		Player p = session.getPlayer();
		int item = request.readInt();
		if(p.getBag().removeItem(item, 1))
		{
			message.init(ClientPacket.REMOVE_ITEM_BAG.getValue());
			message.addInt(item);
			message.addInt(1);
			session.Send(message);
		}
	}
}
