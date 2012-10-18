package org.pokenet.server.messages.events;

import org.pokenet.server.GameServer;
import org.pokenet.server.client.Session;
import org.pokenet.server.messages.MessageEvent;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;

public class WeatherEvent implements MessageEvent
{

	public void Parse(Session Session, ClientMessage Request, ServerMessage Message)
	{
		switch(Request.readInt())
		{
			case 0:
				GameServer.getServiceManager().getTimeService().setForcedWeather(0);
				break;
			case 1:
				GameServer.getServiceManager().getTimeService().setForcedWeather(1);
				break;
			case 2:
				GameServer.getServiceManager().getTimeService().setForcedWeather(2);
				break;
			case 3:
				GameServer.getServiceManager().getTimeService().setForcedWeather(3);
				break;
			case 4:
				GameServer.getServiceManager().getTimeService().setForcedWeather(4);
				break;
			case 5:
				GameServer.getServiceManager().getTimeService().setForcedWeather(9);
				break;

		}
	}
}
