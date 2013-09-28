package org.pokemonium.client.messages.events;

import java.util.ArrayList;
import java.util.List;
import org.pokemonium.client.GameClient;
import org.pokemonium.client.Session;
import org.pokemonium.client.backend.Translator;
import org.pokemonium.client.messages.MessageEvent;
import org.pokemonium.client.protocol.ClientMessage;
import org.pokemonium.client.protocol.ServerMessage;

public class RegistrationSuccessEvent implements MessageEvent
{

	@Override
	public void parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		GameClient.getInstance().getGUI().invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				List<String> translated = new ArrayList<String>();
				translated = Translator.translate("_LOGIN");

				GameClient.getInstance().showMessageDialog(translated.get(23));
				GameClient.getInstance().getLoginScreen().showLogin();
				GameClient.getInstance().getLoginScreen().getRegistration().clear();
			}
		});
	}
}
