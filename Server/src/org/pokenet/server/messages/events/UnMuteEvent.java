package org.pokenet.server.messages.events;

import org.pokenet.server.backend.entity.Player;
import org.pokenet.server.client.Session;
import org.pokenet.server.connections.ActiveConnections;
import org.pokenet.server.constants.ClientPacket;
import org.pokenet.server.constants.UserClasses;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;

public class UnMuteEvent implements MessageEvent
{

	public void Parse(Session session, ClientMessage request, ServerMessage message)
	{
		Player player = ActiveConnections.getPlayer(request.readString());
		Player mod = session.getPlayer();
		if(player != null && mod.getAdminLevel() >= UserClasses.MODERATOR)
		{
			player.setMuted(false);
			ServerMessage unmuteMessage = new ServerMessage(ClientPacket.SERVER_NOTIFICATION);
			unmuteMessage.addString("You have been unmuted.");
			player.getSession().Send(unmuteMessage);
		}
	}
}
