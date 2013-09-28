package org.pokemonium.client.messages.events;

import org.pokemonium.client.GameClient;
import org.pokemonium.client.Session;
import org.pokemonium.client.messages.MessageEvent;
import org.pokemonium.client.protocol.ClientMessage;
import org.pokemonium.client.protocol.ServerMessage;

public class PokemonLeavePartyEvent implements MessageEvent
{

	@Override
	public void parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		int i = Request.readInt();
		GameClient.getInstance().getOurPlayer().setPokemon(i, null);
		GameClient.getInstance().getHUD().refreshParty();
	}
}
