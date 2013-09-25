package org.pokenet.client.ui.frames;

import java.util.LinkedList;
import java.util.Queue;
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
	private Queue<String> textQueue;
	private Queue<Runnable> okButtonQueue;

	/* Creates a new alertdialog
	 * note: This constructor should be only called by GuiPane! */
	public AlertDialog(String Title, String text, Widget widget)
	{
		setTitle(Title);
		setTheme("alertdialog");

		textQueue = new LinkedList<String>();
		okButtonQueue = new LinkedList<Runnable>();

		textModel = new SimpleTextAreaModel(text);
		dialogText = new TextArea(textModel);

		okButton = new Button("Ok");

		this.add(dialogText);
		this.add(okButton);

		popup = new PopupWindow(widget);
		popup.setTheme("alertpopup");
		popup.add(this);
		popup.setCloseOnClickedOutside(false);
		popup.setCloseOnEscape(true);

		setVisible(false);
	}

	@Override
	public void layout()
	{
		okButton.setSize(50, 25);
		dialogText.setMaxSize(300, popup.getRootWidget().getHeight());
		dialogText.adjustSize();

		dialogText.setPosition((getInnerX() + (popup.getWidth() / 2 - dialogText.getWidth() / 2) + 5), getInnerY() + 10);
		okButton.setPosition(getInnerX() + (popup.getWidth() / 2), dialogText.getY() + dialogText.getHeight() + 10);
		setSize(dialogText.getWidth() + 15, dialogText.getHeight() + okButton.getHeight() + 30);
		popup.adjustSize();
		popup.centerPopup();
	}

	/* sets the callback method for the "Ok" button */
	public void setOkListener(Runnable callback)
	{
		if(okCallback != null)
			removeOkCallback();
		okButton.addCallback(callback);
		okCallback = callback;
	}

	/* removes the callback method for the "Ok" button */
	private void removeOkCallback()
	{
		okButton.removeCallback(okCallback);
	}

	/* Sets the text to be shown in the message */
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
			popup.adjustSize();
			popup.openPopupCentered();
		}
		else if(!textQueue.isEmpty() && !okButtonQueue.isEmpty())
		{
			super.setVisible(true);
			setText(textQueue.poll());
			setOkListener(okButtonQueue.poll());
			popup.adjustSize();
		}
		else
		{
			super.setVisible(b);
			if(popup.isOpen())
				popup.closePopup();
			setText("");
		}
	}

	/* Adds the message to the queue so it does not get lost. */
	public void queue(String message, Runnable runnable)
	{
		textQueue.offer(message);
		okButtonQueue.offer(runnable);
	}
}
