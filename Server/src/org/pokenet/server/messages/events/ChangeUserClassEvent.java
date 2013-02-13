package org.pokenet.server.messages.events;

import org.pokenet.server.backend.entity.Player;
import org.pokenet.server.client.Session;
import org.pokenet.server.constants.UserClasses;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.network.MySqlManager;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;

public class ChangeUserClassEvent implements MessageEvent
{

	public void Parse(Session session, ClientMessage request, ServerMessage message)
	{
		Player mod = session.getPlayer();
		String[] playerdata = request.readString().split(",");
		String playername = playerdata[0];
		int adminLvl = Integer.parseInt(playerdata[1]);
		MySqlManager m_database = MySqlManager.getInstance();
		if(mod.getAdminLevel() >= UserClasses.DEVELOPER)
			m_database.query("UPDATE pn_members SET adminLevel = " + adminLvl + " WHERE username = '" + playername + "';");
	}
}
