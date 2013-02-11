package org.pokenet.client.messages.events;

import org.pokenet.client.GameClient;
import org.pokenet.client.Session;
import org.pokenet.client.messages.MessageEvent;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.protocol.ServerMessage;

public class ItemCantUseEvent implements MessageEvent
{
	@Override
	public void parse(Session session, ServerMessage request, ClientMessage message)
	{
		String msg = "You can't use this now.";
		try
		{
			GameClient.getInstance().getUi().getNPCSpeech().advance();
			GameClient.getInstance().getUi().getNPCSpeech().advance();
		}
		catch(Exception e)
		{
		}
		GameClient.getInstance().getUi().talkToNPC(msg);
	}
}
