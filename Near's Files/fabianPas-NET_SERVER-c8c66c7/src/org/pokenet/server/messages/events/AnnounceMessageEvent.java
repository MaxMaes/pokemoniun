package org.pokenet.server.messages.events;

import org.pokenet.server.GameServer;
import org.pokenet.server.backend.entity.PlayerChar;
import org.pokenet.server.backend.map.ServerMap;
import org.pokenet.server.client.Session;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;


public class AnnounceMessageEvent implements MessageEvent {

	@Override
	public void Parse(Session Session, ClientMessage Request, ServerMessage Message)
	{

		PlayerChar p = Session.getPlayer();		
		if (p.getAdminLevel() == 2) {
			String msg = Request.readString();
			ServerMap m = GameServer.getServiceManager().getMovementService().
                    getMapMatrix().getMapByGamePosition(p.getMapX(), p.getMapY());
            if(m != null)
                    m.sendChatMessage(msg, p.getLanguage());
		}

	}
}
