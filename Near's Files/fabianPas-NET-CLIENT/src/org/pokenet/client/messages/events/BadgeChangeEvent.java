package org.pokenet.client.messages.events;

import org.pokenet.client.GameClient;
import org.pokenet.client.Session;
import org.pokenet.client.backend.entity.OurPlayer;
import org.pokenet.client.messages.MessageEvent;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.protocol.ServerMessage;

public class BadgeChangeEvent implements MessageEvent {

	@Override
	public void Parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		int i = Request.readInt();
		OurPlayer player = GameClient.getInstance().getOurPlayer();
		
		// init badges
		if (i == 0) {
			player.initBadges(Request.readString());
			GameClient.getInstance().setOurPlayer(player);
		} else {
			player.addBadge(Request.readInt());
		}
	}
}
