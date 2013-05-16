package org.pokenet.client.twl.ui.frames;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import org.pokenet.client.GameClient;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.TextArea;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.renderer.Image;
import de.matthiasmann.twl.textarea.SimpleTextAreaModel;

/**
 * Base for speech pop-ups
 * 
 * @author Myth1c
 */
public class SpeechFrame extends Widget
{
	protected String stringToPrint;

	private TimerTask animAction;

	private Image bg;
	protected Image triangle;

	private boolean isGoingDown = true;

	private Timer printingTimer = new Timer();

	private TextArea speechDisplay;
	private SimpleTextAreaModel speechModel;
	protected Queue<String> speechQueue;

	/**
	 * Default constructor
	 * 
	 * @param speech
	 */
	public SpeechFrame(String speech)
	{
		speechQueue = new LinkedList<String>();
		for(String line : speech.split("/n"))
			speechQueue.add(line);
		triangulate();
		initGUI();
	}

	/**
	 * Constructs a SpeechFrame, uses Seconds between next line.
	 * 
	 * @param speech
	 * @param seconds
	 */
	public SpeechFrame(String speech, int seconds)
	{
		speechQueue = new LinkedList<String>();
		for(String line : speech.split("/n"))
			speechQueue.add(line);
		triangulate();
		initGUI();
		advance();
		// this.advance();
		// while(canAdvance()){
		// try {
		// wait(seconds*1000);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// advance();
		// }
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
		triangle = null;

		if(animAction == null)
		{
			if(canAdvance())
			{
				((SimpleTextAreaModel) speechDisplay.getModel()).setText("");
				if(stringToPrint != null)
					advancedPast(stringToPrint);
				stringToPrint = speechQueue.poll();
				if(stringToPrint != null)
				{
					animAction = new TimerTask()
					{
						@Override
						public void run()
						{
							if(((SimpleTextAreaModel) speechDisplay.getModel()).toString().equals(stringToPrint))
							{
								animAction = null;
								try
								{
									cancel();
								}
								catch(IllegalStateException e)
								{
								}
								triangulate();
							}
							else
								try
								{
									((SimpleTextAreaModel) speechDisplay.getModel()).setText(stringToPrint.substring(0, ((SimpleTextAreaModel) speechDisplay.getModel()).toString().length() + 1));
								}
								catch(StringIndexOutOfBoundsException e)
								{
									((SimpleTextAreaModel) speechDisplay.getModel()).setText(stringToPrint);
								}
						}
					};
					try
					{
						printingTimer.schedule(animAction, 0, 30);
					}
					catch(Exception e)
					{
						animAction = null;
						e.printStackTrace();
					}
					advancing(stringToPrint);
				}
			}
		}
		else
		{
			animAction.cancel();
			animAction = null;
			((SimpleTextAreaModel) speechDisplay.getModel()).setText(stringToPrint);
			triangulate();
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
	 * Initializes the interface
	 */
	public void initGUI()
	{
		speechModel = new SimpleTextAreaModel();
		speechDisplay = new TextArea(speechModel);

		speechDisplay.setCanAcceptKeyboardFocus(false);
		speechDisplay.setSize(384, 100);
		speechDisplay.setPosition(16, 5);
		add(speechDisplay);

		setSize(400, 100);

		setPosition(Math.round(GameClient.getInstance().getGUIPane().getWidth() / 2 - getWidth() / 2), Math.round(GameClient.getInstance().getGUIPane().getHeight() / 2 + getWidth() / 2));

		setCanAcceptKeyboardFocus(false);
		setFocusKeyEnabled(false);

		// setAlwaysOnTop(true); //TODO: Chappie, teach me your ways :D
		advance();
	}

	/**
	 * Generates the triangle to show when you can continue
	 */
	public void triangulate()
	{
		triangle = GameClient.getInstance().getTheme().getImage("speechframe_triangle");
	}

	@Override
	public void paintWidget(GUI gui)
	{
		super.paintWidget(gui);
		if(triangle != null)
		{
			float triangleX = getWidth() - 30 + getInnerX();
			float triangleY = 60 + getInnerY() + 10;

			if(canAdvance())
			{
				if(triangleY > 584)
					triangleY = 584;
				else if(triangleY < 574)
					triangleY = 574;
				if(triangleY == 574)
					isGoingDown = true;
				else if(triangleY == 584)
					isGoingDown = false;
				if(isGoingDown)
					triangleY += 0.5f;
				else
					triangleY -= 0.5f;
			}
			triangle.draw(getAnimationState(), Math.round(triangleX), Math.round(triangleY));
		}
	}
}
