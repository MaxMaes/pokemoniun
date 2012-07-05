package org.pokenet.server.network;

import java.net.InetSocketAddress;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.pokenet.server.GameServer;
import org.pokenet.server.Log;
import org.pokenet.server.feature.ChatManager;
import org.pokenet.server.feature.DatabaseConnection;
import org.pokenet.server.network.codec.PokenetCodecFactory;

/**
 * Handles all networking
 * 
 * @author shadowkanji
 */
public class NetworkService {
	private final TcpProtocolHandler m_tcpProtocolHandler;
	private final LoginManager m_loginManager;
	private final LogoutManager m_logoutManager;
	private final IoAcceptor m_tcpAcceptor;
	private final ChatManager[] m_chatManager = new ChatManager[3];

	/**
	 * Default constructor
	 */
	public NetworkService() {
		m_tcpAcceptor = new NioSocketAcceptor();
		m_logoutManager = new LogoutManager();
		m_loginManager = new LoginManager(m_logoutManager);
		m_tcpProtocolHandler = new TcpProtocolHandler(m_loginManager,
				m_logoutManager);
	}

	/**
	 * Returns the logout manager
	 * 
	 * @return
	 */
	public LogoutManager getLogoutManager() {
		return m_logoutManager;
	}

	/**
	 * Returns the chat manager with the least amount of processing to be done
	 * 
	 * @return
	 */
	public ChatManager getChatManager() {
		int smallest = 0;
		for (int i = 1; i < m_chatManager.length; i++) {
			if (m_chatManager[i].getProcessingLoad() < m_chatManager[smallest]
					.getProcessingLoad())
				smallest = i;
		}
		return m_chatManager[smallest];
	}

	/**
	 * Start this network service by starting all threads.
	 */
	public void start() {
		/*
		 * Ensure anyone still marked as logged in on this server is unmarked
		 */
		try {
			PreparedStatement ps = DatabaseConnection
					.getConnection()
					.prepareStatement(
							"UPDATE pn_members SET lastLoginServer = ? WHERE lastLoginServer = ?");
			ps.setString(1, null);
			ps.setString(2, GameServer.getServerName());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		/*
		 * Start the login/logout managers
		 */
		m_logoutManager.start();
		m_loginManager.start();

		/*
		 * Start the chat managers
		 */
		for (int i = 0; i < m_chatManager.length; i++) {
			m_chatManager[i] = new ChatManager();
			m_chatManager[i].start();
		}

		/*
		 * Bind the TCP port
		 */
		m_tcpAcceptor.getFilterChain().addLast("codec",
				new ProtocolCodecFilter(new PokenetCodecFactory()));
		m_tcpAcceptor.setHandler(m_tcpProtocolHandler);
		try {
			m_tcpAcceptor.bind(new InetSocketAddress(7002));
			Log.debug("Accepting incoming connections.");
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		Log.debug("Network Service started.");
	}

	/**
	 * Stop this network service by stopping all threads.
	 */
	public void stop() {
		// Stop all threads (do not use thread.stop() )
		// Unbind network address
		for (int i = 0; i < m_chatManager.length; i++)
			m_chatManager[i].stop();
		m_tcpAcceptor.unbind();
		m_tcpProtocolHandler.logoutAll();
		Log.debug("Logged out all players.");
	}
}
