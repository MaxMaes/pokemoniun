package org.pokenet.client.ui;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import mdes.slick.sui.Display;
import org.pokenet.client.ui.base.Notification;

/**
 * Manages notifications
 * 
 * @author shadowkanji
 */
public class NotificationManager implements Runnable
{
	private static Queue<Notification> m_notifications = new ConcurrentLinkedQueue<Notification>();;
	@SuppressWarnings("unused")
	private Display m_display;
	private boolean m_isRunning;
	private Thread m_thread;

	/**
	 * Default constructor
	 * 
	 * @param d
	 */
	public NotificationManager(Display d)
	{
		m_display = d;
	}

	/**
	 * Adds a new notification
	 * 
	 * @param n
	 */
	public static void addNotification(String n)
	{
		m_notifications.add(new Notification(n));
	}

	/**
	 * Called when running
	 */
	@Override
	public void run()
	{
		System.out.println("NotificationMananer started.");
		while(m_isRunning)
			try
			{
				Thread.sleep(500);
			}
			catch(Exception e)
			{
			}
		System.out.println("NotificationMananer stopped.");
	}

	/**
	 * Starts the notification manager
	 */
	public void start()
	{
		m_isRunning = true;
		m_thread = new Thread(this);
		m_thread.start();
	}

	/**
	 * Stops the notification manager
	 */
	public void stop()
	{
		m_isRunning = false;
	}
}
