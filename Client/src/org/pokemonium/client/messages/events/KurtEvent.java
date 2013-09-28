package org.pokemonium.client.messages.events;

import org.pokemonium.client.GameClient;
import org.pokemonium.client.Session;
import org.pokemonium.client.messages.MessageEvent;
import org.pokemonium.client.protocol.ClientMessage;
import org.pokemonium.client.protocol.ServerMessage;

public class KurtEvent implements MessageEvent
{

	@Override
	public void parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		String pokeball = Request.readString();
		String item = Request.readString();
		// max represents the maximum number of items you can buy
		int max = Request.readInt();
		int price = Request.readInt();
		GameClient.getInstance().getHUD().showKurtDialog(pokeball, item, max, price);
	}
}
