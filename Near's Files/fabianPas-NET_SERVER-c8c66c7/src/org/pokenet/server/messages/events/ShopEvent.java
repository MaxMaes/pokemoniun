package org.pokenet.server.messages.events;

import org.pokenet.server.backend.entity.PlayerChar;
import org.pokenet.server.client.Session;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;


public class ShopEvent implements MessageEvent {

	@Override
	public void Parse(Session Session, ClientMessage Request, ServerMessage Message)
	{

		PlayerChar p = Session.getPlayer();		
		int i = Request.readInt();
		int item = Request.readInt();
		
		switch(i) {
		case 0:
			p.buyItem(item, 1);
			break;
		case 1: 
			p.sellItem(item, 1);
			break;
		case 2:
			p.setShopping(false);
			break;
		}

	}
}
