package org.pokemonium.client.ui.frames;

import org.pokemonium.client.GameClient;
import org.pokemonium.client.backend.entity.OurPlayer;
import org.pokemonium.client.constants.ServerPacket;
import org.pokemonium.client.protocol.ClientMessage;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.ListBox;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.Widget;
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
	private Widget pane;

	public TrainChooserDialog(String travel, final OurPlayer p)
	{
		setTheme("chooserDialog");
		pane = new Widget();
		pane.setTheme("content");
		pane.setSize(getWidth(), 190);
		pane.setPosition(0, 0);

		m_locations = new SimpleChangableListModel<String>();
		if(travel.equalsIgnoreCase("kanto"))
		{
			m_locations.addElement("Goldenrod City - $10k");
			m_locations.addElement("Phenac City - $17.5k");
			m_locations.addElement("Pyrite Town - $20k");
		}
		else if(travel.equalsIgnoreCase("johto"))
		{
			m_locations.addElement("Saffron City - $10k");
			m_locations.addElement("Phenac City - $15k");
			m_locations.addElement("Pyrite Town - $17.5k");
		}
		else if(travel.equalsIgnoreCase("phenac"))
		{
			m_locations.addElement("Saffron City - $17.5k");
			m_locations.addElement("Goldenrod City - $15k");
			m_locations.addElement("Pyrite Town - $2.5k");
		}
		else if(travel.equalsIgnoreCase("pyrite"))
		{
			m_locations.addElement("Saffron City - $20k");
			m_locations.addElement("Goldenrod City - $17.5k");
			m_locations.addElement("Phenac City - $2.5k");
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
		m_travelList.setTheme("listbox");
		m_travelList.setSize(245, 70);
		m_travelList.setPosition(2, 25);
		pane.add(m_travelList);

		setTitle("Please choose your destination..");
		setSize(250, 130);
		setPosition(300, 150);
		setResizableAxis(ResizableAxis.NONE);
		setDraggable(true);
		setVisible(true);
		initUse();
		add(pane);
		// System.out.println("end dialog");
	}

	public void initUse()
	{
		Button use = new Button("Let's travel!");
		use.setCanAcceptKeyboardFocus(false);
		use.setTheme("button");
		use.setSize(70, 20);
		use.setPosition(25, 100);
		pane.add(use);
		Button cancel = new Button("Cancel");
		cancel.setCanAcceptKeyboardFocus(false);
		cancel.setPosition(150, 100);
		cancel.setSize(70, 20);
		cancel.setTheme("button");
		pane.add(cancel);

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
				String txt = "Do you wish to go to " + choice + "?\nYou need a trainer level of atleast 25.\nBut for Pyrite Town and Phenac City you also need 17 badges.";
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
