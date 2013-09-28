package org.pokemonium.client.messages.events;

import org.pokemonium.client.Session;
import org.pokemonium.client.backend.BattleManager;
import org.pokemonium.client.messages.MessageEvent;
import org.pokemonium.client.protocol.ClientMessage;
import org.pokemonium.client.protocol.ServerMessage;

public class BattleEnemyDataEvent implements MessageEvent
{

	@Override
	public void parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		BattleManager.getInstance().setEnemyPoke(Request.readInt(), Request.readString(), Request.readInt(), Request.readInt(), Request.readInt(), Request.readInt(), Request.readInt(),
				Request.readBool());
	}
}
