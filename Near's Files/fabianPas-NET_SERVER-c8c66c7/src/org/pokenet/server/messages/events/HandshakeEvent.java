package org.pokenet.server.messages.events;

import org.pokenet.server.client.Session;
import org.pokenet.server.GameServer;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.protocol.*;
import org.pokenet.server.network.Connection;
import org.pokenet.server.backend.entity.PlayerChar;



public class HandshakeEvent implements MessageEvent {

	@Override
	public void Parse(Session Session, ClientMessage Request, ServerMessage Message)
	{
		//String AuthString = Request.readString();
		//String[] Bits = AuthString.split(";");
		//String Username = Bits[0];

		Message.Init(2);
		Message.addBool(true);
		Message.Send();
		
		GameServer.getServiceManager().getNetworkService().getLoginManager();
		
		
	}

}
