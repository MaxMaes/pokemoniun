package org.pokenet.client.messages.events;

import org.pokenet.client.GameClient;
import org.pokenet.client.Session;
import org.pokenet.client.messages.MessageEvent;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.protocol.ServerMessage;

public class PokemonGainExpEvent implements MessageEvent
{

	@Override
	public void Parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		int p1 = Request.readInt();
		int exp = GameClient.getInstance().getOurPlayer().getPokemon()[p1].getExp() + Request.readInt();

		GameClient.getInstance().getOurPlayer().getPokemon()[p1].setExp(exp);
		GameClient.getInstance().getUi().update(false);
	}
}
