package org.pokemonium.server.messages.events;

import org.pokemonium.server.GameServer;
import org.pokemonium.server.backend.entity.Player;
import org.pokemonium.server.battle.impl.PvPBattleField;
import org.pokemonium.server.client.Session;
import org.pokemonium.server.connections.ActiveConnections;
import org.pokemonium.server.messages.MessageEvent;
import org.pokemonium.server.protocol.ClientMessage;
import org.pokemonium.server.protocol.ServerMessage;

public class LogoutRequestEvent implements MessageEvent
{

	public void Parse(Session session, ClientMessage request, ServerMessage message)
	{
		try
		{
			Player player = session.getPlayer();
			if(player.isBattling())
			{
				/* If in PvP battle, the player loses */
				if(player.getBattleField() instanceof PvPBattleField)
				{
					((PvPBattleField) player.getBattleField()).disconnect(player.getBattleId());
				}
				player.setBattleField(null);
				player.setBattling(false);
				player.lostBattle();
			}
			/* If trading, end the trade */
			if(player.isTrading())
			{
				player.getTrade().endTrade();
			}
			GameServer.getServiceManager().getNetworkService().getLogoutManager().queuePlayer(player);
			GameServer.getServiceManager().getMovementService().removePlayer(player.getName());
			ActiveConnections.removeSession(session.getChannel());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
