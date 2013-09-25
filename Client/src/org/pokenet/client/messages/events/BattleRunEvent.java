package org.pokenet.client.messages.events;

import org.pokenet.client.Session;
import org.pokenet.client.backend.BattleManager;
import org.pokenet.client.messages.MessageEvent;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.protocol.ServerMessage;

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
