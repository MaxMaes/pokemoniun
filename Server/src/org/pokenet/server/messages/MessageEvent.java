package org.pokenet.server.messages;

import org.pokenet.server.client.Session;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;

public interface MessageEvent
{
	void Parse(Session session, ClientMessage request, ServerMessage message);
}