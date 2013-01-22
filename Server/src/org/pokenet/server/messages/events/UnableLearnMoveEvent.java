package org.pokenet.server.messages.events;

import org.pokenet.server.backend.entity.Player;
import org.pokenet.server.client.Session;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;

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
