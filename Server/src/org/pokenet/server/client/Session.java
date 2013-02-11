package org.pokenet.server.client;

import org.jboss.netty.channel.Channel;
import org.pokenet.server.GameServer;
import org.pokenet.server.backend.entity.Player;
import org.pokenet.server.protocol.ClientMessage;
import org.pokenet.server.protocol.ServerMessage;

public class Session
{

	private Boolean authenticated;
	private Channel channel;
	private String ipAddress;
	private Player player;

	public Session(Channel channel, String IP)
	{
		this.channel = channel;
		authenticated = false;
		ipAddress = IP;

		ServerMessage Message = new ServerMessage(this);
		Message.init(100);
		Message.addInt(GameServer.REVISION);
		Message.sendResponse();
	}

	public void close()
	{
		getChannel().close();
	}

	public Channel getChannel()
	{
		return channel;
	}

	public String getIpAddress()
	{
		return ipAddress;
	}

	public Boolean getLoggedIn()
	{
		return authenticated;
	}

	public Player getPlayer()
	{
		return player;
	}

	public void parseMessage(ClientMessage msg)
	{
		if(GameServer.getServiceManager().getNetworkService().getConnections().getMessages().contains(msg.getId()))
			GameServer.getServiceManager().getNetworkService().getConnections().getMessages().get(msg.getId()).Parse(this, msg, new ServerMessage(this));
	}

	public void Send(ServerMessage msg)
	{
		channel.write(msg);
		// TODO: Debug mode. System.out.println(msg.getMessage());
	}

	public void setLoggedIn(boolean state)
	{
		authenticated = state;
	}

	public void setPlayer(Player player)
	{
		player.setSession(this);
		this.player = player;

	}
}
