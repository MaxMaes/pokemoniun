package org.pokenet.client.ui.frames;

import org.pokenet.client.GameClient;
import org.pokenet.client.backend.entity.OurPlayer;
import org.pokenet.client.constants.ServerPacket;
import org.pokenet.client.protocol.ClientMessage;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.ListBox;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.model.SimpleChangableListModel;

/**
 * @author sadhi
 * @author Myth1c
 */
public class TrainChooserDialog extends ResizableFrame
{

	protected ListBox<String> m_travelList;
	protected Label m_travelDisplay;
	private SimpleChangableListModel<String> m_locations;
	private String choice;

	public TrainChooserDialog(String travel, final OurPlayer p)
	{
		m_locations = new SimpleChangableListModel<String>();
		if(travel.equalsIgnoreCase("kanto"))
		{
			m_locations.addElement("Goldenrod City - $10k");
		}
		else if(travel.equalsIgnoreCase("johto"))
		{
			m_locations.addElement("Saffron City - $10k");
		}
		else if(travel.equalsIgnoreCase("sinnoh"))
		{
			m_locations.addElement("Resort Area - $100k");
		}
		else if(travel.equalsIgnoreCase("Resort"))
		{
			m_locations.addElement("Snowpoint City - $10k");
		}
		else if(travel.equalsIgnoreCase("Fuchsia"))
		{
			m_locations.addElement("Safari Zone - $30k");
		}
		else if(travel.equalsIgnoreCase("mt.silver"))
		{
			m_locations.addElement("Mt.Silver");
		}

		// m_travelDisplay = new Label();
		// m_travelDisplay.setSize(124,204);
		// m_travelDisplay.setPosition(105, 20);
		// add(m_travelDisplay);
		m_travelList = new ListBox<String>(m_locations);
		m_travelList.setSize(245, 70);
		add(m_travelList);
		setTitle("Please choose your destination..");
		setSize(250, 130);
		setPosition(300, 150);
		setResizableAxis(ResizableAxis.NONE);
		setDraggable(true);
		setVisible(true);
		initUse();
		// System.out.println("end dialog");
	}

	public void initUse()
	{
		Button use = new Button("Let's travel!");
		use.setPosition(25, 75);
		add(use);
		Button cancel = new Button("Cancel");
		cancel.setPosition(150, 75);
		add(cancel);

		cancel.addCallback(new Runnable()
		{
			public void run()
			{
				setVisible(false);
			}
		});
		use.addCallback(new Runnable()
		{
			public void run()
			{
				choice = m_locations.getEntry(getChoice());
				setVisible(false);
				String txt = "Do you wish to go to " + choice + "? You need a trainer level of atleast 25.";
				if(choice.contains("Snowpoint"))
				{
					txt = "Are you sure you want to go back to snowpoint?";
				}
				else if(choice.contains("Resort Area"))
				{
					txt = "Are you sure you wish to go there?\nThe Trainers and Pokémon there are quite strong.\nThat is why you need atleast 30 badges";
				}
				else if(choice.contains("Safari Zone"))
				{
					txt = "Entry to the safari zone costs 30k,\nYou also need to be atleast trainer level 25.\nDo you wish to enter?";
				}
				else if(choice.contains("Mt.Silver"))
				{
					txt = "To enter Mt.Silver you need the 16 badges of Kanto and Johto.\nDo you wish to enter?";
				}
				Runnable yes = new Runnable()
				{
					public void run()
					{
						// GameClient.getInstance().getPacketGenerator().writeTcpMessage("S" + m_travelList.getSelectedName());
						ClientMessage message = new ClientMessage(ServerPacket.TRAVEL);
						message.addString(choice);
						GameClient.getInstance().getSession().send(message);
						GameClient.getInstance().getGUIPane().hideConfirmationDialog();
					}
				};
				Runnable no = new Runnable()
				{
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
