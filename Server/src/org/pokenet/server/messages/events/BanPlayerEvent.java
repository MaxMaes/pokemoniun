package org.pokenet.server.messages.events;

import org.pokenet.server.backend.entity.Player;
import org.pokenet.server.client.Session;
import org.pokenet.server.connections.ActiveConnections;
import org.pokenet.server.constants.ClientPacket;
import org.pokenet.server.constants.UserClasses;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.network.MySqlManager;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;

public class BanPlayerEvent implements MessageEvent
{

	public void Parse(Session session, ClientMessage request, ServerMessage message)
	{
		String playername = request.readString();
		Player player = ActiveConnections.getPlayer(playername);
		Player mod = session.getPlayer();
		MySqlManager m_database = MySqlManager.getInstance();
		if(player == null)
		{
			message = new ServerMessage(ClientPacket.CHAT_PACKET);
			message.addInt(4);
			message.addString("Player " + playername + " is not online or does not exist.");
			mod.getSession().Send(message);
		}
		if(player != null && mod.getAdminLevel() >= UserClasses.SUPER_MOD)
		{
			m_database.query("INSERT INTO pn_bans VALUES ('" + playername + "', '" + player.getIpAddress() + "');");
			message = new ServerMessage(ClientPacket.CHAT_PACKET);
			message.addInt(4);
			message.addString("Player " + playername + " has been banned.");
			mod.getSession().Send(message);
		}
	}
}
