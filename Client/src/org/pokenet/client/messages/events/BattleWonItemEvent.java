package org.pokenet.client.messages.events;

import org.pokenet.client.GameClient;
import org.pokenet.client.Session;
import org.pokenet.client.backend.BattleManager;
import org.pokenet.client.backend.ItemDatabase;
import org.pokenet.client.backend.entity.Item;
import org.pokenet.client.messages.MessageEvent;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.protocol.ServerMessage;

public class BattleWonItemEvent implements MessageEvent
{

	@Override
	public void Parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		int itemID = Request.readInt();
		Item item = ItemDatabase.getInstance().getItem(itemID);
		GameClient.getInstance().getOurPlayer().addItem(item.getId(), 1);
		BattleManager.getInstance().getTimeLine().informItemDropped(item.getName());
	}
}
