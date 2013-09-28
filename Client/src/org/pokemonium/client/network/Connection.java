package org.pokemonium.client.network;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelException;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.frame.LengthFieldPrepender;
import org.pokemonium.client.messages.MessageHandler;
import org.pokemonium.client.protocol.codec.NetworkDecoder;
import org.pokemonium.client.protocol.codec.NetworkEncoder;

public class Connection
{
	private NioClientSocketChannelFactory socketFactory;
	private ClientBootstrap clientBootstrap;
	private MessageHandler messages;
	private String host;
	private int port;

	public Connection(String host, int port)
	{
		socketFactory = new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
		clientBootstrap = new ClientBootstrap(socketFactory);
		messages = new MessageHandler();
		messages.register();
		this.host = host;
		this.port = port;
		SetupSocket();
	}

	public boolean Connect()
	{
		try
		{
			clientBootstrap.connect(new InetSocketAddress(host, port));
		}
		catch(ChannelException ce)
		{
			ce.printStackTrace();
			return false;
		}
		return true;
	}

	public MessageHandler getMessages()
	{
		return messages;
	}

	private void SetupSocket()
	{
		ChannelPipeline pipeline = clientBootstrap.getPipeline();
		pipeline.addLast("lengthEncoder", new LengthFieldPrepender(4));
		pipeline.addLast("encoder", new NetworkEncoder());
		pipeline.addLast("decoder", new NetworkDecoder());
		pipeline.addLast("handler", new ConnectionHandler());
	}
}
