package org.pokemonium.client.messages.events;

import org.pokemonium.client.GameClient;
import org.pokemonium.client.Session;
import org.pokemonium.client.messages.MessageEvent;
import org.pokemonium.client.protocol.ClientMessage;
import org.pokemonium.client.protocol.ServerMessage;

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
		if((mapx == -41 && mapy == 47 && x == 224 && y == 184) || (mapx == -41 && mapy == 47 && x == 608 && y == 184))
		{
			// System.out.println("battle tower lvl 50");
			battle = "BattleTower_lvl50";
		}
		else if((mapx == -41 && mapy == 47 && x == 352 && y == 184) || (mapx == -41 && mapy == 47 && x == 480 && y == 184))
		{
			battle = "BattleTower_any";
		}
		else if(mapx == -40 && mapy == 47 && x == 256 && y == 376)
		{
			battle = "BattlePyramide_any";
		}
		else if(mapx == -43 && mapy == 47 && x == 192 && y == 184)
		{
			battle = "BattlePalace_lvl50";
		}
		else if(mapx == -43 && mapy == 47 && x == 640 && y == 184)
		{
			battle = "BattlePalace_any";
		}
		else if(mapx == -48 && mapy == 47 && x == 256 && y == 248)
		{
			battle = "BattleArena_any";
		}
		else if(mapx == -42 && mapy == 47 && x == 192 && y == 184)
		{
			battle = "BattlePalace_any";
		}
		else if(mapx == -47 && mapy == 47 && x == 192 && y == 312)
		{
			battle = "BattleDome_lvl50";
		}
		else if(mapx == -47 && mapy == 47 && x == 576 && y == 312)
		{
			battle = "BattleDome_any";
		}
		else if(mapx == -44 && mapy == 47 && x == 160 && y == 248)
		{
			battle = "BattleFactory_lvl50";
		}
		else if(mapx == -44 && mapy == 47 && x == 480 && y == 248)
		{
			battle = "BattleFactory_any";
		}
		if(!battle.equals("no_battle"))
			// System.out.println("no battle");
			GameClient.getInstance().getHUD().showBattleFrontierDialog(battle, GameClient.getInstance().getOurPlayer());
	}
}
