package org.pokenet.server.messages.events;

import org.pokenet.server.backend.entity.PlayerChar;
import org.pokenet.server.client.Session;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;


public class AllowEvolutionEvent implements MessageEvent {

	@Override
	public void Parse(Session Session, ClientMessage Request, ServerMessage Message)
	{

		PlayerChar p = Session.getPlayer();		
		int pokemonIndex = Request.readInt();
		if(p.getParty()[pokemonIndex] != null) {
			p.getParty()[pokemonIndex].evolutionResponse(true, p);
		}
	}
}
