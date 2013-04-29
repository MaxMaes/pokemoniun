package org.pokenet.client.messages.events;

import org.pokenet.client.GameClient;
import org.pokenet.client.Session;
import org.pokenet.client.messages.MessageEvent;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.protocol.ServerMessage;

public class LoggedElsewhereEvent implements MessageEvent
{

	@Override
	public void parse(Session session, ServerMessage request, ClientMessage message)
	{
		GameClient.getInstance().messageDialog("You seem to be logged onto another server, please log out there first.");
		GameClient.getInstance().getLoadingScreen().setVisible(false);
		GameClient.getInstance().getLoginScreen().showLogin();
	}
}
