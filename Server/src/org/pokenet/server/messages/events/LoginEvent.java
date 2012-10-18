package org.pokenet.server.messages.events;

import org.pokenet.server.GameServer;
import org.pokenet.server.client.Session;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;

public class LoginEvent implements MessageEvent
{

	public void Parse(Session Session, ClientMessage Request, ServerMessage Message)
	{
		String[] details = Request.readString().split(",");
		GameServer.getServiceManager().getNetworkService().getLoginManager().queuePlayer(Session, details[0], details[1]);
	}

}
