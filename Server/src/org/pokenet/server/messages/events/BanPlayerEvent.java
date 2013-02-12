package org.pokenet.server.messages.events;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.pokenet.server.backend.entity.Player;
import org.pokenet.server.client.Session;
import org.pokenet.server.connections.ActiveConnections;
import org.pokenet.server.constants.UserClasses;
import org.pokenet.server.feature.DatabaseConnection;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;

public class BanPlayerEvent implements MessageEvent
{

	public void Parse(Session session, ClientMessage request, ServerMessage message)
	{
		String playername = request.readString();
		Player player = ActiveConnections.getPlayer(playername);
		Player mod = session.getPlayer();
		if(player != null && mod.getAdminLevel() >= UserClasses.SUPER_MOD)
		{
			try
			{
				PreparedStatement banStatement = DatabaseConnection.getConnection().prepareStatement("INSERT INTO pn_bans VALUES (?, ?)");
				banStatement.setString(1, playername);
				banStatement.setString(2, player.getIpAddress());
				banStatement.executeUpdate();
				banStatement.close();
			}
			catch(SQLException sqle)
			{
				sqle.printStackTrace();
			}
		}
	}
}
