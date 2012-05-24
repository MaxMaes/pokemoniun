package org.pokenet.server.network;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.Queue;

import org.pokenet.server.GameServer;
import org.pokenet.server.backend.entity.Bag;
import org.pokenet.server.backend.entity.Player;
import org.pokenet.server.battle.DataService;
import org.pokenet.server.battle.Pokemon;
import org.pokenet.server.battle.PokemonSpecies;
import org.pokenet.server.battle.mechanics.statuses.abilities.IntrinsicAbility;

/**
 * Handles logging players out
 * 
 * @author shadowkanji
 * 
 */
public class LogoutManager implements Runnable {
	private Queue<Player> m_logoutQueue;
	private Thread m_thread;
	private boolean m_isRunning;
	private MySqlManager m_database;

	/**
	 * Default constructor
	 */
	public LogoutManager() {
		m_database = new MySqlManager();
		m_logoutQueue = new LinkedList<Player>();
		m_thread = null;
	}

	/**
	 * Returns how many players are in the save queue
	 * 
	 * @return
	 */
	public int getPlayerAmount() {
		return m_logoutQueue.size();
	}

	/**
	 * Attempts to logout a player by saving their data. Returns true on success
	 * 
	 * @param player
	 */
	private boolean attemptLogout(Player player) {
		// Remove player from their map if it hasn't been done already
		if (player.getMap() != null)
			player.getMap().removeChar(player);
		TcpProtocolHandler.removePlayer(player);
		UdpProtocolHandler.removePlayer(player);
		GameServer.getInstance().updatePlayerCount();
		m_database = new MySqlManager();
		if (!m_database.connect(GameServer.getDatabaseHost(),
				GameServer.getDatabaseUsername(),
				GameServer.getDatabasePassword()))
			return false;
		m_database.selectDatabase(GameServer.getDatabaseName());
		// Store all player information
		if (!m_database.savePlayer(player)) {
			m_database.close();
			return false;
		}
		// Finally, store that the player is logged out and close connection
		m_database
				.query("UPDATE pn_members SET lastLoginServer='null' WHERE id='"
						+ player.getId() + "'");
		m_database.close();
		GameServer.getServiceManager().getMovementService()
				.removePlayer(player.getName());
		return true;
	}

	/**
	 * Queues a player to be logged out
	 * 
	 * @param player
	 */
	public void queuePlayer(Player player) {
		if (m_thread == null || !m_thread.isAlive())
			start();
		if (!m_logoutQueue.contains(player))
			m_logoutQueue.offer(player);
	}

	/**
	 * Called by m_thread.start()
	 */
	public void run() {
		while (m_isRunning) {
			synchronized (m_logoutQueue) {
				if (m_logoutQueue.peek() != null) {
					Player p = m_logoutQueue.poll();
					synchronized (p) {
						if (p != null) {
							if (!attemptLogout(p)) {
								m_logoutQueue.add(p);
							} else {
								p.dispose();
								System.out.println("INFO: " + p.getName()
										+ " logged out.");
								p = null;
							}
						}
					}
				}
			}
			try {
				Thread.sleep(500);
			} catch (Exception e) {
			}
		}
		m_thread = null;
		System.out.println("INFO: All player data saved successfully.");
	}

	/**
	 * Start this logout manager
	 */
	public void start() {
		if (m_thread == null || !m_thread.isAlive()) {
			m_thread = new Thread(this);
			m_isRunning = true;
			m_thread.start();
		}
	}

	/**
	 * Stop this logout manager
	 */
	public void stop() {
		// Stop the thread
		m_isRunning = false;
	}
}
