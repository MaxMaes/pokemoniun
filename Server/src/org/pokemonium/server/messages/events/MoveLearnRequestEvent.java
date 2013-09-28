package org.pokemonium.server.messages.events;

import org.pokemonium.server.GameServer;
import org.pokemonium.server.backend.entity.Player;
import org.pokemonium.server.backend.item.Item;
import org.pokemonium.server.battle.DataService;
import org.pokemonium.server.battle.Pokemon;
import org.pokemonium.server.client.Session;
import org.pokemonium.server.constants.ClientPacket;
import org.pokemonium.server.messages.MessageEvent;
import org.pokemonium.server.protocol.ClientMessage;
import org.pokemonium.server.protocol.ServerMessage;

public class MoveLearnRequestEvent implements MessageEvent
{

	public void Parse(Session session, ClientMessage request, ServerMessage message)
	{
		Player p = session.getPlayer();
		String moveName = request.readString();
		String price = request.readString();
		int idx = request.readInt();
		Pokemon poke = p.getParty()[idx];
		if(price.charAt(0) == '$')
		{
			p.setShopping(false);
			int money = Integer.parseInt(price.substring(1, 3));
			money *= 1000;
			if(p.getMoney() >= money)
			{
				p.setMoney(p.getMoney() - money);
				p.updateClientMoney();

				ServerMessage msg = new ServerMessage(ClientPacket.MOVE_LEARN_LVL);
				msg.addInt(idx);
				msg.addString(moveName);
				session.Send(msg);
			}
			else
			{
				message = new ServerMessage(ClientPacket.NOT_ENOUGH_MONEY);
				p.getSession().Send(message);
				p.setShopping(false);
			}
		}
		else if(price.charAt(2) == 'B' && price.charAt(2) == 'P')
		{
			p.setShopping(false);
			// this is BP. TODO: not implemented yet
		}
		else
		{
			Item i = GameServer.getServiceManager().getItemDatabase().getItem(price);
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
}
