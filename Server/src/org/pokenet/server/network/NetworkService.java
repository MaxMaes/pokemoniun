package org.pokenet.server.network;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import javax.swing.Timer;
import org.pokenet.server.GameServer;
import org.pokenet.server.backend.SaveManager;
import org.pokenet.server.client.Session;
import org.pokenet.server.connections.ActiveConnections;
import org.pokenet.server.feature.ChatManager;
import org.pokenet.server.protocol.ServerMessage;

/**
 * Handles all networking
 * 
 * @author shadowkanji
 */
public class NetworkService
{
	private static Connection _connection;
	private final ChatManager[] m_chatManager;
	private final LoginManager m_loginManager;
	private final LogoutManager m_logoutManager;
	private final RegistrationManager m_registrationManager;
	private final SaveManager m_saveManager;
	private MySqlManager m_database;
	private Timer autosaver;

	/**
	 * Default constructor
	 */
	public NetworkService()
	{
		m_database = MySqlManager.getInstance();
		m_saveManager = new SaveManager();
		m_logoutManager = new LogoutManager(m_saveManager);
		m_loginManager = new LoginManager(m_logoutManager);
		m_registrationManager = new RegistrationManager();
		m_chatManager = new ChatManager[3];
		autosaver = new Timer(1000 * 60 * 10, new ActionListener() // Change last number to the minutes for the interval
				{
					public void actionPerformed(ActionEvent arg0)
					{
						saveAll();
					}
				});

		autosaver.start();
	}

	/**
	 * Returns the chat manager with the least amount of processing to be done
	 * 
	 * @return
	 */
	public ChatManager getChatManager()
	{
		int smallest = 0;
		for(int i = 1; i < m_chatManager.length; i++)
			if(m_chatManager[i].getProcessingLoad() < m_chatManager[smallest].getProcessingLoad())
				smallest = i;
		return m_chatManager[smallest];
	}

	/**
	 * Returns the connection manager (packet handler)
	 * 
	 * @return
	 */
	public Connection getConnections()
	{
		return _connection;
	}

	/**
	 * Returns the login manager
	 * 
	 * @return
	 */
	public LoginManager getLoginManager()
	{
		return m_loginManager;
	}

	/**
	 * Returns the logout manager
	 * 
	 * @return
	 */
	public LogoutManager getLogoutManager()
	{
		return m_logoutManager;
	}

	/**
	 * Returns the registration manager
	 * 
	 * @return
	 */
	public RegistrationManager getRegistrationManager()
	{
		return m_registrationManager;
	}

	/**
	 * Logs out all players and stops login/logout/registration managers and the autosaver
	 */
	public void logoutAll()
	{
		m_loginManager.stop();
		autosaver.stop();
		/* Queue all players to be saved */
		Iterator<Session> it = ActiveConnections.allSessions().values().iterator();
		while(it.hasNext())
		{
			Session p = it.next();
			m_logoutManager.queuePlayer(p.getPlayer());
		}
		/* Since the method is called during a server shutdown, wait for all players to be logged out */
		while(m_logoutManager.getPlayerAmount() > 0)
			;
		m_logoutManager.stop();
	}

	/**
	 * Start this network service by starting all threads.
	 */
	public void start()
	{
		/* Ensure anyone still marked as logged in on this server is unmarked */
		m_database.query("UPDATE `pn_members` SET `lastLoginServer` = 'null' WHERE `lastLoginServer` = '" + GameServer.getServerName() + "'"); /* <-- Evil Exception = evil. */
		/* Start the login/logout managers. */
		m_logoutManager.start();
		m_loginManager.start();
		_connection = new Connection(GameServer.getPort(), m_logoutManager);
		if(!_connection.StartSocket())
			System.out.println("ERROR: Something is using the port or the IP address is invalid.");
		else
		{
			System.out.println("INFO: Server started on port " + GameServer.getPort());
			/* Start the chat managers */
			for(int i = 0; i < m_chatManager.length; i++)
			{
				m_chatManager[i] = new ChatManager();
				m_chatManager[i].start();
			}
			System.out.println("INFO: Network Service started.");
		}
	}

	/**
	 * Saves all players and logs failures
	 */
	public void saveAll()
	{
		System.out.println("Saving all players");
		/* Queue all players to be saved */
		Iterator<Session> it = ActiveConnections.allSessions().values().iterator();
		Session s;
		while(it.hasNext())
		{
			s = it.next();
			if(s.getPlayer() != null)
			{
				ServerMessage message = new ServerMessage();
				message.init(2);
				message.addString("Saving...");
				s.Send(message);

				if(m_saveManager.savePlayer(s.getPlayer()))
				{
					ServerMessage succesmg = new ServerMessage();
					succesmg.init(2);
					succesmg.addString("Save succesfull.");
					s.Send(succesmg);
				}
				else
				{
					ServerMessage failmsg = new ServerMessage();
					failmsg.init(2);
					failmsg.addString("Save Failed.");
					s.Send(failmsg);
					System.err.println("Error saving player" + s.getPlayer().getName() + " " + s.getPlayer().getId());
				}
			}
			else
			{
				/* Attempted save before the client logged in. */
			}
		}
	}

	/**
	 * Stop this network service by stopping all threads.
	 */
	public void stop()
	{
		/* Stop all threads (do not use thread.stop()). */
		for(int i = 0; i < m_chatManager.length; i++)
			m_chatManager[i].stop();
		// m_tcpAcceptor.unbind();
		// m_tcpProtocolHandler.logoutAll();
		logoutAll();
		System.out.println("Logged out all players.");
		/* TODO: Doesn't stop the server properly, rewrite! */
		_connection.StopSocket();
	}
}
