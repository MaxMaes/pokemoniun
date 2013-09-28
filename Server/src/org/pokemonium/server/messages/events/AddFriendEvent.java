package org.pokemonium.server.messages.events;

import org.pokemonium.server.backend.entity.Player;
import org.pokemonium.server.client.Session;
import org.pokemonium.server.connections.ActiveConnections;
import org.pokemonium.server.messages.MessageEvent;
import org.pokemonium.server.protocol.ClientMessage;
import org.pokemonium.server.protocol.ServerMessage;

public class AddFriendEvent implements MessageEvent
{

	public void Parse(Session session, ClientMessage request, ServerMessage message)
	{
		Player p = session.getPlayer();
		String friend = request.readString();
		if(ActiveConnections.getPlayer(friend) != null)
			p.addFriend(friend);
	}
}
