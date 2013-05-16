package org.pokenet.client.twl.ui.frames;

import org.pokenet.client.GameClient;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.Widget;

/**
 * Shows a message box
 * 
 * @author ZombieBear
 */
public class MessageDialog extends ResizableFrame
{

	private Label label;
	private Widget pane;
	private Button ok;

	/**
	 * Default constructor
	 * 
	 * @param message
	 * @param container
	 */
	public MessageDialog(String message)
	{
		setTheme("confirmationdialog");
		setDraggable(true);
		setSize(300, 80);
		setVisible(false);

		pane = new Widget();
		pane.setTheme("content");
		pane.setSize(getWidth(), 190);
		pane.setPosition(0, 0);

		label = new Label();
		label.setText(message);
		pane.add(label);

		ok = new Button("Ok");
		ok.addCallback(new Runnable()
		{

			@Override
			public void run()
			{
				setVisible(false);
			}
		});
		pane.add(ok);
		setCenter();
	}

	@Override
	public void layout()
	{
		label.adjustSize();
		label.setPosition(getInnerX() + 5, getInnerY() + 15);
		ok.setSize(50, 25);
		ok.setPosition(getInnerX() + (getWidth() / 2 - ok.getWidth() / 2), getInnerY() + (getHeight() - 60));
	}

	/* Centers the dialog */
	public void setCenter()
	{
		int height = (int) GameClient.getInstance().getGUIPane().getHeight();
		int width = (int) GameClient.getInstance().getGUIPane().getWidth();
		int x = width / 2 - (int) getWidth() / 2;
		int y = height / 2 - (int) getHeight() / 2;
		this.setPosition(x, y);
	}
}