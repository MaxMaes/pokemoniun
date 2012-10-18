package org.pokenet.client.messages.events;

import org.pokenet.client.GameClient;
import org.pokenet.client.Session;
import org.pokenet.client.backend.entity.Player;
import org.pokenet.client.backend.entity.Player.Direction;
import org.pokenet.client.messages.MessageEvent;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.protocol.ServerMessage;

public class PlayerMoveEvent implements MessageEvent
{

	@Override
	public void Parse(Session Session, ServerMessage Request, ClientMessage Message)
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
