package org.pokemonium.client.messages.events;

import org.pokemonium.client.Session;
import org.pokemonium.client.backend.BattleManager;
import org.pokemonium.client.messages.MessageEvent;
import org.pokemonium.client.protocol.ClientMessage;
import org.pokemonium.client.protocol.ServerMessage;

public class BattleLevelupEvent implements MessageEvent
{

	@Override
	public void parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		final String[] levelData = Request.readString().split(",");
		BattleManager.getInstance().getNarrator().informLevelUp(levelData[0], Integer.parseInt(levelData[1]));
	}
}
