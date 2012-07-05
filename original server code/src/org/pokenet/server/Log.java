package org.pokenet.server;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {
	
	public static void debug(String message)
	{
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		System.out.println("[" + sdf.format(date) + "] " + message);
	}
}
