package org.pokemonium.client.messages.events;

import org.pokemonium.client.GameClient;
import org.pokemonium.client.Session;
import org.pokemonium.client.backend.entity.Player;
import org.pokemonium.client.backend.entity.Player.Direction;
import org.pokemonium.client.messages.MessageEvent;
import org.pokemonium.client.protocol.ClientMessage;
import org.pokemonium.client.protocol.ServerMessage;

public class PlayerMoveEvent implements MessageEvent
{

	@Override
	public void parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		int player = Request.readInt();
		int dir = Request.readInt();
		Player p = GameClient.getInstance().getMapMatrix().getPlayer(player);
		if(p == null)
			return;

		switch(dir)
		{
			case 0:
				p.queueMovement(Direction.Down);
				break;
			case 1:
				p.queueMovement(Direction.Up);
				break;
			case 2:
				p.queueMovement(Direction.Left);
				break;
			case 3:
				p.queueMovement(Direction.Right);
				break;
		}
	}
}
