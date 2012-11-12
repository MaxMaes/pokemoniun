package org.pokenet.client.messages.events;

import org.pokenet.client.GameClient;
import org.pokenet.client.Session;
import org.pokenet.client.messages.MessageEvent;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.protocol.ServerMessage;

public class PokedexInitializeEvent implements MessageEvent
{

	@Override
	public void parse(Session session, ServerMessage Request, ClientMessage Message)
	{
		//TODO
		String[] details = Request.readString().split(",");
		GameClient.getInstance().getOurPlayer().initializePokedex(details);
	}

}
