package org.pokemonium.client.messages.events;

import java.util.ArrayList;
import java.util.List;
import org.pokemonium.client.GameClient;
import org.pokemonium.client.Session;
import org.pokemonium.client.backend.Translator;
import org.pokemonium.client.messages.MessageEvent;
import org.pokemonium.client.protocol.ClientMessage;
import org.pokemonium.client.protocol.ServerMessage;

public class RegisterNotificationEvent implements MessageEvent
{

	@Override
	public void parse(Session Session, final ServerMessage Request, ClientMessage Message)
	{
		GameClient.getInstance().getGUI().invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				List<String> translated = new ArrayList<String>();
				translated = Translator.translate("_LOGIN");

				switch(Request.readInt())
				{
					case 1:
						// Account server offline
						GameClient.getInstance().showMessageDialog(translated.get(24));
						break;
					case 2:
						GameClient.getInstance().showMessageDialog(translated.get(25));
						break;
					case 3:
						GameClient.getInstance().showMessageDialog(translated.get(26));
						break;
					case 4:
						GameClient.getInstance().showMessageDialog(translated.get(27));
						break;
					case 5:
						GameClient.getInstance().showMessageDialog(translated.get(41));
						break;
					case 6:
						GameClient.getInstance().showMessageDialog("Email too long!");
						break;
					case 7:
						GameClient.getInstance().showMessageDialog("Username cannot contain: " + Request.readString());

				}
				GameClient.getInstance().getLoginScreen().getRegistration().enableRegistration();
			}
		});
	}
}
