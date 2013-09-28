package org.pokemonium.server.messages.events;

import org.pokemonium.server.GameServer;
import org.pokemonium.server.client.Session;
import org.pokemonium.server.messages.MessageEvent;
import org.pokemonium.server.protocol.ClientMessage;
import org.pokemonium.server.protocol.ServerMessage;

public class LoginEvent implements MessageEvent
{

	public void Parse(Session session, ClientMessage request, ServerMessage message)
	{
		String[] details = request.readString().split(",");
		GameServer.getServiceManager().getNetworkService().getLoginManager().queuePlayer(session, details[0], details[1]);
	}

}
