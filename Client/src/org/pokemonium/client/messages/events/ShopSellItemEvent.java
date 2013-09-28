package org.pokemonium.client.messages.events;

import org.pokemonium.client.GameClient;
import org.pokemonium.client.Session;
import org.pokemonium.client.backend.ItemDatabase;
import org.pokemonium.client.messages.MessageEvent;
import org.pokemonium.client.protocol.ClientMessage;
import org.pokemonium.client.protocol.ServerMessage;

public class ShopSellItemEvent implements MessageEvent
{

	@Override
	public void parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		try
		{
			GameClient.getInstance().getHUD().getNPCSpeech().advance();
			GameClient.getInstance().getHUD().getNPCSpeech().advance();
		}
		catch(Exception e)
		{
		}
		GameClient.getInstance().getHUD().talkToNPC("You sold a " + ItemDatabase.getInstance().getItem(Request.readInt()).getName());
		GameClient.getInstance().getHUD().getShop().refreshSell();
	}
}
