package org.pokenet.server.messages.events;

import org.pokenet.server.GameServer;
import org.pokenet.server.battle.Pokemon;
import org.pokenet.server.battle.mechanics.statuses.items.HoldItem;
import org.pokenet.server.client.Session;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;

public class GiveItemEvent implements MessageEvent
{

	public void Parse(Session Session, ClientMessage Request, ServerMessage Message)
	{
		// TODO: Write implementation for give packet.
		/*
		 * Old code used in TcpProtocolHandler. int pIndex = Integer.parseInt(message.split(",")[1]); if(p.getParty()[pIndex] != null) { if(p.getParty()[pIndex].getItemName().equals("") || p.getParty()[pIndex].getItemName() == null) { p.getParty()[pIndex].setItem(new HoldItem(GameServer.getServiceManager().getItemDatabase().getItem(Integer.parseInt(message.substring(1).split(",")[0])).getName())); p.getTcpSession().write( "Ir" + (message.substring(1).split(",")[0]) + ",1" + "," + (p.getParty()[pIndex].getName() + " was given " + p.getParty()[pIndex].getItemName() + " to hold")); p.getBag().removeItem(Integer.parseInt(message.substring(1).split(",")[0]), 1); } else { String pI = p.getParty()[pIndex].getItemName(); p.getTcpSession().write("Iu" + GameServer.getServiceManager().getItemDatabase().getItem(p.getParty()[pIndex].getItemName()).getId() + ",1"); p.getBag().addItem(GameServer.getServiceManager().getItemDatabase().getItem(p.getParty()[pIndex].getItemName()).getId(), 1); p.getParty()[pIndex].setItem(new HoldItem(GameServer.getServiceManager().getItemDatabase().getItem(Integer.parseInt(message.substring(1).split(",")[0])).getName())); p.getTcpSession().write("Ir" + (message.substring(1).split(",")[0]) + ",1" + "," + (pI + " was switched with " + p.getParty()[pIndex].getItemName())); p.getBag().removeItem(Integer.parseInt(message.substring(1).split(",")[0]), 1); } }
		 */
		
		String[] data = Request.readString().split(",");
		int itemId = Integer.parseInt(data[0]);
		int pokeIndex = Integer.parseInt(data[1]);
		
		Pokemon p = Session.getPlayer().getParty()[pokeIndex];
		if(p != null)
		{
			if(p.getItemName().equals("") || p.getItemName() == null)
			{
				HoldItem h = new HoldItem(GameServer.getServiceManager().getItemDatabase().getItem(itemId).getName());
				p.setItem(h);
				ServerMessage message = new ServerMessage(Session);
				message.Init(81);
				message.addInt(itemId);
				message.addInt(1);
				message.sendResponse();
				Session.getPlayer().getBag().removeItem(itemId, 1);
				
				ServerMessage speech = new ServerMessage(Session);
				speech.Init(92);
				speech.addString(p.getName() + " was given " + p.getItemName() + " to hold");
				speech.sendResponse();
			}
			else
			{
				String pI = p.getItemName();
				ServerMessage message = new ServerMessage(Session);
				message.Init(80);
				message.addInt(itemId);
				message.addInt(1);
				message.sendResponse();
				Session.getPlayer().getBag().addItem(itemId, 1);
				HoldItem h = new HoldItem(GameServer.getServiceManager().getItemDatabase().getItem(itemId).getName());
				p.setItem(h);
				ServerMessage holdItemMessage = new ServerMessage(Session);
				holdItemMessage.Init(81);
				holdItemMessage.addInt(itemId);
				holdItemMessage.addInt(1);
				holdItemMessage.sendResponse();
				Session.getPlayer().getBag().removeItem(itemId, 1);
				
				ServerMessage speech = new ServerMessage(Session);
				speech.Init(92);
				speech.addString(pI + " was switched with " + p.getItemName());
				speech.sendResponse();
			}
		}
		
	}

}
