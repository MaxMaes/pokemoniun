package org.pokenet.client.twl.ui;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.pokenet.client.GameClient;
import org.pokenet.client.backend.BattleManager;
import org.pokenet.client.backend.FileLoader;
import org.pokenet.client.backend.MoveLearningManager;
import org.pokenet.client.constants.ServerPacket;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.twl.ui.base.Image;
import org.pokenet.client.ui.base.ConfirmationDialog;
import de.matthiasmann.twl.Alignment;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.Widget;

/**
 * Handles move learning and evolution
 * 
 * @author Myth1c
 */
public class MoveLearning extends ResizableFrame
{
	public List<Button> m_moveButtons = new ArrayList<Button>();
	public List<Label> m_pp = new ArrayList<Label>();
	private Button m_cancel;
	private MoveLearnCanvas m_canvas;
	private String m_move;
	private Widget m_movePane;
	private int m_pokeIndex;
	private ConfirmationDialog m_replace;
	private Button move1, move2, move3, move4;
	private Label pp1, pp2, pp3, pp4;

	InputStream f;
	// Image Loading tools
	String m_path = "res/battle/";

	/**
	 * Default Constructor
	 * 
	 * @param pokeIndex
	 * @param move
	 * @param isMoveLearning
	 */
	public MoveLearning()
	{
		String respath = System.getProperty("res.path");
		if(respath == null)
			respath = "";
		m_path = respath + m_path;
		setPosition(getX() - 1, getY() + 1);
		m_canvas = new MoveLearnCanvas();
		add(m_canvas);
		setSize(259, 369);
		initGUI();
		setCenter();
	}

	/**
	 * Starts the GUI
	 * 
	 * @param isMoveLearning
	 */
	public void initGUI()
	{
		/* TRUE = Move Learning, FALSE = Evolution TODO: Whut is this comment? */
		String respath = System.getProperty("res.path");
		if(respath == null)
			respath = "";

		m_movePane = new Widget();
		m_movePane.setSize(257, 201);
		m_movePane.setPosition(2, 140);

		move1 = new Button("");
		move1.setTheme("battlebutton");
		move2 = new Button("");
		move2.setTheme("battlebutton");
		move3 = new Button("");
		move3.setTheme("battlebutton");
		move4 = new Button("");
		move4.setTheme("battlebutton");

		setResizableAxis(ResizableAxis.NONE);

		// start attackPane
		m_movePane.add(move1);
		move1.setPosition(7, 10);
		move1.setSize(116, 51);
		move1.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				replaceMove(0);
			}
		});
		pp1 = new Label();
		pp1.setAlignment(Alignment.RIGHT);
		pp1.setSize(110, 20);
		pp1.setPosition(7, 40);
		m_movePane.add(pp1);

		m_movePane.add(move2);
		move2.setPosition(130, 10);
		move2.setSize(116, 51);
		move2.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				replaceMove(1);
			}
		});
		pp2 = new Label();
		pp2.setAlignment(Alignment.RIGHT);
		pp2.setSize(110, 20);
		pp2.setPosition(130, 40);
		m_movePane.add(pp2);

		m_movePane.add(move3);
		move3.setPosition(7, 65);
		move3.setSize(116, 51);
		move3.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				replaceMove(2);
			}
		});
		pp3 = new Label();
		pp3.setAlignment(Alignment.RIGHT);
		pp3.setSize(110, 20);
		pp3.setPosition(7, 95);
		m_movePane.add(pp3);

		m_movePane.add(move4);
		move4.setPosition(130, 65);
		move4.setSize(116, 51);
		move4.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				replaceMove(3);
			}
		});
		pp4 = new Label();
		pp4.setAlignment(Alignment.RIGHT);
		pp4.setPosition(130, 95);
		pp4.setSize(110, 20);
		m_movePane.add(pp4);

		m_moveButtons.add(move1);
		m_moveButtons.add(move2);
		m_moveButtons.add(move3);
		m_moveButtons.add(move4);

		m_pp.add(pp1);
		m_pp.add(pp2);
		m_pp.add(pp3);
		m_pp.add(pp4);

		m_cancel = new Button("Cancel");
		m_cancel.setSize(246, 77);
		m_cancel.setPosition(3, 122);
		m_cancel.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				MoveLearningManager.getInstance().removeMoveLearning();
				// GameClient.getInstance().getPacketGenerator().writeTcpMessage("0A" + m_pokeIndex + m_move);
				ClientMessage message = new ClientMessage(ServerPacket.DONT_LEARN_MOVE);
				message.addInt(m_pokeIndex);
				message.addString(m_move);
				GameClient.getInstance().getSession().send(message);
			}
		});
		m_movePane.add(m_cancel);

		add(m_movePane);
	}

	public void learnMove(int pokeIndex, String move)
	{
		// setAlwaysOnTop(true); TODO: Chappie magic :D
		m_pokeIndex = pokeIndex;

		// GameClient.getInstance().getGUIPane().talkToNPC(GameClient.getInstance().getOurPlayer().getPokemon()[pokeIndex].getName() + " wants to learn " + move); TODO:

		m_move = move;

		move1.setText(GameClient.getInstance().getOurPlayer().getPokemon()[m_pokeIndex].getMoves()[0]);
		move2.setText(GameClient.getInstance().getOurPlayer().getPokemon()[m_pokeIndex].getMoves()[1]);
		move3.setText(GameClient.getInstance().getOurPlayer().getPokemon()[m_pokeIndex].getMoves()[2]);
		move4.setText(GameClient.getInstance().getOurPlayer().getPokemon()[m_pokeIndex].getMoves()[3]);

		for(int i = 0; i < 4; i++)
			if(m_moveButtons.get(i).getText().equals(""))
				m_pp.get(i).setVisible(false);
			else
			{
				m_pp.get(i).setText(
						GameClient.getInstance().getOurPlayer().getPokemon()[pokeIndex].getMoveCurPP()[i] + "/" + GameClient.getInstance().getOurPlayer().getPokemon()[pokeIndex].getMoveMaxPP()[i]);
				m_pp.get(i).setVisible(true);
			}

		m_movePane.setVisible(true);
		m_canvas.draw(pokeIndex);
	}

	/**
	 * Centers the frame
	 */
	public void setCenter()
	{
		int height = (int) GameClient.getInstance().getDisplay().getHeight();
		int width = (int) GameClient.getInstance().getDisplay().getWidth();
		int x = width / 2 - 130;
		int y = height / 2 - 238;
		this.setPosition(x, y);
	}

	/**
	 * Handles move replacement
	 * 
	 * @param i
	 */
	private void replaceMove(int i)
	{
		final int j = i;
		if(!GameClient.getInstance().getDisplay().containsChild(m_replace))
			if(m_moveButtons.get(i).getText().equals(""))
			{
				GameClient.getInstance().getOurPlayer().getPokemon()[m_pokeIndex].setMoves(j, m_move);
				if(BattleManager.getInstance().getBattleWindow().isVisible())
					BattleManager.getInstance().updateMoves();
				// GameClient.getInstance().getPacketGenerator().writeTcpMessage("09" + m_pokeIndex + i + m_move);
				ClientMessage message = new ClientMessage(ServerPacket.LEARN_MOVE);
				message.addInt(m_pokeIndex);
				message.addInt(i);
				message.addString(m_move);
				GameClient.getInstance().getSession().send(message);
				MoveLearningManager.getInstance().removeMoveLearning();
			}
			else
			{
				// setAlwaysOnTop(false); TODO: Chappie magic :D
				Runnable yes = new Runnable()
				{
					@Override
					public void run()
					{
						GameClient.getInstance().getOurPlayer().getPokemon()[m_pokeIndex].setMoves(j, m_move);
						BattleManager.getInstance().updateMoves();
						// GameClient.getInstance().getPacketGenerator().writeTcpMessage("09" + m_pokeIndex + j + m_move);
						ClientMessage message = new ClientMessage(ServerPacket.LEARN_MOVE);
						message.addInt(m_pokeIndex);
						message.addInt(j);
						message.addString(m_move);
						GameClient.getInstance().getSession().send(message);
						GameClient.getInstance().getDisplay().remove(m_replace);
						m_replace = null;
						MoveLearningManager.getInstance().removeMoveLearning();
					}
				};
				Runnable no = new Runnable()
				{
					@Override
					public void run()
					{
						GameClient.getInstance().getDisplay().remove(m_replace);
						m_replace = null;
						// setAlwaysOnTop(true); TODO: Chappie magic :D
					}
				};
				GameClient.getInstance().getGUIPane().showConfirmationDialog("Are you sure you want to forget " + m_moveButtons.get(i).getText() + " to learn " + m_move + "?", yes, no);
			}
	}
}

/**
 * Canvas for Move Learning screen
 * 
 * @author ZombieBear
 */
class MoveLearnCanvas extends Widget
{
	private Image poke = new Image();

	public MoveLearnCanvas()
	{
		setSize(257, 144);
		setVisible(true);
	}

	public void draw(int pokeIndex)
	{
		poke.setImage(FileLoader.toTWLImage(GameClient.getInstance().getOurPlayer().getPokemon()[pokeIndex].getSprite(), true));
		poke.setPosition(getWidth() / 2 - 40, getHeight() / 2 - 40);
		this.add(poke);
	}
}