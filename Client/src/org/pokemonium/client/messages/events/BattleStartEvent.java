package org.pokemonium.client.messages.events;

import org.pokemonium.client.Session;
import org.pokemonium.client.backend.BattleManager;
import org.pokemonium.client.messages.MessageEvent;
import org.pokemonium.client.protocol.ClientMessage;
import org.pokemonium.client.protocol.ServerMessage;

public class BattleStartEvent implements MessageEvent
{

	@Override
	public void parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		/* Dit heb ik veranderd van char naar bool! Want we kunnen nu bools lezen. */
		BattleManager.getInstance().startBattle(Request.readBool(), Request.readInt());
	}
}
