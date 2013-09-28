package org.pokemonium.client.ui.frames.speechframes;

import java.util.LinkedList;
import java.util.Queue;
import org.pokemonium.client.GameClient;
import org.pokemonium.client.ui.components.Image;
import de.matthiasmann.twl.TextArea;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.textarea.SimpleTextAreaModel;

// import de.matthiasmann.twl.renderer.Image;

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

	// private Timer textAnimationTimer = new Timer();

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
		currentString = "";
		for(String line : speech.split("/n"))
		{
			speechQueue.add(line);
		}
		loadTriangle();
		initGUI();
		advance();
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
		currentString = "";
		for(String line : speech.split("/n"))
			speechQueue.add(line);
		loadTriangle();
		initGUI();
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
	public void initGUI()
	{
		speechModel = new SimpleTextAreaModel();
		speechDisplay = new TextArea(speechModel);

		speechDisplay.setCanAcceptKeyboardFocus(false);
		add(speechDisplay);

		setPosition(Math.round(GameClient.getInstance().getGUIPane().getWidth() / 2 - getWidth() / 2), Math.round(GameClient.getInstance().getGUIPane().getHeight() / 2 + getWidth() / 2));
		setCanAcceptKeyboardFocus(false);
		setFocusKeyEnabled(false);
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
				textModel.setText(stringToPrint, false);
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
	public void loadTriangle()
	{
		triangle = new Image(GameClient.getInstance().getTheme().getImage("speechframe_triangle"));
		add(triangle);
	}

	@Override
	public void layout()
	{
		speechDisplay.setSize(384, 100);
		speechDisplay.setPosition(getInnerX() + 12, getInnerY() + 15);
		setSize(400, 78);
		setPosition(200, 522);

		if(triangle != null)
		{
			int triangleX = getWidth() - 20 + getInnerX();
			int triangleY = triangle.getInnerY();

			if(canAdvance())
			{
				if(triangleY > 590)
				{
					triangleY = 590;
				}
				else if(triangleY < 580)
				{
					triangleY = 580;
				}
				if(triangleY == 580)
				{
					isGoingDown = true;
				}
				else if(triangleY == 590)
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
