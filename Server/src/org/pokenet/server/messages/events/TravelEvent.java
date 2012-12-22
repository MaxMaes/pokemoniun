package org.pokenet.server.messages.events;

import org.pokenet.server.GameServer;
import org.pokenet.server.backend.entity.Player;
import org.pokenet.server.client.Session;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;

public class TravelEvent implements MessageEvent
{

	public void Parse(Session Session, ClientMessage Request, ServerMessage Message)
	{

		Player p = Session.getPlayer();
		String travel = Request.readString();

		if(p.getIsTaveling())
		{
//			String travel = message.substring(1);
			boolean ticket = false;
			ServerMessage message = new ServerMessage();
			if (travel.contains("Vermillion City"))
			{
				if(p.getAdminLevel() > 0 || (p.getMoney() >= 10000 && p.getTrainingLevel()>=25))
				{
					if(p.getAdminLevel() == 0)
					{
						p.setMoney(p.getMoney() - 10000);
						p.updateClientMoney();
					}
					p.setIsTaveling(false);
					p.setX(736);
					p.setY(1048);
					//System.out.println(p.getX() + ", " + p.getY());
					p.setMap(GameServer.getServiceManager().getMovementService().
							getMapMatrix().getMapByGamePosition(6, 0), null);
//						p.getTcpSession().write("U" + p.getX() + "," + p.getY());
						
						message.Init(64);
						message.addInt(p.getX());
						message.addInt(p.getY());
						Session.Send(message);
					
				}
			}
			else if (travel.contains("Saffron City"))
			{
				if(p.getAdminLevel() > 0 || (p.getMoney() >= 10000 && p.getTrainingLevel()>=25))
				{
					if(p.getAdminLevel() == 0)
					{
						p.setMoney(p.getMoney() - 10000);
						p.updateClientMoney();
					}
					p.setIsTaveling(false);
					p.setX(128);
					p.setY(56);
					p.setMap(GameServer.getServiceManager().getMovementService().
							getMapMatrix().getMapByGamePosition(-50, -13), null);
//					p.getTcpSession().write("U" + p.getX() + "," + p.getY());
					message.Init(64);
					message.addInt(p.getX());
					message.addInt(p.getY());
					Session.Send(message);
				}
			}
			//Fuchsia City kanto
			else if (travel.contains("Safari Zone"))
			{
				if(p.getAdminLevel() > 0 || (p.getMoney() >= 30000 && p.getTrainingLevel()>=25))
				{
					if(p.getAdminLevel() == 0)
					{
						p.setMoney(p.getMoney() - 30000);
						p.updateClientMoney();
					}
					p.setIsTaveling(false);
					p.setX(800);
					p.setY(952);
					p.setMap(GameServer.getServiceManager().getMovementService().
							getMapMatrix().getMapByGamePosition(16, 0), null);
//					p.getTcpSession().write("U" + p.getX() + "," + p.getY());
					message.Init(64);
					message.addInt(p.getX());
					message.addInt(p.getY());
					Session.Send(message);
				}
			}
			else if (travel.contains("Mt.Silver"))
			{
				if(p.getAdminLevel() > 0 || (p.getTrainingLevel()>=25 && p.getBadgeCount()>=16))
				{
					p.setIsTaveling(false);
					p.setX(32*32);
					p.setY(14*28);
					p.setMap(GameServer.getServiceManager().getMovementService().
							getMapMatrix().getMapByGamePosition(0, 1), null);
//					p.getTcpSession().write("U" + p.getX() + "," + p.getY());
					message.Init(64);
					message.addInt(p.getX());
					message.addInt(p.getY());
					Session.Send(message);
				}
			}
			else if (travel.contains("Olivine City"))
			{
				if(p.getAdminLevel() > 0 || (p.getMoney() >= 10000 && p.getTrainingLevel()>=25))
				{
					if(p.getAdminLevel() > 0)
					{}
					else if(!ticket){
						p.setMoney(p.getMoney() - 10000);
						p.updateClientMoney();
					}
					p.setIsTaveling(false);
					p.setX(640);
					p.setY(664);
					p.setMap(GameServer.getServiceManager().getMovementService().
							getMapMatrix().getMapByGamePosition(-6, -3), null);
//					p.getTcpSession().write("U" + p.getX() + "," + p.getY());
					message.Init(64);
					message.addInt(p.getX());
					message.addInt(p.getY());
					Session.Send(message);
				}
			}
			else if (travel.contains("Goldenrod City"))
			{
				if(p.getAdminLevel() > 0 || (p.getMoney() >= 10000 && p.getTrainingLevel()>=25))
				{
					if(!(p.getAdminLevel() > 0))
					{
						p.setMoney(p.getMoney() - 10000);
						p.updateClientMoney();
					}
					p.setIsTaveling(false);
					p.setX(352);
					p.setY(152);
					p.setMap(GameServer.getServiceManager().getMovementService().
							getMapMatrix().getMapByGamePosition(-31, -39), null);
//					p.getTcpSession().write("U" + p.getX() + "," + p.getY());
					message.Init(64);
					message.addInt(p.getX());
					message.addInt(p.getY());
					Session.Send(message);
				}
			}
			else if (travel.contains("Slateport"))
			{
				if(p.getBag().containsItem(559) != -1)
				{
					ticket = true;
				}
				if(p.getAdminLevel() > 0 || ((p.getMoney() >= 125000 || (ticket && p.getMoney() >= 10000))&& p.getBadgeCount()>=16))
				{
					if(p.getAdminLevel() > 0)
					{}
					else if(ticket)
					{
						p.setMoney(p.getMoney() - 10000);
						p.updateClientMoney();
					}
					else
					{
						p.setMoney(p.getMoney() - 125000);
						p.updateClientMoney();
						p.getBag().addItem(559, 1);
					}
					p.setIsTaveling(false);
					p.setX(960);
					p.setY(376);
					p.setMap(GameServer.getServiceManager().getMovementService().
							getMapMatrix().getMapByGamePosition(27, 24), null);
//					p.getTcpSession().write("U" + p.getX() + "," + p.getY());
					message.Init(64);
					message.addInt(p.getX());
					message.addInt(p.getY());
					Session.Send(message);
				}
			}
			else if (travel.contains("Lilycove"))
			{
				if(p.getBag().containsItem(559) != -1)
				{
					ticket = true;
				}
				if(p.getAdminLevel() > 0 || ((p.getMoney() >= 125000 || (ticket && p.getMoney() >= 10000)) && p.getBadgeCount()>=16))
				{
					if(p.getAdminLevel() > 0)
					{}
					else if(ticket)
					{
						p.setMoney(p.getMoney() - 10000);
						p.updateClientMoney();
					}
					else
					{
						p.setMoney(p.getMoney() - 125000);
						p.updateClientMoney();
						p.getBag().addItem(559, 1);
					}
					p.setIsTaveling(false);
					p.setX(384);
					p.setY(1112);
					p.setMap(GameServer.getServiceManager().getMovementService().
							getMapMatrix().getMapByGamePosition(32, 20), null);
//					p.getTcpSession().write("U" + p.getX() + "," + p.getY());
					message.Init(64);
					message.addInt(p.getX());
					message.addInt(p.getY());
					Session.Send(message);
				}
			}
			else if (travel.contains("Canalave"))
			{
				if(p.getBag().containsItem(557) != -1)
				{
					ticket = true;
				}
				if(p.getAdminLevel() > 0 || ((p.getMoney() >= 175000 || (ticket && p.getMoney() >= 10000)) && p.getBadgeCount()>=16 && p.getTrainingLevel()>=50))
				{
					if(p.getAdminLevel() > 0)
					{}
					else if(ticket)
					{
						p.setMoney(p.getMoney() - 10000);
						p.updateClientMoney();
					}
					else
					{
						p.setMoney(p.getMoney() - 175000);
						p.updateClientMoney();
						p.getBag().addItem(557, 1);
					}
					p.setIsTaveling(false);
					p.setX(384);
					p.setY(1336);
					p.setMap(GameServer.getServiceManager().getMovementService().
							getMapMatrix().getMapByGamePosition(33, -42), null);
//					p.getTcpSession().write("U" + p.getX() + "," + p.getY());
					message.Init(64);
					message.addInt(p.getX());
					message.addInt(p.getY());
					Session.Send(message);
				}
			}
		}

	}
}
