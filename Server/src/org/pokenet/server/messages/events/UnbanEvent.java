package org.pokenet.server.messages.events;

import org.pokenet.server.GameServer;
import org.pokenet.server.backend.entity.Player;
import org.pokenet.server.client.Session;
import org.pokenet.server.constants.UserClasses;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.network.MySqlManager;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;

public class UnbanEvent implements MessageEvent
{

	public void Parse(Session session, ClientMessage request, ServerMessage message)
	{
		/* TODO: Rewrite once MySqlManager is more efficient. */
		Player mod = session.getPlayer();
		if(mod.getAdminLevel() >= UserClasses.SUPER_MOD)
		{
			MySqlManager m = new MySqlManager();
			if(!m.connect(GameServer.getDatabaseHost(), GameServer.getDatabaseUsername(), GameServer.getDatabasePassword()))
				return;
			if(!m.selectDatabase(GameServer.getDatabaseName()))
				return;
			String bannedPlayer = request.readString();
			m.query("DELETE FROM pn_bans WHERE playername = '" + bannedPlayer + "'");
		}
	}
}
