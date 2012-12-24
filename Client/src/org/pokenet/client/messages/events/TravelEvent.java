package org.pokenet.client.messages.events;

import org.pokenet.client.GameClient;
import org.pokenet.client.Session;
import org.pokenet.client.messages.MessageEvent;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.protocol.ServerMessage;
import org.pokenet.client.ui.frames.TrainChooserDialog;

public class TravelEvent implements MessageEvent
{

	@Override
	public void parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		int mapx = GameClient.getInstance().getMapMatrix().getCurrentMap().m_x;
		int mapy = GameClient.getInstance().getMapMatrix().getCurrentMap().m_y;
		String travel = "no_travel";
		System.out.println(mapx + ", " + mapy);
		if (mapx == -49 && mapy == -12)
		{
//			System.out.println("kanto time");
			travel = "kanto";
		}
		else if (mapx == -30 && mapy == -38)
		{
			travel = "johto";
		}
		else if (mapx == 13 && mapy == 18)
		{
			travel = "Fuchsia";
		}
		else if (mapx == -46 && mapy == -33)
		{
			travel = "mt.silver";
		}
		if (!travel.equals("no_travel"))
			GameClient.getInstance().getDisplay().add(new TrainChooserDialog(travel,GameClient.getInstance().getOurPlayer()));
	}
}
