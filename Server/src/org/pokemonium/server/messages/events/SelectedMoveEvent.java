package org.pokemonium.server.messages.events;

import org.pokemonium.server.backend.entity.Player;
import org.pokemonium.server.battle.BattleTurn;
import org.pokemonium.server.client.Session;
import org.pokemonium.server.messages.MessageEvent;
import org.pokemonium.server.protocol.ClientMessage;
import org.pokemonium.server.protocol.ServerMessage;

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
