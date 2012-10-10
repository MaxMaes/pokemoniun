package org.pokenet.client.messages.events;

import org.pokenet.client.Session;
import org.pokenet.client.backend.MoveLearningManager;
import org.pokenet.client.messages.MessageEvent;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.protocol.ServerMessage;

public class LevelLearnMoveEvent implements MessageEvent {

	@Override
	public void Parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		int i = Request.readInt();
		MoveLearningManager.getInstance().queueMoveLearning(i, Request.readString());
	}
}
