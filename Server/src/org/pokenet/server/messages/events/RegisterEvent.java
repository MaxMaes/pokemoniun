package org.pokenet.server.messages.events;

import org.pokenet.server.GameServer;
import org.pokenet.server.client.Session;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;

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
			message.Init(87);
			message.addInt(3);
			session.Send(message);
		}
	}

}
