package org.pokenet.server.network;

import org.pokenet.server.GameServer;

/**
 * A class which kicks players if they've been idle for too long
 */
public class IdleTimer implements Runnable {
	private boolean m_isRunning = false;
	
	public void run() {
		GameServer.THREADS++;
		System.out.println("IdleTimer started.");
		while(m_isRunning) {
			/*
			 * Loop through all players and check for idling
			 * If they've idled, disconnect them
			 */
			TcpProtocolHandler.kickIdlePlayers();
			try {
				Thread.sleep(30 * 1000);
			} catch (Exception e) {}
		}
        GameServer.THREADS--;
		System.out.println("IdleTimer stopped (" + GameServer.THREADS + " threads remaining)");
	}
	
	/**
	 * Starts the idle timer
	 */
	public void start() {
		m_isRunning = true;
		new Thread(this).start();
	}
	
	/**
	 * Stops the idle timer
	 */
	public void stop() {
		m_isRunning = false;
	}
}
