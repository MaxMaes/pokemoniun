package org.pokenet.server.network;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelException;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.pokenet.server.messages.MessageHandler;
import org.pokenet.server.protocol.codec.NetworkDecoder;
import org.pokenet.server.protocol.codec.NetworkEncoder;

public class Connection
{
	private NioServerSocketChannelFactory Factory;
	private ServerBootstrap Bootstrap;
	private MessageHandler Messages;
	private int Port;
	private LogoutManager m_logoutManager;
	public Connection(int port, LogoutManager logoutManager)
	{
		m_logoutManager = logoutManager;		
		
		this.Factory = new NioServerSocketChannelFactory
				(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()
				);

		this.Bootstrap = new ServerBootstrap(Factory);
		this.Messages = new MessageHandler();
		this.Messages.register();
		this.Port = port;

		SetupSocket();
	}

	private void SetupSocket()
	{
		ChannelPipeline pipeline = this.Bootstrap.getPipeline();

		pipeline.addLast("encoder", new NetworkEncoder());
		pipeline.addLast("decoder", new NetworkDecoder());
		pipeline.addLast("handler", new ConnectionHandler(m_logoutManager));
	}

	public boolean StartSocket()
	{
		try
		{
			this.Bootstrap.bind(new InetSocketAddress(Port));
		}
		catch (ChannelException ex)
		{
			return false;
		}

		return true;
	}
	
	public void StopSocket() {
		this.Bootstrap.bind().close();
	}
	
	public MessageHandler getMessages() {
		return Messages;
	}
}
