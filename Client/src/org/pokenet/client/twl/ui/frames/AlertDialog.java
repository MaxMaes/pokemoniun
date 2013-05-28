package org.pokenet.client.twl.ui.frames;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.PopupWindow;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.TextArea;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.textarea.SimpleTextAreaModel;

/**
 * The Alert dialog
 * 
 * @author Chappie112
 */

public class AlertDialog extends ResizableFrame
{
	private Runnable okCallback;
	private Button okButton;
	private TextArea dialogText;
	private SimpleTextAreaModel textModel;
	private PopupWindow popup;

	/*
	 * 
	 */
	public AlertDialog(String Title, String text, Widget widget)
	{
		setTitle(Title);
		setTheme("alertdialog");

		textModel = new SimpleTextAreaModel(text);
		dialogText = new TextArea(textModel);

		okButton = new Button("Ok");

		popup = new PopupWindow(widget);
		popup.setTheme("alertpopup");
		
		this.add(dialogText);
		this.add(okButton);
		popup.add(this);
		
		popup.setCloseOnClickedOutside(false);
		popup.setCloseOnEscape(true);
		setVisible(false);
		setSize(dialogText.getWidth() + 15, dialogText.getHeight() + okButton.getHeight() + 30);
		popup.adjustSize();
	}

	@Override
	public void layout()
	{
		okButton.setSize(50, 25);
		dialogText.setMaxSize(300, popup.getRootWidget().getHeight());
		dialogText.adjustSize();
		
		dialogText.setPosition((getInnerX() + (popup.getWidth() / 2 - dialogText.getWidth() / 2) + 5), getInnerY() + 10);
		okButton.setPosition(getInnerX() + (popup.getWidth() / 2), dialogText.getY() + dialogText.getHeight() + 10);
	}

	/* adds the callback method for the "Ok" button */
	public void addOkListener(Runnable callback)
	{
		if(okCallback != null)
			removeOkCallback();
		okButton.addCallback(callback);
		okCallback = callback;
	}

	private void removeOkCallback()
	{
		okButton.removeCallback(okCallback);
	}

	public void setText(String text)
	{
		textModel.setText(text);
	}
	
	@Override
	public void setVisible(boolean b)
	{
		if(b)
		{
			super.setVisible(b);
			popup.openPopupCentered();
		}
		else
		{
			super.setVisible(b);
			if(popup.isOpen())
				popup.closePopup();
			setText("");
		}
	}
}
