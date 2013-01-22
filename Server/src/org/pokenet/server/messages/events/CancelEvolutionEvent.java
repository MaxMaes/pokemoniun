package org.pokenet.server.messages.events;

import org.pokenet.server.backend.entity.Player;
import org.pokenet.server.client.Session;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;

public class CancelEvolutionEvent implements MessageEvent
{

	public void Parse(Session session, ClientMessage request, ServerMessage message)
	{

		Player p = session.getPlayer();
		int pokemonIndex = request.readInt();
		if(p.getParty()[pokemonIndex] != null)
			p.getParty()[pokemonIndex].evolutionResponse(false, p);
	}
}
