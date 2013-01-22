package org.pokenet.server.messages.events;

import org.pokenet.server.GameServer;
import org.pokenet.server.client.Session;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;

public class HandshakeEvent implements MessageEvent
{

	public void Parse(Session session, ClientMessage request, ServerMessage message)
	{
		// String AuthString = request.readString();
		// String[] Bits = AuthString.split(";");
		// String Username = Bits[0];

		message.Init(2);
		message.addBool(true);
		message.sendResponse();
		GameServer.getServiceManager().getNetworkService().getLoginManager();
	}

}
