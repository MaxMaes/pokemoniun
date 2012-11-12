package org.pokenet.server.messages.events;

import org.pokenet.server.client.Session;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;

public class GiveItemEvent implements MessageEvent
{

	public void Parse(Session Session, ClientMessage Request, ServerMessage Message)
	{
		// TODO: Write implementation for give packet.
		/* Old code used in TcpProtocolHandler.
		 * int pIndex = Integer.parseInt(message.split(",")[1]);
		 * if(p.getParty()[pIndex] != null)
		 * {
		 * if(p.getParty()[pIndex].getItemName().equals("") || p.getParty()[pIndex].getItemName() == null)
		 * {
		 * p.getParty()[pIndex].setItem(new HoldItem(GameServer.getServiceManager().getItemDatabase().getItem(Integer.parseInt(message.substring(1).split(",")[0])).getName()));
		 * p.getTcpSession().write(
		 * "Ir" + (message.substring(1).split(",")[0]) + ",1" + "," + (p.getParty()[pIndex].getName() + " was given " + p.getParty()[pIndex].getItemName() + " to hold"));
		 * p.getBag().removeItem(Integer.parseInt(message.substring(1).split(",")[0]), 1);
		 * }
		 * else
		 * {
		 * String pI = p.getParty()[pIndex].getItemName();
		 * p.getTcpSession().write("Iu" + GameServer.getServiceManager().getItemDatabase().getItem(p.getParty()[pIndex].getItemName()).getId() + ",1");
		 * p.getBag().addItem(GameServer.getServiceManager().getItemDatabase().getItem(p.getParty()[pIndex].getItemName()).getId(), 1);
		 * p.getParty()[pIndex].setItem(new HoldItem(GameServer.getServiceManager().getItemDatabase().getItem(Integer.parseInt(message.substring(1).split(",")[0])).getName()));
		 * p.getTcpSession().write("Ir" + (message.substring(1).split(",")[0]) + ",1" + "," + (pI + " was switched with " + p.getParty()[pIndex].getItemName()));
		 * p.getBag().removeItem(Integer.parseInt(message.substring(1).split(",")[0]), 1);
		 * }
		 * } */
	}

}
