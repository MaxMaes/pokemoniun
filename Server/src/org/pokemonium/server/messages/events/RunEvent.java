package org.pokemonium.server.messages.events;

import org.pokemonium.server.backend.entity.Player;
import org.pokemonium.server.battle.impl.WildBattleField;
import org.pokemonium.server.client.Session;
import org.pokemonium.server.messages.MessageEvent;
import org.pokemonium.server.protocol.ClientMessage;
import org.pokemonium.server.protocol.ServerMessage;

public class RunEvent implements MessageEvent
{

	public void Parse(Session session, ClientMessage request, ServerMessage message)
	{
		Player p = session.getPlayer();
		if(p.isBattling())
			if(p.getBattleField() instanceof WildBattleField)
				((WildBattleField) p.getBattleField()).flee();
	}

}
