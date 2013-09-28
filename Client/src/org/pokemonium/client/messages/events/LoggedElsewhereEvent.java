package org.pokemonium.client.messages.events;

import org.pokemonium.client.GameClient;
import org.pokemonium.client.Session;
import org.pokemonium.client.messages.MessageEvent;
import org.pokemonium.client.protocol.ClientMessage;
import org.pokemonium.client.protocol.ServerMessage;

public class LoggedElsewhereEvent implements MessageEvent
{

	@Override
	public void parse(Session session, ServerMessage request, ClientMessage message)
	{
		GameClient.getInstance().getGUI().invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				GameClient.getInstance().showMessageDialog("You seem to be logged onto another server, please log out there first.");
				GameClient.getInstance().getGUIPane().hideLoadingScreen();
				GameClient.getInstance().getLoginScreen().showLogin();
			}
		});
	}
}
