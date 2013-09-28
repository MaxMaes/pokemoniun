package org.pokemonium.server.messages.events;

import org.pokemonium.server.backend.entity.Player;
import org.pokemonium.server.client.Session;
import org.pokemonium.server.constants.ShopInteraction;
import org.pokemonium.server.messages.MessageEvent;
import org.pokemonium.server.protocol.ClientMessage;
import org.pokemonium.server.protocol.ServerMessage;

public class ShopEvent implements MessageEvent
{

	public void Parse(Session session, ClientMessage request, ServerMessage message)
	{
		Player player = session.getPlayer();
		int action = request.readInt();
		int quantity = 1;
		int item;
		switch(action)
		{
			case ShopInteraction.BUY_ITEM:
				item = request.readInt();
				player.buyItem(item, quantity);
				break;
			case ShopInteraction.SELL_ITEM:
				item = request.readInt();
				player.sellItem(item, quantity);
				break;
			case ShopInteraction.DONE_SHOPPING:
				player.setShopping(false);
				break;
			case ShopInteraction.BUY_MULTIPLE_ITEM:
				item = request.readInt();
				quantity = request.readInt();
				player.buyItem(item, quantity);
				break;
			default:
				player.setShopping(false);
				break;
		}
	}
}
