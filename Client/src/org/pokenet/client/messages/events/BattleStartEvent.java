package org.pokenet.client.messages.events;

import org.pokenet.client.Session;
import org.pokenet.client.backend.BattleManager;
import org.pokenet.client.messages.MessageEvent;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.protocol.ServerMessage;

public class BattleStartEvent implements MessageEvent
{

	@Override
	public void parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		/* Dit heb ik veranderd van char naar bool! Want we kunnen nu bools lezen. */
		BattleManager.getInstance().startBattle(Request.readBool(), Request.readInt());
	}
}
