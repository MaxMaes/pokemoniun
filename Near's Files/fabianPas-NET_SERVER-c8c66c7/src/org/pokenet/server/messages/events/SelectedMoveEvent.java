package org.pokenet.server.messages.events;

import org.pokenet.server.backend.entity.PlayerChar;
import org.pokenet.server.battle.BattleTurn;
import org.pokenet.server.client.Session;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;



public class SelectedMoveEvent implements MessageEvent {

	@Override
	public void Parse(Session Session, ClientMessage Request, ServerMessage Message)
	{
		PlayerChar p = Session.getPlayer();	
		BattleTurn turn;
		if(p.isBattling()) {
			turn = BattleTurn.getMoveTurn(Request.readInt());
			try {
				p.getBattleField().queueMove(p.getBattleId(), turn);
			} catch (Exception e) {} // this is dubious and check it!
		}
	}

}
