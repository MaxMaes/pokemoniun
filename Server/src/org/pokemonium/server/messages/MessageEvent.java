package org.pokemonium.server.messages;

import org.pokemonium.server.client.Session;
import org.pokemonium.server.protocol.ClientMessage;
import org.pokemonium.server.protocol.ServerMessage;

public interface MessageEvent
{
	void Parse(Session session, ClientMessage request, ServerMessage message);
}