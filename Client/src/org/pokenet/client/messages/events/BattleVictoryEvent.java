package org.pokenet.client.messages.events;

import org.pokenet.client.Session;
import org.pokenet.client.backend.BattleManager;
import org.pokenet.client.messages.MessageEvent;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.protocol.ServerMessage;

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
