package org.pokemonium.server.feature;

import org.pokemonium.server.backend.entity.Player;

public class CheatManager
{
	private static CheatManager mInstance;

	public CheatManager()
	{

	}

	public static CheatManager getInstance()
	{
		if(mInstance == null)
			mInstance = new CheatManager();
		return mInstance;
	}

	public void log(Player player, String message)
	{
		System.out.println("[CheatManager] Player " + player.getName() + " (" + player.getIpAddress() + "), " + message);
	}
}
