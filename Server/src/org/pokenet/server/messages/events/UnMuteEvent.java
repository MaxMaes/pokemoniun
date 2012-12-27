package org.pokenet.server.messages.events;

import org.pokenet.server.backend.entity.Player;
import org.pokenet.server.client.Session;
import org.pokenet.server.connections.ActiveConnections;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;

public class UnMuteEvent implements MessageEvent
{

	public void Parse(Session Session, ClientMessage Request, ServerMessage Message)
	{
		Player player = ActiveConnections.getPlayer(Request.readString());
		if(player != null)
		{
			player.setMuted(false);
			ServerMessage unmuteMessage = new ServerMessage();
			unmuteMessage.Init(1);
			unmuteMessage.addString("You have been unmuted.");
			player.getSession().Send(unmuteMessage);
		}
	}
}
