package org.pokenet.client.messages.events;

import org.pokenet.client.GameClient;
import org.pokenet.client.Session;
import org.pokenet.client.messages.MessageEvent;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.protocol.ServerMessage;
import de.matthiasmann.twl.model.SimpleChangableListModel;

public class InfoEvent implements MessageEvent
{

	@Override
	public void parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		String name = Request.readString();
		switch(name)
		{
			case "MoveRelearner":
				String moves = Request.readString();
				SimpleChangableListModel<String> m_moves = new SimpleChangableListModel<>();
				for(String s : moves.split(", "))
				{
					if(s.equals("/END"))
						break;
					m_moves.addElement(s);
				}
				GameClient.getInstance().getHUD().getRelearnDialog().initUse(m_moves);
				break;
			default:
				break;
		}
	}
}
