package org.pokenet.client.twl.ui.frames;

import org.pokenet.client.GameClient;
import org.pokenet.client.backend.entity.OurPlayer;
import org.pokenet.client.backend.entity.Pokemon;
import org.pokenet.client.constants.ServerPacket;
import org.pokenet.client.protocol.ClientMessage;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.ListBox;
import de.matthiasmann.twl.ResizableFrame;
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

	public BattleFrontierDialog(String battle, final OurPlayer p)
	{
		this.p = p;
		choice = battle;
		m_locations = new SimpleChangableListModel<String>();
		m_locations.addElement("Challenge");
		m_locations.addElement("Info");

		// m_travelDisplay = new Label();
		// m_travelDisplay.setSize(124,204);
		// m_travelDisplay.setPosition(105, 20);
		// add(m_travelDisplay);
		m_travelList = new ListBox<String>(m_locations);

		m_travelList.setSize(175, 70);
		add(m_travelList);
		setTitle("Please choose..");
		setSize(180, 130);
		setPosition(300, 150);
		setResizableAxis(ResizableAxis.NONE);
		setDraggable(true);
		setVisible(true);
		initUse();
		// System.out.println("end dialog");
	}

	public void initUse()
	{
		Button use = new Button("Ok");
		use.setPosition(25, 75);
		add(use);
		Button cancel = new Button("Cancel");
		cancel.setPosition(80, 75);
		add(cancel);

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
