package org.pokenet.server.messages.events;

import org.pokenet.server.GameServer;
import org.pokenet.server.client.Session;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;


public class RegisterEvent implements MessageEvent {

	@Override
	public void Parse(Session Session, ClientMessage Request, ServerMessage Message)
	{
		try {
			GameServer.getServiceManager().getNetworkService().getRegistrationManager().register(Session, Request.readInt(), Request.readString());
		} catch (Exception e) {
			e.printStackTrace();
			
			ServerMessage message = new ServerMessage();
			message.Init(87);
			message.addInt(3);
			Session.Send(message);
		}
	}

}
