package org.pokenet.server.messages.events;

import org.pokenet.server.backend.entity.Player;
import org.pokenet.server.client.Session;
import org.pokenet.server.connections.ActiveConnections;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;

public class MuteEvent implements MessageEvent
{

	public void Parse(Session session, ClientMessage request, ServerMessage message)
	{
		Player player = ActiveConnections.getPlayer(request.readString());
		if(player != null)
		{
			player.setMuted(true);
			ServerMessage muteMessage = new ServerMessage();
			muteMessage.Init(1);
			muteMessage.addString("You have been muted.");
			player.getSession().Send(muteMessage);
		}
	}
}
