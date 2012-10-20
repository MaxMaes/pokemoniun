package org.pokenet.client.messages.events;

import org.pokenet.client.GameClient;
import org.pokenet.client.Session;
import org.pokenet.client.messages.MessageEvent;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.protocol.ServerMessage;

public class TradeAddPokemonEvent implements MessageEvent
{

	@Override
	public void parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		// CHECK THIS ONE
		// String[] offerData = Request.readString().split(",");
		int index = Request.readInt();
		String[] data = Request.readString().split(",");
		GameClient.getInstance().getUi().getTrade().addPoke(index, data);
	}
}
