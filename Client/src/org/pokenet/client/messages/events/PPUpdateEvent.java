package org.pokenet.client.messages.events;

import org.pokenet.client.Session;
import org.pokenet.client.backend.BattleManager;
import org.pokenet.client.messages.MessageEvent;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.protocol.ServerMessage;

public class PPUpdateEvent implements MessageEvent
{

	@Override
	public void parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		for(int i = 0; i < 4; i++)
		{

			BattleManager.getInstance().getCurPoke().setMoveCurPP(i, Request.readInt());
			if(BattleManager.getInstance().getCurPoke().getMoveMaxPP()[i] != 0)
				BattleManager.getInstance().getBattleWindow().getPPLabel(i)
						.setText(BattleManager.getInstance().getCurPoke().getMoveCurPP()[i] + "/" + BattleManager.getInstance().getCurPoke().getMoveMaxPP()[i]);
		}
	}
}
