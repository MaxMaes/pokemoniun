package org.pokenet.server.messages.events;

import org.pokenet.server.backend.entity.Player;
import org.pokenet.server.backend.entity.Player.RequestType;
import org.pokenet.server.client.Session;
import org.pokenet.server.connections.ActiveConnections;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;

public class TradeRequestEvent implements MessageEvent
{

	public void Parse(Session session, ClientMessage request, ServerMessage message)
	{
		Player p = session.getPlayer();
		String player = request.readString();
		// Battle Request rbUSERNAME
		if(ActiveConnections.getPlayer(player) != null)
		{
			ServerMessage bRequest = new ServerMessage();
			bRequest.init(83);
			bRequest.addString(p.getName());
			ActiveConnections.getPlayer(player).getSession().Send(bRequest);
			p.addRequest(player, RequestType.TRADE);
		}
	}

}
