package org.pokenet.server.messages.events;

import org.pokenet.server.backend.entity.PlayerChar;
import org.pokenet.server.client.Session;
import org.pokenet.server.connections.ActiveConnections;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;


public class KickEvent implements MessageEvent {

	@Override
	public void Parse(Session Session, ClientMessage Request, ServerMessage Message)
	{	
		//TODO maybe do session player rank check?	
		PlayerChar o = ActiveConnections.getPlayer(Request.readString());
		if(o != null) {
			ServerMessage msg = new ServerMessage();
			msg.Init(1);
			msg.addString("You have been unmuted.");
			o.getSession().Send(msg);
			o.getSession().close();
		}
	}
}
