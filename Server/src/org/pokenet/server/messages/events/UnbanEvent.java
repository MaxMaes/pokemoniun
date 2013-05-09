package org.pokenet.server.messages.events;

import org.pokenet.server.backend.entity.Player;
import org.pokenet.server.client.Session;
import org.pokenet.server.constants.ClientPacket;
import org.pokenet.server.constants.UserClasses;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.network.MySqlManager;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;

public class UnbanEvent implements MessageEvent
{

	public void Parse(Session session, ClientMessage request, ServerMessage message)
	{
		Player mod = session.getPlayer();
		String bannedPlayer = request.readString();
		if(mod.getAdminLevel() >= UserClasses.SUPER_MOD)
		{
			MySqlManager m_database = MySqlManager.getInstance();
			m_database.query("DELETE FROM pn_bans WHERE playername = '" + bannedPlayer + "';");
			message = new ServerMessage(ClientPacket.CHAT_PACKET);
			message.addInt(4);
			message.addString("Player " + bannedPlayer + " has been unbanned.");
			mod.getSession().Send(message);
		}
	}
}
