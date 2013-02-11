package org.pokenet.server.messages.events;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.pokenet.server.backend.entity.Player;
import org.pokenet.server.client.Session;
import org.pokenet.server.constants.UserClasses;
import org.pokenet.server.feature.DatabaseConnection;
import org.pokenet.server.messages.MessageEvent;
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
		if(mod.getAdminLevel() >= UserClasses.DEVELOPER)
		{
			try
			{
				PreparedStatement userClassQuery = DatabaseConnection.getConnection().prepareStatement("UPDATE pn_members SET adminLevel = ? WHERE username = ?");
				userClassQuery.setInt(1, adminLvl);
				userClassQuery.setString(2, playername);
				userClassQuery.execute();
			}
			catch(SQLException sqle)
			{
				sqle.printStackTrace();
			}
		}
	}
}
