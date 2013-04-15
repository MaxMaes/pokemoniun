package org.pokenet.server.messages.events;

import org.pokenet.server.GameServer;
import org.pokenet.server.backend.entity.Player;
import org.pokenet.server.battle.Pokemon;
import org.pokenet.server.client.Session;
import org.pokenet.server.constants.UserClasses;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;

public class StartBattlefrontierEvent implements MessageEvent
{

	public void Parse(Session session, ClientMessage request, ServerMessage message)
	{
		Player p = session.getPlayer();
		String battle = request.readString();
		if(battle.split("_")[0].equalsIgnoreCase("BattleTower"))
		{
			if(battle.split("_")[1].equalsIgnoreCase("lvl50"))
			{
				p.setX(128);
				p.setY(152);
				p.setMap(GameServer.getServiceManager().getMovementService().getMapMatrix().getMapByGamePosition(-42, 45), null);
				message.init(64);
				message.addInt(p.getX());
				message.addInt(p.getY());
				session.Send(message);
			}
			else
			{
				p.setX(128);
				p.setY(152);
				p.setMap(GameServer.getServiceManager().getMovementService().getMapMatrix().getMapByGamePosition(-41, 45), null);
				message.init(64);
				message.addInt(p.getX());
				message.addInt(p.getY());
				session.Send(message);
			}
		}
		else if(battle.split("_")[0].equalsIgnoreCase("BattlePalace"))
		{
			if(battle.split("_")[1].equalsIgnoreCase("lvl50"))
			{
				p.setX(320);
				p.setY(152);
				p.setMap(GameServer.getServiceManager().getMovementService().getMapMatrix().getMapByGamePosition(-44, 45), null);
				message.init(64);
				message.addInt(p.getX());
				message.addInt(p.getY());
				session.Send(message);
			}
			else
			{
				p.setX(320);
				p.setY(152);
				p.setMap(GameServer.getServiceManager().getMovementService().getMapMatrix().getMapByGamePosition(-43, 45), null);
				message.init(64);
				message.addInt(p.getX());
				message.addInt(p.getY());
				session.Send(message);
			}
		}
		else if(battle.split("_")[0].equalsIgnoreCase("BattleArena"))
		{
			
		}
		else if(battle.split("_")[0].equalsIgnoreCase("BattleFactory"))
		{
			if(battle.split("_")[1].equalsIgnoreCase("lvl50"))
			{
//				p.setX(160);
//				p.setY(184);
//				p.setMap(GameServer.getServiceManager().getMovementService().getMapMatrix().getMapByGamePosition(-45, 45), null);
//				message.init(64);
//				message.addInt(p.getX());
//				message.addInt(p.getY());
//				session.Send(message);
			}
			else
			{
				
			}
		}
		else if(battle.split("_")[0].equalsIgnoreCase("BattlePike"))
		{
			
		}
		else if(battle.split("_")[0].equalsIgnoreCase("BattlePyramide"))
		{
			
		}
		else if(battle.split("_")[0].equalsIgnoreCase("BattleDome"))
		{
			if(battle.split("_")[1].equalsIgnoreCase("lvl50"))
			{
				
			}
			else
			{
				
			}
		}
	}
}
