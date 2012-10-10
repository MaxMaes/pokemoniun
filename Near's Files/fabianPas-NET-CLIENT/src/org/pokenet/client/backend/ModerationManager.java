package org.pokenet.client.backend;

import org.pokenet.client.GameClient;
import org.pokenet.client.protocol.ClientMessage;

/**
 * Parses chat text for commands
 * @author ZombieBear
 *
 */
public class ModerationManager {
	
	public static void parseLine(String x){
		// Announcement
		if (x.length() >= 9 && x.substring(0, 9).equalsIgnoreCase("announce ")) {
			ClientMessage message = new ClientMessage();
			message.Init(24);
			message.addString(x.substring(9));
			GameClient.session.Send(message);
		}
		// Mute
		else if (x.length() >= 5 && x.substring(0, 5).equalsIgnoreCase("mute ")) {
			ClientMessage message = new ClientMessage();
			message.Init(29);
			message.addString(x.substring(5));
			GameClient.session.Send(message);
		}
		// Unmute
		else if (x.length() >= 7 && x.substring(0, 7).equalsIgnoreCase("unmute ")) {
			ClientMessage message = new ClientMessage();
			message.Init(30);
			message.addString(x.substring(7));
			GameClient.session.Send(message);
		}
		// Kick
		else if (x.length() >= 5 && x.substring(0, 5).equalsIgnoreCase("kick ")) {
			ClientMessage message = new ClientMessage();
			message.Init(31);
			message.addString(x.substring(5));
			GameClient.session.Send(message);
		}
		// Ban
		else if (x.length() >= 4 && x.substring(0, 4).equalsIgnoreCase("ban ")) {
			ClientMessage message = new ClientMessage();
			message.Init(26);
			message.addString(x.substring(4));
			GameClient.session.Send(message);
		}
		// Unban
		else if (x.length() >= 6 && x.substring(0, 6).equalsIgnoreCase("unban ")) {
			ClientMessage message = new ClientMessage();
			message.Init(27);
			message.addString(x.substring(6));
			GameClient.session.Send(message);
		}
		// Jump to [player]
		else if (x.length() >= 7 && x.substring(0, 7).equalsIgnoreCase("jumpto ")) {
			ClientMessage message = new ClientMessage();
			message.Init(28);
			message.addString(x.substring(7));
			GameClient.session.Send(message);
		}
		// Player count
		else if (x.length() >= 11 && x.substring(0, 11).equalsIgnoreCase("playercount")) {
			ClientMessage message = new ClientMessage();
			message.Init(23);
			message.addString(x.substring(11));
			GameClient.session.Send(message);
		}
		// Change Weather
		else if (x.length() >= 8 && x.substring(0, 8).equalsIgnoreCase("weather ")) {
			// Normal
			if (x.substring(8).equalsIgnoreCase("normal") ||
					x.substring(8).equalsIgnoreCase("sunny")) {
				
				ClientMessage message = new ClientMessage();
				message.Init(32);
				message.addInt(0);
				GameClient.session.Send(message);
			}
			// Rain
			else if (x.substring(8).equalsIgnoreCase("rain")) {
				ClientMessage message = new ClientMessage();
				message.Init(32);
				message.addInt(1);
				GameClient.session.Send(message);
			}
			// Snow
			else if (x.substring(8).equalsIgnoreCase("snow") ||
					x.substring(8).equalsIgnoreCase("hail")) {
				ClientMessage message = new ClientMessage();
				message.Init(32);
				message.addInt(2);
				GameClient.session.Send(message);
			}
			// Fog
			else if (x.substring(8).equalsIgnoreCase("fog")) {
				ClientMessage message = new ClientMessage();
				message.Init(32);
				message.addInt(3);
				GameClient.session.Send(message);
			}
			// Sandstorm
			else if (x.substring(8).equalsIgnoreCase("sandstorm")) {
				ClientMessage message = new ClientMessage();
				message.Init(32);
				message.addInt(4);
				GameClient.session.Send(message);
			}
			// Random
			else if (x.substring(8).equalsIgnoreCase("random")) {
				ClientMessage message = new ClientMessage();
				message.Init(32);
				message.addInt(5);
				GameClient.session.Send(message);
			}
		}
		// Stop server
		else if (x.length() >= 4 && x.substring(0, 4).equalsIgnoreCase("stop")) {
			ClientMessage message = new ClientMessage();
			message.Init(33);
			GameClient.session.Send(message);
		}
	}
}
