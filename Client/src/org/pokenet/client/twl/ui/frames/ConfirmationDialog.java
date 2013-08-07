package org.pokenet.client.twl.ui.frames;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.PopupWindow;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.TextArea;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.textarea.SimpleTextAreaModel;

/**
 * The confirmation dialog
 * 
 * @author Chappie112
 */

public class ConfirmationDialog extends ResizableFrame
{
	private Runnable yesCallback, noCallback;
	private Button yesButton, noButton;
	private TextArea dialogText;
	private SimpleTextAreaModel textModel;
	private PopupWindow popup;

	/*
	 * Creates a new ConfirmationDialog
	 * - This Dialog is only used for questions as "Do you really want to exit".
	 * 
	 * note: this constructor should only be called by GuiPane!
	 */
	public ConfirmationDialog(String text, Widget guiPane)
	{
		setTheme("confirmationdialog");

		textModel = new SimpleTextAreaModel(text);
		dialogText = new TextArea(textModel);

		yesButton = new Button("Yes");
		noButton = new Button("No");

		popup = new PopupWindow(guiPane);
		popup.setTheme("confirmationPopup");

		this.add(dialogText);
		this.add(yesButton);
		this.add(noButton);

		popup.add(this);

		popup.setCloseOnClickedOutside(false);
		popup.setCloseOnEscape(true);

		setVisible(false);
	}

	@Override
	public void layout()
	{
		yesButton.setSize(50, 25);
		noButton.setSize(50, 25);
		dialogText.setMaxSize(300, popup.getRootWidget().getHeight());
		dialogText.adjustSize();

		dialogText.setPosition((getInnerX() + (popup.getWidth() / 2 - dialogText.getWidth() / 2) + 5), getInnerY() + 10);
		yesButton.setPosition(getInnerX() + (popup.getWidth() / 2 - 60), dialogText.getY() + dialogText.getHeight() + 10);
		noButton.setPosition(getInnerX() + (popup.getWidth() / 2 + 10), yesButton.getY());
		setSize(dialogText.getWidth() + 15, dialogText.getHeight() + yesButton.getHeight() + 30);
		popup.adjustSize();
	}

	/* Sets the callback method for the "yes" button */
	public void setYesListener(Runnable callback)
	{
		if(yesCallback != null)
			removeYesCallback();
		yesButton.addCallback(callback);
		yesCallback = callback;
	}
	/* removes the callback method for the "yes" button */
	private void removeYesCallback()
	{
		yesButton.removeCallback(yesCallback);
	}

	/* Sets the callbackmethod for the "no" button */
	public void setNoListener(Runnable callback)
	{
		if(noCallback != null)
			removeNoCallback();
		noButton.addCallback(callback);
		noCallback = callback;
	}
	
	/* Removes the callback method for the "no" button */
	private void removeNoCallback()
	{
		noButton.removeCallback(noCallback);
	}

	/* Sets the text for the message */
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

	/* Simulates the events for the "yes" button */
	public void runYes()
	{
		yesCallback.run();
	}
	
	/* Simulates the events for the "no" button */
	
	public void runNo()
	{
		noCallback.run();
	}
}
