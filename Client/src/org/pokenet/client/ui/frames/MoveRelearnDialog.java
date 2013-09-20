package org.pokenet.client.ui.frames;

import org.pokenet.client.GameClient;
import org.pokenet.client.constants.ServerPacket;
import org.pokenet.client.protocol.ClientMessage;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.ListBox;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.model.SimpleChangableListModel;

/**
 * Class which displays and controls the dialog for relearning previous moves to a pokemon.
 * 
 * @author Myth1c, Sadhi
 */

public class MoveRelearnDialog extends ResizableFrame
{
	protected ListBox<String> m_moveList;
	// protected Label m_travelDisplay;
	private SimpleChangableListModel<String> m_moves;
	private String choice;
	private Widget pane;
	private String price;
	private String item;
	private PartyDialog dialog;
	private int idx;
	private int l = 0;
	private Button use, cancel;

	public MoveRelearnDialog(String p)
	{
		price = "" + p.split("-")[0] + " " + p.split("-")[1];
		item = p.split("-")[1];
		setTheme("chooserDialog");
		dialog = new PartyDialog(this);
		add(dialog);
		setVisible(false);
		layout();
		setResizableAxis(ResizableAxis.NONE);
		setDraggable(true);
		setVisible(true);
		// add(pane);
	}

	@Override
	public void layout()
	{
		super.layout();
		if(l == 0)
		{
			setSize(250, 230);
			setPosition(300, 150);
			dialog.setSize(250, 130);
			dialog.setPosition(getInnerX(), getInnerY() + 20);
			this.setTitle("Please choose your Pokemon..");
		}
		else if(l != 0)
		{
			this.setTitle("Please choose your move..");
			pane.setSize(250, 230);
			pane.setPosition(getInnerX(), getInnerY() + 20);

			use.setSize(70, 20);
			use.setPosition(getInnerX() + 25, getInnerY() + 200);

			cancel.setSize(70, 20);
			cancel.setPosition(getInnerX() + 150, getInnerY() + 200);

			m_moveList.setSize(245, 170);
			m_moveList.setPosition(getInnerX() + 2, getInnerY() + 25);
		}
	}

	public void initUse(String movelist)
	{
		l = 1;
		dialog.setVisible(false);
		removeChild(dialog);
		pane = new Widget();
		pane.setTheme("content");
		for(String s : movelist.split(", "))
		{
			if(s.equals("/END"))
				break;
			m_moves.addElement(s);
		}
		use = new Button("Learn!");
		use.setTheme("button");
		pane.add(use);
		cancel = new Button("Cancel");
		cancel.setTheme("button");
		pane.add(cancel);

		cancel.addCallback(new Runnable()
		{
			public void run()
			{
				setVisible(false);
				GameClient.getInstance().getHUD().removeRelearnDialog();
			}
		});
		use.addCallback(new Runnable()
		{
			public void run()
			{
				choice = m_moves.getEntry(m_moveList.getSelected());
				String note = "Are you sure you want to relearn " + choice + "?\nIt will cost you " + price + ".";

				Runnable yes = new Runnable()
				{
					@Override
					public void run()
					{
						setVisible(false);
						GameClient.getInstance().getGUIPane().hideConfirmationDialog();
						ClientMessage message = new ClientMessage(ServerPacket.REQUEST_MOVE_LEARN);
						message.addString(choice);
						message.addString(item);
						message.addInt(idx);
						GameClient.getInstance().getSession().send(message);
						GameClient.getInstance().getHUD().removeRelearnDialog();
					}
				};
				Runnable no = new Runnable()
				{
					public void run()
					{
						GameClient.getInstance().getGUIPane().hideConfirmationDialog();
					}
				};
				GameClient.getInstance().getGUIPane().showConfirmationDialog(note, yes, no);
			}
		});

		m_moveList = new ListBox<String>(m_moves);
		m_moveList.setTheme("listbox");
		pane.add(m_moveList);
		layout();
		add(pane);
	}

	public int getChoice()
	{
		return m_moveList.getSelected();
	}

	public int getPartyIndex()
	{
		return idx;
	}

	public void setPartyIndex(int idx)
	{
		this.idx = idx;
	}
}

/**
 * PopUp that lists the player's pokemon in order to use/give an item
 * 
 * @author Myth1c
 */
class PartyDialog extends Widget
{
	private Button[] pokeButtons;
	private MoveRelearnDialog parent;

	/**
	 * Default constructor
	 * 
	 * @param itemId
	 * @param use
	 * @param useOnPoke
	 */
	public PartyDialog(MoveRelearnDialog w)
	{
		parent = w;
		setPosition(getX() - 1, getY() + 1);

		pokeButtons = new Button[GameClient.getInstance().getOurPlayer().getPokemon().length];
		for(int i = 0; i < GameClient.getInstance().getOurPlayer().getPartyCount(); i++)
		{
			final int idx = i;
			pokeButtons[i] = new Button(GameClient.getInstance().getOurPlayer().getPokemon()[i].getName());
			pokeButtons[i].addCallback(new Runnable()
			{
				@Override
				public void run()
				{
					ClientMessage message = new ClientMessage(ServerPacket.REQUEST_POKEMON_INFO);
					message.addString("MoveRelearner");
					message.addInt(idx);
					GameClient.getInstance().getSession().send(message);
					parent.setPartyIndex(idx);
					// parent.initUse(idx);
				}
			});
			add(pokeButtons[i]);
		}

		// Frame configuration
		setVisible(true);
	}

	public void setPopupPosition(int x, int y)
	{
		this.setPosition(x, y);
	}

	@Override
	public void layout()
	{
		int y = 10;
		for(int i = 0; i < GameClient.getInstance().getOurPlayer().getPartyCount(); i++)
		{
			pokeButtons[i].setSize(100, 25);
			pokeButtons[i].setPosition(getInnerX() + 5, getInnerY() + y);
			y += 25;
		}

		setSize(110, GameClient.getInstance().getOurPlayer().getPartyCount() * 25 + 20);
	}
}
