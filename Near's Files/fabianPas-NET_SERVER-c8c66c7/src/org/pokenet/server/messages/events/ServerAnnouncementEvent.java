package org.pokenet.server.messages.events;

import org.pokenet.server.backend.entity.PlayerChar;
import org.pokenet.server.client.Session;
import org.pokenet.server.connections.ActiveConnections;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;


public class ServerAnnouncementEvent implements MessageEvent {

	@Override
	public void Parse(Session Session, ClientMessage Request, ServerMessage Message)
	{
		PlayerChar p = Session.getPlayer();		
		if(p.getAdminLevel() > 0) {
			for (Session session : ActiveConnections.allSessions().values()) {
				if(session.getPlayer() != null) {
					ServerMessage msg = new ServerMessage();
					msg.Init(2);
					msg.addString(Request.readString());
					session.Send(msg);
				}
			}
		}
	}

}
