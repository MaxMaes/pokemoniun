package org.pokenet.client.messages.events;

import java.util.ArrayList;
import java.util.List;
import org.pokenet.client.GameClient;
import org.pokenet.client.Session;
import org.pokenet.client.backend.Translator;
import org.pokenet.client.messages.MessageEvent;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.protocol.ServerMessage;

public class LoginErrorEvent implements MessageEvent
{

	@Override
	public void Parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		List<String> translated = new ArrayList<String>();
		translated = Translator.translate("_LOGIN");

		GameClient.messageDialog(translated.get(21), GameClient.getInstance().getDisplay());
		GameClient.getInstance().getLoadingScreen().setVisible(false);
		GameClient.getInstance().getLoginScreen().enableLogin();
	}
}
