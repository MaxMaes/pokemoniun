package org.pokemonium.client.messages.events;

import org.pokemonium.client.GameClient;
import org.pokemonium.client.Session;
import org.pokemonium.client.backend.BattleManager;
import org.pokemonium.client.backend.ItemDatabase;
import org.pokemonium.client.backend.entity.Item;
import org.pokemonium.client.messages.MessageEvent;
import org.pokemonium.client.protocol.ClientMessage;
import org.pokemonium.client.protocol.ServerMessage;

public class BattleWonItemEvent implements MessageEvent
{

	@Override
	public void parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		int itemID = Request.readInt();
		Item item = ItemDatabase.getInstance().getItem(itemID);
		GameClient.getInstance().getOurPlayer().addItem(item.getId(), 1);
		BattleManager.getInstance().getNarrator().informItemDropped(item.getName());
	}
}
