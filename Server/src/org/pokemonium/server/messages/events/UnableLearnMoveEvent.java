package org.pokemonium.server.messages.events;

import org.pokemonium.server.backend.entity.Player;
import org.pokemonium.server.client.Session;
import org.pokemonium.server.messages.MessageEvent;
import org.pokemonium.server.protocol.ClientMessage;
import org.pokemonium.server.protocol.ServerMessage;

public class UnableLearnMoveEvent implements MessageEvent
{

	public void Parse(Session session, ClientMessage request, ServerMessage message)
	{
		// Player is allowing move to be learned
		Player p = session.getPlayer();
		int pokemonIndex = request.readInt();
		String move = request.readString();

		if(p.getParty()[pokemonIndex] != null)
			if(p.getParty()[pokemonIndex].getMovesLearning().contains(move))
				p.getParty()[pokemonIndex].getMovesLearning().remove(move);
	}

}
