package org.pokemonium.client.ui.frames.speechframes;

public class BattleSpeechFrame extends SpeechFrame implements Runnable
{
	private String advancedLine;
	private Thread m_thread;
	private String newMsg;

	public BattleSpeechFrame()
	{
		super("");
		if(m_thread == null || !m_thread.isAlive())
			start();
	}

	@Override
	public void addSpeech(String speech)
	{
		if(m_thread == null || !m_thread.isAlive())
			start();

		newMsg = speech;
		if(stringToPrint != null && (stringToPrint.equals("Awaiting your move.") || stringToPrint.equals("Awaiting players' moves.")) && speechQueue.peek() == null)
		{
			loadTriangle();
		}
		speechQueue.add(speech);
		if(stringToPrint == null || stringToPrint.equals(""))
		{
			try
			{
				advance();
			}
			catch(Exception e)
			{
				System.out.println(newMsg);
				System.err.println("Failed to advance the text");
			}
		}
	}

	@Override
	public void advancedPast(String printed)
	{
		advancedLine = printed;
	}

	@Override
	public void advancing(String toPrint)
	{
	}

	@Override
	public boolean canAdvance()
	{
		if(speechQueue.peek() == null && stringToPrint != null
				&& (stringToPrint.equals("Awaiting your move.") || stringToPrint.equals("Awaiting players' moves.") || stringToPrint.equals("Awaiting opponent's Pokemon switch.")))
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	public String getAdvancedLine()
	{
		return advancedLine;
	}

	public String getCurrentLine()
	{
		return stringToPrint;
	}

	@Override
	public void run()
	{
		while(!getCurrentLine().equalsIgnoreCase(newMsg))
			;
		while(!getAdvancedLine().equalsIgnoreCase(newMsg))
			;
	}

	public void start()
	{
		if(m_thread == null || !m_thread.isAlive())
		{
			m_thread = new Thread(this);
			m_thread.start();
		}
	}

	/* TODO:
	 * @Override
	 * public void update(GUIContext ctx, int delta)
	 * {
	 * super.update(ctx, delta);
	 * if(speechDisplay.getText().equals("") && speechQueue.peek() != null
	 * && (speechQueue.peek().equals("Awaiting your move.") || speechQueue.peek().equals("Awaiting players' moves.") || speechQueue.peek().equals("Awaiting opponent's Pokemon switch.")))
	 * advance();
	 * } */

}
