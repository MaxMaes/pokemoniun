package org.pokenet.server.messages.events;

import org.pokenet.server.backend.entity.PlayerChar;
import org.pokenet.server.client.Session;
import org.pokenet.server.connections.ActiveConnections;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;


public class WarpEvent implements MessageEvent {

	@Override
	public void Parse(Session Session, ClientMessage Request, ServerMessage Message)
	{	
		PlayerChar p = Session.getPlayer();		
		PlayerChar o = ActiveConnections.getPlayer(Request.readString());
		if(o != null) {
			p.setX(o.getX());
			p.setY(o.getY());
			p.setMap(o.getMap(), null);
		}
	}
}
