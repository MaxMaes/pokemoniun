package org.pokemonium.client.messages.events;

import org.pokemonium.client.GameClient;
import org.pokemonium.client.Session;
import org.pokemonium.client.messages.MessageEvent;
import org.pokemonium.client.protocol.ClientMessage;
import org.pokemonium.client.protocol.ServerMessage;

public class HMHigherLevelEvent implements MessageEvent
{

	@Override
	public void parse(Session Session, ServerMessage Request, ClientMessage Message)
	{

		int level = Request.readInt();

		GameClient.getInstance().showMessageDialog("You are not strong enough to do this.\n" + "Your trainer level must be " + level + " to do this.");
	}
}
