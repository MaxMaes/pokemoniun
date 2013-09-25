package org.pokenet.client.network;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.pokenet.client.GameClient;
import org.pokenet.client.Session;
import org.pokenet.client.protocol.ServerMessage;

public class ConnectionHandler extends SimpleChannelHandler
{

	@Override
	public void channelClosed(ChannelHandlerContext channelContext, ChannelStateEvent channelState)
	{
		GameClient.getInstance().reset();
		GameClient.getInstance().showMessageDialog("You have been disconnected from the server");
	}

	@Override
	public void channelOpen(ChannelHandlerContext channelContext, ChannelStateEvent channelState)
	{
		System.out.println("Connected to game server.");
		GameClient.getInstance().setSession(new Session(channelContext.getChannel()));
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext channelContext, ExceptionEvent channelState)
	{
		channelContext.getChannel().close();
		System.out.println(channelState);
	}

	@Override
	public void messageReceived(ChannelHandlerContext channelContext, MessageEvent messageEvent)
	{
		try
		{
			ServerMessage msg = (ServerMessage) messageEvent.getMessage();
			if(GameClient.getInstance().getSession() != null)
				GameClient.getInstance().getSession().parseMessage(msg);
			else
				System.out.print("The session has been closed. GameClient.getInstance().getSession() == null");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
