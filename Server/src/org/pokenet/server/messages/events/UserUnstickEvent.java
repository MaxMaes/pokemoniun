package org.pokenet.server.messages.events;

import org.pokenet.server.GameServer;
import org.pokenet.server.backend.entity.Player;
import org.pokenet.server.client.Session;
import org.pokenet.server.connections.ActiveConnections;
import org.pokenet.server.constants.UserClasses;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;

public class UserUnstickEvent implements MessageEvent
{

	public void Parse(Session session, ClientMessage request, ServerMessage message)
	{
		Player mod = session.getPlayer();
		Player player = ActiveConnections.getPlayer(request.readString());
		if(mod.getAdminLevel() >= UserClasses.MODERATOR)
		{
			if(player != null)
			{
				player.setX(player.getHealX());
				player.setY(player.getHealY());
				player.setMap(GameServer.getServiceManager().getMovementService().getMapMatrix().getMapByGamePosition(player.getHealMapX(), player.getHealMapY()), null);
			}
		}
	}
}
