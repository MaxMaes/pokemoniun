package org.pokenet.client.twl.ui.frames;

import org.pokenet.client.GameClient;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.Widget;


/**
 * The Alert dialog
 * @author Chappie112
 *
 */

public class AlertDialog extends ResizableFrame
{
	private Runnable okCallback;
	private Button okButton;
	private Widget pane;
	private Label dialogText;

	/*
	 * 
	 */
	public AlertDialog(String Title,String text)
	{
		setTitle(Title);
		setTheme("confirmationdialog");
		setDraggable(true);
		setSize(300,80);
		setVisible(false);

		pane = new Widget();
		pane.setTheme("content");
		pane.setSize(getWidth(), 190);
		pane.setPosition(0, 0);

		dialogText = new Label(text);
		pane.add(dialogText);

		okButton = new Button("Ok");
		pane.add(okButton);
		add(pane);

		setCenter();
	}

	@Override
	public void layout()
	{
		dialogText.adjustSize();
		dialogText.setPosition(getInnerX() +( pane.getWidth()/2 - dialogText.getWidth()/2), getInnerY() + 30);
		okButton.setSize(50, 25);
		okButton.setPosition(getInnerX() + (pane.getWidth() / 2 - okButton.getWidth()/2), getInnerY() + dialogText.getY() + 10);
	} 

	/*
	 * adds the callback method for the "Ok" button
	 */
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

	/*
	 * Centers the dialog
	 */
	public void setCenter()
	{
		int height = (int) GameClient.getInstance().getDisplay().getHeight();
		int width = (int) GameClient.getInstance().getDisplay().getWidth();
		int x = width / 2 - (int) getWidth() / 2;
		int y = height / 2 - (int) getHeight() / 2;
		this.setPosition(x, y);
	}

	public void setText(String text) 
	{
		dialogText.setText(text);
		dialogText.adjustSize();
	}

	public String getText()
	{
		return dialogText.getText();
	}
}
