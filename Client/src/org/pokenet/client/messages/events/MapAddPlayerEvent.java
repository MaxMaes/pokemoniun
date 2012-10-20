package org.pokenet.client.messages.events;

import org.pokenet.client.GameClient;
import org.pokenet.client.Session;
import org.pokenet.client.backend.entity.HMObject;
import org.pokenet.client.backend.entity.Player;
import org.pokenet.client.backend.entity.Player.Direction;
import org.pokenet.client.messages.MessageEvent;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.protocol.ServerMessage;

public class MapAddPlayerEvent implements MessageEvent
{

	@Override
	public void parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		// Add player
		String[] details = Request.readString().split(",");
		Player p = new Player();
		try
		{
			HMObject hm = new HMObject(HMObject.parseHMObject(details[0]));
			hm.setId(Integer.parseInt(details[1]));
			hm.setSprite(Integer.parseInt(details[2]));
			hm.setX(Integer.parseInt(details[3]));
			hm.setServerX(Integer.parseInt(details[3]));
			hm.setY(Integer.parseInt(details[4]));
			hm.setServerY(Integer.parseInt(details[4]));
			hm.setDirection(Direction.Down);
			hm.loadSpriteImage();
			p = hm;
			p.setId(hm.getId());
		}
		catch(Exception e)
		{
			p.setUsername(details[0]);
			p.setId(Integer.parseInt(details[1]));
			p.setSprite(Integer.parseInt(details[2]));
			p.setX(Integer.parseInt(details[3]));
			p.setY(Integer.parseInt(details[4]));
			p.setServerX(Integer.parseInt(details[3]));
			p.setServerY(Integer.parseInt(details[4]));
			switch(details[5].charAt(0))
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
		}

		GameClient.getInstance().getMapMatrix().addPlayer(p);
	}
}
