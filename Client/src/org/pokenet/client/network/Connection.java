package org.pokenet.client.network;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelException;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.frame.LengthFieldPrepender;
import org.pokenet.client.messages.MessageHandler;
import org.pokenet.client.protocol.codec.NetworkDecoder;
import org.pokenet.client.protocol.codec.NetworkEncoder;

public class Connection
{
	private ClientBootstrap Bootstrap;
	private NioClientSocketChannelFactory Factory;

	private String Host;

	private MessageHandler Messages;
	private int Port;

	public Connection(String Host, int port)
	{

		Factory = new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());

		Bootstrap = new ClientBootstrap(Factory);
		Messages = new MessageHandler();
		Messages.register();
		Port = port;
		this.Host = Host;

		SetupSocket();
	}

	public boolean Connect()
	{
		try
		{
			Bootstrap.connect(new InetSocketAddress(Host, Port));
		}
		catch(ChannelException ex)
		{
			System.out.println(ex.getStackTrace());
			return false;
		}

		return true;
	}

	public MessageHandler getMessages()
	{
		return Messages;
	}

	private void SetupSocket()
	{
		ChannelPipeline pipeline = Bootstrap.getPipeline();

		pipeline.addLast("lengthEncoder", new LengthFieldPrepender(4));
		pipeline.addLast("encoder", new NetworkEncoder());
		pipeline.addLast("decoder", new NetworkDecoder(512, 0, 4, 0, 0));
		pipeline.addLast("handler", new ConnectionHandler());
	}
}
