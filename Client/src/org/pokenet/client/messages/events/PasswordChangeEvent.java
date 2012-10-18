package org.pokenet.client.messages.events;

import org.pokenet.client.Session;
import org.pokenet.client.messages.MessageEvent;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.protocol.ServerMessage;

public class PasswordChangeEvent implements MessageEvent
{

	@Override
	public void Parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		// 0 is failure // 1 is success;
		/* if (Request.readInt() == 1) { GameClient.getInstance().getUserManager().login(m_game.getPacketGenerator().getLastUsername(), m_game.getPacketGenerator().getLastPassword()); } else { GameClient.getInstance().getUserManager().login(m_game.getPacketGenerator().getLastUsername(), m_game.getPacketGenerator().getLastPassword()); } */
	}
}
