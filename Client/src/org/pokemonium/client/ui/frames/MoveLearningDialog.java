package org.pokemonium.client.ui.frames;

import java.util.ArrayList;
import java.util.List;
import org.pokemonium.client.GameClient;
import org.pokemonium.client.backend.BattleManager;
import org.pokemonium.client.backend.MoveLearningManager;
import org.pokemonium.client.constants.ServerPacket;
import org.pokemonium.client.protocol.ClientMessage;
import org.pokemonium.client.ui.components.Image;
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
		setPosition(getX() - 1, getY() + 1);
		add(pane);
		initGUI(root);
		setCenter();
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
		pp1.setText("10/10");
		pp1.setTheme("pplabel");
		// pp1.setAlignment(Alignment.RIGHT);
		Label move1Type = new Label();
		move1Type.setTheme("movetypelabel");
		// move1Type.setAlignment(Alignment.LEFT);
		move1Type.setText("???");
		m_movePane.add(move1Type);
		m_movePane.add(pp1);

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
		pp2.setText("10/10");
		// pp2.setAlignment(Alignment.RIGHT);
		pp2.setFont(GameClient.getInstance().getTheme().getFont("normal_white"));
		Label move2Type = new Label();
		move2Type.setTheme("movetypelabel");
		move2Type.setText("???");
		// move2Type.setAlignment(Alignment.LEFT);
		m_movePane.add(move2Type);
		m_movePane.add(pp2);

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
		pp3.setText("10/10");
		// pp3.setAlignment(Alignment.RIGHT);
		pp3.setFont(GameClient.getInstance().getTheme().getFont("normal_white"));
		Label move3Type = new Label();
		move3Type.setTheme("movetypelabel");
		move3Type.setText("???");
		move3Type.setAlignment(Alignment.LEFT);
		m_movePane.add(move3Type);
		m_movePane.add(pp3);

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
		pp4.setText("10/10");
		// pp4.setAlignment(Alignment.RIGHT);
		pp4.setFont(GameClient.getInstance().getTheme().getFont("normal_white"));
		Label move4Type = new Label();
		move4Type.setTheme("movetypelabel");
		move4Type.setText("???");
		// move4Type.setAlignment(Alignment.LEFT);
		m_movePane.add(move4Type);
		m_movePane.add(pp4);

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

	@Override
	public void layout()
	{
		super.layout();

		setSize(259, 369);
		pane.setSize(259, 369);
		pane.setPosition(0, 0);
		m_canvas.setPosition(0, 0);
		m_canvas.setSize(257, 144);

		m_movePane.setSize(257, 201);
		m_movePane.setPosition(2, 140);

		m_moveButtons.get(0).setPosition(7, 140);
		m_moveButtons.get(0).setSize(116, 51);
		m_moveButtons.get(1).setPosition(130, 140);
		m_moveButtons.get(1).setSize(116, 51);
		m_moveButtons.get(2).setPosition(7, 205);
		m_moveButtons.get(2).setSize(116, 51);
		m_moveButtons.get(3).setPosition(130, 205);
		m_moveButtons.get(3).setSize(116, 51);

		m_moveTypeLabels.get(0).setSize(GameClient.getInstance().getFontSmall().getWidth(m_moveTypeLabels.get(0).getText()),
				GameClient.getInstance().getFontSmall().getHeight(m_moveTypeLabels.get(0).getText()));
		m_moveTypeLabels.get(0).setPosition(12, 35);
		m_moveTypeLabels.get(1).setSize(GameClient.getInstance().getFontSmall().getWidth(m_moveTypeLabels.get(1).getText()),
				GameClient.getInstance().getFontSmall().getHeight(m_moveTypeLabels.get(1).getText()));
		m_moveTypeLabels.get(1).setPosition(135, 35);
		m_moveTypeLabels.get(2).setSize(GameClient.getInstance().getFontSmall().getWidth(m_moveTypeLabels.get(2).getText()),
				GameClient.getInstance().getFontSmall().getHeight(m_moveTypeLabels.get(2).getText()));
		m_moveTypeLabels.get(2).setPosition(12, 95);
		m_moveTypeLabels.get(3).setSize(GameClient.getInstance().getFontSmall().getWidth(m_moveTypeLabels.get(3).getText()),
				GameClient.getInstance().getFontSmall().getHeight(m_moveTypeLabels.get(3).getText()));
		m_moveTypeLabels.get(3).setPosition(135, 95);

		m_pp.get(0).setSize(GameClient.getInstance().getFontSmall().getWidth(m_pp.get(0).getText()), GameClient.getInstance().getFontSmall().getHeight(m_pp.get(0).getText()));
		m_pp.get(0).setPosition(85, 35);
		m_pp.get(1).setSize(GameClient.getInstance().getFontSmall().getWidth(m_pp.get(1).getText()), GameClient.getInstance().getFontSmall().getHeight(m_pp.get(1).getText()));
		m_pp.get(1).setPosition(210, 35);
		m_pp.get(2).setSize(GameClient.getInstance().getFontSmall().getWidth(m_pp.get(2).getText()), GameClient.getInstance().getFontSmall().getHeight(m_pp.get(2).getText()));
		m_pp.get(2).setPosition(86, 95);
		m_pp.get(3).setSize(GameClient.getInstance().getFontSmall().getWidth(m_pp.get(3).getText()), GameClient.getInstance().getFontSmall().getHeight(m_pp.get(3).getText()));
		m_pp.get(3).setPosition(210, 95);

		m_cancel.setSize(246, 77);
		m_cancel.setPosition(3, 122);
	}

	public void learnMove(int pokeIndex, String move)
	{
		m_pokeIndex = pokeIndex;

		// GameClient.getInstance().getGUIPane().talkToNPC(GameClient.getInstance().getOurPlayer().getPokemon()[pokeIndex].getName() + " wants to learn " + move); TODO:

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
			if(BattleManager.getInstance().getBattleWindow().isVisible())
				BattleManager.getInstance().updateMoves();
			// GameClient.getInstance().getPacketGenerator().writeTcpMessage("09" + m_pokeIndex + i + m_move);
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
		poke.setImage(GameClient.getInstance().getOurPlayer().getPokemon()[pokeIndex].getFrontSprite());
		try
		{
			this.add(poke);
		}
		catch(Exception e)
		{
			this.removeChild(poke);
			this.add(poke);
		}

	}

	@Override
	public void layout()
	{
		super.layout();

		poke.setPosition(getWidth() / 2 - 40, getHeight() / 2 - 40);
		poke.setSize(80, 80);
	}
}