package org.pokenet.client.messages.events;

import org.pokenet.client.Session;
import org.pokenet.client.backend.BattleManager;
import org.pokenet.client.messages.MessageEvent;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.protocol.ServerMessage;

public class BattleEnemyDataEvent implements MessageEvent {

	@Override
	public void Parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		BattleManager.getInstance().setEnemyPoke(Request.readInt(),
				Request.readString(), Request.readInt(), Request.readInt(), Request.readInt(), Request.readInt(), Request.readInt(), 
						Request.readBool());
	}
}
