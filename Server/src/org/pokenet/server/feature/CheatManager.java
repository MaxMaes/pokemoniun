package org.pokenet.server.feature;

import java.io.BufferedWriter;
import java.io.FileWriter;
import org.pokenet.server.backend.entity.Player;

public class CheatManager
{
	// TODO: Write this to the database.

	public static void report(Player player, String message)
	{
		String report;
		if(player != null) {
			report = "Player: " + player.getName() + " (" + player.getIpAddress() + ") " + message;
		} else {
			report = "Unknown player " + message;
		}
		writeToFile(report);
	}

	private static void writeToFile(String message)
	{
		try
		{
			// Create file
			FileWriter fstream = new FileWriter("cheatlog.txt", true);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(message + "\n");
			// Close the output stream
			out.close();
		}
		catch(Exception e)
		{
			System.err.println("Error: " + e.getMessage());
		}
	}
}
