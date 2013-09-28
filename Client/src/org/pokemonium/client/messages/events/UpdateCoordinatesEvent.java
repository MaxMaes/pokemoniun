package org.pokemonium.client.messages.events;

import org.pokemonium.client.GameClient;
import org.pokemonium.client.Session;
import org.pokemonium.client.backend.entity.Player;
import org.pokemonium.client.messages.MessageEvent;
import org.pokemonium.client.protocol.ClientMessage;
import org.pokemonium.client.protocol.ServerMessage;

public class UpdateCoordinatesEvent implements MessageEvent
{

	@Override
	public void parse(Session Session, ServerMessage Request, ClientMessage Message)
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
