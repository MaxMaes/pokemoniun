package org.pokenet.client.backend;

import org.pokenet.client.GameClient;
import org.pokenet.client.constants.ServerPacket;
import org.pokenet.client.protocol.ClientMessage;

/**
 * Parses chat text for commands
 * 
 * @author ZombieBear
 */
public class ModerationManager
{

	public static void parseLine(String x)
	{
		// Announcement
		if(x.length() >= 9 && x.substring(0, 9).equalsIgnoreCase("announce "))
		{
			ClientMessage message = new ClientMessage(ServerPacket.SERVER_ANNOUNCEMENT);
			message.addString(x.substring(9));
			GameClient.getSession().send(message);
		}
		// Player count
		else if(x.length() >= 11 && x.substring(0, 11).equalsIgnoreCase("playercount"))
		{
			ClientMessage message = new ClientMessage(ServerPacket.PLAYER_COUNT);
			GameClient.getSession().send(message);
		}
		/* Alert TODO: Redundant?
		 * else if(x.length() >= 6 && x.substring(0, 6).equalsIgnoreCase("alert "))
		 * {
		 * ClientMessage message = new ClientMessage(52);
		 * message.addString(x.substring(6));
		 * GameClient.getSession().send(message);
		 * } */
		// Reset player to Heal Location
		else if(x.length() >= 6 && x.substring(0, 6).equalsIgnoreCase("reset "))
		{
			ClientMessage message = new ClientMessage(ServerPacket.PLAYER_UNSTICK);
			message.addString(x.substring(6));
			GameClient.getSession().send(message);
		}
		// Mute
		else if(x.length() >= 5 && x.substring(0, 5).equalsIgnoreCase("mute "))
		{
			ClientMessage message = new ClientMessage(ServerPacket.PLAYER_MUTE);
			message.addString(x.substring(5));
			GameClient.getSession().send(message);
		}
		// Unmute
		else if(x.length() >= 7 && x.substring(0, 7).equalsIgnoreCase("unmute "))
		{
			ClientMessage message = new ClientMessage(ServerPacket.PLAYER_UNMUTE);
			message.addString(x.substring(7));
			GameClient.getSession().send(message);
		}
		// Kick
		else if(x.length() >= 5 && x.substring(0, 5).equalsIgnoreCase("kick "))
		{
			ClientMessage message = new ClientMessage(ServerPacket.PLAYER_KICK);
			message.addString(x.substring(5));
			GameClient.getSession().send(message);
		}
		// Ban
		else if(x.length() >= 4 && x.substring(0, 4).equalsIgnoreCase("ban "))
		{
			ClientMessage message = new ClientMessage(ServerPacket.PLAYER_BAN);
			message.addString(x.substring(4));
			GameClient.getSession().send(message);
		}
		// Unban
		else if(x.length() >= 6 && x.substring(0, 6).equalsIgnoreCase("unban "))
		{
			ClientMessage message = new ClientMessage(ServerPacket.PLAYER_UNBAN);
			message.addString(x.substring(6));
			GameClient.getSession().send(message);
		}
		// Warp player to you
		else if(x.length() >= 9 && x.substring(0, 9).equalsIgnoreCase("jumptome "))
		{
			ClientMessage message = new ClientMessage(ServerPacket.PLAYER_ME_WARP);
			message.addString(x.substring(9));
			GameClient.getSession().send(message);
		}
		// Jump to [player]
		else if(x.length() >= 7 && x.substring(0, 7).equalsIgnoreCase("jumpto "))
		{
			ClientMessage message = new ClientMessage(ServerPacket.PLAYER_WARP);
			message.addString(x.substring(7));
			GameClient.getSession().send(message);
		}
		// Warp player to player
		else if(x.length() >= 5 && x.substring(0, 5).equalsIgnoreCase("jump "))
		{
			ClientMessage message = new ClientMessage(ServerPacket.PLAYER_PLAYER_WARP);
			message.addString(x.substring(5));
			GameClient.getSession().send(message);
		}
		// Set user class
		else if(x.length() >= 6 && x.substring(0, 6).equalsIgnoreCase("class "))
		{
			ClientMessage message = new ClientMessage(ServerPacket.PLAYER_CLASS);
			message.addString(x.substring(6));
			GameClient.getSession().send(message);
		}
		// Change Weather
		else if(x.length() >= 8 && x.substring(0, 8).equalsIgnoreCase("weather "))
		{
			// Normal
			if(x.substring(8).equalsIgnoreCase("normal") || x.substring(8).equalsIgnoreCase("sunny"))
			{

				ClientMessage message = new ClientMessage(ServerPacket.WEATHER);
				message.addInt(0);
				GameClient.getSession().send(message);
			}
			// Rain
			else if(x.substring(8).equalsIgnoreCase("rain"))
			{
				ClientMessage message = new ClientMessage(ServerPacket.WEATHER);
				message.addInt(1);
				GameClient.getSession().send(message);
			}
			// Snow
			else if(x.substring(8).equalsIgnoreCase("snow") || x.substring(8).equalsIgnoreCase("hail"))
			{
				ClientMessage message = new ClientMessage(ServerPacket.WEATHER);
				message.addInt(2);
				GameClient.getSession().send(message);
			}
			// Fog
			else if(x.substring(8).equalsIgnoreCase("fog"))
			{
				ClientMessage message = new ClientMessage(ServerPacket.WEATHER);
				message.addInt(3);
				GameClient.getSession().send(message);
			}
			// Sandstorm
			else if(x.substring(8).equalsIgnoreCase("sandstorm"))
			{
				ClientMessage message = new ClientMessage(ServerPacket.WEATHER);
				message.addInt(4);
				GameClient.getSession().send(message);
			}
			// Random
			else if(x.substring(8).equalsIgnoreCase("random"))
			{
				ClientMessage message = new ClientMessage(ServerPacket.WEATHER);
				message.addInt(5);
				GameClient.getSession().send(message);
			}
		}
		// Stop server
		else if(x.length() >= 4 && x.substring(0, 4).equalsIgnoreCase("stop"))
		{
			ClientMessage message = new ClientMessage(ServerPacket.STOP_SERVER);
			GameClient.getSession().send(message);
		}
	}
}
