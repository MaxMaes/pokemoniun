package org.pokenet.client.messages.events;

import org.pokenet.client.GameClient;
import org.pokenet.client.Session;
import org.pokenet.client.messages.MessageEvent;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.protocol.ServerMessage;

public class MoveTutorEvent implements MessageEvent
{

	@Override
	public void parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		String s = Request.readString();
		String name = s.split("_")[0];
		if(name.equals("MoveRelearner"))
		{
			GameClient.getInstance().getHUD().showRelearnDialog(s.split("_")[1]);
		}
		else if(name.equals("MoveTutor"))
		{
			String moves = s.split("_")[1];
		}
	}
}
