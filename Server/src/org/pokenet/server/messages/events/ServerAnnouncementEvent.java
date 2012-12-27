package org.pokenet.server.messages.events;

import org.pokenet.server.backend.entity.Player;
import org.pokenet.server.client.Session;
import org.pokenet.server.connections.ActiveConnections;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;

public class ServerAnnouncementEvent implements MessageEvent
{

	public void Parse(Session Session, ClientMessage Request, ServerMessage Message)
	{
		Player p = Session.getPlayer();
		if(p.getAdminLevel() > 0)
			for(Session session : ActiveConnections.allSessions().values())
				if(session.getPlayer() != null)
				{
					ServerMessage announceMessage = new ServerMessage();
					announceMessage.Init(2);
					announceMessage.addString(Request.readString());
					session.Send(announceMessage);
				}
	}

}
