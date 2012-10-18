package org.pokenet.server.messages.events;

import org.pokenet.server.backend.entity.Player;
import org.pokenet.server.client.Session;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;

public class CanLearnMoveEvent implements MessageEvent
{

	public void Parse(Session Session, ClientMessage Request, ServerMessage Message)
	{
		// Player is allowing move to be learned
		Player p = Session.getPlayer();
		int pokemonIndex = Request.readInt();
		int moveIndex = Request.readInt();
		String move = Request.readString();

		if(move != null && !move.equalsIgnoreCase("") && p.getParty()[pokemonIndex] != null)
			if(p.getParty()[pokemonIndex].getMovesLearning().contains(move))
			{
				p.getParty()[pokemonIndex].learnMove(moveIndex, move);
				p.updateClientPP(pokemonIndex, moveIndex);
			}
	}

}
