package org.pokemonium.server.messages.events;

import org.pokemonium.server.backend.entity.Player;
import org.pokemonium.server.battle.BattleTurn;
import org.pokemonium.server.client.Session;
import org.pokemonium.server.messages.MessageEvent;
import org.pokemonium.server.protocol.ClientMessage;
import org.pokemonium.server.protocol.ServerMessage;

public class PokemonSwitchEvent implements MessageEvent
{

	public void Parse(Session session, ClientMessage request, ServerMessage message)
	{
		Player p = session.getPlayer();
		int pIndex = request.readInt();
		BattleTurn turn;
		if(p.isBattling())
			if(p.getParty()[pIndex] != null)
				if(!p.getParty()[pIndex].isFainted())
				{
					turn = BattleTurn.getSwitchTurn(pIndex);
					try
					{
						p.getBattleField().queueMove(p.getBattleId(), turn);
					}
					catch(Exception e)
					{
					} // this is dubious and check it
				}
	}

}
