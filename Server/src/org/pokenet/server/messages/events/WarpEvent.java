package org.pokenet.server.messages.events;

import org.pokenet.server.backend.entity.Player;
import org.pokenet.server.client.Session;
import org.pokenet.server.connections.ActiveConnections;
import org.pokenet.server.constants.ClientPacket;
import org.pokenet.server.constants.UserClasses;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;

public class WarpEvent implements MessageEvent
{

	public void Parse(Session session, ClientMessage request, ServerMessage message)
	{
		Player mod = session.getPlayer();
		String playername = request.readString();
		Player player = ActiveConnections.getPlayer(playername);
		if(mod.getAdminLevel() >= UserClasses.MODERATOR)
		{
			if(player != null)
			{
				mod.setX(player.getX());
				mod.setY(player.getY());
				mod.setMap(player.getMap(), null);
				message = new ServerMessage(ClientPacket.CHAT_PACKET);
				message.addInt(4);
				message.addString("Don't go sneaking up on people!");
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
