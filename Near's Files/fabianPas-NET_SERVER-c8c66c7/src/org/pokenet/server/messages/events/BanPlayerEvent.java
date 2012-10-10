package org.pokenet.server.messages.events;

import org.pokenet.server.GameServer;
import org.pokenet.server.backend.entity.PlayerChar;
import org.pokenet.server.client.Session;
import org.pokenet.server.connections.ActiveConnections;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.network.MySQLManager;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;


public class BanPlayerEvent implements MessageEvent {

	@Override
	public void Parse(Session Session, ClientMessage Request, ServerMessage Message)
	{	
		PlayerChar o = ActiveConnections.getPlayer(Request.readString());
		if(o != null) {
			MySQLManager m = new MySQLManager();
			if(m.connect(GameServer.getDatabaseHost(), 
					GameServer.getDatabaseUsername(), 
					GameServer.getDatabasePassword())) {
				m.selectDatabase(GameServer.getDatabaseName());
				m.query("INSERT INTO pn_bans (ip) VALUE ('" + 
						o.getIpAddress()
						+ "')");
				m.close();
			}
		}
	}
}
