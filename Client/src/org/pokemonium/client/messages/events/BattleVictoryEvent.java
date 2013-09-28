package org.pokemonium.client.messages.events;

import org.pokemonium.client.Session;
import org.pokemonium.client.backend.BattleManager;
import org.pokemonium.client.messages.MessageEvent;
import org.pokemonium.client.protocol.ClientMessage;
import org.pokemonium.client.protocol.ServerMessage;

public class BattleVictoryEvent implements MessageEvent
{

	@Override
	public void parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		int condition = Request.readInt();
		switch(condition)
		{
			case 0: /* You won! */
				BattleManager.getInstance().getNarrator().informVictory();
				BattleManager.getInstance().deleteInstance();
				break;
			case 1: /* You lost! */
				BattleManager.getInstance().getNarrator().informLoss();
				BattleManager.getInstance().deleteInstance();
				break;
			case 2: /* We caught the Pokemon! */
				BattleManager.getInstance().endBattle();
				BattleManager.getInstance().deleteInstance();
				break;
		}
	}
}
