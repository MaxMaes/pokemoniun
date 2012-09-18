package org.pokenet.server.network;

import java.util.LinkedList;
import java.util.Queue;
import org.pokenet.server.GameServer;
import org.pokenet.server.backend.SaveManager;
import org.pokenet.server.backend.entity.Player;

/**
 * Handles logging players out
 * 
 * @author shadowkanji
 */
public class LogoutManager implements Runnable
{
	private Queue<Player> m_logoutQueue;
	private Thread m_thread;
	private boolean m_isRunning = false;
	private MySqlManager m_database;
	private SaveManager m_saveManager;

	/**
	 * Default constructor
	 * 
	 * @param m_saveManager
	 */
	public LogoutManager(SaveManager saveManager)
	{
		m_database = MySqlManager.getInstance();
		m_logoutQueue = new LinkedList<Player>();
		m_saveManager = saveManager;
	}

	/**
	 * Returns how many players are in the save queue
	 * 
	 * @return
	 */
	public int getPlayerAmount()
	{
		return m_logoutQueue.size();
	}

	/**
	 * Attempts to logout a player by saving their data. Returns true on success
	 * 
	 * @param player
	 */
	private boolean attemptLogout(Player player)
	{
		// Remove player from their map if it hasn't been done already
		if(player.getMap() != null)
			player.getMap().removeChar(player);
		TcpProtocolHandler.removePlayer(player);
		GameServer.getInstance().updatePlayerCount();
		// Store all player information
		if(!m_saveManager.savePlayer((player)))
		{
			return false;
		}
		// Finally, store that the player is logged out and close connection
		m_database.query("UPDATE `pn_members` SET `lastLoginServer` = 'null' WHERE `id` = '" + player.getId() + "'");
		GameServer.getServiceManager().getMovementService().removePlayer(player.getName());
		return true;
	}

	/**
	 * Queues a player to be logged out
	 * 
	 * @param player
	 */
	public void queuePlayer(Player player)
	{
		if(m_thread == null || !m_thread.isAlive())
			start();
		if(!m_logoutQueue.contains(player))
			m_logoutQueue.offer(player);
	}

	/**
	 * Called by m_thread.start()
	 */
	public void run()
	{
		while(m_isRunning)
		{
			synchronized(m_logoutQueue)
			{
				if(m_logoutQueue.peek() != null)
				{
					Player player = m_logoutQueue.poll();
					synchronized(player)
					{
						if(player != null)
						{
							if(!attemptLogout(player))
							{
								m_logoutQueue.add(player);
							}
							else
							{
								player.dispose();
								System.out.println("INFO: " + player.getName() + " logged out.");
							}
						}
					}
				}
			}
			try
			{
				Thread.sleep(500);
			}
			catch(InterruptedException e)
			{
			}
		}
		m_thread = null;
		System.out.println("INFO: All player data saved successfully.");
	}

	/**
	 * Start this logout manager
	 */
	public void start()
	{
		if(m_thread == null || !m_thread.isAlive())
		{
			m_thread = new Thread(this);
			m_isRunning = true;
			m_thread.start();
		}
	}

	/**
	 * Stop this logout manager
	 */
	public void stop()
	{
		// Stop the thread
		m_isRunning = false;
	}
}
