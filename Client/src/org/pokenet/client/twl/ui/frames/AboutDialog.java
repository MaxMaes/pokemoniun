package org.pokenet.client.twl.ui.frames;

import java.util.List;

import org.pokenet.client.backend.Translator;

import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.TextArea;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.textarea.SimpleTextAreaModel;

/**
 * A window with information about the game
 * 
 * @author Myth1c
 */
public class AboutDialog extends ResizableFrame
{
	private TextArea m_info;
	private Widget panel;

	/**
	 * Default constructor
	 */
	public AboutDialog()
	{
		List<String> translated = Translator.translate("_LOGIN");
		setTitle(translated.get(34));
		setPosition(0, 0);
		setSize(288, 320);
		
		panel = new Widget();
		panel.setTheme("content");
		panel.setSize(288, 320);
		panel.setPosition(0, 0);

		SimpleTextAreaModel tam = new SimpleTextAreaModel();
        tam.setText(translated.get(35) + "\n" + translated.get(36) + "\n" + translated.get(37) + "\n" + translated.get(38) + "\n" + translated.get(39) + "\n");
		m_info = new TextArea(tam);
		m_info.setPosition(0, 25);
		m_info.setSize(280, 500);
		panel.add(m_info);
		add(panel);
		
		addCloseCallback(new Runnable() {
			@Override
			public void run() {
				setVisible(false);
			}
		});
		
		setVisible(false);
	}

	public void reloadStrings()
	{
		List<String> translated = Translator.translate("_LOGIN");
		setTitle(translated.get(34));
		SimpleTextAreaModel tam = new SimpleTextAreaModel();
		tam.setText(translated.get(35) + "\n" + translated.get(36) + "\n" + translated.get(37) + "\n" + translated.get(38) + "\n" + translated.get(39) + "\n");
		m_info.setModel(tam);
	}
}
