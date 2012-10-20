package org.pokenet.client.messages.events;

import org.pokenet.client.Session;
import org.pokenet.client.messages.MessageEvent;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.protocol.ServerMessage;

public class TestEvent implements MessageEvent
{

	@Override
	public void parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		// String AuthString = Request.readString();
		// String[] Bits = AuthString.split(";");
		// String Username = Bits[0];

		System.out.println("RECEIVED HEADER: " + Request.readBool());
	}

}
