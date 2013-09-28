package org.pokemonium.client.messages.events;

import org.pokemonium.client.Session;
import org.pokemonium.client.backend.BattleManager;
import org.pokemonium.client.messages.MessageEvent;
import org.pokemonium.client.protocol.ClientMessage;
import org.pokemonium.client.protocol.ServerMessage;

public class BattleNotifyHealthEvent implements MessageEvent
{

	@Override
	public void parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		if(Request.readInt() == 0)
			// Our pokemon's health
			BattleManager.getInstance().getNarrator().informHealthChanged(Request.readString().split(","), 0);
		else
			// Enemy pokemon's health
			BattleManager.getInstance().getNarrator().informHealthChanged(Request.readString().split(","), 1);
	}
}
