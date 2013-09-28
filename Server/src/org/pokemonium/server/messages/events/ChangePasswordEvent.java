package org.pokemonium.server.messages.events;

import org.pokemonium.server.GameServer;
import org.pokemonium.server.client.Session;
import org.pokemonium.server.messages.MessageEvent;
import org.pokemonium.server.protocol.ClientMessage;
import org.pokemonium.server.protocol.ServerMessage;

public class ChangePasswordEvent implements MessageEvent
{

	public void Parse(Session session, ClientMessage request, ServerMessage message)
	{
		String[] details = request.readString().split(",");
		GameServer.getServiceManager().getNetworkService().getLoginManager().queuePasswordChange(session, details[0], details[1], details[2]);
	}

}
