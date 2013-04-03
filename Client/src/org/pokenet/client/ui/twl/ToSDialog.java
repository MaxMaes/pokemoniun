package org.pokenet.client.ui.twl;

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
public class ToSDialog extends ResizableFrame
{
	private TextArea m_info;
	private Widget panel;

	/**
	 * Default constructor
	 */
	public ToSDialog()
	{
		List<String> translated = Translator.translate("_LOGIN");
		setTitle(translated.get(18));
		setPosition(128, 256);
		setResizableAxis(ResizableAxis.NONE);
		setSize(288, 320);
		setVisible(false);
		
		panel = new Widget();
		panel.setTheme("content");
		m_info = new TextArea();
		m_info.setSize(280, 320);
		m_info.setPosition(0, 25);
		
		SimpleTextAreaModel tam = new SimpleTextAreaModel();
		tam.setText(translated.get(33));
		m_info.setModel(tam);
		panel.add(m_info);
		add(panel);
		
		addCloseCallback(new Runnable() {
			@Override
			public void run() {
				setVisible(false);
			}
		});
	}
	
	public void reloadStrings()
	{
		List<String> translated = Translator.translate("_LOGIN");
		setTitle(translated.get(18));
		SimpleTextAreaModel tam = new SimpleTextAreaModel();
		tam.setText(translated.get(33));
	}
}
