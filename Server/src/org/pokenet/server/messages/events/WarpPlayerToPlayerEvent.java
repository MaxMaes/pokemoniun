package org.pokenet.server.messages.events;

import org.pokenet.server.backend.entity.Player;
import org.pokenet.server.client.Session;
import org.pokenet.server.connections.ActiveConnections;
import org.pokenet.server.constants.ClientPacket;
import org.pokenet.server.constants.UserClasses;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;

public class WarpPlayerToPlayerEvent implements MessageEvent
{

	public void Parse(Session session, ClientMessage request, ServerMessage message)
	{
		Player mod = session.getPlayer();
		String[] players = request.readString().split(",");
		Player player1 = ActiveConnections.getPlayer(players[0]);
		Player player2 = ActiveConnections.getPlayer(players[1]);
		if(mod.getAdminLevel() >= UserClasses.SUPER_MOD)
		{
			if(player1 != null && player2 != null)
			{
				player1.setX(player2.getX());
				player1.setY(player2.getY());
				player1.setMap(player2.getMap(), null);
				message = new ServerMessage(ClientPacket.CHAT_PACKET);
				message.addInt(4);
				message.addString("Teleported player " + players[0] + " to " + players[1] + " succesfully.");
				mod.getSession().Send(message);
			}
			else
			{
				message = new ServerMessage(ClientPacket.CHAT_PACKET);
				message.addInt(4);
				message.addString("Player(s) " + players[0] + " and/or " + players[1] + " are not online or don't exist.");
				mod.getSession().Send(message);
			}
		}
	}

}
