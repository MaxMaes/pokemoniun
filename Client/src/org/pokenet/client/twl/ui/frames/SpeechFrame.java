package org.pokenet.client.twl.ui.frames;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import org.pokenet.client.GameClient;
import org.pokenet.client.twl.ui.base.Image;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.PopupWindow;
import de.matthiasmann.twl.TextArea;
import de.matthiasmann.twl.Widget;
// import de.matthiasmann.twl.renderer.Image;
import de.matthiasmann.twl.textarea.SimpleTextAreaModel;

/**
 * Base for speech pop-ups
 * 
 * @author Myth1c
 */
public class SpeechFrame extends Widget
{
	protected String stringToPrint;
	protected String currentString;

	protected Image triangle;

	private boolean isGoingDown = true;

	private Timer textAnimationTimer = new Timer();

	private TextArea speechDisplay;
	private SimpleTextAreaModel speechModel;
	protected Queue<String> speechQueue;

	private PopupWindow popup;

	/**
	 * Default constructor
	 * 
	 * @param speech
	 */
	public SpeechFrame(String speech, Widget root)
	{
		speechQueue = new LinkedList<String>();
		currentString = "";
		for(String line : speech.split("/n"))
			speechQueue.add(line);
		triangulate();
		initGUI(root);
	}

	/**
	 * Constructs a SpeechFrame, uses Seconds between next line.
	 * 
	 * @param speech
	 * @param seconds
	 */
	public SpeechFrame(String speech, int seconds, Widget root)
	{
		speechQueue = new LinkedList<String>();
		currentString = "";
		for(String line : speech.split("/n"))
			speechQueue.add(line);
		triangulate();
		initGUI(root);
		advance();
		this.advance();
		while(canAdvance())
		{
			try
			{
				wait(seconds * 1000);
			}
			catch(InterruptedException e)
			{
				e.printStackTrace();
			}
			advance();
		}
	}

	/**
	 * Initializes the interface
	 */
	public void initGUI(Widget root)
	{
		speechModel = new SimpleTextAreaModel();
		speechDisplay = new TextArea(speechModel);

		speechDisplay.setCanAcceptKeyboardFocus(false);
		add(speechDisplay);

		setPosition(Math.round(GameClient.getInstance().getGUIPane().getWidth() / 2 - getWidth() / 2), Math.round(GameClient.getInstance().getGUIPane().getHeight() / 2 + getWidth() / 2));

		setCanAcceptKeyboardFocus(false);
		setFocusKeyEnabled(false);

		popup = new PopupWindow(root);
		popup.setTheme("speechframepopup");
		popup.add(this);
		popup.setCloseOnClickedOutside(false);
		popup.setCloseOnEscape(false);
		popup.adjustSize();

		advance();
	}

	/**
	 * Adds a line to the queue
	 * 
	 * @param speech
	 */
	public void addSpeech(String speech)
	{
		speechQueue.add(speech);
	}

	/**
	 * Advances to next message
	 */
	public void advance()
	{
		if(canAdvance())
		{
			SimpleTextAreaModel textModel = (SimpleTextAreaModel) speechDisplay.getModel();
			if(stringToPrint != null)
			{
				advancedPast(stringToPrint);
			}
			stringToPrint = speechQueue.poll();
			if(stringToPrint != null)
			{
				// textModel.setText(stringToPrint, false);

				TimerTask textAnimation = new TimerTask()
				{

					@Override
					public void run()
					{
						SimpleTextAreaModel textModel = (SimpleTextAreaModel) speechDisplay.getModel();
						if(currentString.equalsIgnoreCase(stringToPrint))
						{
							// The string is fully displayed, no need to run this any further.
							textAnimationTimer.cancel();
						}
						else
						{
							// Take a substring of 1 more character than current length.
							if(currentString.length() + 1 <= stringToPrint.length())
							{
								currentString = stringToPrint.substring(0, currentString.length() + 1);
								textModel.setText(currentString, false);
							}
							else
							{
								currentString = stringToPrint;
								textModel.setText(currentString, false);
							}
						}
					}
				};
				textAnimationTimer.schedule(textAnimation, 0, 30);
			}
		}
	}

	/**
	 * ???
	 * 
	 * @param done
	 */
	public void advanced(String done)
	{

	}

	/**
	 * Sends a packet when finished showing dialog
	 * 
	 * @param printed
	 */
	public void advancedPast(String printed)
	{

	}

	/**
	 * ?????
	 * 
	 * @param toPrint
	 */
	public void advancing(String toPrint)
	{

	}

	/**
	 * Returns true if the player can advance
	 * 
	 * @return
	 */
	public boolean canAdvance()
	{
		return true;
	}

	/**
	 * Generates the triangle to show when you can continue
	 */
	public void triangulate()
	{
		triangle = new Image(GameClient.getInstance().getTheme().getImage("speechframe_triangle"));
		add(triangle);
	}

	public void setSpeechOpened(boolean isOpen)
	{
		if(isOpen)
		{
			popup.openPopup();
		}
		else
		{
			popup.closePopup();
		}
	}

	public boolean isOpen()
	{
		return popup.isOpen();
	}

	public void destroyPopup()
	{
		popup.closePopup();
		popup.destroy();
	}

	@Override
	public void paintWidget(GUI gui)
	{
		super.paintWidget(gui);
		/* if(triangle != null)
		 * {
		 * float triangleX = getWidth() - 30 + getInnerX();
		 * float triangleY = 60 + getInnerY() + 10;
		 * if(canAdvance())
		 * {
		 * if(triangleY > 584)
		 * {
		 * triangleY = 584;
		 * }
		 * else if(triangleY < 574)
		 * {
		 * triangleY = 574;
		 * }
		 * if(triangleY == 574)
		 * {
		 * isGoingDown = true;
		 * }
		 * else if(triangleY == 584)
		 * {
		 * isGoingDown = false;
		 * }
		 * if(isGoingDown)
		 * {
		 * triangleY += 0.5f;
		 * }
		 * else
		 * {
		 * triangleY -= 0.5f;
		 * }
		 * }
		 * triangle.draw(getAnimationState(), Math.round(triangleX), Math.round(triangleY));
		 * } */
	}

	@Override
	public void layout()
	{
		speechDisplay.setSize(384, 100);
		speechDisplay.setPosition(getInnerX() + 12, getInnerY() + 15);
		setSize(400, 78);

		popup.setPosition(200, 500);

		if(triangle != null)
		{
			int triangleX = getWidth() - 20 + getInnerX();
			int triangleY = triangle.getInnerY();

			if(canAdvance())
			{
				if(triangleY > 560)
				{
					triangleY = 560;
				}
				else if(triangleY < 550)
				{
					triangleY = 550;
				}
				if(triangleY == 550)
				{
					isGoingDown = true;
				}
				else if(triangleY == 560)
				{
					isGoingDown = false;
				}
				if(isGoingDown)
				{
					triangleY += 1;
				}
				else
				{
					triangleY -= 1;
				}
			}
			triangle.setPosition(triangleX, triangleY);
		}
	}
}
