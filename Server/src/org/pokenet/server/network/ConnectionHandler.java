package org.pokenet.server.network;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.pokenet.server.backend.entity.Player;
import org.pokenet.server.battle.impl.PvPBattleField;
import org.pokenet.server.connections.ActiveConnections;
import org.pokenet.server.protocol.ClientMessage;

public class ConnectionHandler extends SimpleChannelHandler
{

	private LogoutManager m_logoutManager;

	public ConnectionHandler(LogoutManager logoutManager)
	{
		m_logoutManager = logoutManager;
	}

	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
	{
		try
		{
			if(ctx.getChannel().isOpen())
			{
				Player p = ActiveConnections.GetUserByChannel(ctx.getChannel()).getPlayer();
				if(p != null)
				{
					if(p.isBattling())
					{
						/* If in PvP battle, the player loses */
						if(p.getBattleField() instanceof PvPBattleField)
							((PvPBattleField) p.getBattleField()).disconnect(p.getBattleId());
						p.setBattleField(null);
						p.setBattling(false);
						p.lostBattle();
					}
					/* If trading, end the trade */
					if(p.isTrading())
						p.getTrade().endTrade();
					m_logoutManager.queuePlayer(p);
					// GameServer.getServiceManager().getMovementService().removePlayer(p.getName());
					ActiveConnections.removeSession(ctx.getChannel());
					ctx.getChannel().close();
				}
			}
		}
		catch(Exception er)
		{
			er.printStackTrace();
		}
	}

	@Override
	public void channelOpen(ChannelHandlerContext ctnx, ChannelStateEvent e)
	{

		if(!ActiveConnections.addSession(ctnx.getChannel(), ctnx.getChannel().getRemoteAddress().toString()))
			ctnx.getChannel().disconnect(); // failed to connect
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
	{
		try
		{
			Player p = ActiveConnections.GetUserByChannel(ctx.getChannel()).getPlayer();
			if(p != null)
			{
				if(p.isBattling())
				{
					/* If in PvP battle, the player loses */
					if(p.getBattleField() instanceof PvPBattleField)
						((PvPBattleField) p.getBattleField()).disconnect(p.getBattleId());
					p.setBattleField(null);
					p.setBattling(false);
					p.lostBattle();
				}
				/* If trading, end the trade */
				if(p.isTrading())
					p.getTrade().endTrade();
				m_logoutManager.queuePlayer(p);
				// GameServer.getServiceManager().getMovementService().removePlayer(p.getName());
				ActiveConnections.removeSession(ctx.getChannel());
			}
		}
		catch(Exception er)
		{
			ctx.getChannel().close();
			System.out.println(e);
		}

	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
	{
		try
		{
			ClientMessage msg = (ClientMessage) e.getMessage();

			if(ActiveConnections.hasSession(ctx.getChannel()))
				ActiveConnections.GetUserByChannel(ctx.getChannel()).parseMessage(msg);
			else
				System.out.print("error ohno");
		}
		catch(Exception e1)
		{
			e1.printStackTrace();
		}
	}
}
