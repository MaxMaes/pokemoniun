package org.pokenet.server.messages.events;

import org.pokenet.server.backend.entity.PlayerChar;
import org.pokenet.server.client.Session;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;


public class UnableLearnMoveEvent implements MessageEvent {

	@Override
	public void Parse(Session Session, ClientMessage Request, ServerMessage Message)
	{
		//Player is allowing move to be learned
		PlayerChar p = Session.getPlayer();		
		int pokemonIndex = Request.readInt();
		String move = Request.readString();
		
		if(p.getParty()[pokemonIndex] != null) {
			if(p.getParty()[pokemonIndex].getMovesLearning().contains(move)) {
				p.getParty()[pokemonIndex].getMovesLearning().remove(move);
			}
		}
	}

}
