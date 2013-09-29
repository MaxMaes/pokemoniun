package org.pokemonium.client.messages.events;

import org.pokemonium.client.GameClient;
import org.pokemonium.client.Session;
import org.pokemonium.client.messages.MessageEvent;
import org.pokemonium.client.protocol.ClientMessage;
import org.pokemonium.client.protocol.ServerMessage;

public class LoginSuccessEvent implements MessageEvent
{

	@Override
	public void parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		final int id = Request.readInt();
		final String time = Request.readString();
		GameClient.getInstance().getGUI().invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				GameClient.getInstance().getGUIPane().hideLoginScreen();
			}
		});
		GameClient.getInstance().setPlayerId(id);
		GameClient.getInstance().getHUD().showChat();
		GameClient.getInstance().getTimeService().setTime(Integer.parseInt(time.substring(0, 2)), Integer.parseInt(time.substring(2)));

	}
}
