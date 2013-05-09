package org.pokenet.server.messages.events;

import org.pokenet.server.GameServer;
import org.pokenet.server.backend.entity.Player;
import org.pokenet.server.client.Session;
import org.pokenet.server.connections.ActiveConnections;
import org.pokenet.server.constants.ClientPacket;
import org.pokenet.server.constants.UserClasses;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;

public class UserUnstickEvent implements MessageEvent
{

	public void Parse(Session session, ClientMessage request, ServerMessage message)
	{
		Player mod = session.getPlayer();
		String playername = request.readString();
		Player player = ActiveConnections.getPlayer(playername);
		if(mod.getAdminLevel() >= UserClasses.SUPER_MOD)
		{
			if(player != null)
			{
				player.setX(player.getHealX());
				player.setY(player.getHealY());
				player.setMap(GameServer.getServiceManager().getMovementService().getMapMatrix().getMapByGamePosition(player.getHealMapX(), player.getHealMapY()), null);
				message = new ServerMessage(ClientPacket.CHAT_PACKET);
				message.addInt(4);
				message.addString("Player " + playername + " has been teleported to his last heal location.");
				mod.getSession().Send(message);
			}
			else
			{
				message = new ServerMessage(ClientPacket.CHAT_PACKET);
				message.addInt(4);
				message.addString("Player " + playername + " is not online or does not exist.");
				mod.getSession().Send(message);
			}
		}
	}
}
