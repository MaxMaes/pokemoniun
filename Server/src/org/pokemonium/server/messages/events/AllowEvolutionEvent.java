package org.pokemonium.server.messages.events;

import org.pokemonium.server.backend.entity.Player;
import org.pokemonium.server.client.Session;
import org.pokemonium.server.messages.MessageEvent;
import org.pokemonium.server.protocol.ClientMessage;
import org.pokemonium.server.protocol.ServerMessage;

public class AllowEvolutionEvent implements MessageEvent
{

	public void Parse(Session session, ClientMessage request, ServerMessage message)
	{

		Player p = session.getPlayer();
		int pokemonIndex = request.readInt();
		if(p.getParty()[pokemonIndex] != null)
			p.getParty()[pokemonIndex].evolutionResponse(true, p);
	}
}
