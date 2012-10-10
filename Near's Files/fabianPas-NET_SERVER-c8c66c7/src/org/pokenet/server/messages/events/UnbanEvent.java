package org.pokenet.server.messages.events;

import org.pokenet.server.GameServer;
import org.pokenet.server.client.Session;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.network.MySQLManager;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;


public class UnbanEvent implements MessageEvent {

	@Override
	public void Parse(Session Session, ClientMessage Request, ServerMessage Message)
	{	
			MySQLManager m = new MySQLManager();
			if(m.connect(GameServer.getDatabaseHost(), 
					GameServer.getDatabaseUsername(), 
					GameServer.getDatabasePassword())) {
				m.selectDatabase(GameServer.getDatabaseName());
				m.query("DELETE FROM pn_bans WHERE ip='" + 
						Request.readString()
						+ "'");
				m.close();
			}
	}
}
