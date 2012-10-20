package org.pokenet.client.messages.events;

import org.pokenet.client.GameClient;
import org.pokenet.client.Session;
import org.pokenet.client.messages.MessageEvent;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.protocol.ServerMessage;

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
