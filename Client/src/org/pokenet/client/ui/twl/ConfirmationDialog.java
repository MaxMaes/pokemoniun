package org.pokenet.client.ui.twl;

import org.pokenet.client.GameClient;

import de.matthiasmann.twl.*;
import de.matthiasmann.twl.textarea.SimpleTextAreaModel;

/**
 * The confirmation dialog
 * @author Chappie112
 *
 */

public class ConfirmationDialog extends ResizableFrame
{
	private Button yesButton,noButton;
	private Widget pane;
	private TextArea dialogText;
	
	/*
	 * 
	 */
	public ConfirmationDialog(String text)
	{
		this(text,"Awaiting Confirmation");
	}
	
	/*
	 * 
	 */
	public ConfirmationDialog(String text, String title)
	{
		setTheme("confirmationdialog");
		setTitle(title);
		setResizableAxis(ResizableAxis.NONE);
		setDraggable(false);
		setSize(300,80);
		setVisible(false);
		
		pane = new Widget();
		pane.setTheme("content");
		pane.setSize(getWidth(), 190);
		pane.setPosition(0, 0);

		SimpleTextAreaModel tam = new SimpleTextAreaModel();
		tam.setText(text);
		dialogText = new TextArea(tam);
		dialogText.setPosition(10, 30);
		dialogText.setSize(pane.getWidth()-(dialogText.getX()*2), 120);
		pane.add(dialogText);

		yesButton = new Button();
		noButton = new Button();
		
		yesButton.setText("Yes");
		yesButton.setSize(50, 25);
		yesButton.setPosition(pane.getWidth() / 2 -60, yesButton.getWidth());
		
		noButton.setText("No");
		noButton.setSize(50, 25);
		noButton.setPosition(pane.getWidth() /2 + 10, noButton.getWidth());
		pane.add(yesButton);
		pane.add(noButton);
		
		add(pane);
	
		setCenter();
	}
	
	public ConfirmationDialog(String text, Runnable callbackYes, Runnable callbackNo) 
	{
		this(text);
		addYesListener(callbackYes);
		addNoListener(callbackNo);
	}

	/*
	 * adds the callback method for the "yes" button
	 */
	public void addYesListener(Runnable callback)
	{
		yesButton.addCallback(callback);
	}
	
	/*
	 * adds the callbackmethod for the "no" button
	 */
	public void addNoListener(Runnable callback)
	{
		noButton.addCallback(callback);
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
}
