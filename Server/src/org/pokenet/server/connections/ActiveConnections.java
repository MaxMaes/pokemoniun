package org.pokenet.server.connections;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.jboss.netty.channel.Channel;
import org.pokenet.server.backend.entity.Player;
import org.pokenet.server.client.Session;

public class ActiveConnections
{
	private static ConcurrentMap<Integer, Session> sessions = new ConcurrentHashMap<Integer, Session>();

	public static boolean addSession(Channel channel, String IP)
	{
		return sessions.putIfAbsent(channel.getId(), new Session(channel, IP)) == null;
	}

	public static ConcurrentMap<Integer, Session> allSessions()
	{
		return sessions;
	}

	public static Player getPlayer(String username)
	{
		for(Session session : sessions.values())
			if(session.getPlayer() != null)
				if(session.getPlayer().getName().equalsIgnoreCase(username))
					return session.getPlayer();
		return null;
	}

	public static Session GetUserByChannel(Channel channel)
	{
		return sessions.get(channel.getId());
	}

	public static boolean hasSession(Channel channel)
	{
		return sessions.containsKey(channel.getId());
	}

	public static void removeSession(Channel channel)
	{
		sessions.remove(channel.getId());
	}

	public static int getActiveConnections()
	{
		int online = 0;
		for(Session s : sessions.values())
		{
			if(s.getPlayer() != null)
				online++;
		}
		return online;
	}
}