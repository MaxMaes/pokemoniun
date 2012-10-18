package org.pokenet.server.messages.events;

import org.pokenet.server.backend.entity.Player;
import org.pokenet.server.client.Session;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;

public class DropItemEvent implements MessageEvent
{

	public void Parse(Session Session, ClientMessage Request, ServerMessage Message)
	{
		Player p = Session.getPlayer();
		int item = Request.readInt();
		if(p.getBag().removeItem(item, 1))
		{
			ServerMessage message = new ServerMessage();
			message.Init(81);
			message.addInt(item);
			message.addInt(1);
			Session.Send(message);
		}
	}
}
