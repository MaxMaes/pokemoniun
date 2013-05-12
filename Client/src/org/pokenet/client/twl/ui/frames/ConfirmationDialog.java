package org.pokenet.client.twl.ui.frames;

import org.pokenet.client.GameClient;
import org.pokenet.client.twl.ui.GUIPane;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.PopupWindow;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.TextArea;
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
	 * 
	 */
	public ConfirmationDialog(String text, GUIPane guiPane)
	{
		setTheme("confirmationdialog");
		
		textModel = new SimpleTextAreaModel(text);
		dialogText = new TextArea(textModel);

		yesButton = new Button();
		noButton = new Button();
		
		yesButton.setText("Yes");
		noButton.setText("No");

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
		
		yesButton.setSize(50,25);
		noButton.setSize(50,25);
		dialogText.setMaxSize(300, popup.getRootWidget().getHeight());
		dialogText.adjustSize();
		
		dialogText.setPosition((getInnerX() + (popup.getWidth() / 2 - dialogText.getWidth() / 2)+5), getInnerY() + 10);
		yesButton.setPosition(getInnerX() + (popup.getWidth() / 2 - 60), dialogText.getY() + dialogText.getHeight() + 10);
		noButton.setPosition(getInnerX() + (popup.getWidth() / 2 + 10), yesButton.getY());
		setSize(dialogText.getWidth() + 15,dialogText.getHeight() + yesButton.getHeight() + 30);
		popup.adjustSize();
	}

	/* adds the callback method for the "yes" button */
	public void setYesListener(Runnable callback)
	{
		if(yesCallback != null)
			removeYesCallback();
		yesButton.addCallback(callback);
		yesCallback = callback;
	}

	private void removeYesCallback()
	{
		yesButton.removeCallback(yesCallback);
	}

	/* adds the callbackmethod for the "no" button */
	public void setNoListener(Runnable callback)
	{
		if(noCallback != null)
			removeNoCallback();
		noButton.addCallback(callback);
		noCallback = callback;
	}

	private void removeNoCallback()
	{
		noButton.removeCallback(noCallback);
	}

	/* Centers the dialog */
	public void setCenter()
	{
		if(GameClient.getInstance().getGUI() != null)
		{
			int height = (int) GameClient.getInstance().getGUI().getHeight();
			int width = (int) GameClient.getInstance().getGUI().getWidth();
			int x = width / 2 - (int) getWidth() / 2;
			int y = height / 2 - (int) getHeight() / 2;
			this.setPosition(x, y);
		}
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

	public void runYes()
	{
		yesCallback.run();
	}
}
