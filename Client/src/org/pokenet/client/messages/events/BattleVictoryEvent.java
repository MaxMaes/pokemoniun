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
		// also, change this in server message ;)
		int condition = Request.readInt();
		switch(condition)
		{
			case 0: // YOU WIN!
				BattleManager.getInstance().getTimeLine().informVictory();
				break;
			case 1: // YOU LOSE
				BattleManager.getInstance().getTimeLine().informLoss();
				break;
			case 2: // WE CAUGHT HE WOKEMON
				BattleManager.getInstance().endBattle();
				break;
		}
	}
}
