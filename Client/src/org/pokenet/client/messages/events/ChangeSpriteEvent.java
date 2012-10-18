package org.pokenet.client.messages.events;

import org.pokenet.client.GameClient;
import org.pokenet.client.Session;
import org.pokenet.client.backend.entity.Player;
import org.pokenet.client.messages.MessageEvent;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.protocol.ServerMessage;

public class ChangeSpriteEvent implements MessageEvent
{

	@Override
	public void Parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		int player = Request.readInt();
		int sprite = Request.readInt();

		Player p = GameClient.getInstance().getMapMatrix().getPlayer(player);
		if(p != null)
		{
			p.setSprite(sprite);
			p.loadSpriteImage();
		}
	}
}
