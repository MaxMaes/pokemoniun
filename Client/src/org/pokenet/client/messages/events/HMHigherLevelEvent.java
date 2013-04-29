package org.pokenet.client.messages.events;

import org.pokenet.client.GameClient;
import org.pokenet.client.Session;
import org.pokenet.client.messages.MessageEvent;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.protocol.ServerMessage;

public class HMHigherLevelEvent implements MessageEvent
{

	@Override
	public void parse(Session Session, ServerMessage Request, ClientMessage Message)
	{

		int level = Request.readInt();

		GameClient.getInstance().messageDialog("You are not strong enough to do this.\n" + "Your trainer level must be " + level + " to do this.");
	}
}
