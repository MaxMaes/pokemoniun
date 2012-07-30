package org.pokenet.server.battle.impl;

import java.util.Random;

import org.pokenet.server.GameServer;
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
		System.out.println("INFO: Npc sleep timer started");
		Random r = new Random();
		while(m_running) {
			/*
			 * Loop through every map
			 */
			for(int x = 0; x < 100; x++) {
				for(int y = 0; y < 100; y++) {
					ServerMap m = GameServer.getServiceManager().
						getMovementService().getMapMatrix().getMapByRealPosition(x, y);
					if(m != null) {
						/*
						 * Loop through every npc on the map
						 * If they're sleeping, check if its time to wake them
						 */
						for(int i = 0; i < m.getNpcs().size(); i++) {
							NPC npc = m.getNpcs().get(i);
							if(npc != null && !npc.canBattle() && 
									System.currentTimeMillis() - npc.getLastBattleTime()
									>= 5 * 60 * 1000 + r.nextInt(5 * 60 * 1000)) {
								npc.setLastBattleTime(0);
							}
						}
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {}
					}
				}
			}
			// It takes 100 * 100 * 500ms to get here (83 minutes ...)
			try {
				Thread.sleep(5 * 60 * 1000); // 5 minutes
			} catch (InterruptedException e) {}
		}
		System.out.println("INFO: Npc sleep timer stopped");
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
