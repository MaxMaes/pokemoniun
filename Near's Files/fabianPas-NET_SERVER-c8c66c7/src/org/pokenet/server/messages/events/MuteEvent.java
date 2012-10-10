package org.pokenet.server.messages.events;

import org.pokenet.server.backend.entity.PlayerChar;
import org.pokenet.server.client.Session;
import org.pokenet.server.connections.ActiveConnections;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;


public class MuteEvent implements MessageEvent {

	@Override
	public void Parse(Session Session, ClientMessage Request, ServerMessage Message)
	{	
		PlayerChar o = ActiveConnections.getPlayer(Request.readString());
		if(o != null) {
			o.setMuted(true);
			ServerMessage msg = new ServerMessage();
			msg.Init(1);
			msg.addString("You have been muted.");
			o.getSession().Send(msg);
		}
	}
}
