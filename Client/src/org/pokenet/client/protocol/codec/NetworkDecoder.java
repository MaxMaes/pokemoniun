package org.pokenet.client.protocol.codec;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder;
import org.pokenet.client.protocol.ServerMessage;

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
			if(buffer.readableBytes() < 4)
				return null;
			buffer.markReaderIndex();
			long length = buffer.readUnsignedInt();
			if(buffer.readableBytes() < length)
			{
				buffer.resetReaderIndex();
				return null;
			}
			int id = buffer.readUnsignedByte();
			System.out.println("[Length] <- " + length + " [ID] <- " + id);
			return new ServerMessage(buffer, id);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
}