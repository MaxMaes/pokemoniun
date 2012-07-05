package org.pokenet.server.battle.impl;

import java.util.Random;

import org.pokenet.server.GameServer;
import org.pokenet.server.Log;
import org.pokenet.server.backend.entity.NPC;
import org.pokenet.server.backend.map.ServerMap;

/**
 * A thread which wakes sleeping NPCs (NPCs sleep for ~15 minutes after battle)
 * @author shadowkanji
 *
 */
public class NpcSleepTimer implements Runnable {
	private boolean m_running;

	public void run() {
		GameServer.THREADS++;
		Log.debug("NpcSleepTimer started.");
		Random r = new Random();
		NPC npc;
		ServerMap serverMap;
		while(m_running) {
			/*
			 * Loop through every map
			 */
			for(int x = 0; x < 100; x++) {
				for(int y = 0; y < 100; y++) {
					serverMap = GameServer.getServiceManager().
						getMovementService().getMapMatrix().getMapByRealPosition(x, y);
					if(serverMap != null) {
						/*
						 * Loop through every npc on the map
						 * If they're sleeping, check if its time to wake them
						 */
						for(int i = 0; i < serverMap.getNpcs().size(); i++) {
							npc = serverMap.getNpcs().get(i);
							if(npc != null && !npc.canBattle() && 
									System.currentTimeMillis() - npc.getLastBattleTime()
									>= 300000 + r.nextInt(300000)) {
								npc.setLastBattleTime(0);
							}
							if(npc.getShop() != null)
							{
								npc.getShop().updateStock();
							}
						}
					}
					npc = null;
				}
			}
			try {
				Thread.sleep(30 * 1000); // 30 seconds, maybe lower this?
			} catch (InterruptedException ex) {}
		}
		GameServer.THREADS--;
		Log.debug("NpcSleepTimer stopped (" + GameServer.THREADS + " threads remaining)");
	}

	/**
	 * Starts the timer
	 */
	public void start() {
		m_running = true;
		new Thread(this).start();
	}
	
	/**
	 * Stops the timer
	 */
	public void stop() {
		m_running = false;
	}
}
