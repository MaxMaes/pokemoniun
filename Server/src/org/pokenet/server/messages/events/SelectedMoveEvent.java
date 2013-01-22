package org.pokenet.server.messages.events;

import org.pokenet.server.backend.entity.Player;
import org.pokenet.server.battle.BattleTurn;
import org.pokenet.server.client.Session;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;

public class SelectedMoveEvent implements MessageEvent
{

	public void Parse(Session session, ClientMessage request, ServerMessage message)
	{
		Player p = session.getPlayer();
		BattleTurn turn;
		if(p.isBattling())
		{
			turn = BattleTurn.getMoveTurn(request.readInt());
			try
			{
				p.getBattleField().queueMove(p.getBattleId(), turn);
			}
			catch(Exception e)
			{
			} // this is dubious and check it!
		}
	}

}
