package org.pokenet.client.messages.events;

import org.pokenet.client.GameClient;
import org.pokenet.client.Session;
import org.pokenet.client.messages.MessageEvent;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.protocol.ServerMessage;

public class PokemonLevelChangeEvent implements MessageEvent
{

	@Override
	public void parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		String[] levelData = Request.readString().split(",");

		GameClient.getInstance().getOurPlayer().getPokemon()[Integer.parseInt(levelData[0])].setLevel(Integer.parseInt(levelData[1]));
		GameClient.getInstance().getUi().update(false);
	}
}
