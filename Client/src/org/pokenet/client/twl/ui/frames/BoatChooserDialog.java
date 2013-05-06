package org.pokenet.client.twl.ui.frames;

import org.pokenet.client.GameClient;
import org.pokenet.client.constants.ServerPacket;
import org.pokenet.client.protocol.ClientMessage;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.ListBox;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.model.SimpleChangableListModel;

/**
 * Class which displays and controls the dialog for traveling via boat.
 * 
 * @author Myth1c, Sadhi
 */

public class BoatChooserDialog extends ResizableFrame
{
	protected ListBox<String> m_travelList;
	protected Label m_travelDisplay;
	private SimpleChangableListModel<String> m_locations;
	private String choice;

	public BoatChooserDialog(String currentLocation)
	{
		initUse(currentLocation);
		m_travelList = new ListBox<String>();
		m_travelList.setSize(245, 70);
		add(m_travelList);
		setTitle("Please choose your destination..");
		setVisible(false);
		setSize(250, 130);
		setPosition(300, 150);
		setResizableAxis(ResizableAxis.NONE);
		setDraggable(true);
		setVisible(false);
	}

	private void initUse(String currentLocation)
	{
		initList(currentLocation);
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
				choice = m_locations.getEntry(m_travelList.getSelected());
				String txt = "a trainer level > 24";
				if(choice.contains("Slateport") || choice.contains("Lilycove") || choice.contains("One"))
				{
					txt = "at least 16 badges";
				}
				else if(choice.contains("Canalave") || choice.contains("Snowpoint"))
				{
					txt = "at least 20 badges and trainer level 40";
				}
				else if(choice.contains("Navel"))
				{
					txt = "dev status";
				}
				String note = "Are you sure you want to travel?\nYou need " + txt + " otherwise I can't take you with me!";
				if(choice.split(" - ")[1].contains("canceled"))// || choice.contains("Two") || choice.contains("Three") || choice.contains("Four") || choice.contains("Five") || choice.contains("Iron") || choice.contains("Resort") || choice.contains("Battlefrontier"))
				{
					note = "This trip is canceled.\nWe will resume travel when the weather calms down.\nPick another one.";
				}

				Runnable yes = new Runnable()
				{
					@Override
					public void run()
					{
						setVisible(false);
						GameClient.getInstance().getGUIPane().hideConfirmationDialog();
						ClientMessage message = new ClientMessage(ServerPacket.TRAVEL);
						message.addString(choice);
						GameClient.getInstance().getSession().send(message);
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
	}

	private void initList(String currentLocation)
	{
		m_locations = new SimpleChangableListModel<>();

		if(!currentLocation.equalsIgnoreCase("kanto"))
		{
			m_locations.addElement("Vermillion City - $10k");
		}
		else
		{
			m_locations.addElement("One Island - $5k");
		}
		if(!currentLocation.equalsIgnoreCase("johto"))
		{
			m_locations.addElement("Olivine City - $10k");
		}
		if(!currentLocation.equalsIgnoreCase("slateport"))
		{
			if(GameClient.getInstance().getOurPlayer().getItemQuantity(559) != 0)
			{
				m_locations.addElement("Slateport - $10k");
			}
			else
			{
				m_locations.addElement("Slateport - $125k");
			}
		}
		else
		{
			m_locations.addElement("Battlefrontier - canceled");
		}
		if(!currentLocation.equalsIgnoreCase("lilycove"))
		{
			if(GameClient.getInstance().getOurPlayer().getItemQuantity(559) != 0)
			{
				m_locations.addElement("Lilycove - $10k");
			}
			else
			{
				m_locations.addElement("Lilycove - $125k");
			}
		}
		else
		{
			m_locations.addElement("Battlefrontier - canceled");
		}
		if(!currentLocation.equalsIgnoreCase("canalave"))
		{
			if(GameClient.getInstance().getOurPlayer().getItemQuantity(557) != 0)
			{
				m_locations.addElement("Canalave - $10k");
			}
			else
			{
				m_locations.addElement("Canalave - $175k");
			}
		}
		else
		{
			m_locations.addElement("Iron Island - $15k");
		}
		if(!currentLocation.equalsIgnoreCase("snowpoint"))
		{
			if(GameClient.getInstance().getOurPlayer().getItemQuantity(557) != 0)
			{
				m_locations.addElement("Snowpoint - $10k");
			}
			else
			{
				m_locations.addElement("Snowpoint - $175k");
			}
		}
		else
		{
			m_locations.addElement("Resort Area - canceled");
		}
		if(currentLocation.equalsIgnoreCase("navel"))
		{
			m_locations.clear();
			m_locations.addElement("Vermillion City - $10k");
			m_locations.addElement("One Island - $5k");
		}
		if(currentLocation.equalsIgnoreCase("iron"))
		{
			m_locations.clear();
			m_locations.addElement("Canalave - $0");
		}
		if(currentLocation.equalsIgnoreCase("one"))
		{
			m_locations.clear();
			m_locations.addElement("Vermillion City - $10k");
			m_locations.addElement("Two Island - $5k");
			m_locations.addElement("Three Island - $5k");
			m_locations.addElement("Four Island - canceled");
			m_locations.addElement("Five Island - canceled");
		}
		if(currentLocation.equalsIgnoreCase("two"))
		{
			m_locations.clear();
			m_locations.addElement("Vermillion City - $10k");
			m_locations.addElement("One Island - $5k");
			m_locations.addElement("Three Island - $5k");
			m_locations.addElement("Four Island - canceled");
			m_locations.addElement("Five Island - canceled");
		}
		if(currentLocation.equalsIgnoreCase("three"))
		{
			m_locations.clear();
			m_locations.addElement("Vermillion City - $10k");
			m_locations.addElement("One Island - $5k");
			m_locations.addElement("Two Island - $5k");
			m_locations.addElement("Four Island - canceled");
			m_locations.addElement("Five Island - canceled");
		}
		if(currentLocation.equalsIgnoreCase("four"))
		{
			m_locations.clear();
			m_locations.addElement("Vermillion City - $10k");
			m_locations.addElement("One Island - $5k");
			m_locations.addElement("Two Island - $5k");
			m_locations.addElement("Three Island - $5k");
			m_locations.addElement("Five Island - $5k");
			m_locations.addElement("Navel Rock");
		}
		if(currentLocation.equalsIgnoreCase("five"))
		{
			m_locations.clear();
			m_locations.addElement("Vermillion City - $5k");
			m_locations.addElement("One Island - $5k");
			m_locations.addElement("Two Island - $5k");
			m_locations.addElement("Three Island - $5k");
			m_locations.addElement("Four Island - $5k");
			m_locations.addElement("Navel Rock");
		}
	}

	public int getChoice()
	{
		return m_travelList.getSelected();
	}
}
