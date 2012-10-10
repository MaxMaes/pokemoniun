package org.pokenet.server.messages.events;

import org.pokenet.server.backend.entity.PlayerChar;
import org.pokenet.server.backend.entity.PlayerChar.RequestType;
import org.pokenet.server.client.Session;
import org.pokenet.server.connections.ActiveConnections;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;


public class TradeRequestEvent implements MessageEvent {

	@Override
	public void Parse(Session Session, ClientMessage Request, ServerMessage Message)
	{
		PlayerChar p = Session.getPlayer();		
		String player = Request.readString();
		//Battle Request rbUSERNAME
		if(ActiveConnections.getPlayer(player) != null) {
			ServerMessage bRequest = new ServerMessage();
			bRequest.Init(83);
			bRequest.addString(p.getName());
			ActiveConnections.getPlayer(player).getSession().Send(bRequest);
			p.addRequest(player, RequestType.TRADE);
		}
	}

}
