package org.pokemonium.client.messages.events;

import org.pokemonium.client.Session;
import org.pokemonium.client.messages.MessageEvent;
import org.pokemonium.client.protocol.ClientMessage;
import org.pokemonium.client.protocol.ServerMessage;

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
