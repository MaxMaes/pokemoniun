package org.pokenet.client.messages.events;

import org.pokenet.client.GameClient;
import org.pokenet.client.Session;
import org.pokenet.client.messages.MessageEvent;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.protocol.ServerMessage;

public class InfoEvent implements MessageEvent
{

	@Override
	public void parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		String name = Request.readString();
		switch(name)
		{
			case "MoveRelearner":
				GameClient.getInstance().getHUD().getRelearnDialog().initUse(Request.readString());
				break;
			default:
				break;
		}
	}
}
