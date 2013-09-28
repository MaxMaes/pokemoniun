package org.pokenet.client.ui.frames;

import java.util.ArrayList;
import java.util.HashMap;
import org.pokenet.client.GameClient;
import org.pokenet.client.constants.ServerPacket;
import org.pokenet.client.protocol.ClientMessage;
import de.matthiasmann.twl.ComboBox;
import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.Event;
import de.matthiasmann.twl.ScrollPane;
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
	private ScrollPane chat;
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

		chatView = new TextArea(chats.get("Global"));
		input = new EditField();

		chat = new ScrollPane(chatView);

		possibleBoxesModel = new SimpleChangableListModel<>();
		possibleBoxes = new ComboBox<String>(possibleBoxesModel);
		possibleBoxes.setCanAcceptKeyboardFocus(false);
		possibleBoxes.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				updateChatview();
			}
		});

		enterCallback = new EditField.Callback()
		{
			@Override
			public void callback(int key)
			{
				if(key == Event.KEY_RETURN)
				{
					if(input.getText() != null && input.getText().length() != 0)
						if(input.getText().charAt(0) == '/')
						{
							ClientMessage message = new ClientMessage(ServerPacket.PLAYER_COMMAND);
							message.addString(input.getText().substring(1));
							GameClient.getInstance().getSession().send(message);
						}
						else if(getSelectedChatboxName().equalsIgnoreCase("Global"))
						{
							ClientMessage message = new ClientMessage(ServerPacket.CHAT);
							message.addInt(1);
							message.addString(input.getText());
							GameClient.getInstance().getSession().send(message);
						}
						else if(getSelectedChatboxName().equalsIgnoreCase("System"))
						{
							// Ignore
						}
						else
						{
							ClientMessage message = new ClientMessage(ServerPacket.CHAT);
							message.addInt(2);
							message.addString(getSelectedChatboxName() + "," + input.getText());
							GameClient.getInstance().getSession().send(message);
							addWhisperLine(getSelectedChatboxName(), "<" + GameClient.getInstance().getOurPlayer().getUsername() + "> " + input.getText());
						}
					input.setText("");
					input.giveupKeyboardFocus();
					giveupKeyboardFocus();
				}
			}
		};
		input.addCallback(enterCallback);

		addChatChannel("Global", false);
		addChatChannel("System", false);
		possibleBoxes.setSelected(0);

		// add(chatView);
		add(possibleBoxes);
		add(input);
		add(chat);
	}

	private void updateChatview()
	{
		String selected = possibleBoxesModel.getEntry(possibleBoxes.getSelected());
		chatView.setModel(chats.get(selected));
		if(selected.equalsIgnoreCase("System"))
		{
			input.setEnabled(false);
			input.setVisible(false);
		}
		else
		{
			input.setEnabled(true);
			input.setVisible(true);
		}
	}

	/**
	 * Adds a line to the given chatbox
	 * 
	 * @param text
	 */
	public void addLineTo(final String text, final String chatbox)
	{
		GameClient.getInstance().getGUI().invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				SimpleTextAreaModel chatModel = chats.get(chatbox);
				ArrayList<String> lines = chatlines.get(chatbox);
				lines.add(text);

				String txt = "";
				for(String s : lines)
				{
					txt += s;
					txt += "\n";
				}
				chatModel.setText(txt);
				chat.validateLayout();
				chat.setScrollPositionY(chat.getMaxScrollPosY());
			}
		});
	}

	/**
	 * Creates a new chat channel
	 * 
	 * @param chat
	 */
	public void addChatChannel(String chattitle, boolean isWhisper)
	{
		if(!chats.containsKey(chattitle))
		{
			SimpleTextAreaModel newModel = new SimpleTextAreaModel();
			chats.put(chattitle, newModel);
			ArrayList<String> newLines = new ArrayList<String>();
			chatlines.put(chattitle, newLines);
			possibleBoxesModel.addElement(chattitle);
		}

		possibleBoxes.setSelected(possibleBoxesModel.findElement(chattitle));
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
			addChatChannel(chat, true);
		}
		addLineTo(line, chat);
	}

	private String getSelectedChatboxName()
	{
		return possibleBoxesModel.getEntry(possibleBoxes.getSelected());
	}

	/**
	 * Add a line to the System chatbox and change to it.
	 * 
	 * @param text
	 */
	public void addSystemMessage(String text)
	{
		addLineTo(text, "System");
		possibleBoxes.setSelected(possibleBoxesModel.findElement("System"));
		setVisible(true);
	}

	/**
	 * Adds a line to the global chat
	 * 
	 * @param text
	 */
	public void addLine(String text)
	{
		addLineTo(text, "Global");
	}

	@Override
	public void layout()
	{
		setSize(300, 200);
		setPosition(0, GameClient.getInstance().getGUI().getHeight() - getHeight());
		input.setSize(getWidth(), 25);
		possibleBoxes.setSize(getWidth(), 22);
		possibleBoxes.setPosition(getInnerX(), getInnerY());
		input.setPosition(getInnerX(), getInnerY() + getHeight() - input.getHeight());

		chatView.setSize(getWidth() - 25, getHeight() - possibleBoxes.getHeight() - input.getHeight() - 10);
		chatView.setPosition(0, 0);

		chat.setPosition(getInnerX(), getInnerY() + possibleBoxes.getHeight() + 2);
		chat.setSize(getWidth(), getHeight() - possibleBoxes.getHeight() - input.getHeight() - 4);
	}
}
