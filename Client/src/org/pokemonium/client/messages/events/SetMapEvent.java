package org.pokemonium.client.messages.events;

import org.pokemonium.client.GameClient;
import org.pokemonium.client.Session;
import org.pokemonium.client.backend.time.WeatherService.Weather;
import org.pokemonium.client.messages.MessageEvent;
import org.pokemonium.client.protocol.ClientMessage;
import org.pokemonium.client.protocol.ServerMessage;

public class SetMapEvent implements MessageEvent
{

	@Override
	public void parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		char direction = Request.readString().charAt(0);
		GameClient.getInstance().getMapMatrix().setNewMapPos(direction);
		GameClient.getInstance().setMap(Request.readInt(), Request.readInt());
		switch(Request.readInt())
		{
			case 0:
				GameClient.getInstance().getWeatherService().setWeather(Weather.NORMAL);
				break;
			case 1:
				GameClient.getInstance().getWeatherService().setWeather(Weather.RAIN);
				break;
			case 2:
				GameClient.getInstance().getWeatherService().setWeather(Weather.HAIL);
				break;
			case 3:
				GameClient.getInstance().getWeatherService().setWeather(Weather.SANDSTORM);
				break;
			case 4:
				GameClient.getInstance().getWeatherService().setWeather(Weather.FOG);
				break;
			default:
				GameClient.getInstance().getWeatherService().setWeather(Weather.NORMAL);
				break;
		}
	}
}
