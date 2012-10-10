package org.pokenet.server.messages.events;

import org.pokenet.server.backend.entity.PlayerChar;
import org.pokenet.server.client.Session;
import org.pokenet.server.connections.ActiveConnections;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;


public class AddFriendEvent implements MessageEvent {

	@Override
	public void Parse(Session Session, ClientMessage Request, ServerMessage Message)
	{
		PlayerChar p = Session.getPlayer();		
		String friend = Request.readString();
		if(ActiveConnections.getPlayer(friend) != null) {
			p.addFriend(friend);
		}
	}
}
