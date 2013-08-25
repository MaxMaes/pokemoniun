package org.pokenet.client.twl.ui.frames;

import org.pokenet.client.GameClient;
import org.pokenet.client.constants.ServerPacket;
import org.pokenet.client.protocol.ClientMessage;
import de.matthiasmann.twl.Widget;

/**
 * NPC speech pop-up
 * 
 * @author Myth1c
 */
public class NPCSpeechFrame extends SpeechFrame
{
	/**
	 * Default Constructor
	 * 
	 * @param text
	 */
	public NPCSpeechFrame(String text, Widget root)
	{
		super(text, root);
	}

	/**
	 * Modified constructor, sets time to auto-skip to the next line.
	 * 
	 * @param text
	 * @param seconds
	 */
	public NPCSpeechFrame(String text, int seconds, Widget root)
	{
		super(text, seconds, root);
	}

	/**
	 * Sends a packet when finished displaying text
	 */
	@Override
	public void advancedPast(String advancedMe)
	{
		if(speechQueue.peek() == null)
		{
			triangle = null;
			GameClient.getInstance().getHUD().removeNPCSpeechFrame();
			ClientMessage message = new ClientMessage(ServerPacket.TALKING_FINISH);
			GameClient.getInstance().getSession().send(message);
		}
	}
}
