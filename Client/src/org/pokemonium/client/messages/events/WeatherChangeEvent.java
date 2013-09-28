package org.pokemonium.client.messages.events;

import org.pokemonium.client.GameClient;
import org.pokemonium.client.Session;
import org.pokemonium.client.backend.time.WeatherService.Weather;
import org.pokemonium.client.messages.MessageEvent;
import org.pokemonium.client.protocol.ClientMessage;
import org.pokemonium.client.protocol.ServerMessage;

public class WeatherChangeEvent implements MessageEvent
{

	@Override
	public void parse(Session Session, ServerMessage Request, ClientMessage Message)
	{
		switch(Request.readInt())
		{
			case 1:
				GameClient.getInstance().getWeatherService().setWeather(Weather.NORMAL);
				break;
			case 2:
				GameClient.getInstance().getWeatherService().setWeather(Weather.RAIN);
				break;
			case 3:
				GameClient.getInstance().getWeatherService().setWeather(Weather.HAIL);
				break;
			case 4:
				GameClient.getInstance().getWeatherService().setWeather(Weather.SANDSTORM);
				break;

		}
	}
}
