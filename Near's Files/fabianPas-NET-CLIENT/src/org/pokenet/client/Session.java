package org.pokenet.client;

import org.jboss.netty.channel.Channel;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.protocol.ServerMessage;

public class Session {

	private Channel channel;
	private Boolean isLoggedIn;

	public Session(Channel channel) {
		this.channel = channel;
		this.isLoggedIn = false;
	}

	public void parseMessage(ServerMessage msg)
	{
		if (GameClient.getConnections().getMessages().contains(msg.getId())) {
			GameClient.getConnections().getMessages().get(msg.getId()).Parse(this, msg, new ClientMessage(this));
		}
	}

	public void Send(ClientMessage msg) {
		channel.write(msg);
		System.out.println(msg.getMessage());
	}

	public Channel getChannel() {
		return channel;
	}

	public Boolean getLoggedIn() {
		return isLoggedIn;
	}

	public void setLoggedIn(boolean state) {
		this.isLoggedIn = state;
	}
}
