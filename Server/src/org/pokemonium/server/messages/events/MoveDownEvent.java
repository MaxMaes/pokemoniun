package org.pokemonium.server.messages.events;

import org.pokemonium.server.backend.entity.Positionable.Direction;
import org.pokemonium.server.client.Session;
import org.pokemonium.server.messages.MessageEvent;
import org.pokemonium.server.protocol.ClientMessage;
import org.pokemonium.server.protocol.ServerMessage;

public class MoveDownEvent implements MessageEvent
{

	public void Parse(Session session, ClientMessage request, ServerMessage message)
	{
		session.getPlayer().move(Direction.Down);
	}

}
