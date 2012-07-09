package org.pokenet.server.backend;

import java.io.File;
import org.pokenet.server.GameServer;
import org.pokenet.server.Log;
import org.pokenet.server.backend.map.ServerMap;
import org.pokenet.server.backend.map.ServerMapMatrix;
import org.pokenet.server.battle.impl.NpcSleepTimer;
import tiled.io.xml.XMLMapTransformer;

/**
 * Stores the map matrix and movement managers.
 * @author shadowkanji
 *
 */
public class MovementService {
	private MovementManager [] m_movementManager;
	private ServerMapMatrix m_mapMatrix;
	private NpcSleepTimer m_sleepTimer;
	
	/**
	 * Default constructor
	 */
	public MovementService() {
		m_movementManager = new MovementManager[GameServer.getMovementThreadAmount()];
		m_mapMatrix = new ServerMapMatrix();
		m_sleepTimer = new NpcSleepTimer();
	}
	
	/**
	 * Removes a player from the movement service
	 * @param username
	 */
	public void removePlayer(String username) {
		for(int i = 0; i < m_movementManager.length; i++) {
			if(m_movementManager[i].removePlayer(username)) {
				return;
			}
		}
	}
	
	/**
	 * Returns the movement manager with the smallest processing load
	 * @return
	 */
	public MovementManager getMovementManager() {
		int smallest = 0;
		if(m_movementManager.length > 1) {
			for(int i = 0; i < m_movementManager.length; i++) {
				if(m_movementManager[i].getProcessingLoad() < m_movementManager[smallest].getProcessingLoad())
					smallest = i;
			}
		}
		if(m_movementManager[smallest] == null) {
			m_movementManager[smallest] = new MovementManager();
		}
		if(!m_movementManager[smallest].isRunning()) {
			m_movementManager[smallest].start();
		}
		return m_movementManager[smallest];
	}
	
	/**
	 * Returns the map matrix
	 * @return
	 */
	public ServerMapMatrix getMapMatrix() {
		return m_mapMatrix;
	}
	
	/**
	 * Reloads all maps while the server is running. Puts all players in m_tempMap.
	 * An NPC is there to allow them to return to where they last where when they are ready.
	 * Optionally, we can skip saving players in a temporary map.
	 * @param forceSkip
	 */
	public void loadMaps() {
		XMLMapTransformer loader = new XMLMapTransformer();
		for(int x = -50; x < 50; x++) {
			for(int y = -50; y < 50; y++) {
				File nextMap = new File("res/maps/" + x + "." + y + ".tmx");
				if(nextMap.exists()) {
					try {
						ServerMap s = new ServerMap(loader.readMap(nextMap.getCanonicalPath()), x, y);
						s.setMapMatrix(m_mapMatrix);
						s.loadData();
						m_mapMatrix.setMap(s , x + 50, y + 50);
					} catch (Exception ex) {
						System.err.println("Error loading " + x + "." + y + ".tmx - Bad map file");
						m_mapMatrix.setMap(null, x + 50, y + 50);
					}
				}
			}
		}
		Log.debug("Maps loaded");
	}
	
	/**
	 * Starts the movement service
	 */
	public void start() {
		loadMaps();
		m_sleepTimer.start();
		for(int i = 0; i < m_movementManager.length; i++) {
			m_movementManager[i] = new MovementManager();
			m_movementManager[i].start();
		}
		Log.debug("Movement Service started");
	}
	
	/**
	 * Stops the movement service
	 */
	public void stop() {
		m_sleepTimer.stop();
		for(int i = 0; i < m_movementManager.length; i++) {
			m_movementManager[i].stop();
		}
		Log.debug("Movement Service stopped");
	}
}