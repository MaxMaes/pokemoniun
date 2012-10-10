package org.pokenet.server;

import java.io.File;
import java.io.PrintStream;

public class GameServer {
	private static ServiceManager m_serviceManager;
	private static String m_dbServer, m_dbName, m_dbUsername, m_dbPassword, m_serverName;
	/* The revision of the game server */ 
	// would be more helpful if this was the SVN version.. so it auto updated
	public static int REVISION = 1786;	
	
	public static void main(String[] args)
	{
		Configuration Config = new Configuration();
		
		try {
			PrintStream p = new PrintStream(new File("./errors.txt"));
			System.setErr(p);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (Config.Load("server.properties"))
		{
			m_dbServer = Config.GrabValue("jdbc.net.host");
			m_dbName = Config.GrabValue("jdbc.net.database");
			m_dbUsername = Config.GrabValue("jdbc.net.username");
			m_dbPassword = Config.GrabValue("jdbc.net.password");
			m_serverName = Config.GrabValue("net.servername");;
			
			System.out.println("INFO: Configuration succesfully parsed.");
		}
		else
		{
			System.out.println("ERROR: Config could not be found! It should be 'server.properties'");
			System.exit(1);
		}
		
		m_serviceManager = new ServiceManager();
		m_serviceManager.start();
	}	
	
	/**
	 * Stops the game server
	 */
	public static void stop() {
		m_serviceManager.stop();
		try {
			/* Let threads finish up */
			Thread.sleep(10000);
			/* Exit */
			System.out.println("Exiting server...");
			System.exit(0);
		} catch (Exception e) {}
	}
	
	/**
	 * Returns the service manager of the server
	 * @return
	 */
	public static ServiceManager getServiceManager() {
		return m_serviceManager;
	}
	
	
	/**
	 * Returns the database host
	 * @return
	 */
	public static String getDatabaseHost() {
		return m_dbServer;
	}
	
	/**
	 * Returns the database username
	 * @return
	 */
	public static String getDatabaseUsername() {
		return m_dbUsername;
	}
	
	/**
	 * Returns the database password
	 * @return
	 */
	public static String getDatabasePassword() {
		return m_dbPassword;
	}
	
	/**
	 * Returns the name of this server
	 * @return
	 */
	public static String getServerName() {
		return m_serverName;
	}
	
	/**
	 * Returns the database selected
	 * @return
	 */
	public static String getDatabaseName() {
		return m_dbName;
	}
}
