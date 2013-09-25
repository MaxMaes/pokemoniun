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
		final int index = Request.readInt();
		final String[] data = Request.readString().split(",");
		GameClient.getInstance().getGUI().invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				GameClient.getInstance().getHUD().getTradeDialog().addPoke(index, data);
			}
		});
	}
}
