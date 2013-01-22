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

	public void Parse(Session session, ClientMessage request, ServerMessage message)
	{
		Player player = session.getPlayer();
		int type = request.readInt();
		String msg = request.readString();
		switch(type)
		{
			case 0: // local
				if(!player.isMuted())
				{
					ServerMap map = GameServer.getServiceManager().getMovementService().getMapMatrix().getMapByGamePosition(player.getMapX(), player.getMapY());
					if(map != null)
						map.sendChatMessage("<" + player.getName() + "> " + msg, player.getLanguage());
				}
				break;
			case 1: // global
				if(!player.isMuted())
				{
					for(Session ses : ActiveConnections.allSessions().values())
						if(ses.getPlayer() != null)
						{
							ServerMessage globalChat = new ServerMessage();
							globalChat.Init(50);
							globalChat.addInt(0);
							globalChat.addString("<" + player.getName() + "> " + msg);
							ses.Send(globalChat);
						}
				}
			case 2: // private
				String[] details = msg.split(",");
				String targetPlayer = details[0];
				Player target = ActiveConnections.getPlayer(targetPlayer);
				if(target != null)
				{
					ServerMessage targetMessage = new ServerMessage();
					targetMessage.Init(50);
					targetMessage.addInt(1);
					targetMessage.addString(player.getName() + "," + "<" + player.getName() + "> " + details[1]);
					target.getSession().Send(targetMessage);
				}
				break;
		}
	}
}
