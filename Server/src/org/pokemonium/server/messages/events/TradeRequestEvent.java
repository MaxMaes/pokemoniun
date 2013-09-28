package org.pokemonium.server.messages.events;

import org.pokemonium.server.backend.entity.Player;
import org.pokemonium.server.backend.entity.Player.RequestType;
import org.pokemonium.server.client.Session;
import org.pokemonium.server.connections.ActiveConnections;
import org.pokemonium.server.constants.ClientPacket;
import org.pokemonium.server.messages.MessageEvent;
import org.pokemonium.server.protocol.ClientMessage;
import org.pokemonium.server.protocol.ServerMessage;

public class TradeRequestEvent implements MessageEvent
{

	public void Parse(Session session, ClientMessage request, ServerMessage message)
	{
		Player p = session.getPlayer();
		String player = request.readString();
		if(ActiveConnections.getPlayer(player) != null)
		{
			ServerMessage bRequest = new ServerMessage(ClientPacket.TRADE_REQUEST);
			bRequest.addString(p.getName());
			ActiveConnections.getPlayer(player).getSession().Send(bRequest);
			p.addRequest(player, RequestType.TRADE);
		}
	}

}
