package org.pokenet.client.messages.events;

import org.pokenet.client.GameClient;
import org.pokenet.client.Session;
import org.pokenet.client.messages.MessageEvent;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.protocol.ServerMessage;

public class LoginFullEvent implements MessageEvent
{

	@Override
	public void Parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		GameClient.messageDialog("This server is full, please try another", GameClient.getInstance().getDisplay());
		GameClient.getInstance().getLoadingScreen().setVisible(false);
		GameClient.getInstance().getLoginScreen().showLogin();

	}
}
