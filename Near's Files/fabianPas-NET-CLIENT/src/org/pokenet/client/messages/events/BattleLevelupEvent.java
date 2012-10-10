package org.pokenet.client.messages.events;

import org.pokenet.client.Session;
import org.pokenet.client.backend.BattleManager;
import org.pokenet.client.messages.MessageEvent;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.protocol.ServerMessage;

public class BattleLevelupEvent implements MessageEvent {

	@Override
	public void Parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		final String[] levelData = Request.readString().split(",");
		BattleManager.getInstance().getTimeLine().informLevelUp(levelData[0],
				Integer.parseInt(levelData[1]));
	}
}
