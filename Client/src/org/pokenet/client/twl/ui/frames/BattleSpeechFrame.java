package org.pokenet.client.twl.ui.frames;

import de.matthiasmann.twl.Widget;

public class BattleSpeechFrame extends SpeechFrame implements Runnable
{
	private String advancedLine;
	private Thread m_thread;
	private String newMsg;

	public BattleSpeechFrame(Widget root)
	{
		super("",root);
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
			triangulate();
		speechQueue.add(speech);
		if(stringToPrint == null || stringToPrint.equals(""))
			advance();
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
			return false;
		else
			return true;
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

		/* if(BattleManager.getInstance().canFinish())
		 * {
		 * System.out.println("Battle over, no more speech!");
		 * GameClient.getInstance().getUi().hideHUD(false);
		 * BattleManager.getInstance().getTimeLine().endBattle();
		 * BattleManager.getInstance().getBattleWindow().setVisible(false);
		 * BattleManager.getInstance().setBattling(false);
		 * BattleManager.getInstance().setFinish(false);
		 * if(GameClient.getInstance().getDisplay().containsChild(BattleManager.getInstance().getBattleWindow().m_bag))
		 * GameClient.getInstance().getDisplay().remove(BattleManager.getInstance().getBattleWindow().m_bag);
		 * GameClient.getInstance().getDisplay().remove(BattleManager.getInstance().getBattleWindow());
		 * while(GameClient.getInstance().getDisplay().containsChild(BattleManager.getInstance().getBattleWindow()))
		 * ;
		 * GameClient.getSoundPlayer().setTrackByLocation(GameClient.getInstance().getMapMatrix().getCurrentMap().getName());
		 * if(GameClient.getSoundPlayer().getTrack() == Music.PVNPC)
		 * GameClient.getSoundPlayer().setTrack(BattleManager.getInstance().getCurrentTrack());
		 * m_thread = null;
		 * } */
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
