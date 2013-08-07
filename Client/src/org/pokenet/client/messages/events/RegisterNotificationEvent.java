package org.pokenet.client.messages.events;

import java.util.ArrayList;
import java.util.List;
import org.pokenet.client.GameClient;
import org.pokenet.client.Session;
import org.pokenet.client.backend.Translator;
import org.pokenet.client.messages.MessageEvent;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.protocol.ServerMessage;

public class RegisterNotificationEvent implements MessageEvent
{

	@Override
	public void parse(Session Session, ServerMessage Request, ClientMessage Message)
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
		// GameClient.getInstance().getLoginScreen().getRegistration().enableRegistration(); TODO: uncomment when registration is done
		GameClient.getInstance().getGUIPane().hideLoadingScreen();
	}
}
