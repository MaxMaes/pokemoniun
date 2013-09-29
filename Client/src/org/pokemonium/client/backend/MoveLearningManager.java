package org.pokemonium.client.backend;

import java.util.LinkedList;
import java.util.Queue;
import org.pokemonium.client.GameClient;
import org.pokemonium.client.ui.frames.MoveLearningDialog;

/**
 * Handles move learning, and allowis for queing items.
 * 
 * @author ZombieBear
 */
public class MoveLearningManager extends Thread
{
	private static MoveLearningManager m_instance;
	private boolean m_canLearn = false;
	private boolean m_running = true;
	private MoveLearningDialog m_moveLearning;
	private Queue<MoveLearnQueueObject> m_moveLearningQueue;

	/**
	 * Default constructor
	 */
	private MoveLearningManager()
	{
		m_moveLearningQueue = new LinkedList<MoveLearnQueueObject>();
		m_moveLearning = new MoveLearningDialog(GameClient.getInstance().getHUD());
		System.out.println("Move Learning Manager started.");
	}

	/**
	 * Returns the instance
	 * 
	 * @return ths instance
	 */
	public static MoveLearningManager getInstance()
	{
		if(m_instance == null)
			m_instance = new MoveLearningManager();
		return m_instance;
	}

	/**
	 * Returns the Move Learning window
	 * 
	 * @return the Move Learning window
	 */
	public MoveLearningDialog getMoveLearning()
	{
		return m_moveLearning;
	}

	/**
	 * A pokemon wants to learn a move
	 * 
	 * @param pokeIndex
	 * @param move
	 */
	public void learnMove(int pokeIndex, String move)
	{
		if(BattleManager.getInstance().getBattleWindow() != null)
		{
			BattleManager.getInstance().getBattleWindow().setVisible(false);
		}
		m_moveLearning.learnMove(pokeIndex, move);
		try
		{
			GameClient.getInstance().getGUIPane().add(m_moveLearning);
		}
		catch(Exception e)
		{
			System.err.println("movelearningdialog was already in tree, retrying");
			GameClient.getInstance().getGUIPane().removeChild(m_moveLearning);
			GameClient.getInstance().getGUIPane().add(m_moveLearning);
		}
	}

	/**
	 * Queues a move to be learned
	 * 
	 * @param index
	 * @param move
	 */
	public void queueMoveLearning(int index, String move)
	{
		m_running = true;
		if(m_moveLearningQueue.isEmpty())
			m_canLearn = true;
		m_moveLearningQueue.add(new MoveLearnQueueObject(index, move));
	}

	/**
	 * Removes the Move Learning window
	 */
	public void removeMoveLearning()
	{
		// BattleManager.getInstance().getBattleWindow().setAlwaysOnTop(true); TODO: //Chappie magic :D
		if(!m_moveLearningQueue.isEmpty())
			m_canLearn = true;
		GameClient.getInstance().getGUIPane().removeChild(m_moveLearning);
	}

	/**
	 * Actions to be performed while the thread runs
	 */
	@Override
	public void run()
	{
		while(m_running)
		{
			if(m_canLearn && !m_moveLearningQueue.isEmpty())
			{
				MoveLearnQueueObject temp = m_moveLearningQueue.poll();
				learnMove(temp.getPokeIndex(), temp.getMoveName());
				m_canLearn = false;
			}
			try
			{
				Thread.sleep(500);
			}
			catch(InterruptedException ie)
			{
				ie.printStackTrace();
			}
		}
	}

	public void stopMoveLearning()
	{
		m_running = false;
	}
}

/**
 * . Queue object for move learning
 * 
 * @author ZombieBear
 */
class MoveLearnQueueObject
{
	private String m_move;
	private int m_pokeIndex;

	/**
	 * Default constructor
	 * 
	 * @param index
	 * @param move
	 */
	public MoveLearnQueueObject(int index, String move)
	{
		m_pokeIndex = index;
		m_move = move;
	}

	/**
	 * Returns the name of the move to be learned
	 * 
	 * @return the name of the move to be learned
	 */
	public String getMoveName()
	{
		return m_move;
	}

	/**
	 * Returns the pokemon's index
	 * 
	 * @return the pokemon's index
	 */
	public int getPokeIndex()
	{
		return m_pokeIndex;
	}
}