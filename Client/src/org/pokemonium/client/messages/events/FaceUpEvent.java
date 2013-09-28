package org.pokemonium.client.messages.events;

import org.pokemonium.client.GameClient;
import org.pokemonium.client.Session;
import org.pokemonium.client.backend.entity.Player;
import org.pokemonium.client.backend.entity.Player.Direction;
import org.pokemonium.client.messages.MessageEvent;
import org.pokemonium.client.protocol.ClientMessage;
import org.pokemonium.client.protocol.ServerMessage;

public class FaceUpEvent implements MessageEvent
{

	@Override
	public void parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		Player p = GameClient.getInstance().getMapMatrix().getPlayer(Request.readInt());
		if(p != null)
		{
			p.setDirection(Direction.Up);
			p.loadSpriteImage();
		}
	}
}
