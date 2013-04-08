package org.pokenet.client.messages.events;

import org.pokenet.client.GameClient;
import org.pokenet.client.Session;
import org.pokenet.client.messages.MessageEvent;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.protocol.ServerMessage;

public class BattlefrontierEvent implements MessageEvent
{

	@Override
	public void parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		int mapx = GameClient.getInstance().getMapMatrix().getCurrentMap().m_x;
		int mapy = GameClient.getInstance().getMapMatrix().getCurrentMap().m_y;
		int x = GameClient.getInstance().getOurPlayer().getX();
		int y = GameClient.getInstance().getOurPlayer().getY();
		String battle = "no_battle";
		System.out.println(mapx + ", " + mapy + " - " + x + ", " + y);
		if ((mapx == -42 && mapy == 47 && x == 224 && y == 184) || (mapx == -42 && mapy == 47 && x == 608 && y == 184))
		{
//			System.out.println("battle tower lvl 50");
			battle = "BattleTower_lvl50";
		}
		else if ((mapx == -42 && mapy == 47 && x == 352 && y == 184) || (mapx == -42 && mapy == 47 && x == 480 && y == 184))
		{
			battle = "BattleTower_any";
		}
		if (!battle.equals("no_battle"))
			System.out.println("no battle");
			//GameClient.getInstance().getDisplay().add(new TrainChooserDialog(battle,GameClient.getInstance().getOurPlayer()));
	}
}
