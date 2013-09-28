package org.pokemonium.server.messages.events;

import org.pokemonium.server.GameServer;
import org.pokemonium.server.client.Session;
import org.pokemonium.server.constants.ClientPacket;
import org.pokemonium.server.messages.MessageEvent;
import org.pokemonium.server.protocol.ClientMessage;
import org.pokemonium.server.protocol.ServerMessage;

public class RegisterEvent implements MessageEvent
{

	public void Parse(Session session, ClientMessage request, ServerMessage message)
	{
		try
		{
			GameServer.getServiceManager().getNetworkService().getRegistrationManager().register(session, request.readInt(), request.readString());
		}
		catch(Exception e)
		{
			e.printStackTrace();
			message.init(ClientPacket.REGISTER_ISSUES.getValue());
			message.addInt(3);
			session.Send(message);
		}
	}

}
