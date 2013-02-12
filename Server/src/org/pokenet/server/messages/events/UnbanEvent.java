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

public class UnbanEvent implements MessageEvent
{

	public void Parse(Session session, ClientMessage request, ServerMessage message)
	{
		Player mod = session.getPlayer();
		String bannedPlayer = request.readString();
		if(mod.getAdminLevel() >= UserClasses.SUPER_MOD)
		{
			try
			{
				PreparedStatement unbanStatement = DatabaseConnection.getConnection().prepareStatement("DELETE FROM pn_bans WHERE playername = ?");
				unbanStatement.setString(1, bannedPlayer);
				unbanStatement.executeUpdate();
				unbanStatement.close();
			}
			catch(SQLException sqle)
			{
				sqle.printStackTrace();
			}

		}
	}
}
