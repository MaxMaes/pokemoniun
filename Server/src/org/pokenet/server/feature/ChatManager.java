package org.pokenet.server.feature;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.apache.mina.core.session.IoSession;
import org.pokenet.server.GameServer;
import org.pokenet.server.backend.entity.Player.Language;
import org.pokenet.server.backend.map.ServerMap;
import org.pokenet.server.client.Session;
import org.pokenet.server.protocol.ServerMessage;

/**
 * Handles chat messages sent by players
 * 
 * @author shadowkanji
 */
public class ChatManager implements Runnable
{
	private boolean m_isRunning = false;
	/* Local chat queue
	 * [Message, x, y] */
	private Queue<Object[]> m_localQueue;
	/* Private chat queue
	 * [session, sender, message] */
	private Queue<Object[]> m_privateQueue;
	private Thread m_thread;

	/**
	 * Default Constructor
	 */
	public ChatManager()
	{
		m_thread = new Thread(this);
		m_localQueue = new ConcurrentLinkedQueue<Object[]>();
		m_privateQueue = new ConcurrentLinkedQueue<Object[]>();
	}

	/**
	 * Returns how many messages are queued in this chat manager
	 * 
	 * @return
	 */
	public int getProcessingLoad()
	{
		return m_localQueue.size() + m_privateQueue.size();
	}

	/**
	 * Queues a local chat message
	 * 
	 * @param message
	 * @param mapX
	 * @param mapY
	 */
	public void queueLocalChatMessage(String message, int mapX, int mapY, Language l)
	{
		m_localQueue.add(new Object[] { message, String.valueOf(mapX), String.valueOf(mapY), String.valueOf(l) });
	}

	/**
	 * Queues a private chat message
	 * 
	 * @param message
	 * @param receiver
	 * @param sender
	 */
	public void queuePrivateMessage(String message, IoSession receiver, String sender)
	{
		m_privateQueue.add(new Object[] { receiver, sender, message });
	}

	/**
	 * Called by m_thread.start()
	 */
	public void run()
	{
		Object[] o;
		ServerMap m;
		Session s;
		while(m_isRunning)
		{
			// Send next local chat message
			if(m_localQueue.peek() != null)
			{
				o = m_localQueue.poll();
				m = GameServer.getServiceManager().getMovementService().getMapMatrix().getMapByGamePosition(Integer.parseInt((String) o[1]), Integer.parseInt((String) o[2]));
				if(m != null)
					m.sendChatMessage((String) o[0], Language.valueOf((String) o[3]));
			}
			// Send next private chat message
			if(m_privateQueue.peek() != null)
			{
				o = m_privateQueue.poll();
				s = (Session) o[0];
				if(s.getLoggedIn())
				{
					/* TcpProtocolHandler.writeMessage(s, new ChatMessage(
					 * ChatMessageType.PRIVATE, ((String) o[1]) + "," + ((String) o[2]))); */
					ServerMessage startBattle = new ServerMessage(s);
					startBattle.Init(50);
					startBattle.addString("p" + (String) o[2]);
					startBattle.sendResponse();
				}
			}
			try
			{
				Thread.sleep(250);
			}
			catch(Exception e)
			{
			}
		}
	}

	/**
	 * Start this chat manager
	 */
	public void start()
	{
		m_isRunning = true;
		m_thread.start();
	}

	/**
	 * Stop this chat manager
	 */
	public void stop()
	{
		m_isRunning = false;
	}
}