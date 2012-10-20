package org.pokenet.client.messages.events;

import org.pokenet.client.Session;
import org.pokenet.client.backend.BattleManager;
import org.pokenet.client.messages.MessageEvent;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.protocol.ServerMessage;

public class BattleNotifyHealthEvent implements MessageEvent
{

	@Override
	public void parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		if(Request.readInt() == 0)
			// Our pokemon's health
			BattleManager.getInstance().getTimeLine().informHealthChanged(Request.readString().split(","), 0);
		else
			// Enemy pokemon's health
			BattleManager.getInstance().getTimeLine().informHealthChanged(Request.readString().split(","), 1);
	}
}
