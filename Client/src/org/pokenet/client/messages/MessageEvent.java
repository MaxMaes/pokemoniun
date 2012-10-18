package org.pokenet.client.messages;

import org.pokenet.client.Session;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.protocol.ServerMessage;

public interface MessageEvent
{
	void Parse(Session Session, ServerMessage Request, ClientMessage Message);
}