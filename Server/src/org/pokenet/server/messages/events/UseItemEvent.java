package org.pokenet.server.messages.events;

import org.pokenet.server.backend.ItemProcessor;
import org.pokenet.server.backend.entity.Player;
import org.pokenet.server.client.Session;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;

public class UseItemEvent implements MessageEvent
{

	public void Parse(Session session, ClientMessage request, ServerMessage message)
	{
		Player p = session.getPlayer();
		// Use an item, applies inside and outside of battle
		String[] details = request.readString().split(",");
		new Thread(new ItemProcessor(p, details), "Item-Thread").start();

		// TODO: the fuq, i should check this out o.o a thread??
	}
}
