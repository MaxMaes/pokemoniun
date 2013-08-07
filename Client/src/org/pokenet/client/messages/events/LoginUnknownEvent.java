package org.pokenet.client.messages.events;

import org.pokenet.client.GameClient;
import org.pokenet.client.Session;
import org.pokenet.client.messages.MessageEvent;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.protocol.ServerMessage;

public class LoginUnknownEvent implements MessageEvent
{

	@Override
	public void parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		// Unknown problem occurred
		GameClient.getInstance().showMessageDialog("An unknown problem occurred!");
		GameClient.getInstance().getGUIPane().hideLoadingScreen();
		GameClient.getInstance().getLoginScreen().enableLogin();
	}
}
