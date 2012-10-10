package org.pokenet.client.messages.events;

import org.pokenet.client.GameClient;
import org.pokenet.client.Session;
import org.pokenet.client.messages.MessageEvent;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.protocol.ServerMessage;

public class FishLevelEvent implements MessageEvent {

	@Override
	public void Parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		int fishLevel = Request.readInt();
		
		if(GameClient.getInstance().getOurPlayer().getFishingLevel() != -1 && GameClient.getInstance().getOurPlayer().getFishingLevel() != fishLevel) {
			GameClient.getInstance().getUi().getChat().addSystemMessage("*" + "Congratulations! Your fishing level is now " + fishLevel + ".");
		}
		// gained no level? init it awaynyway! :)
		GameClient.getInstance().getOurPlayer().setFishingLevel(fishLevel);
	}
}
