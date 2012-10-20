package org.pokenet.client.protocol;

import java.io.IOException;
import java.nio.charset.Charset;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferOutputStream;
import org.jboss.netty.buffer.ChannelBuffers;
import org.pokenet.client.Session;

public class ClientMessage
{

	private ChannelBuffer body;
	private ChannelBufferOutputStream bodystream;
	private String message;
	private Session Player;
	
	public ClientMessage(int id)
	{
		init(id);
	}

	public ClientMessage(Session Session)
	{
		Player = Session;
	}

	public void addBool(Boolean obj)
	{
		try
		{
			bodystream.writeBoolean(obj);
			message = message + ";BOOL: " + obj;
		}
		catch(IOException e)
		{
		}
	}

	public void addByte(int obj)
	{
		try
		{
			bodystream.writeByte(obj);
			message = message + ";BYTE: " + obj;
		}
		catch(IOException e)
		{
		}
	}

	public void addInt(Integer obj)
	{
		try
		{
			bodystream.writeInt(obj);
			message = message + ";INT: " + obj;
		}
		catch(IOException e)
		{
		}
	}

	public void addShort(int obj)
	{
		try
		{
			bodystream.writeShort((short) obj);
			message = message + ";SHORT: " + obj;
		}
		catch(IOException e)
		{
		}
	}

	public void addString(String obj)
	{
		try
		{
			bodystream.writeShort(obj.length());
			bodystream.writeChars(obj);
			message = message + ";STRING: " + obj;
			// bodystream.w(obj);
		}
		catch(IOException e)
		{
		}
	}

	public ChannelBuffer get()
	{

		// body.setInt(0, body.writerIndex() - 4);
		return body;
	}

	public String getBodyString()
	{
		String str = new String(body.toString(Charset.defaultCharset()));

		String consoleText = str;

		for(int i = 0; i < 13; i++)
			consoleText = consoleText.replace(Character.toString((char) i), "{" + i + "}");

		return consoleText;
	}

	public String getMessage()
	{
		return message;
	}

	private void init(int id)
	{
		body = ChannelBuffers.dynamicBuffer();
		bodystream = new ChannelBufferOutputStream(body);
		message = "[Out] -> ID" + id;

		body.writeByte(id);

		try
		{

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void Send()
	{
		Player.send(this);
	}
}
