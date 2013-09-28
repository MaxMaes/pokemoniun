package org.pokemonium.client.messages.events;

import org.pokemonium.client.GameClient;
import org.pokemonium.client.Session;
import org.pokemonium.client.messages.MessageEvent;
import org.pokemonium.client.protocol.ClientMessage;
import org.pokemonium.client.protocol.ServerMessage;

public class MoveTutorEvent implements MessageEvent
{

	@Override
	public void parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		String s = Request.readString();
		String name = s.split("_")[0];
		if(name.equals("MoveRelearner"))
		{
			GameClient.getInstance().getHUD().showRelearnDialog(s.split("_")[1]);
		}
		else if(name.equals("MoveTutor"))
		{
			GameClient.getInstance().getHUD().showTutorDialog(s.split("_")[1]);
		}
	}
}
