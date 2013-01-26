package org.pokenet.server.network;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelException;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.frame.LengthFieldPrepender;
import org.pokenet.server.messages.MessageHandler;
import org.pokenet.server.protocol.codec.NetworkDecoder;
import org.pokenet.server.protocol.codec.NetworkEncoder;

public class Connection
{
	private ServerBootstrap serverBootstrap;
	private NioServerSocketChannelFactory socketFactory;
	private LogoutManager m_logoutManager;
	private MessageHandler messages;
	private int port;

	public Connection(int port, LogoutManager logoutManager)
	{
		m_logoutManager = logoutManager;
		socketFactory = new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
		serverBootstrap = new ServerBootstrap(socketFactory);
		messages = new MessageHandler();
		messages.register();
		this.port = port;
		SetupSocket();
	}

	public boolean StartSocket()
	{
		try
		{
			serverBootstrap.bind(new InetSocketAddress(port));
		}
		catch(ChannelException ex)
		{
			return false;
		}
		return true;
	}

	public void StopSocket()
	{
		serverBootstrap.bind().close();
	}

	public MessageHandler getMessages()
	{
		return messages;
	}

	private void SetupSocket()
	{
		ChannelPipeline pipeline = serverBootstrap.getPipeline();
		pipeline.addLast("lengthEncoder", new LengthFieldPrepender(4));
		pipeline.addLast("encoder", new NetworkEncoder());
		pipeline.addLast("decoder", new NetworkDecoder());
		pipeline.addLast("handler", new ConnectionHandler(m_logoutManager));
	}
}
