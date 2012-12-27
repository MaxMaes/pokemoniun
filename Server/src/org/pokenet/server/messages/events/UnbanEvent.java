package org.pokenet.server.messages.events;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.pokenet.server.GameServer;
import org.pokenet.server.client.Session;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.network.MySqlManager;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;

public class UnbanEvent implements MessageEvent
{

	public void Parse(Session Session, ClientMessage Request, ServerMessage Message)
	{
		/* TODO: Rewrite once MySqlManager is more efficient. */
		MySqlManager m = new MySqlManager();
		if(!m.connect(GameServer.getDatabaseHost(), GameServer.getDatabaseUsername(), GameServer.getDatabasePassword()))
			return;
		if(!m.selectDatabase(GameServer.getDatabaseName()))
			return;
		String bannedPlayer = Request.readString();
		ResultSet ip = m.query("SELECT lastLoginIP FROM pn_members WHERE username = '" + bannedPlayer + "'");
		String bannedIP = "";
		try
		{
			ip.first();
			bannedIP = ip.getString(1);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		m.query("DELETE FROM pn_bans WHERE ip = '" + bannedIP + "'");
	}
}
