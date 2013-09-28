package org.pokemonium.client.messages.events;

import org.pokemonium.client.GameClient;
import org.pokemonium.client.Session;
import org.pokemonium.client.messages.MessageEvent;
import org.pokemonium.client.protocol.ClientMessage;
import org.pokemonium.client.protocol.ServerMessage;

public class PokemonPPEvent implements MessageEvent
{

	@Override
	public void parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		int poke = Request.readInt();
		int move = Request.readInt();
		int curPP = Request.readInt();
		int maxPP = Request.readInt();
		GameClient.getInstance().getOurPlayer().getPokemon()[poke].setMoveCurPP(move, curPP);
		GameClient.getInstance().getOurPlayer().getPokemon()[poke].setMoveMaxPP(move, maxPP);
	}
}
