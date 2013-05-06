package org.pokenet.server.messages.events;

import org.pokenet.server.backend.entity.Player;
import org.pokenet.server.client.Session;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;

public class StartTalkingEvent implements MessageEvent
{

	public void Parse(Session session, ClientMessage request, ServerMessage message)
	{
		Player player = session.getPlayer();
		/* The session has no player, not logged in yet or already logged out. */
		if(player != null)
		{
			if(!player.isTalking() && !player.isBattling())
				player.talkToNpc();
		}
	}
}
