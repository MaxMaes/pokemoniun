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
	private ServerBootstrap Bootstrap;
	private NioServerSocketChannelFactory Factory;
	private LogoutManager m_logoutManager;
	private MessageHandler Messages;
	private int Port;

	public Connection(int port, LogoutManager logoutManager)
	{
		m_logoutManager = logoutManager;

		Factory = new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());

		Bootstrap = new ServerBootstrap(Factory);
		Messages = new MessageHandler();
		Messages.register();
		Port = port;

		SetupSocket();
	}

	public MessageHandler getMessages()
	{
		return Messages;
	}

	public boolean StartSocket()
	{
		try
		{
			Bootstrap.bind(new InetSocketAddress(Port));
		}
		catch(ChannelException ex)
		{
			return false;
		}

		return true;
	}

	public void StopSocket()
	{
		Bootstrap.bind().close();
	}

	private void SetupSocket()
	{
		ChannelPipeline pipeline = Bootstrap.getPipeline();

		pipeline.addLast("lengthEncoder", new LengthFieldPrepender(4));
		pipeline.addLast("encoder", new NetworkEncoder());
		pipeline.addLast("decoder", new NetworkDecoder(512, 0, 4, 0, 0));
		pipeline.addLast("handler", new ConnectionHandler(m_logoutManager));
	}
}
