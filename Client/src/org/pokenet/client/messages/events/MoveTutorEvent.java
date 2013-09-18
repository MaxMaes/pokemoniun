package org.pokenet.client.messages.events;

import org.pokenet.client.GameClient;
import org.pokenet.client.Session;
import org.pokenet.client.messages.MessageEvent;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.protocol.ServerMessage;

public class MoveTutorEvent implements MessageEvent
{

	@Override
	public void parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		String s = Request.readString();
		String name = s.split("_")[0];
		if(name.equals("MoveRelearner"))
		{
			// String price = s.split("_")[1];
			// !!!!!!!!!!!!!!!!!!!!!THIS IS ILLIGAL SADHI!!!!!!!!!!!!!!!!!!!!!!!!!!!
			// MoveRelearnDialog dialog = new MoveRelearnDialog(s.split("_")[1]); //
			GameClient.getInstance().getHUD().setRelearnDialog(s.split("_")[1]); //
			// GameClient.getInstance().getHUD().add(dialog); //
			// !!!!!!!!!!!!!!!!!!!!!THIS IS ILLIGAL SADHI!!!!!!!!!!!!!!!!!!!!!!!!!!!
			// ik fix dat nog wel
			// maar daar gaat het nu niet over
			// echt wel/

			// gelijk goed implementeren boy
			// nu?
			// ja
			// -_- ...
		}
		else if(name.equals("MoveTutor"))
		{
			String moves = s.split("_")[1];
		}
	}
}
