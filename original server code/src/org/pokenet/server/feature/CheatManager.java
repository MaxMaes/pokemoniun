package org.pokenet.server.feature;

import org.pokenet.server.Log;
import org.pokenet.server.backend.entity.Player;

public class CheatManager {
	private static CheatManager mInstance;
	
	public CheatManager()
	{
		
	}
	
	public static CheatManager getInstance()
	{
		if(mInstance == null) 
		{
			mInstance = new CheatManager();
		}
		return mInstance;
	}
	
	public void log(Player player, String message) 
	{
		Log.debug("[CheatManager] Player " + player.getName() + " (" + player.getIpAddress() + "), " + message);
	}
}
