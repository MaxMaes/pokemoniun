package org.pokenet.server.protocol.codec;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.pokenet.server.protocol.ClientMessage;

public class NetworkDecoder extends FrameDecoder 
{
	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer)
	{
		try
		{
			int opcode = buffer.readUnsignedByte();
			System.out.println("[In] <- " + opcode);
			return new ClientMessage(buffer, opcode);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}