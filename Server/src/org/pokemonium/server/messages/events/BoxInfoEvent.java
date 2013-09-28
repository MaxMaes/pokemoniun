package org.pokemonium.server.messages.events;

import org.pokemonium.server.backend.entity.Player;
import org.pokemonium.server.client.Session;
import org.pokemonium.server.messages.MessageEvent;
import org.pokemonium.server.protocol.ClientMessage;
import org.pokemonium.server.protocol.ServerMessage;

public class BoxInfoEvent implements MessageEvent
{

	public void Parse(Session session, ClientMessage request, ServerMessage message)
	{

		Player p = session.getPlayer();
		if(p.isBoxing())
		{
			int boxNum = request.readInt();
			if(boxNum >= 0 && boxNum < 9)
				p.sendBoxInfo(boxNum);
		}
	}
}
