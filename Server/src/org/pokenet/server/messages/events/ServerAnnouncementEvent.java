package org.pokenet.server.messages.events;

import org.pokenet.server.backend.entity.Player;
import org.pokenet.server.client.Session;
import org.pokenet.server.connections.ActiveConnections;
import org.pokenet.server.constants.ClientPacket;
import org.pokenet.server.constants.UserClasses;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;

public class ServerAnnouncementEvent implements MessageEvent
{

	public void Parse(Session session, ClientMessage request, ServerMessage message)
	{
		String announcement = request.readString();
		Player player = session.getPlayer();
		if(player.getAdminLevel() >= UserClasses.MODERATOR)
		{
			for(Session s : ActiveConnections.allSessions().values())
			{
				if(s.getPlayer() != null)
				{
					message.init(ClientPacket.SERVER_ANNOUNCEMENT.getValue());
					message.addString(announcement);
					s.Send(message);
				}
			}
		}
	}

}
