package org.pokenet.server.messages.events;

import org.pokenet.server.client.Session;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.network.MySqlManager;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;

public class UnbanEvent implements MessageEvent
{

	public void Parse(Session Session, ClientMessage Request, ServerMessage Message)
	{
		MySqlManager m = new MySqlManager();
		m.query("DELETE FROM pn_bans WHERE ip='" + Request.readString() + "'");
	}
}
