package org.pokenet.client.messages.events;

import org.pokenet.client.GameClient;
import org.pokenet.client.Session;
import org.pokenet.client.messages.MessageEvent;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.protocol.ServerMessage;

public class ItemUseEvent implements MessageEvent
{

	@Override
	public void parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		try
		{
			GameClient.getInstance().getUi().getNPCSpeech().advance();
			GameClient.getInstance().getUi().getNPCSpeech().advance();
		}
		catch(Exception e)
		{
		}
		GameClient.getInstance().getUi().talkToNPC(Request.readString());
	}
}
