package org.pokemonium.client.messages.events;

import org.pokemonium.client.GameClient;
import org.pokemonium.client.Session;
import org.pokemonium.client.messages.MessageEvent;
import org.pokemonium.client.protocol.ClientMessage;
import org.pokemonium.client.protocol.ServerMessage;

public class ServerRevisionEvent implements MessageEvent
{
	@Override
	public void parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		GameClient.getInstance().getGUIPane().getLoginScreen().setServerRevision(Request.readInt());
		GameClient.getInstance().getGUIPane().getLoginScreen().showServerRevision();
	}
}
