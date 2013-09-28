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
 * Class which displays and controls the dialog for teaching moves to a pokemon.
 * 
 * @author Myth1c, Sadhi
 */

public class MoveTutorDialog extends ResizableFrame
{
	protected ListBox<String> m_moveList;
	// protected Label m_travelDisplay;
	private SimpleChangableListModel<String> m_moves;
	private String choice;
	private Widget pane;
	private String moves[];
	private PartyDialog dialog;
	private int idx;
	private Button use, cancel;

	public MoveTutorDialog(String p)
	{
		setTheme("chooserDialog");
		dialog = new PartyDialog(this);
		add(dialog);
		setVisible(false);
		setResizableAxis(ResizableAxis.NONE);
		setDraggable(true);
		setVisible(true);
		initUse(p);
		// add(pane);
	}

	@Override
	public void layout()
	{
		super.layout();
		setSize(250, 230);
		setPosition(300, 150);
		dialog.setSize(250, 130);
		dialog.setPosition(getInnerX(), getInnerY() + 20);
		pane.setSize(250, 230);
		pane.setPosition(getInnerX(), getInnerY() + 20);

		use.setSize(70, 20);
		use.setPosition(getInnerX() + 25, getInnerY() + 200);

		cancel.setSize(70, 20);
		cancel.setPosition(getInnerX() + 150, getInnerY() + 200);

		m_moveList.setSize(245, 170);
		m_moveList.setPosition(getInnerX() + 2, getInnerY() + 25);
	}

	public void initUse(String movelist)
	{
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
				GameClient.getInstance().getHUD().removeTutorDialog();
			}
		});
		use.addCallback(new Runnable()
		{
			public void run()
			{
				choice = m_moves.getEntry(m_moveList.getSelected());
				String note = "Are you sure you want to learn " + choice.split(" - ")[0] + "?\nIt will cost you " + choice.split(" - ")[1] + ".";

				Runnable yes = new Runnable()
				{
					@Override
					public void run()
					{
						setVisible(false);
						GameClient.getInstance().getGUIPane().hideConfirmationDialog();
						ClientMessage message = new ClientMessage(ServerPacket.REQUEST_MOVE_LEARN);
						message.addString(choice.split(" - ")[0]); // moves
						message.addString(choice.split(" - ")[1]); // price
						message.addInt(idx);
						GameClient.getInstance().getSession().send(message);
						GameClient.getInstance().getHUD().removeTutorDialog();
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
		dialog.setVisible(false);
		removeChild(dialog);
		add(pane);
		pane.setVisible(true);
	}
}
