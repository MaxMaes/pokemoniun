package org.pokenet.server.client;

import org.jboss.netty.channel.Channel;
import org.pokenet.server.GameServer;
import org.pokenet.server.backend.entity.PlayerChar;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;


public class Session {

	private Channel channel;
	private Boolean authenticated;
	private PlayerChar player;
	private String ipAddress;

	public Session(Channel channel, String IP) {
		this.channel = channel;
		this.authenticated = false;
		this.ipAddress = IP;
		
		ServerMessage Message = new ServerMessage(this);
		Message.Init(0);
		Message.addInt(GameServer.REVISION);
		Message.sendResponse();	
	}

	public void parseMessage(ClientMessage msg)
	{
		if (GameServer.getServiceManager().getNetworkService().getConnections().getMessages().contains(msg.getId())) {
			GameServer.getServiceManager().getNetworkService().getConnections().getMessages().get(msg.getId()).Parse(this, msg, new ServerMessage(this));
		}
	}

	public void Send(ServerMessage msg) {
		channel.write(msg);
		System.out.println(msg.getMessage());
	}

	public Channel getChannel() {
		return channel;
	}
	
	public void close() {
		getChannel().close();
	}

	public Boolean getLoggedIn() {
		return authenticated;
	}

	public void setLoggedIn(boolean state) {
		this.authenticated = state;
	}
	
	public void setPlayer(PlayerChar player) {
		player.setSession(this);
		this.player = player;
		
	}
	
	public PlayerChar getPlayer() {
		return this.player;
	}	
	
	public String getIpAddress() {
		return this.ipAddress;
	}
}
