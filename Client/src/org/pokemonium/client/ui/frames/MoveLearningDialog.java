package org.pokemonium.client.ui.frames;

import java.util.ArrayList;
import java.util.List;
import org.pokemonium.client.GameClient;
import org.pokemonium.client.backend.BattleManager;
import org.pokemonium.client.backend.MoveLearningManager;
import org.pokemonium.client.constants.ServerPacket;
import org.pokemonium.client.protocol.ClientMessage;
import org.pokemonium.client.ui.components.Image;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.Widget;

/**
 * Handles move learning and evolution
 * 
 * @author Myth1c
 */
public class MoveLearningDialog extends ResizableFrame
{
	public List<Button> m_moveButtons = new ArrayList<Button>();
	private List<Label> m_moveTypeLabels = new ArrayList<Label>();
	public List<Label> m_pp = new ArrayList<Label>();
	private Button m_cancel;
	private MoveLearnCanvas m_canvas;
	private String m_move;
	private Widget m_movePane;
	private int m_pokeIndex;
	private Widget pane;

	/**
	 * Default Constructor
	 * 
	 * @param pokeIndex
	 * @param move
	 * @param isMoveLearning
	 */
	public MoveLearningDialog(Widget root)
	{
		setTheme("movelearndialog");
		pane = new Widget();
		pane.setTheme("content");
		m_canvas = new MoveLearnCanvas();
		pane.add(m_canvas);
		add(pane);
		initGUI(root);
		setCenter();
		setClip(true);
	}

	/**
	 * Starts the GUI
	 * 
	 * @param isMoveLearning
	 */
	public void initGUI(Widget root)
	{
		String respath = System.getProperty("res.path");
		if(respath == null)
			respath = "";

		m_movePane = new Widget();
		m_movePane.setTheme("attackpane");

		Button move1 = new Button("");
		move1.setCanAcceptKeyboardFocus(false);
		move1.setTheme("battlebutton");
		Button move2 = new Button("");
		move2.setCanAcceptKeyboardFocus(false);
		move2.setTheme("battlebutton");
		Button move3 = new Button("");
		move3.setCanAcceptKeyboardFocus(false);
		move3.setTheme("battlebutton");
		Button move4 = new Button("");
		move4.setCanAcceptKeyboardFocus(false);
		move4.setTheme("battlebutton");

		// start attackPane
		m_movePane.add(move1);
		move1.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				replaceMove(0);
			}
		});
		Label pp1 = new Label();
		pp1.setTheme("pplabel");
		Label move1Type = new Label();
		move1Type.setTheme("movetypelabel");
		move1.add(move1Type);
		move1.add(pp1);

		m_movePane.add(move2);
		move2.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				replaceMove(1);
			}
		});
		Label pp2 = new Label();
		pp2.setTheme("pplabel");
		pp2.setFont(GameClient.getInstance().getTheme().getFont("normal_white"));
		Label move2Type = new Label();
		move2Type.setTheme("movetypelabel");
		move2.add(move2Type);
		move2.add(pp2);

		m_movePane.add(move3);
		move3.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				replaceMove(2);
			}
		});
		Label pp3 = new Label();
		pp3.setTheme("pplabel");
		pp3.setFont(GameClient.getInstance().getTheme().getFont("normal_white"));
		Label move3Type = new Label();
		move3Type.setTheme("movetypelabel");
		move3.add(move3Type);
		move3.add(pp3);

		m_movePane.add(move4);
		move4.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				replaceMove(3);
			}
		});
		Label pp4 = new Label();
		pp4.setTheme("pplabel");

		pp4.setFont(GameClient.getInstance().getTheme().getFont("normal_white"));
		Label move4Type = new Label();
		move4Type.setTheme("movetypelabel");
		move4.add(move4Type);
		move4.add(pp4);

		m_moveButtons.add(move1);
		m_moveButtons.add(move2);
		m_moveButtons.add(move3);
		m_moveButtons.add(move4);

		m_pp.add(pp1);
		m_pp.add(pp2);
		m_pp.add(pp3);
		m_pp.add(pp4);

		m_moveTypeLabels.add(move1Type);
		m_moveTypeLabels.add(move2Type);
		m_moveTypeLabels.add(move3Type);
		m_moveTypeLabels.add(move4Type);

		m_cancel = new Button("Cancel");
		m_cancel.setTheme("battlebutton_small");
		m_cancel.setCanAcceptKeyboardFocus(false);
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

		pane.add(m_movePane);

	}

	public void learnMove(int pokeIndex, String move)
	{
		m_pokeIndex = pokeIndex;

		GameClient.getInstance().getHUD().talkToNPC(GameClient.getInstance().getOurPlayer().getPokemon()[pokeIndex].getName() + " wants to learn " + move);

		m_move = move;

		m_moveButtons.get(0).setText(GameClient.getInstance().getOurPlayer().getPokemon()[m_pokeIndex].getMoves()[0]);
		m_moveButtons.get(1).setText(GameClient.getInstance().getOurPlayer().getPokemon()[m_pokeIndex].getMoves()[1]);
		m_moveButtons.get(2).setText(GameClient.getInstance().getOurPlayer().getPokemon()[m_pokeIndex].getMoves()[2]);
		m_moveButtons.get(3).setText(GameClient.getInstance().getOurPlayer().getPokemon()[m_pokeIndex].getMoves()[3]);

		for(int i = 0; i < 4; i++)
			if(m_moveButtons.get(i).getText().equals(""))
			{
				m_pp.get(i).setVisible(false);
				m_moveTypeLabels.get(i).setVisible(false);
			}
			else
			{
				m_pp.get(i).setText(
						GameClient.getInstance().getOurPlayer().getPokemon()[pokeIndex].getMoveCurPP()[i] + "/" + GameClient.getInstance().getOurPlayer().getPokemon()[pokeIndex].getMoveMaxPP()[i]);
				m_pp.get(i).setVisible(true);
				m_moveTypeLabels.get(i).setText(GameClient.getInstance().getOurPlayer().getPokemon()[pokeIndex].getMoveType(i));
				m_moveTypeLabels.get(i).setVisible(true);
			}

		m_movePane.setVisible(true);
		m_canvas.draw(pokeIndex);
	}

	/**
	 * Centers the frame
	 */
	public void setCenter()
	{
		int height = (int) GameClient.getInstance().getGUIPane().getHeight();
		int width = (int) GameClient.getInstance().getGUIPane().getWidth();
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
		if(m_moveButtons.get(i).getText().equals(""))
		{
			GameClient.getInstance().getOurPlayer().getPokemon()[m_pokeIndex].setMoves(j, m_move);
			if(BattleManager.getInstance().getBattleWindow() != null)
			{
				if(BattleManager.getInstance().getBattleWindow().isVisible())
				{
					BattleManager.getInstance().updateMoves();
				}
			}
			ClientMessage message = new ClientMessage(ServerPacket.LEARN_MOVE);
			message.addInt(m_pokeIndex);
			message.addInt(i);
			message.addString(m_move);
			GameClient.getInstance().getSession().send(message);
			m_canvas.removeChild(m_canvas.poke);
			MoveLearningManager.getInstance().removeMoveLearning();
		}
		else
		{
			this.setVisible(false);
			Runnable yes = new Runnable()
			{
				@Override
				public void run()
				{
					GameClient.getInstance().getOurPlayer().getPokemon()[m_pokeIndex].setMoves(j, m_move);
					if(BattleManager.getInstance().isBattling())
						BattleManager.getInstance().updateMoves();
					ClientMessage message = new ClientMessage(ServerPacket.LEARN_MOVE);
					message.addInt(m_pokeIndex);
					message.addInt(j);
					message.addString(m_move);
					GameClient.getInstance().getSession().send(message);
					m_canvas.removeChild(m_canvas.poke);
					MoveLearningManager.getInstance().removeMoveLearning();
					GameClient.getInstance().getGUIPane().hideConfirmationDialog();
				}
			};
			Runnable no = new Runnable()
			{
				@Override
				public void run()
				{
					GameClient.getInstance().getGUIPane().hideConfirmationDialog();
					setVisible(true);
				}
			};
			GameClient.getInstance().getGUIPane().showConfirmationDialog("Are you sure you want to forget " + m_moveButtons.get(i).getText() + " to learn " + m_move + "?", yes, no);
		}
	}

	@Override
	public void layout()
	{
		super.layout();

		setSize(259, 369);
		pane.setSize(259, 369);
		pane.setPosition(getInnerX() + 0, getInnerY() + 0);
		m_canvas.setPosition(getInnerX() + 0, getInnerY() + 0);
		m_canvas.setSize(257, 144);

		m_movePane.setSize(257, 201);
		m_movePane.setPosition(getInnerX() + 2, getInnerY() + 140);

		for(int i = 0; i < 4; i++)
		{
			m_moveButtons.get(i).setSize(116, 51);
			m_moveTypeLabels.get(i).adjustSize();
			m_pp.get(i).adjustSize();
		}

		m_moveButtons.get(0).setPosition(getInnerX() + 12, getInnerY() + 150);
		m_moveButtons.get(1).setPosition(getInnerX() + 125, getInnerY() + 150);
		m_moveButtons.get(2).setPosition(getInnerX() + 12, getInnerY() + 215);
		m_moveButtons.get(3).setPosition(getInnerX() + 125, getInnerY() + 215);

		m_moveTypeLabels.get(0).setPosition(m_moveButtons.get(0).getInnerX() + 2, m_moveButtons.get(0).getInnerY() + 35);
		m_moveTypeLabels.get(1).setPosition(m_moveButtons.get(1).getInnerX() + 125, m_moveButtons.get(1).getInnerY() + 35);
		m_moveTypeLabels.get(2).setPosition(m_moveButtons.get(2).getInnerX() + 2, m_moveButtons.get(2).getInnerY() + 95);
		m_moveTypeLabels.get(3).setPosition(m_moveButtons.get(3).getInnerX() + 125, m_moveButtons.get(3).getInnerY() + 95);

		m_pp.get(0).setPosition(m_moveButtons.get(0).getInnerX() + 60, m_moveButtons.get(0).getInnerY() + 35);
		m_pp.get(1).setPosition(m_moveButtons.get(0).getInnerX() + 175, m_moveButtons.get(0).getInnerY() + 35);
		m_pp.get(2).setPosition(m_moveButtons.get(0).getInnerX() + 60, m_moveButtons.get(0).getInnerY() + 95);
		m_pp.get(3).setPosition(m_moveButtons.get(0).getInnerX() + 175, m_moveButtons.get(0).getInnerY() + 95);

		m_cancel.setSize(82, 48);
		m_cancel.setPosition(getInnerX() + (getWidth() / 2) - (m_cancel.getWidth() / 2), getInnerY() + 270);
	}
}

/**
 * Canvas for Move Learning screen
 * 
 * @author ZombieBear
 */
class MoveLearnCanvas extends Widget
{
	protected Image poke = new Image();

	public MoveLearnCanvas()
	{
		setTheme("pokemonpane");
		setVisible(true);
	}

	public void draw(int pokeIndex)
	{
		final int pokeIDX = pokeIndex;
		GameClient.getInstance().getGUI().invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				poke.setImage(GameClient.getInstance().getOurPlayer().getPokemon()[pokeIDX].getFrontSprite());
				try
				{
					add(poke);
				}
				catch(Exception e)
				{
					removeChild(poke);
					add(poke);
				}
			}
		});
	}

	@Override
	public void layout()
	{
		super.layout();

		poke.setPosition(getInnerX() + getWidth() / 2 - 40, getInnerY() + getHeight() / 2 - 40);
		poke.setSize(80, 80);
	}
}