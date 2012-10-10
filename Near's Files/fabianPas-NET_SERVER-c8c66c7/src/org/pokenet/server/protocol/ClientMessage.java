package org.pokenet.server.protocol;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferInputStream;

public class ClientMessage
{
	private int id;
	private ChannelBuffer buffer;
	private ChannelBufferInputStream CBIS;

	public ClientMessage(ChannelBuffer b, int id)
	{	
		try {
			this.buffer = b;
			this.id = id;
			this.CBIS = new ChannelBufferInputStream(buffer);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int readInt() {
		try {
			return CBIS.readInt();
		} catch (Exception e) {
			return 0;
		}
	}
	public String readString()
	{
		try
		{
			int len = buffer.readUnsignedShort();

			char[] characters = new char[len];
			for (int i = 0; i < len; i++) {
				characters[i] = buffer.readChar();
			}

			return new String(characters);
		}
		catch (Exception e)
		{
			return "";
		}
	}
	
	public boolean readBool() {
		try {
			return CBIS.readBoolean();
		} catch (Exception e) {
			return false;
		}
		
	}

	public ChannelBuffer getBuffer() {
		return buffer;
	}

	public int getId() {
		return id;
	}
}
