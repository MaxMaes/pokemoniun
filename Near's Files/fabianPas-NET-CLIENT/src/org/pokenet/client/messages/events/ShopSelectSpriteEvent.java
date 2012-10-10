package org.pokenet.client.messages.events;

import org.pokenet.client.GameClient;
import org.pokenet.client.Session;
import org.pokenet.client.messages.MessageEvent;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.protocol.ServerMessage;
import org.pokenet.client.ui.frames.SpriteChooserDialog;

public class ShopSelectSpriteEvent implements MessageEvent {

	@Override
	public void Parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		GameClient.getInstance().getDisplay().add(new SpriteChooserDialog());
	}
}
