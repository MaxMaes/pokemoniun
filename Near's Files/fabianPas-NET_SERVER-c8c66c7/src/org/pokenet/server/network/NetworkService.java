package org.pokenet.server.network;

import java.util.Iterator;

import org.pokenet.server.GameServer;
import org.pokenet.server.client.Session;
import org.pokenet.server.connections.ActiveConnections;

/**
 * Handles all networking
 * @author shadowkanji
 */
public class NetworkService {
	private static Connection _connection;
	private LoginManager m_loginManager;
	private LogoutManager m_logoutManager;
	private RegistrationManager m_registrationManager;
	
	
	/**
	 * Default constructor
	 */
	public NetworkService() {
		m_loginManager = new LoginManager();
		m_logoutManager = new LogoutManager();
		
	}
	
    
    /**
	 * Returns the login manager
	 * @return
	 */
	public LoginManager getLoginManager() {
		return m_loginManager;
	}
	
	/**
	 * Returns the registration manager
	 * @return
	 */
	public RegistrationManager getRegistrationManager() {
		return m_registrationManager;
	}
	
	
	
	/**
	 * Returns the logout manager
	 * @return
	 */
	public LogoutManager getLogoutManager() {
		return m_logoutManager;
	}
	
	
	/**
	 * Returns the connection manager (packet handler)
	 * @return
	 */
	public Connection getConnections() {
		return _connection;
	}
	
	
	/**
	 * Start this network service by starting all threads.
	 */
	public void start() {
		//Load MySQL JDBC Driver
        try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		 * Ensure anyone still marked as logged in on this server
		 * is unmarked
		 */
		MySQLManager m = new MySQLManager();
		
		System.out.println("INFO: Creating MySQL connection with password.");
		if(m.connect(GameServer.getDatabaseHost(), 
				GameServer.getDatabaseUsername(),
				GameServer.getDatabasePassword())) {
			m.selectDatabase(GameServer.getDatabaseName());
			m.query("UPDATE pn_members SET lastLoginServer='null' WHERE lastLoginServer='"
					+ GameServer.getServerName() + "'");
			m.close();
		} else {
			System.out.println("ERROR: Could not create MySQL connection.");
			GameServer.stop();
		}
		
		System.out.println("INFO: Created MySQL connection succesfully!");
		m = null;
		
		m_logoutManager.start();
		m_loginManager.start();
		
        _connection = new Connection(7001, m_logoutManager);
		if (!_connection.StartSocket()) {
			System.out.println("ERROR: Something is using the port or ip address is invalid.");
		}
		else {
			System.out.println("INFO: Server started on port 7001");
		}
		
		System.out.println("INFO: Network Service started.");
	}
	
	/**
	 * Logs out all players and stops login/logout/registration managers
	 */
	public void logoutAll() {
		m_loginManager.stop();
		/*
		 * Queue all players to be saved
		 */
		Iterator<Session> it = ActiveConnections.allSessions().values().iterator();
		Session p;
		while(it.hasNext()) {
			p = it.next();
			m_logoutManager.queuePlayer(p.getPlayer());
		}
		/*
		 * Since the method is called during a server shutdown, wait for all players to be logged out
		 */
		while(m_logoutManager.getPlayerAmount() > 0);
		m_logoutManager.stop();
	}
	
	
	/**
	 * Stop this network service by stopping all threads.
	 */
	public void stop() {
		logoutAll();
		//TODO Check!  expiremental not sure if working correctly
		_connection.StopSocket();
		
	}
}
