package org.pokenet.server.connections;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.jboss.netty.channel.Channel;
import org.pokenet.server.backend.entity.Player;
import org.pokenet.server.client.Session;

public class ActiveConnections
{
	/* TODO: Retrieve playercount from here? */
	private static ConcurrentMap<Integer, Session> Sessions = new ConcurrentHashMap<Integer, Session>();

	public static boolean addSession(Channel channel, String IP)
	{
		return Sessions.putIfAbsent(channel.getId(), new Session(channel, IP)) == null;
	}

	public static ConcurrentMap<Integer, Session> allSessions()
	{
		return Sessions;
	}

	public static Player getPlayer(String username)
	{
		for(Session session : Sessions.values())
			if(session.getPlayer() != null)
				if(session.getPlayer().getName().equals(username))
					return session.getPlayer();
		return null;
	}

	public static Session GetUserByChannel(Channel channel)
	{
		return Sessions.get(channel.getId());
	}

	public static boolean hasSession(Channel channel)
	{
		return Sessions.containsKey(channel.getId());
	}

	public static void removeSession(Channel channel)
	{
		Sessions.remove(channel.getId());
	}
}