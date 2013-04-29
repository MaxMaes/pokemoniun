package org.pokenet.client.twl.ui.frames;

import java.util.LinkedList;

import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.TextArea;
import de.matthiasmann.twl.textarea.SimpleTextAreaModel;
/**
 * The dialog which controls and displays chat.
 * @author Myth1c
 *
 */
public class ChatDialog extends ResizableFrame {
	private TextArea chat;
	private EditField input;
	private LinkedList<String> lines;
	private static int MAX_LINES = 20;
	
	public ChatDialog() {
		setSize(400, 195);
		SimpleTextAreaModel model = new SimpleTextAreaModel();
		chat = new TextArea(model);
		input = new EditField();
		lines = new LinkedList<String>();
	}
	
	public void addLine(String text) {
		lines.add(text);
		if(lines.size() > MAX_LINES)
			lines.removeFirst();
		
		String txt = "";
		for(String s : lines) {
			txt += s;
			txt += "\n";
		}
		((SimpleTextAreaModel) chat.getModel()).setText(txt);
	}

}
