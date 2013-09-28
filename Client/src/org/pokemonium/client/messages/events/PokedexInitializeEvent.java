package org.pokemonium.client.messages.events;

import org.pokemonium.client.GameClient;
import org.pokemonium.client.Session;
import org.pokemonium.client.messages.MessageEvent;
import org.pokemonium.client.protocol.ClientMessage;
import org.pokemonium.client.protocol.ServerMessage;

public class PokedexInitializeEvent implements MessageEvent
{

	@Override
	public void parse(Session session, ServerMessage Request, ClientMessage Message)
	{
		String[] details = Request.readString().split(",");
		GameClient.getInstance().getOurPlayer().initializePokedex(details);
	}

}
