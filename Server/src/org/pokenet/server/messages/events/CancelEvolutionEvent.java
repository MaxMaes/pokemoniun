package org.pokenet.server.messages.events;

import org.pokenet.server.backend.entity.Player;
import org.pokenet.server.client.Session;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;

public class CancelEvolutionEvent implements MessageEvent
{

	public void Parse(Session Session, ClientMessage Request, ServerMessage Message)
	{

		Player p = Session.getPlayer();
		int pokemonIndex = Request.readInt();
		if(p.getParty()[pokemonIndex] != null)
			p.getParty()[pokemonIndex].evolutionResponse(false, p);
	}
}
