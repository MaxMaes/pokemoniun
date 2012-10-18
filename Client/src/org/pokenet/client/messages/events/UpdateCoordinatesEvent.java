package org.pokenet.client.messages.events;

import org.pokenet.client.GameClient;
import org.pokenet.client.Session;
import org.pokenet.client.backend.entity.Player;
import org.pokenet.client.messages.MessageEvent;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.protocol.ServerMessage;

public class UpdateCoordinatesEvent implements MessageEvent
{

	@Override
	public void Parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		// get coordinates
		int x = Request.readInt();
		int y = Request.readInt();

		// get player
		Player p = GameClient.getInstance().getOurPlayer();

		// set coordinates clientside/serverside
		p.setX(x);
		p.setY(y);
		p.setServerX(p.getX());
		p.setServerY(p.getY());

		/* Reposition screen above player */
		GameClient.getInstance().getMapMatrix().getCurrentMap().setXOffset(400 - p.getX(), false);
		GameClient.getInstance().getMapMatrix().getCurrentMap().setYOffset(300 - p.getY(), false);
		GameClient.getInstance().getMapMatrix().recalibrate();
	}
}
