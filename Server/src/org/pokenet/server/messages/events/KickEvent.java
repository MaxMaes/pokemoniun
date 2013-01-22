package org.pokenet.server.messages.events;

import org.pokenet.server.backend.entity.Player;
import org.pokenet.server.client.Session;
import org.pokenet.server.connections.ActiveConnections;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;

public class KickEvent implements MessageEvent
{

	public void Parse(Session session, ClientMessage request, ServerMessage message)
	{
		Player player = ActiveConnections.getPlayer(request.readString());
		if(player != null)
		{
			ServerMessage kickMessage = new ServerMessage();
			kickMessage.init(1);
			kickMessage.addString("You have been kicked from the server!");
			player.getSession().Send(kickMessage);
			player.getSession().close();
		}
	}
}
