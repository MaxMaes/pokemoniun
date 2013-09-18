package org.pokenet.server.messages.events;

import org.pokenet.server.GameServer;
import org.pokenet.server.backend.entity.Player;
import org.pokenet.server.backend.item.Item;
import org.pokenet.server.battle.DataService;
import org.pokenet.server.battle.Pokemon;
import org.pokenet.server.client.Session;
import org.pokenet.server.constants.ClientPacket;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;

public class MoveLearnRequestEvent implements MessageEvent
{

	public void Parse(Session session, ClientMessage request, ServerMessage message)
	{
		Player p = session.getPlayer();
		String moveName = request.readString();
		String price = request.readString();
		int idx = request.readInt();
		Item i = GameServer.getServiceManager().getItemDatabase().getItem(price);
		Pokemon poke = p.getParty()[idx];
		if(p.getBag().containsItem(i.getId()) != -1)
		{
			if(DataService.getMoveSetData().getMoveSet(poke.getSpeciesNumber()).canLearn(moveName))
			{
				p.setShopping(false);
				poke.getMovesLearning().add(moveName);
				ServerMessage msg = new ServerMessage(ClientPacket.MOVE_LEARN_LVL);
				msg.addInt(idx);
				msg.addString(moveName);
				session.Send(msg);
				p.getBag().removeItem(i.getId(), 1);

			}
		}
		else
		{
			p.setShopping(false);
			/* Return You don't have that item, fool! */
			ServerMessage msg = new ServerMessage(ClientPacket.DONT_HAVE_ITEM);
			msg.addString(price);
			p.getSession().Send(msg);
		}

	}
}
