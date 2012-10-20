package org.pokenet.client.messages.events;

import org.pokenet.client.GameClient;
import org.pokenet.client.Session;
import org.pokenet.client.messages.MessageEvent;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.protocol.ServerMessage;

public class TrainerLevelEvent implements MessageEvent
{

	@Override
	public void parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		int TrainerLevel = Request.readInt();

		if(GameClient.getInstance().getOurPlayer().getTrainerLevel() != -1 && GameClient.getInstance().getOurPlayer().getTrainerLevel() != TrainerLevel)
			GameClient.getInstance().getUi().getChat().addSystemMessage("*" + "Congratulations! Your trainer level is now " + TrainerLevel + ".");
		// gained no level? init it awaynyway! :)
		GameClient.getInstance().getOurPlayer().setTrainerLevel(TrainerLevel);
	}
}
