package org.pokenet.client.twl.ui.frames;

import java.util.ArrayList;
import java.util.HashMap;
import org.pokenet.client.GameClient;
import org.pokenet.client.backend.ModerationManager;
import org.pokenet.client.constants.ServerPacket;
import org.pokenet.client.protocol.ClientMessage;
import de.matthiasmann.twl.ComboBox;
import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.TextArea;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.model.SimpleChangableListModel;
import de.matthiasmann.twl.textarea.SimpleTextAreaModel;

/**
 * The dialog which controls and displays chat.
 * 
 * @author Myth1c
 */
public class ChatDialog extends Widget
{
	private TextArea chatView;
	private EditField input;
	private ComboBox<String> possibleBoxes;

	private HashMap<String, SimpleTextAreaModel> chats;
	private HashMap<String, ArrayList<String>> chatlines;

	private EditField.Callback enterCallback;
	private SimpleChangableListModel<String> possibleBoxesModel;

	public ChatDialog()
	{
		chats = new HashMap<String, SimpleTextAreaModel>();
		chatlines = new HashMap<String, ArrayList<String>>();

		SimpleTextAreaModel global = new SimpleTextAreaModel();
		chats.put("Global", global);
		ArrayList<String> globallines = new ArrayList<String>();
		chatlines.put("Global", globallines);

		chatView = new TextArea(global);
		input = new EditField();

		possibleBoxesModel = new SimpleChangableListModel<>("Global");
		possibleBoxes = new ComboBox<String>(possibleBoxesModel);
		possibleBoxes.setSelected(0);

		enterCallback = new EditField.Callback()
		{
			@Override
			public void callback(int key)
			{
				if(key == Event.KEY_RETURN)
				{
					if(input.getText() != null && input.getText().length() != 0)
						if(input.getText().charAt(0) == '/')
							ModerationManager.parseLine(input.getText().substring(1));
						else if(getSelectedChatboxName().equalsIgnoreCase("Global"))
						{
							// GameClient.getInstance().getPacketGenerator().writeTcpMessage("39" + m_inputBox.getText());
							ClientMessage message = new ClientMessage(ServerPacket.CHAT);
							message.addInt(1);
							message.addString(input.getText());
							GameClient.getInstance().getSession().send(message);
						}
						else
						{
							ClientMessage message = new ClientMessage(ServerPacket.CHAT);
							message.addInt(2);
							message.addString(getSelectedChatboxName() + "," + input.getText());
							GameClient.getInstance().getSession().send(message);
							// GameClient.getInstance().getPacketGenerator().writeTcpMessage("3B" + m_possibleChats.getSelected() + "," + m_inputBox.getText());
							addWhisperLine(getSelectedChatboxName(), "<" + GameClient.getInstance().getOurPlayer().getUsername() + "> " + input.getText());
						}
					input.setText("");
					input.requestKeyboardFocus();
				}
			}
		};
		input.addCallback(enterCallback);
		add(chatView);
		add(possibleBoxes);
		add(input);
	}

	/**
	 * Adds a line to the global chat
	 * 
	 * @param text
	 */
	public void addLine(String text)
	{
		SimpleTextAreaModel global = chats.get("Global");
		ArrayList<String> lines = chatlines.get("Global");
		lines.add(text);

		String txt = "";
		for(String s : lines)
		{
			txt += s;
			txt += "\n";
		}
		global.setText(txt);
	}

	/**
	 * Adds a line to the global chat
	 * 
	 * @param text
	 */
	public void addLineTo(String text, String chatbox)
	{
		SimpleTextAreaModel chat = chats.get(chatbox);
		ArrayList<String> lines = chatlines.get(chatbox);

		String txt = "";
		for(String s : lines)
		{
			txt += s;
			txt += "\n";
		}
		chat.setText(txt);
	}

	/**
	 * Creates a new private chat channel
	 * 
	 * @param chat
	 */
	public void addChat(String chat, boolean isWhisper)
	{
		if(!chats.containsKey(chat))
		{
			chatlines.put(chat, new ArrayList<String>());
			possibleBoxesModel.addElement(chat);
		}
		possibleBoxes.setSelected(possibleBoxesModel.findElement(chat));
	}

	/**
	 * Adds a line to a private chat, creates the private chat if it doesn't exist
	 * 
	 * @param chat
	 * @param line
	 */
	public void addWhisperLine(String chat, String line)
	{
		if(!chats.containsKey(chat))
		{
			addChat(chat, true);
		}
		addLineTo(line, chat);
	}

	private String getSelectedChatboxName()
	{
		return possibleBoxesModel.getEntry(possibleBoxes.getSelected());
	}

	/**
	 * Adds the text to every chatbox.
	 * TODO: Add chatbox: SYSTEM and autochange to it on line add.
	 * 
	 * @param text
	 */
	public void addSystemMessage(String text)
	{
		for(String chatbox : chats.keySet())
		{
			addLineTo(text, chatbox);
		}
	}

	public void addAnnouncement(String readString)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void layout()
	{
		setSize(300, 200);
		setPosition(0, GameClient.getInstance().getGUI().getHeight() - getHeight());
		possibleBoxes.setSize(getWidth(), 22);
		possibleBoxes.setPosition(getInnerX(), getInnerY());
		chatView.setSize(getWidth(), 300);
		chatView.setPosition(getInnerX(), getInnerY() + possibleBoxes.getHeight() + 2);
		input.setSize(getWidth(), 25);
		input.setPosition(getInnerX(), getInnerY() + getHeight() - input.getHeight());

	}
}
