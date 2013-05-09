package org.pokenet.server.messages.events;

import org.pokenet.server.GameServer;
import org.pokenet.server.backend.entity.Player;
import org.pokenet.server.client.Session;
import org.pokenet.server.connections.ActiveConnections;
import org.pokenet.server.constants.ClientPacket;
import org.pokenet.server.constants.UserClasses;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;

public class KickEvent implements MessageEvent
{

	public void Parse(Session session, ClientMessage request, ServerMessage message)
	{
		String playername = request.readString();
		Player player = ActiveConnections.getPlayer(playername);
		Player mod = session.getPlayer();
		if(player == null)
		{
			message = new ServerMessage(ClientPacket.CHAT_PACKET);
			message.addInt(4);
			message.addString("Player " + playername + " is not online or does not exist.");
			mod.getSession().Send(message);
		}
		if(player != null && mod.getAdminLevel() >= UserClasses.MODERATOR)
		{
			ServerMessage kickMessage = new ServerMessage(ClientPacket.SERVER_NOTIFICATION);
			kickMessage.addString("You have been kicked from the server!");
			player.getSession().Send(kickMessage);
			message.init(ClientPacket.RETURN_TO_LOGIN.getValue());
			player.getSession().Send(message);
			GameServer.getServiceManager().getNetworkService().getLogoutManager().queuePlayer(player);
			GameServer.getServiceManager().getMovementService().removePlayer(player.getName());
			message = new ServerMessage(ClientPacket.CHAT_PACKET);
			message.addInt(4);
			message.addString("Player " + playername + " has been kicked from the server.");
			mod.getSession().Send(message);
		}
	}
}
