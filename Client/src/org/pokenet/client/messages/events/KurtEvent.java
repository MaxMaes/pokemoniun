package org.pokenet.client.messages.events;

import org.pokenet.client.GameClient;
import org.pokenet.client.Session;
import org.pokenet.client.messages.MessageEvent;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.protocol.ServerMessage;

public class KurtEvent implements MessageEvent
{

	@Override
	public void parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		String pokeball = Request.readString();
		String item = Request.readString();
		int max = Request.readInt();
		GameClient.getInstance().getHUD().setKurtDialog(pokeball, item, max);
	}
}
