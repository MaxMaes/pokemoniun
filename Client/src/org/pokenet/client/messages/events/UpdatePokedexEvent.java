package org.pokenet.client.messages.events;

import org.pokenet.client.GameClient;
import org.pokenet.client.Session;
import org.pokenet.client.messages.MessageEvent;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.protocol.ServerMessage;

public class UpdatePokedexEvent implements MessageEvent
{

	@Override
	public void parse(Session session, ServerMessage request, ClientMessage message)
	{
		// TODO: Test message functionality.
		GameClient.getInstance().getOurPlayer().updatePokedex(request.readInt(), request.readInt());
	}

}
