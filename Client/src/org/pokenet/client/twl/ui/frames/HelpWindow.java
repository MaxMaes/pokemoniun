package org.pokenet.client.twl.ui.frames;

import java.util.List;
import org.pokenet.client.backend.Translator;

import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.TextArea;
import de.matthiasmann.twl.textarea.SimpleTextAreaModel;
import de.matthiasmann.twl.textarea.TextAreaModel;

/**
 * Instructions for new players
 * 
 * @author ZombieBear
 */
public class HelpWindow extends ResizableFrame
{
	private TextArea helptext;

	/**
	 * Default constructor
	 */
	public HelpWindow()
	{
		initGUI();
	}

	/**
	 * Sets the height
	 */
	public void setHeight(int height)
	{
		if(helptext != null)
			helptext.setSize(helptext.getWidth(), height - 5);
	}

	/**
	 * Sets the size
	 */
	@Override
	public boolean setSize(int width, int height)
	{
		if(helptext != null && width >= 10 && height >= 10)
			helptext.setSize(width - 10, height - 10);
		return super.setSize(width, height);
	}

	/**
	 * Sets the width
	 */
	public void setWidth(int width)
	{
		if(helptext != null)
			helptext.setSize(width - 5, helptext.getHeight());
	}

	/**
	 * Initializes the interface
	 */
	private void initGUI()
	{
		List<String> translated = Translator.translate("_GUI");
		setTitle(translated.get(20));

		setResizableAxis(ResizableAxis.NONE);
		helptext = new TextArea();
		helptext.setSize(355, 455);
		// setText Mover stuff to help panel.
		TextAreaModel model = new SimpleTextAreaModel(translated.get(21) + translated.get(22) + translated.get(23) + translated.get(24) + translated.get(25) + translated.get(26));
		helptext.setModel(model);
		setSize(360, 460);
		add(helptext);
		setDraggable(false);
	}
	
	@Override
	public void layout()
	{
		setPosition(200, 0);
		helptext.setPosition(getInnerX() + 5, getInnerY() + 40);
	}
}
