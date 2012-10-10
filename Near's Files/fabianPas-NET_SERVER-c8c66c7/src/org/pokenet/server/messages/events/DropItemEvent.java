package org.pokenet.server.messages.events;

import org.pokenet.server.backend.entity.PlayerChar;
import org.pokenet.server.client.Session;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;


public class DropItemEvent implements MessageEvent {

	@Override
	public void Parse(Session Session, ClientMessage Request, ServerMessage Message)
	{
		PlayerChar p = Session.getPlayer();		
		int item = Request.readInt();
		if(p.getBag().removeItem(item, 1)) {
			ServerMessage message = new ServerMessage();
			message.Init(81);
			message.addInt(item);
			message.addInt(1);
			Session.Send(message);
		}
	}
}
