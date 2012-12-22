package org.pokenet.client.messages.events;

import org.pokenet.client.GameClient;
import org.pokenet.client.Session;
import org.pokenet.client.messages.MessageEvent;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.protocol.ServerMessage;
import org.pokenet.client.ui.frames.BoatChooserDialog;

public class BoatEvent implements MessageEvent
{

	@Override
	public void parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		int mapx = GameClient.getInstance().getMapMatrix().getCurrentMap().m_x;
		int mapy = GameClient.getInstance().getMapMatrix().getCurrentMap().m_y;
		String boat = "no_travel";
		if (mapx == 7 && mapy == 1)
		{
			boat = "kanto";
		}
		else if (mapx == -5 && mapy == -2)
		{
			boat = "johto";
		}
		else if (mapx == 28 && mapy == 25)
		{
			boat = "slateport";
		}
		else if (mapx == 33 && mapy == 21)
		{
			boat = "lilycove";
		}
		else if (mapx == 34 && mapy == -41)
		{
			boat = "canalave";
		}
		GameClient.getInstance().getDisplay().add(new BoatChooserDialog(boat,GameClient.getInstance().getOurPlayer()));
		
	}
}
