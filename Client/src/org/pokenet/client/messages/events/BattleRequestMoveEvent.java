package org.pokenet.client.messages.events;

import org.pokenet.client.Session;
import org.pokenet.client.backend.BattleManager;
import org.pokenet.client.messages.MessageEvent;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.protocol.ServerMessage;

public class BattleRequestMoveEvent implements MessageEvent
{

	@Override
	public void parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		BattleManager.getInstance().getTimeLine().informMoveRequested();
	}
}
