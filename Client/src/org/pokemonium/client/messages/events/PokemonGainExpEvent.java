package org.pokemonium.client.messages.events;

import org.pokemonium.client.GameClient;
import org.pokemonium.client.Session;
import org.pokemonium.client.messages.MessageEvent;
import org.pokemonium.client.protocol.ClientMessage;
import org.pokemonium.client.protocol.ServerMessage;

public class PokemonGainExpEvent implements MessageEvent
{

	@Override
	public void parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		int p1 = Request.readInt();
		int exp = GameClient.getInstance().getOurPlayer().getPokemon()[p1].getExp() + Request.readInt();

		GameClient.getInstance().getOurPlayer().getPokemon()[p1].setExp(exp);
		GameClient.getInstance().getHUD().update(false);
	}
}
