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
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
	{
		// nun
	}

	@Override
	public void channelOpen(ChannelHandlerContext ctnx, ChannelStateEvent e)
	{

		System.out.println("Connected to game server.");
		GameClient.setSession(new Session(ctnx.getChannel()));

		/* if (!ActiveConnections.addSession(ctnx.getChannel())) { ctnx.getChannel().disconnect(); // failed to connect } */

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
	{
		ctx.getChannel().close();
		System.out.println(e);
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
	{
		try
		{
			ServerMessage msg = (ServerMessage) e.getMessage();

			if(GameClient.getSession() != null)
				GameClient.getSession().parseMessage(msg);
			else
				System.out.print("error ohno >: GameClient.getSession() == null");
		}
		catch(Exception e1)
		{
			e1.printStackTrace();
		}
	}
}
