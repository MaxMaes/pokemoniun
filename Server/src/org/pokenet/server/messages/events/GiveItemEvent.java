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

	public void Parse(Session session, ClientMessage request, ServerMessage message)
	{
		String[] data = request.readString().split(",");
		int itemId = Integer.parseInt(data[0]);
		int pokeIndex = Integer.parseInt(data[1]);

		Pokemon p = session.getPlayer().getParty()[pokeIndex];
		if(p != null)
		{
			if(p.getItemName().equals("") || p.getItemName() == null)
			{
				HoldItem h = new HoldItem(GameServer.getServiceManager().getItemDatabase().getItem(itemId).getName());
				p.setItem(h);
				ServerMessage removeItemMessage = new ServerMessage(session);
				removeItemMessage.init(81);
				removeItemMessage.addInt(itemId);
				removeItemMessage.addInt(1);
				removeItemMessage.sendResponse();
				session.getPlayer().getBag().removeItem(itemId, 1);

				ServerMessage speech = new ServerMessage(session);
				speech.init(92);
				speech.addString(p.getName() + " was given " + p.getItemName() + " to hold");
				speech.sendResponse();
			}
			else
			{
				String pI = p.getItemName();
				ServerMessage addItemMessage = new ServerMessage(session);
				addItemMessage.init(80);
				addItemMessage.addInt(itemId);
				addItemMessage.addInt(1);
				addItemMessage.sendResponse();
				session.getPlayer().getBag().addItem(itemId, 1);

				HoldItem h = new HoldItem(GameServer.getServiceManager().getItemDatabase().getItem(itemId).getName());
				p.setItem(h);
				ServerMessage holdItemMessage = new ServerMessage(session);
				holdItemMessage.init(81);
				holdItemMessage.addInt(itemId);
				holdItemMessage.addInt(1);
				holdItemMessage.sendResponse();
				session.getPlayer().getBag().removeItem(itemId, 1);

				ServerMessage speech = new ServerMessage(session);
				speech.init(92);
				speech.addString(pI + " was switched with " + p.getItemName());
				speech.sendResponse();
			}
		}
	}
}
