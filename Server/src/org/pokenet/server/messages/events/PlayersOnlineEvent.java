package org.pokenet.server.messages.events;

import org.pokenet.server.backend.entity.Player;
import org.pokenet.server.client.Session;
import org.pokenet.server.connections.ActiveConnections;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;

public class PlayersOnlineEvent implements MessageEvent
{

	public void Parse(Session Session, ClientMessage Request, ServerMessage Message)
	{
		Player p = Session.getPlayer();
		ServerMessage message = new ServerMessage();
		message.Init(50);
		message.addInt(4);
		message.addString(ActiveConnections.getActiveConnections() + " player(s) online");
		p.getSession().Send(message);
	}
}
