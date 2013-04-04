package org.pokenet.client.messages.events;

import org.pokenet.client.GameClient;
import org.pokenet.client.Session;
import org.pokenet.client.messages.MessageEvent;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.protocol.ServerMessage;

public class LoginSuccessEvent implements MessageEvent
{

	@Override
	public void parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		int id = Request.readInt();
		String time = Request.readString();
		GameClient.getInstance().getGUI().hideLoginScreen();
		GameClient.getInstance().getLoadingScreen().setVisible(false);
		GameClient.getInstance().setPlayerId(id);
		GameClient.getInstance().getUi().setVisible(true);
		GameClient.getInstance().getUi().getChat().setVisible(true);
		GameClient.getInstance().getTimeService().setTime(Integer.parseInt(time.substring(0, 2)), Integer.parseInt(time.substring(2)));
	}
}
