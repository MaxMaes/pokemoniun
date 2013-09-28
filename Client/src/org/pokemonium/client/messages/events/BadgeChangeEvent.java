package org.pokemonium.client.messages.events;

import org.pokemonium.client.GameClient;
import org.pokemonium.client.Session;
import org.pokemonium.client.backend.entity.OurPlayer;
import org.pokemonium.client.messages.MessageEvent;
import org.pokemonium.client.protocol.ClientMessage;
import org.pokemonium.client.protocol.ServerMessage;

public class BadgeChangeEvent implements MessageEvent
{

	@Override
	public void parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		int i = Request.readInt();
		OurPlayer player = GameClient.getInstance().getOurPlayer();

		// init badges
		if(i == 0)
		{
			player.initBadges(Request.readString());
			GameClient.getInstance().setOurPlayer(player);
		}
		else
			player.addBadge(Request.readInt());
	}
}
