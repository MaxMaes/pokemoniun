package org.pokemonium.client.messages;

import org.pokemonium.client.Session;
import org.pokemonium.client.protocol.ClientMessage;
import org.pokemonium.client.protocol.ServerMessage;

public interface MessageEvent
{
	public void parse(Session session, ServerMessage request, ClientMessage message);
}