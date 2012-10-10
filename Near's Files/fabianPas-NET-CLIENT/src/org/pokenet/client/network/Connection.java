package org.pokenet.client.network;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelException;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.pokenet.client.messages.MessageHandler;
import org.pokenet.client.protocol.codec.NetworkDecoder;
import org.pokenet.client.protocol.codec.NetworkEncoder;

public class Connection
{
	private NioClientSocketChannelFactory Factory;
	private ClientBootstrap Bootstrap;

	private MessageHandler Messages;

	private int Port;
	private String Host;

	public Connection(String Host, int port)
	{

		this.Factory = new NioClientSocketChannelFactory
				(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()
				);

		this.Bootstrap = new ClientBootstrap(Factory);
		this.Messages = new MessageHandler();
		this.Messages.register();
		this.Port = port;
		this.Host = Host;

		SetupSocket();
	}

	private void SetupSocket()
	{
		ChannelPipeline pipeline = this.Bootstrap.getPipeline();

		pipeline.addLast("encoder", new NetworkEncoder());
		pipeline.addLast("decoder", new NetworkDecoder());
		pipeline.addLast("handler", new ConnectionHandler());
	}

	public boolean Connect()
	{
		try
		{
			this.Bootstrap.connect(new InetSocketAddress(Host, Port));
		}
		catch (ChannelException ex)
		{
			System.out.println(ex.getStackTrace());
			return false;
		}

		return true;
	}
	public MessageHandler getMessages() {
		return Messages;
	}
}
