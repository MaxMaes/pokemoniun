package org.pokenet.client.protocol;

import java.io.IOException;
import java.nio.charset.Charset;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferOutputStream;
import org.jboss.netty.buffer.ChannelBuffers;
import org.pokenet.client.Session;

public class ClientMessage {

	private ChannelBuffer body;
	private ChannelBufferOutputStream bodystream;
	private Session Player;
	private String message;

	public ClientMessage(Session Session) {
		Player = Session;
	}
	public ClientMessage() {
	}
	
	public void Init(int id)
	{
		this.body = ChannelBuffers.dynamicBuffer();
		this.bodystream = new ChannelBufferOutputStream(body);
		this.message = "[Out] -> ID" + id;

		body.writeByte(id);

		try {

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void addString(String obj)
	{
		try {
			this.bodystream.writeShort(obj.length());
			this.bodystream.writeChars(obj);
			this.message = message + ";STRING: " + obj;
			//bodystream.w(obj);
		} catch (IOException e) {
		}
	}
	public void addInt(Integer obj)
	{
		try {
			bodystream.writeInt(obj);
			this.message = message + ";INT: " + obj;
		} catch (IOException e) {
		}
	}
	public void addShort(int obj)
	{
		try {
			bodystream.writeShort((short)obj);
			this.message = message + ";SHORT: " + obj;
		} catch (IOException e) {
		}
	}
	public void addByte(int obj)
	{
		try {
			bodystream.writeByte(obj);
			this.message = message + ";BYTE: " + obj;
		} catch (IOException e) {
		}
	}
	public void addBool(Boolean obj)
	{
		try {
			bodystream.writeBoolean(obj);
			this.message = message + ";BOOL: " + obj;
		} catch (IOException e) {
		}
	}
	public String getBodyString()
	{
		String str = new String(this.body.toString(Charset.defaultCharset()));

		String consoleText = str;

		for (int i = 0; i < 13; i++)
		{ 
			consoleText = consoleText.replace(Character.toString((char)i), "{" + i + "}");
		}

		return consoleText;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public void Send() {
		Player.Send(this);
	}
	public ChannelBuffer get() {

		//body.setInt(0, body.writerIndex() - 4);
		return this.body;
	}
}
