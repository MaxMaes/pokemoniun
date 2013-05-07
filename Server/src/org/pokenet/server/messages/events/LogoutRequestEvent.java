package org.pokenet.server.messages.events;

import org.pokenet.server.GameServer;
import org.pokenet.server.backend.entity.Player;
import org.pokenet.server.battle.impl.PvPBattleField;
import org.pokenet.server.client.Session;
import org.pokenet.server.connections.ActiveConnections;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;

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
