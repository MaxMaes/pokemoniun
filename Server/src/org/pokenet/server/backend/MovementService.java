package org.pokenet.server.backend;

import java.io.File;
import java.util.HashMap;
import org.pokenet.server.GameServer;
import org.pokenet.server.backend.entity.Player;
import org.pokenet.server.backend.map.ServerMap;
import org.pokenet.server.backend.map.ServerMapMatrix;
import org.pokenet.server.battle.impl.NpcSleepTimer;
import tiled.io.xml.XMLMapTransformer;

/**
 * Stores the map matrix and movement managers.
 * 
 * @author shadowkanji
 */
public class MovementService
{
	public static final int MAX_MAP_THREADS = 5;
	private final ServerMapMatrix m_mapMatrix;
	private final MovementManager[] m_movementManager;
	private final NpcSleepTimer m_sleepTimer;
	private static int m_mapThreads = 0;
	private ServerMap m_tempMap;

	/**
	 * Default constructor
	 */
	public MovementService()
	{
		m_movementManager = new MovementManager[GameServer.MOVEMENT_THREADS];
		m_mapMatrix = new ServerMapMatrix();
		m_sleepTimer = new NpcSleepTimer();
	}

	/**
	 * Returns the map matrix
	 * 
	 * @return
	 */
	public ServerMapMatrix getMapMatrix()
	{
		return m_mapMatrix;
	}

	/**
	 * Returns the movement manager with the smallest processing load
	 * 
	 * @return
	 */
	public MovementManager getMovementManager()
	{
		int smallest = 0;
		if(m_movementManager.length > 1)
			for(int i = 0; i < m_movementManager.length; i++)
				if(m_movementManager[i].getProcessingLoad() < m_movementManager[smallest].getProcessingLoad())
					smallest = i;
		return m_movementManager[smallest];
	}

	/**
	 * Reloads all maps while the server is still running.
	 */
	public void reloadMaps()
	{
		this.reloadMaps(false);
	}

	/**
	 * Reloads all maps while the server is running. Puts all players in m_tempMap.
	 * An NPC is there to allow them to return to where they last where when they are ready.
	 * Optionally, we can skip saving players in a temporary map.
	 * 
	 * @param forceSkip
	 */
	public void reloadMaps(boolean forceSkip)
	{
		/* First move all players out of their maps */
		if(!forceSkip)
		{
			HashMap<String, Player> players;
			for(int x = 0; x < 100; x++)
				for(int y = 0; y < 100; y++)
					if(m_mapMatrix.getMapByRealPosition(x, y) != null)
					{
						players = m_mapMatrix.getMapByRealPosition(x, y).getPlayers();
						for(Player p : players.values())
						{
							p.setLastHeal(p.getX(), p.getY(), p.getMapX(), p.getMapY());
							p.setMap(m_tempMap, null);
						}
					}
		}
		/* TODO: Multithreaded implementation to speed up server start!
		 * Reload all the maps */
		XMLMapTransformer xmlLoader = new XMLMapTransformer();
		File nextMap;
		ServerMap map;
		for(int x = -50; x < 50; x++)
			for(int y = -50; y < 50; y++)
			{
				/* MapThread currentMap = new MapThread(x, y);
				 * currentMap.start(); */
				nextMap = new File("res/maps/" + String.valueOf(x) + "." + String.valueOf(y) + ".tmx");
				// System.out.println("trying: " + x + ", " +y);
				if(nextMap.exists())
					try
					{
						map = new ServerMap(xmlLoader.readMap(nextMap.getCanonicalPath()), x, y);
						map.setMapMatrix(m_mapMatrix);
						map.loadData();
						m_mapMatrix.setMap(map, x + 50, y + 50);
						System.out.println("loaded map: " + x + ", " + y);
					}
					catch(Exception e)
					{
						System.err.println("Error loading " + x + "." + y + ".tmx - Bad map file");
						m_mapMatrix.setMap(null, x + 50, y + 50);
					}
			}
		System.out.println("INFO: Maps loaded");
	}

	private class MapThread extends Thread
	{
		private XMLMapTransformer xmlLoader;
		private File nextMap;
		private ServerMap map;
		private int x, y;

		public MapThread(int mapx, int mapy)
		{
			super("Map-Thread " + mapx + "." + mapy);
			m_mapThreads++;
			xmlLoader = new XMLMapTransformer();
			x = mapx;
			y = mapy;
		}

		public void run()
		{
			nextMap = new File("res/maps/" + String.valueOf(x) + "." + String.valueOf(y) + ".tmx");
			// System.out.println("trying: " + x + ", " + y);
			if(nextMap.exists())
				try
				{
					map = new ServerMap(xmlLoader.readMap(nextMap.getCanonicalPath()), x, y);
					map.setMapMatrix(m_mapMatrix);
					map.loadData();
					m_mapMatrix.setMap(map, x + 50, y + 50);
					System.out.println("loaded map: " + x + ", " + y);
				}
				catch(Exception e)
				{
					System.err.println("Error loading " + x + "." + y + ".tmx - Bad map file");
					m_mapMatrix.setMap(null, x + 50, y + 50);
				}
			m_mapThreads--;
		}
	}

	/**
	 * Removes a player from the movement service
	 * 
	 * @param username
	 */
	public void removePlayer(String username)
	{
		for(int i = 0; i < m_movementManager.length; i++)
			if(m_movementManager[i].removePlayer(username))
				break;
	}

	/**
	 * Starts the movement service
	 */
	public void start()
	{
		this.reloadMaps(true);
		m_sleepTimer.start();
		for(int i = 0; i < m_movementManager.length; i++)
		{
			m_movementManager[i] = new MovementManager();
			m_movementManager[i].start();
		}
		System.out.println("INFO: Movement Service started");
	}

	/**
	 * Stops the movement service
	 */
	public void stop()
	{
		m_sleepTimer.finish();
		for(int i = 0; i < m_movementManager.length; i++)
			m_movementManager[i].stop();
		System.out.println("INFO: Movement Service stopped");
	}
}