package org.pokemonium.server.messages.events;

import org.pokemonium.server.GameServer;
import org.pokemonium.server.backend.entity.Player;
import org.pokemonium.server.backend.map.ServerMap;
import org.pokemonium.server.client.Session;
import org.pokemonium.server.connections.ActiveConnections;
import org.pokemonium.server.constants.ClientPacket;
import org.pokemonium.server.messages.MessageEvent;
import org.pokemonium.server.protocol.ClientMessage;
import org.pokemonium.server.protocol.ServerMessage;

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
							ServerMessage globalChat = new ServerMessage(ClientPacket.CHAT_PACKET);
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
					ServerMessage targetMessage = new ServerMessage(ClientPacket.CHAT_PACKET);
					targetMessage.addInt(1);
					targetMessage.addString(player.getName() + "," + "<" + player.getName() + "> " + details[1]);
					target.getSession().Send(targetMessage);
				}
				break;
		}
	}
}
