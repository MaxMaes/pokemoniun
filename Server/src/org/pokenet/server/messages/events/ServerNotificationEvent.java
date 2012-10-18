package org.pokenet.server.messages.events;

import org.pokenet.server.backend.entity.Player;
import org.pokenet.server.client.Session;
import org.pokenet.server.connections.ActiveConnections;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;

public class ServerNotificationEvent implements MessageEvent
{

	public void Parse(Session Session, ClientMessage Request, ServerMessage Message)
	{
		Player p = Session.getPlayer();
		if(p.getAdminLevel() > 1)
			for(Session session : ActiveConnections.allSessions().values())
				if(session.getPlayer() != null)
				{
					ServerMessage msg = new ServerMessage();
					msg.Init(1);
					msg.addString(Request.readString());
					session.Send(msg);
				}
	}
}
