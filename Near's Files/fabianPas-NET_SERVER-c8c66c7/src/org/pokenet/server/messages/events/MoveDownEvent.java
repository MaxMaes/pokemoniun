package org.pokenet.server.messages.events;

import org.pokenet.server.backend.entity.Positionable.Direction;
import org.pokenet.server.client.Session;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;


public class MoveDownEvent implements MessageEvent {

	@Override
	public void Parse(Session Session, ClientMessage Request, ServerMessage Message)
	{
		Session.getPlayer().move(Direction.Down);
	}

}
