package org.pokenet.client.ui.frames;

import org.pokenet.client.GameClient;
import org.pokenet.client.backend.entity.OurPlayer;
import org.pokenet.client.backend.entity.Pokemon;
import org.pokenet.client.constants.ServerPacket;
import org.pokenet.client.protocol.ClientMessage;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.ListBox;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.model.SimpleChangableListModel;

/**
 * @author Sadhi, Myth1c
 */
public class BattleFrontierDialog extends ResizableFrame
{
	protected ListBox<String> m_travelList;
	protected Label m_travelDisplay;
	private SimpleChangableListModel<String> m_locations;
	private String choice, pick;
	private OurPlayer p;
	private Widget pane;
	private Button use;

	public BattleFrontierDialog(String battle, final OurPlayer p)
	{
		this.p = p;
		choice = battle;
		setTheme("chooserDialog");
		m_locations = new SimpleChangableListModel<String>();
		m_locations.addElement("Challenge");
		m_locations.addElement("Info");

		pane = new Widget();
		pane.setTheme("content");
		pane.setSize(180, 130);
		pane.setPosition(0, 0);

		m_travelList = new ListBox<String>(m_locations);
		m_travelList.setTheme("listbox");
		m_travelList.setPosition(2, 25);
		m_travelList.setSize(175, 70);
		pane.add(m_travelList);
		setTitle("Please choose..");
		setSize(180, 130);
		setPosition(300, 150);
		setResizableAxis(ResizableAxis.NONE);
		setDraggable(true);
		setVisible(true);
		initUse();
		add(pane);
	}

	public void initUse()
	{
		use = new Button("Ok");
		use.setCanAcceptKeyboardFocus(false);
		use.setTheme("button");
		use.setSize(60, 20);
		use.setPosition(10, 100);
		pane.add(use);
		Button cancel = new Button("Cancel");
		cancel.setPosition(90, 100);
		cancel.setSize(70, 20);
		cancel.setTheme("button");
		pane.add(cancel);

		cancel.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				GameClient.getInstance().getGUIPane().getHUD().removeBattleFrontierDialog();
			}
		});
		use.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				pick = m_locations.getEntry(m_travelList.getSelected());
				String txt = "Are you sure you wish to start?";
				if(p.getPartyCount() > 3)
				{
					if(choice.split("_")[1].equalsIgnoreCase("lvl50"))
					{
						for(Pokemon poke : p.getPokemon())
						{
							if(poke.getLevel() > 50)
							{
								txt = "You do not meet the requirements. \nEither you have more than 3 pokemon or your pokemon are above lvl50. \nFix it before comming back.";
								pick = "not";
								break;
							}
						}
					}
					else
					{
						txt = "You do not meet the requirements. \nYou have more than 3 pokemon. \nFix it before comming back.";
						pick = "not";
					}
				}

				Runnable yes = new Runnable()
				{
					@Override
					public void run()
					{
						GameClient.getInstance().getGUIPane().hideConfirmationDialog();
						if(pick.equals("Challenge"))
						{
							ClientMessage message = new ClientMessage(ServerPacket.BATTLEFRONTIER_FACILITY);
							message.addString(choice);
							GameClient.getInstance().getSession().send(message);
						}
						GameClient.getInstance().getGUIPane().getHUD().removeBattleFrontierDialog();
					}
				};
				Runnable no = new Runnable()
				{
					@Override
					public void run()
					{
						GameClient.getInstance().getGUIPane().hideConfirmationDialog();
					}
				};
				GameClient.getInstance().getGUIPane().showConfirmationDialog(txt, yes, no);
			}
		});
	}

	public int getChoice()
	{
		return m_travelList.getSelected();
	}
}
