package org.pokenet.client;

import org.jboss.netty.channel.Channel;
import org.pokenet.client.messages.MessageHandler;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.protocol.ServerMessage;

public class Session
{
	private final Channel channel;
	private boolean isLoggedIn = false;

	public Session(Channel channel)
	{
		this.channel = channel;
	}

	public Channel getChannel()
	{
		return channel;
	}

	public Boolean getLoggedIn()
	{
		return isLoggedIn;
	}

	public void parseMessage(ServerMessage message)
	{
		MessageHandler handler = GameClient.getInstance().getConnections().getMessages();
		if(handler.contains(message.getId()))
		{
			handler.get(message.getId()).parse(this, message, new ClientMessage(this));
		}
	}

	public void send(ClientMessage msg)
	{
		channel.write(msg);
	}

	public void setLoggedIn(boolean state)
	{
		isLoggedIn = state;
	}
}
