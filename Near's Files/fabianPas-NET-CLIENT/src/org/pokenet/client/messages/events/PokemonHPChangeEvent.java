package org.pokenet.client.messages.events;

import org.pokenet.client.GameClient;
import org.pokenet.client.Session;
import org.pokenet.client.messages.MessageEvent;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.protocol.ServerMessage;

public class PokemonHPChangeEvent implements MessageEvent {

	@Override
	public void Parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		GameClient.getInstance().getOurPlayer().getPokemon()[Request.readInt()]
                .setCurHP(Request.readInt());
		GameClient.getInstance().getUi().update(false);
	}
}
