package org.pokenet.server.messages.events;

import org.pokenet.server.GameServer;
import org.pokenet.server.backend.entity.Player;
import org.pokenet.server.client.Session;
import org.pokenet.server.constants.UserClasses;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;

public class TravelEvent implements MessageEvent
{

	public void Parse(Session session, ClientMessage request, ServerMessage message)
	{
		Player p = session.getPlayer();
		String travel = request.readString();
		if(p.getIsTaveling())
		{
			boolean ticket = false;
			if(travel.contains("Vermillion City"))
			{
				if(p.getAdminLevel() >= UserClasses.MODERATOR || (p.getMoney() >= 10000 && p.getTrainingLevel() >= 25))
				{
					if(p.getAdminLevel() <= UserClasses.DONATOR)
					{
						p.setMoney(p.getMoney() - 10000);
						p.updateClientMoney();
					}
					p.setIsTaveling(false);
					p.setX(736);
					p.setY(1048);
					p.setMap(GameServer.getServiceManager().getMovementService().getMapMatrix().getMapByGamePosition(6, 0), null);
					message.init(64);
					message.addInt(p.getX());
					message.addInt(p.getY());
					session.Send(message);
				}
			}
			else if(travel.contains("Saffron City"))
			{
				if(p.getAdminLevel() >= UserClasses.MODERATOR || (p.getMoney() >= 10000 && p.getTrainingLevel() >= 25))
				{
					if(p.getAdminLevel() <= UserClasses.DONATOR)
					{
						p.setMoney(p.getMoney() - 10000);
						p.updateClientMoney();
					}
					p.setIsTaveling(false);
					p.setX(128);
					p.setY(56);
					p.setMap(GameServer.getServiceManager().getMovementService().getMapMatrix().getMapByGamePosition(-50, -13), null);
					message.init(64);
					message.addInt(p.getX());
					message.addInt(p.getY());
					session.Send(message);
				}
			}
			// Fuchsia City kanto
			else if(travel.contains("Safari Zone"))
			{
				if(p.getAdminLevel() >= UserClasses.MODERATOR || (p.getMoney() >= 30000 && p.getTrainingLevel() >= 25))
				{
					if(p.getAdminLevel() <= UserClasses.DONATOR)
					{
						p.setMoney(p.getMoney() - 30000);
						p.updateClientMoney();
					}
					p.setIsTaveling(false);
					p.setX(800);
					p.setY(952);
					p.setMap(GameServer.getServiceManager().getMovementService().getMapMatrix().getMapByGamePosition(16, 0), null);
					message.init(64);
					message.addInt(p.getX());
					message.addInt(p.getY());
					session.Send(message);
				}
			}
			else if(travel.contains("Mt.Silver"))
			{
				if(p.getAdminLevel() >= UserClasses.MODERATOR || (p.getTrainingLevel() >= 25 && p.getBadgeCount() >= 16))
				{
					p.setIsTaveling(false);
					p.setX(1664);
					p.setY(888);
					p.setMap(GameServer.getServiceManager().getMovementService().getMapMatrix().getMapByGamePosition(1, 1), null);
					message.init(64);
					message.addInt(p.getX());
					message.addInt(p.getY());
					session.Send(message);
				}
			}
			else if(travel.contains("Olivine City"))
			{
				if(p.getAdminLevel() >= UserClasses.MODERATOR || (p.getMoney() >= 10000 && p.getTrainingLevel() >= 25))
				{
					if(p.getAdminLevel() > UserClasses.DEFAULT)
					{
					}
					else if(!ticket)
					{
						p.setMoney(p.getMoney() - 10000);
						p.updateClientMoney();
					}
					p.setIsTaveling(false);
					p.setX(640);
					p.setY(664);
					p.setMap(GameServer.getServiceManager().getMovementService().getMapMatrix().getMapByGamePosition(-6, -3), null);
					message.init(64);
					message.addInt(p.getX());
					message.addInt(p.getY());
					session.Send(message);
				}
			}
			else if(travel.contains("Goldenrod City"))
			{
				if(p.getAdminLevel() >= UserClasses.MODERATOR || (p.getMoney() >= 10000 && p.getTrainingLevel() >= 25))
				{
					if(!(p.getAdminLevel() > UserClasses.DEFAULT))
					{
						p.setMoney(p.getMoney() - 10000);
						p.updateClientMoney();
					}
					p.setIsTaveling(false);
					p.setX(352);
					p.setY(152);
					p.setMap(GameServer.getServiceManager().getMovementService().getMapMatrix().getMapByGamePosition(-31, -39), null);
					message.init(64);
					message.addInt(p.getX());
					message.addInt(p.getY());
					session.Send(message);
				}
			}
			else if(travel.contains("Slateport"))
			{
				if(p.getBag().containsItem(559) != -1)
				{
					ticket = true;
				}
				if(p.getAdminLevel() >= UserClasses.MODERATOR || ((p.getMoney() >= 125000 || (ticket && p.getMoney() >= 10000)) && p.getBadgeCount() >= 16))
				{
					if(p.getAdminLevel() >= UserClasses.MODERATOR)
					{
					}
					else if(ticket)
					{
						p.setMoney(p.getMoney() - 10000);
						p.updateClientMoney();
					}
					else
					{
						p.setMoney(p.getMoney() - 125000);
						p.updateClientMoney();

					}
					p.getBag().addItem(559, 1);
					p.setIsTaveling(false);
					p.setX(896);
					p.setY(408);
					p.setMap(GameServer.getServiceManager().getMovementService().getMapMatrix().getMapByGamePosition(27, 24), null);
					message.init(64);
					message.addInt(p.getX());
					message.addInt(p.getY());
					session.Send(message);
				}
			}
			else if(travel.contains("Lilycove"))
			{
				if(p.getBag().containsItem(559) != -1)
				{
					ticket = true;
				}
				if(p.getAdminLevel() >= UserClasses.MODERATOR || ((p.getMoney() >= 125000 || (ticket && p.getMoney() >= 10000)) && p.getBadgeCount() >= 16))
				{
					if(p.getAdminLevel() >= UserClasses.MODERATOR)
					{
					}
					else if(ticket)
					{
						p.setMoney(p.getMoney() - 10000);
						p.updateClientMoney();
					}
					else
					{
						p.setMoney(p.getMoney() - 125000);
						p.updateClientMoney();

					}
					p.getBag().addItem(559, 1);
					p.setIsTaveling(false);
					p.setX(384);
					p.setY(1112);
					p.setMap(GameServer.getServiceManager().getMovementService().getMapMatrix().getMapByGamePosition(32, 20), null);
					message.init(64);
					message.addInt(p.getX());
					message.addInt(p.getY());
					session.Send(message);
				}
			}
			else if(travel.contains("Canalave"))
			{
				if(p.getBag().containsItem(557) != -1)
				{
					ticket = true;
				}
				if(p.getAdminLevel() >= UserClasses.MODERATOR || (p.getMapX() == 1 && p.getMapY() == -46)
						|| ((p.getMoney() >= 175000 || (ticket && p.getMoney() >= 10000)) && p.getBadgeCount() >= 20 && p.getTrainingLevel() >= 40))
				{

					if(p.getAdminLevel() >= UserClasses.MODERATOR || (p.getMapX() == 1 && p.getMapY() == -46))
					{
					}
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
					p.setMap(GameServer.getServiceManager().getMovementService().getMapMatrix().getMapByGamePosition(33, -42), null);
					message.init(64);
					message.addInt(p.getX());
					message.addInt(p.getY());
					session.Send(message);
				}
			}
			else if(travel.contains("Snowpoint"))
			{
				if(p.getBag().containsItem(557) != -1)
				{
					ticket = true;
				}
				if(p.getAdminLevel() >= UserClasses.MODERATOR || ((p.getMoney() >= 175000 || (ticket && p.getMoney() >= 10000)) && p.getBadgeCount() >= 20 && p.getTrainingLevel() >= 40))
				{
					if(p.getAdminLevel() >= UserClasses.MODERATOR)
					{
					}
					else if(ticket)
					{
						p.setMoney(p.getMoney() - 10000);
						p.updateClientMoney();
					}
					else
					{
						p.setMoney(p.getMoney() - 175000);
						p.updateClientMoney();

					}
					p.getBag().addItem(557, 1);
					p.setIsTaveling(false);
					p.setX(192);
					p.setY(1880);
					p.setMap(GameServer.getServiceManager().getMovementService().getMapMatrix().getMapByGamePosition(39, -48), null);
					message.init(64);
					message.addInt(p.getX());
					message.addInt(p.getY());
					session.Send(message);
				}
			}
			else if(travel.contains("One"))
			{
				if(p.getAdminLevel() >= UserClasses.MODERATOR || (p.getMoney() >= 5000 && p.getBadgeCount() >= 16))
				{
					if(p.getAdminLevel() <= UserClasses.DONATOR)
					{
						p.setMoney(p.getMoney() - 5000);
						p.updateClientMoney();
					}
					p.setIsTaveling(false);
					p.setX(512);
					p.setY(568);
					p.setMap(GameServer.getServiceManager().getMovementService().getMapMatrix().getMapByGamePosition(-29, 2), null);
					message.init(64);
					message.addInt(p.getX());
					message.addInt(p.getY());
					session.Send(message);
				}
			}
			else if(travel.contains("Two"))
			{
				if(p.getAdminLevel() >= UserClasses.MODERATOR || (p.getMoney() >= 5000 && p.getBadgeCount() >= 16))
				{
					if(p.getAdminLevel() <= UserClasses.DONATOR)
					{
						p.setMoney(p.getMoney() - 5000);
						p.updateClientMoney();
					}
					p.setIsTaveling(false);
					p.setX(320);
					p.setY(1336);
					p.setMap(GameServer.getServiceManager().getMovementService().getMapMatrix().getMapByGamePosition(-44, 5), null);
					message.init(64);
					message.addInt(p.getX());
					message.addInt(p.getY());
					session.Send(message);
				}
			}
			else if(travel.contains("Three"))
			{
				if(p.getAdminLevel() >= UserClasses.MODERATOR || (p.getMoney() >= 5000 && p.getBadgeCount() >= 16))
				{
					if(p.getAdminLevel() <= UserClasses.DONATOR)
					{
						p.setMoney(p.getMoney() - 5000);
						p.updateClientMoney();
					}
					p.setIsTaveling(false);
					p.setX(416);
					p.setY(408);
					p.setMap(GameServer.getServiceManager().getMovementService().getMapMatrix().getMapByGamePosition(-28, 5), null);
					message.init(64);
					message.addInt(p.getX());
					message.addInt(p.getY());
					session.Send(message);
				}
			}
			else if(travel.contains("Four"))
			{
				if(p.getAdminLevel() >= UserClasses.MODERATOR)
				{
					p.setIsTaveling(false);
					p.setX(416);
					p.setY(1048);
					p.setMap(GameServer.getServiceManager().getMovementService().getMapMatrix().getMapByGamePosition(-44, 10), null);
					message.init(64);
					message.addInt(p.getX());
					message.addInt(p.getY());
					session.Send(message);
				}
			}
			else if(travel.contains("Five"))
			{
				if(p.getAdminLevel() >= UserClasses.MODERATOR)
				{
					p.setIsTaveling(false);
					p.setX(512);
					p.setY(664);
					p.setMap(GameServer.getServiceManager().getMovementService().getMapMatrix().getMapByGamePosition(-29, 8), null);
					message.init(64);
					message.addInt(p.getX());
					message.addInt(p.getY());
					session.Send(message);
				}
			}
			else if(travel.contains("Navel"))
			{
				if(p.getAdminLevel() >= UserClasses.DEVELOPER)
				{
					p.setIsTaveling(false);
					p.setX(672);
					p.setY(3192);
					p.setMap(GameServer.getServiceManager().getMovementService().getMapMatrix().getMapByGamePosition(-39, 12), null);
					message.init(64);
					message.addInt(p.getX());
					message.addInt(p.getY());
					session.Send(message);
				}
			}
			else if(travel.contains("Iron"))
			{
				if(p.getAdminLevel() >= UserClasses.MODERATOR || (p.getMoney() >= 15000 && p.getBadgeCount() >= 24))
				{
					if(p.getAdminLevel() <= UserClasses.DONATOR)
					{
						p.setMoney(p.getMoney() - 15000);
						p.updateClientMoney();
					}
					p.setIsTaveling(false);
					p.setX(2752);
					p.setY(568);
					p.setMap(GameServer.getServiceManager().getMovementService().getMapMatrix().getMapByGamePosition(1, -46), null);
					message.init(64);
					message.addInt(p.getX());
					message.addInt(p.getY());
					session.Send(message);
				}
			}
			else if(travel.contains("Battlefrontier"))
			{
				if(p.getAdminLevel() >= UserClasses.SUPER_MOD)
				{
					p.setIsTaveling(false);
					p.setX(512);
					p.setY(2520);
					p.setMap(GameServer.getServiceManager().getMovementService().getMapMatrix().getMapByGamePosition(29, 26), null);
					// p.getTcpSession().write("U" + p.getX() + "," + p.getY());
					message.init(64);
					message.addInt(p.getX());
					message.addInt(p.getY());
					session.Send(message);
				}
			}
		}
	}
}
