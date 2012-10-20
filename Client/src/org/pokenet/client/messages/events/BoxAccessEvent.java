package org.pokenet.client.messages.events;

import org.pokenet.client.GameClient;
import org.pokenet.client.Session;
import org.pokenet.client.messages.MessageEvent;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.protocol.ServerMessage;

public class BoxAccessEvent implements MessageEvent
{

	@Override
	public void parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		// Box access - receiving a string of pokedex numbers, e.g. B15,23,24,
		int[] pokes = new int[30];
		/* NOTE: -1 identifies that no pokemon is in a slot */
		if(Request.readInt() == 1)
		{
			String[] indexes = Request.readString().split(",");
			for(int i = 0; i < 30; i++)
				if(indexes.length > i)
					if(indexes[i] == null || indexes[i].compareTo("") == 0)
						pokes[i] = -1;
					else
						pokes[i] = Integer.parseInt(indexes[i]);
				else
					pokes[i] = -1;
		}
		else
			for(int i = 0; i < pokes.length; i++)
				pokes[i] = -1;
		if(GameClient.getInstance().getUi().getStorageBox() == null)
			GameClient.getInstance().getUi().useStorageBox(pokes);
		else
			GameClient.getInstance().getUi().getStorageBox().changeBox(pokes);
	}
}
