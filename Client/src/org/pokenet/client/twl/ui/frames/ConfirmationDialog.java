package org.pokenet.client.twl.ui.frames;

import org.pokenet.client.GameClient;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.Widget;


/**
 * The confirmation dialog
 * @author Chappie112
 *
 */

public class ConfirmationDialog extends ResizableFrame
{
	private Runnable yesCallback, noCallback;
	private Button yesButton,noButton;
	private Widget pane;
	private Label dialogText;
	
	/*
	 * 
	 */
	public ConfirmationDialog(String text)
	{
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

		yesButton = new Button();
		noButton = new Button();
		yesButton.setText("Yes");
		noButton.setText("No");
		pane.add(yesButton);
		pane.add(noButton);
		add(pane);
	
		setCenter();
	}
	
	@Override
	public void layout()
	{
		dialogText.adjustSize();
		dialogText.setPosition(getInnerX() +( pane.getWidth()/2 - dialogText.getWidth()/2), getInnerY() + 30);
		yesButton.setSize(50, 25);
		yesButton.setPosition(getInnerX() + (pane.getWidth() / 2 -60), getInnerY() + yesButton.getWidth());
		noButton.setSize(50, 25);
		noButton.setPosition(getInnerX() + (pane.getWidth() /2 + 10), getInnerY() + noButton.getWidth());
	} 

	/*
	 * adds the callback method for the "yes" button
	 */
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

	/*
	 * adds the callbackmethod for the "no" button
	 */
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
	
	public void runYes()
	{
		yesCallback.run();
	}
}
