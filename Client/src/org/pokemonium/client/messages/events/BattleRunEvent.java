package org.pokemonium.client.messages.events;

import org.pokemonium.client.Session;
import org.pokemonium.client.backend.BattleManager;
import org.pokemonium.client.messages.MessageEvent;
import org.pokemonium.client.protocol.ClientMessage;
import org.pokemonium.client.protocol.ServerMessage;

public class BattleRunEvent implements MessageEvent
{

	@Override
	public void parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		// hier in server bool van maken ;) Die kunnen we nu tenslotte lezen! :)
		boolean canRun = Request.readBool();
		BattleManager.getInstance().getNarrator().informRun(canRun);
	}
}
