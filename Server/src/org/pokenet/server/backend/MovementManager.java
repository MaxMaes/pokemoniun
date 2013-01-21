package org.pokenet.server.backend;

import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;
import org.pokenet.server.backend.entity.Character;
import org.pokenet.server.backend.entity.HMObject;
import org.pokenet.server.backend.entity.HMObject.ObjectType;

/**
 * Loops through all players and moves them if they request to be moved
 * 
 * @author shadowkanji
 */
public class MovementManager implements Runnable
{
	/** Comparator for comparing chars */
	private Comparator<Character> m_comp;
	private boolean m_isRunning = true;
	private Queue<Character> m_moved;
	private int m_pLoad = 0;
	private Thread m_thread;
	private Queue<Character> m_waiting;

	/**
	 * Default constructor.
	 */
	public MovementManager()
	{
		m_comp = new Comparator<Character>()
		{
			public int compare(Character arg0, Character arg1)
			{
				return arg0.getPriority() - arg1.getPriority();
			}
		};
		m_waiting = new PriorityQueue<Character>(11, m_comp);
		m_moved = new PriorityQueue<Character>(1, m_comp);
	}

	public void addHMObject(HMObject obj)
	{
		synchronized(m_waiting)
		{
			if(obj.getType() == ObjectType.STRENGTH_BOULDER)
			{
				m_pLoad++;
				m_waiting.add(obj);
			}
		}
	}

	/**
	 * Adds a player to this movement service
	 * 
	 * @param player
	 */
	public void addPlayer(Character player)
	{
		synchronized(m_waiting)
		{
			m_pLoad++;
			m_waiting.offer(player);
		}
	}

	/**
	 * Returns how many players are in this thread (the processing load)
	 */
	public int getProcessingLoad()
	{
		return m_pLoad;
	}

	/**
	 * Returns true if the movement manager is running
	 * 
	 * @return
	 */
	public boolean isRunning()
	{
		return m_thread != null && m_thread.isAlive();
	}

	/**
	 * Removes a player from this movement service, returns true if the player was in the thread and was removed.
	 * Otherwise, returns false.
	 * 
	 * @param player
	 */
	public boolean removePlayer(String player)
	{
		/* Check waiting list */
		synchronized(m_waiting)
		{
			Iterator<Character> it = m_waiting.iterator();
			while(it.hasNext())
			{
				Character c = it.next();
				if(c.getName().equalsIgnoreCase(player))
				{
					m_waiting.remove(c);
					m_pLoad--;
					return true;
				}
			}
		}
		/* Check moved list */
		synchronized(m_moved)
		{
			Iterator<Character> it = m_moved.iterator();
			while(it.hasNext())
			{
				Character c = it.next();
				if(c.getName().equalsIgnoreCase(player))
				{
					m_moved.remove(c);
					m_pLoad--;
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Called by m_thread.start(). Loops through all players calling Player.move() if the player requested to be moved.
	 */
	public void run()
	{
		Character tmp = null;
		// ArrayList<Char> tmpArray = null;
		while(m_isRunning)
		{
			/* Pull char of highest priority */
			if(m_waiting != null && !m_waiting.isEmpty())
			{
				synchronized(m_waiting)
				{
					tmp = m_waiting.poll();
				}
				/* Move character */
				tmp.move();
				/* Place him in moved array */
				synchronized(m_moved)
				{
					m_moved.offer(tmp);
				}
			}
			/* If waiting array is empty, swap arrays */
			synchronized(m_waiting)
			{
				if(m_waiting.isEmpty())
				{
					m_waiting = m_moved;
					m_moved = new PriorityQueue<Character>(1, m_comp);
				}
			}
			try
			{
				Thread.sleep(10);
			}
			catch(InterruptedException e)
			{
			}
		}
	}

	/**
	 * Starts the movement thread
	 */
	public void start()
	{
		m_thread = new Thread(this, "MovementManager-Thread");
		m_thread.start();
	}

	/**
	 * Stops the movement thread
	 */
	public void stop()
	{
		m_moved.clear();
		m_waiting.clear();
		m_isRunning = false;
	}

}