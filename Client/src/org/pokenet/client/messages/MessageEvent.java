package org.pokenet.client.messages;

import org.pokenet.client.Session;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.protocol.ServerMessage;

public interface MessageEvent
{
	public void parse(Session session, ServerMessage request, ClientMessage message);
}