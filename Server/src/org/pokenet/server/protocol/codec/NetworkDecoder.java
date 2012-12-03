package org.pokenet.server.protocol.codec;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder;
import org.pokenet.server.protocol.ClientMessage;

public class NetworkDecoder extends LengthFieldBasedFrameDecoder
{

	public NetworkDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip)
	{
		super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer)
	{
		try
		{
			long length = buffer.readUnsignedInt();
			if(buffer.readableBytes() < length)
				return null;
			else
			{
				int id = buffer.readUnsignedByte();
				System.out.println("[Length] <- " + length + " [ID] <- " + id);
				return new ClientMessage(buffer, id);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
}