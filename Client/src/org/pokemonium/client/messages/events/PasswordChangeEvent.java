package org.pokemonium.client.messages.events;

import org.pokemonium.client.Session;
import org.pokemonium.client.messages.MessageEvent;
import org.pokemonium.client.protocol.ClientMessage;
import org.pokemonium.client.protocol.ServerMessage;

public class PasswordChangeEvent implements MessageEvent
{

	@Override
	public void parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		// 0 is failure // 1 is success;
		/* if (Request.readInt() == 1) { GameClient.getInstance().getUserManager().login(m_game.getPacketGenerator().getLastUsername(), m_game.getPacketGenerator().getLastPassword()); } else { GameClient.getInstance().getUserManager().login(m_game.getPacketGenerator().getLastUsername(), m_game.getPacketGenerator().getLastPassword()); } */
	}
}
