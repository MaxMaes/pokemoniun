package org.pokenet.server.messages.events;

import org.pokenet.server.GameServer;
import org.pokenet.server.backend.entity.Player;
import org.pokenet.server.backend.map.ServerMap;
import org.pokenet.server.client.Session;
import org.pokenet.server.connections.ActiveConnections;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;

public class ChatEvent implements MessageEvent
{

	public void Parse(Session Session, ClientMessage Request, ServerMessage Message)
	{
		Player p = Session.getPlayer();
		int type = Request.readInt();
		String msg = Request.readString();
		switch(type)
		{
			case 0: // local
				if(!p.isMuted())
				{
					ServerMap m = GameServer.getServiceManager().getMovementService().getMapMatrix().getMapByGamePosition(p.getMapX(), p.getMapY());
					if(m != null)
						m.sendChatMessage("<" + p.getName() + "> " + msg, p.getLanguage());
				}
				break;
			case 1: // private
				String[] details = msg.split(",");
				String targetPlayer = details[0];
				Player target = ActiveConnections.getPlayer(targetPlayer);
				if(target != null)
				{
					ServerMessage targetMessage = new ServerMessage();
					targetMessage.Init(50);
					targetMessage.addInt(1);
					targetMessage.addString(p.getName() + "," + "<" + p.getName() + "> " + details[1]);
					target.getSession().Send(targetMessage);
				}
				break;
		}
	}
}
