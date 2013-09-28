package org.pokemonium.server.messages.events;

import org.pokemonium.server.backend.ItemProcessor;
import org.pokemonium.server.backend.entity.Player;
import org.pokemonium.server.client.Session;
import org.pokemonium.server.messages.MessageEvent;
import org.pokemonium.server.protocol.ClientMessage;
import org.pokemonium.server.protocol.ServerMessage;

public class UseItemEvent implements MessageEvent
{

	public void Parse(Session session, ClientMessage request, ServerMessage message)
	{
		Player p = session.getPlayer();
		// Use an item, applies inside and outside of battle
		String[] details = request.readString().split(",");
		new Thread(new ItemProcessor(p, details), "Item-Thread").start();
	}
}
