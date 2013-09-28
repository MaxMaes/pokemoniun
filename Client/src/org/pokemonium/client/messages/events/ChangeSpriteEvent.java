package org.pokemonium.client.messages.events;

import org.pokemonium.client.GameClient;
import org.pokemonium.client.Session;
import org.pokemonium.client.backend.entity.Player;
import org.pokemonium.client.messages.MessageEvent;
import org.pokemonium.client.protocol.ClientMessage;
import org.pokemonium.client.protocol.ServerMessage;

public class ChangeSpriteEvent implements MessageEvent
{

	@Override
	public void parse(Session Session, ServerMessage Request, ClientMessage Message)
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
