package org.pokenet.client.messages.events;

import org.pokenet.client.GameClient;
import org.pokenet.client.Session;
import org.pokenet.client.messages.MessageEvent;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.protocol.ServerMessage;

public class BreedLevelEvent implements MessageEvent
{

	@Override
	public void Parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		int breedLevel = Request.readInt();

		if(GameClient.getInstance().getOurPlayer().getBreedingLevel() != -1 && GameClient.getInstance().getOurPlayer().getBreedingLevel() != breedLevel)
			GameClient.getInstance().getUi().getChat().addSystemMessage("*" + "Congratulations! Your breeding level is now " + breedLevel + ".");
		// gained no level? init it awaynyway! :)
		GameClient.getInstance().getOurPlayer().setBreedingLevel(breedLevel);
	}
}
