package org.pokenet.client.messages.events;

import org.pokenet.client.GameClient;
import org.pokenet.client.Session;
import org.pokenet.client.backend.entity.HMObject;
import org.pokenet.client.backend.entity.OurPlayer;
import org.pokenet.client.backend.entity.Player;
import org.pokenet.client.backend.entity.Player.Direction;
import org.pokenet.client.messages.MessageEvent;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.protocol.ServerMessage;

public class MapInitPlayersEvent implements MessageEvent
{

	@Override
	public void parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		GameClient.getInstance().getMapMatrix().getPlayers().clear();
		GameClient.getInstance().getMapMatrix().getPlayers().trimToSize();
		String[] details = Request.readString().split(",");
		/* Parse all the information. This packet contains details for all players on this map */
		for(int i = 0; i < details.length - 1; i++)
		{
			Player p = new Player();
			try
			{
				HMObject hm = new HMObject(HMObject.parseHMObject(details[i]));
				i++;
				hm.setId(Integer.parseInt(details[i]));
				i++;
				hm.setSprite(Integer.parseInt(details[i]));
				i++;
				hm.setX(Integer.parseInt(details[i]));
				hm.setServerX(Integer.parseInt(details[i]));
				i++;
				hm.setY(Integer.parseInt(details[i]));
				hm.setServerY(Integer.parseInt(details[i]));
				i++;
				hm.setDirection(Direction.Down);
				hm.loadSpriteImage();
				p = hm;
			}
			catch(Exception e)
			{
				p.setUsername(details[i]);
				i++;
				p.setId(Integer.parseInt(details[i]));
				i++;
				p.setSprite(Integer.parseInt(details[i]));
				i++;
				p.setX(Integer.parseInt(details[i]));
				p.setServerX(Integer.parseInt(details[i]));
				i++;
				p.setY(Integer.parseInt(details[i]));
				p.setServerY(Integer.parseInt(details[i]));
				i++;
				switch(details[i].charAt(0))
				{
					case 'D':
						p.setDirection(Direction.Down);
						break;
					case 'L':
						p.setDirection(Direction.Left);
						break;
					case 'R':
						p.setDirection(Direction.Right);
						break;
					case 'U':
						p.setDirection(Direction.Up);
						break;
					default:
						p.setDirection(Direction.Down);
						break;
				}
				i++;
				p.setAdminLevel(Integer.parseInt(details[i]));
				p.loadSpriteImage();
			}

			if(p.getId() == GameClient.getInstance().getPlayerId())
			{
				/* This dude is our player! Store this information */
				p.setOurPlayer(true);
				OurPlayer pl;
				if(GameClient.getInstance().getOurPlayer() == null)
					pl = new OurPlayer();
				else
					pl = new OurPlayer(GameClient.getInstance().getOurPlayer());
				pl.set(p);
				GameClient.getInstance().setOurPlayer(pl);
				GameClient.getInstance().getMapMatrix().addPlayer(pl);
				GameClient.getInstance().setOurPlayer(pl);
				GameClient.getInstance().getOurPlayer().setAnimating(true);
			}
			else
				GameClient.getInstance().getMapMatrix().addPlayer(p);
		}
	}
}
