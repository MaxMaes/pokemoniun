package org.pokenet.server.messages.events;

import org.pokenet.server.backend.entity.Player;
import org.pokenet.server.client.Session;
import org.pokenet.server.connections.ActiveConnections;
import org.pokenet.server.constants.UserClasses;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;

public class WarpPlayerToMeEvent implements MessageEvent
{

	public void Parse(Session session, ClientMessage request, ServerMessage message)
	{
		Player mod = session.getPlayer();
		if(mod.getAdminLevel() >= UserClasses.MODERATOR)
		{
			Player player = ActiveConnections.getPlayer(request.readString());
			if(player != null)
			{
				player.setX(mod.getX());
				player.setY(mod.getY());
				player.setMap(mod.getMap(), null);
			}
		}
	}

}
