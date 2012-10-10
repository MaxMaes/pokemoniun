package org.pokenet.server.messages.events;

import org.pokenet.server.backend.entity.PlayerChar;
import org.pokenet.server.client.Session;
import org.pokenet.server.connections.ActiveConnections;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;


public class PlayersOnlineEvent implements MessageEvent {

	@Override
	public void Parse(Session Session, ClientMessage Request, ServerMessage Message)
	{
		PlayerChar p = Session.getPlayer();		
		if(p.getAdminLevel() > 0) {
			ServerMessage message = new ServerMessage();
			message.Init(50);
			message.addString("l" + ActiveConnections.allSessions().size() + " players online" );
			p.getSession().Send(message);
		}
	}

}
