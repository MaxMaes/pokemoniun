package org.pokenet.server.messages.events;

import org.pokenet.server.client.Session;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;

public class LogoutRequestEvent implements MessageEvent
{

	public void Parse(Session Session, ClientMessage Request, ServerMessage Message)
	{
		// TODO: Implement LogoutRequest
	}

}
