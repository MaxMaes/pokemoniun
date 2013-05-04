package org.pokenet.client.ui.frames;

import java.util.ArrayList;
import java.util.List;
import mdes.slick.sui.Button;
import mdes.slick.sui.Frame;
import mdes.slick.sui.Label;
import mdes.slick.sui.event.ActionEvent;
import mdes.slick.sui.event.ActionListener;
import org.pokenet.client.GameClient;
import org.pokenet.client.backend.entity.OurPlayer;
import org.pokenet.client.constants.ServerPacket;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.ui.base.ConfirmationDialog;
import org.pokenet.client.ui.base.ListBox;

// @author sadhi
public class BoatChooserDialog extends Frame
{

	protected ListBox m_travelList;
	protected Label m_travelDisplay;
	private List<String> m_locations;
	private String choice;

	public BoatChooserDialog(String travel, final OurPlayer p)
	{
		m_locations = new ArrayList<String>();
		if(!travel.equalsIgnoreCase("kanto"))
		{
			m_locations.add("Vermillion City - $10k");
		}
		else
		{
			m_locations.add("One Island - $5k");
		}
		if(!travel.equalsIgnoreCase("johto"))
		{
			m_locations.add("Olivine City - $10k");
		}
		if(!travel.equalsIgnoreCase("slateport"))
		{
			if(GameClient.getInstance().getOurPlayer().getItemQuantity(559) != 0)
			{
				m_locations.add("Slateport - $10k");
			}
			else
			{
				m_locations.add("Slateport - $125k");
			}
		}
		else
		{
			m_locations.add("Battlefrontier - canceled");
		}
		if(!travel.equalsIgnoreCase("lilycove"))
		{
			if(GameClient.getInstance().getOurPlayer().getItemQuantity(559) != 0)
			{
				m_locations.add("Lilycove - $10k");
			}
			else
			{
				m_locations.add("Lilycove - $125k");
			}
		}
		else
		{
			m_locations.add("Battlefrontier - canceled");
		}
		if(!travel.equalsIgnoreCase("canalave"))
		{
			if(GameClient.getInstance().getOurPlayer().getItemQuantity(557) != 0)
			{
				m_locations.add("Canalave - $10k");
			}
			else
			{
				m_locations.add("Canalave - $175k");
			}
		}
		else
		{
			m_locations.add("Iron Island - $15k");
		}
		if(!travel.equalsIgnoreCase("snowpoint"))
		{
			if(GameClient.getInstance().getOurPlayer().getItemQuantity(557) != 0)
			{
				m_locations.add("Snowpoint - $10k");
			}
			else
			{
				m_locations.add("Snowpoint - $175k");
			}
		}
		else
		{
			m_locations.add("Resort Area - canceled");
		}
		if(travel.equalsIgnoreCase("navel"))
		{
			m_locations.clear();
			m_locations.add("Vermillion City - $10k");
			m_locations.add("One Island - $5k");
		}
		if(travel.equalsIgnoreCase("iron"))
		{
			m_locations.clear();
			m_locations.add("Canalave - $0");
		}
		if(travel.equalsIgnoreCase("one"))
		{
			m_locations.clear();
			m_locations.add("Vermillion City - $10k");
			m_locations.add("Two Island - $5k");
			m_locations.add("Three Island - $5k");
			m_locations.add("Four Island - canceled");
			m_locations.add("Five Island - canceled");
		}
		if(travel.equalsIgnoreCase("two"))
		{
			m_locations.clear();
			m_locations.add("Vermillion City - $10k");
			m_locations.add("One Island - $5k");
			m_locations.add("Three Island - $5k");
			m_locations.add("Four Island - canceled");
			m_locations.add("Five Island - canceled");
		}
		if(travel.equalsIgnoreCase("three"))
		{
			m_locations.clear();
			m_locations.add("Vermillion City - $10k");
			m_locations.add("One Island - $5k");
			m_locations.add("Two Island - $5k");
			m_locations.add("Four Island - canceled");
			m_locations.add("Five Island - canceled");
		}
		if(travel.equalsIgnoreCase("four"))
		{
			m_locations.clear();
			m_locations.add("Vermillion City - $10k");
			m_locations.add("One Island - $5k");
			m_locations.add("Two Island - $5k");
			m_locations.add("Three Island - $5k");
			m_locations.add("Five Island - $5k");
			m_locations.add("Navel Rock");
		}
		if(travel.equalsIgnoreCase("five"))
		{
			m_locations.clear();
			m_locations.add("Vermillion City - $5k");
			m_locations.add("One Island - $5k");
			m_locations.add("Two Island - $5k");
			m_locations.add("Three Island - $5k");
			m_locations.add("Four Island - $5k");
			m_locations.add("Navel Rock");
		}
		getContentPane().setX(getContentPane().getX() - 1);
		getContentPane().setY(getContentPane().getY() + 1);
		// m_travelDisplay = new Label();
		// m_travelDisplay.setSize(124,204);
		// m_travelDisplay.setLocation(105, 20);
		// getContentPane().add(m_travelDisplay);
		m_travelList = new ListBox(m_locations, false)
		{
			@Override
			protected void itemClicked(String itemName, int idx)
			{
				super.itemClicked(itemName, idx);

			}
		};
		m_travelList.setSize(245, 70);
		getContentPane().add(m_travelList);
		setTitle("Please choose your destination..");
		getCloseButton().setVisible(false);
		setSize(250, 130);
		setLocation(300, 150);
		setResizable(false);
		setDraggable(true);
		setVisible(true);
		initUse();
	}

	public void initUse()
	{

		final BoatChooserDialog thisDialog = this;
		Button use = new Button("Let's travel!");
		use.pack();
		use.setLocation(25, 75);
		getContentPane().add(use);
		Button cancel = new Button("Cancel");
		cancel.pack();
		cancel.setLocation(150, 75);
		getContentPane().add(cancel);

		cancel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				GameClient.getInstance().getDisplay().remove(thisDialog);
			}
		});
		use.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				choice = m_travelList.getSelectedName();
				GameClient.getInstance().getDisplay().remove(thisDialog);
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
				final ConfirmationDialog confirm = new ConfirmationDialog(note);
				confirm.addYesListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						confirm.setVisible(false);
						GameClient.getInstance().getDisplay().remove(confirm);
						// GameClient.getInstance().getPacketGenerator().writeTcpMessage("S" + m_travelList.getSelectedName());
						ClientMessage message = new ClientMessage(ServerPacket.TRAVEL);
						message.addString(choice);
						GameClient.getInstance().getSession().send(message);
					}
				});
				confirm.addNoListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						confirm.setVisible(false);
						GameClient.getInstance().getDisplay().remove(confirm);
					}
				});
				GameClient.getInstance().getDisplay().add(confirm);
			}
		});
	}

	public int getChoice()
	{
		return m_travelList.getSelectedIndex();
	}
}
