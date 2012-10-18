package org.pokenet.server.messages.events;

import org.pokenet.server.backend.entity.Player;
import org.pokenet.server.client.Session;
import org.pokenet.server.connections.ActiveConnections;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;

public class MuteEvent implements MessageEvent
{

	public void Parse(Session Session, ClientMessage Request, ServerMessage Message)
	{
		Player o = ActiveConnections.getPlayer(Request.readString());
		if(o != null)
		{
			o.setMuted(true);
			ServerMessage msg = new ServerMessage();
			msg.Init(1);
			msg.addString("You have been muted.");
			o.getSession().Send(msg);
		}
	}
}
