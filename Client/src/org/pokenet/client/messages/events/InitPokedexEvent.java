package org.pokenet.client.messages.events;

import org.pokenet.client.GameClient;
import org.pokenet.client.Session;
import org.pokenet.client.messages.MessageEvent;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.protocol.ServerMessage;

public class InitPokedexEvent implements MessageEvent
{

	@Override
	public void parse(Session session, ServerMessage request, ClientMessage message)
	{
		// TODO: Test implementation.
		String[] details = request.readString().split(",");
		GameClient.getInstance().getOurPlayer().initializePokedex(details);
	}

}
