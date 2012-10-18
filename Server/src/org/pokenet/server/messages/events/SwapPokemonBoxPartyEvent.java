package org.pokenet.server.messages.events;

import org.pokenet.server.backend.entity.Player;
import org.pokenet.server.client.Session;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;

public class SwapPokemonBoxPartyEvent implements MessageEvent
{

	public void Parse(Session Session, ClientMessage Request, ServerMessage Message)
	{

		Player p = Session.getPlayer();
		if(p.isBoxing())
			p.swapFromBox(Request.readInt(), Request.readInt(), Request.readInt());
	}
}
