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

//@author sadhi
public class BoatChooserDialog extends Frame {

	protected ListBox m_travelList;
	protected Label m_travelDisplay;
	private List<String> m_locations;
	private String choice;
	
	public BoatChooserDialog(String travel, final OurPlayer p)
	{
		m_locations = new ArrayList<String>();
		if (!travel.equalsIgnoreCase("kanto"))
		{
			m_locations.add("Vermillion City - $10k") ;
		}
		if (!travel.equalsIgnoreCase("johto"))
		{
			m_locations.add("Olivine City - $10k") ;
		}
		if (!travel.equalsIgnoreCase("slateport"))
		{
			m_locations.add("Slateport - $125k") ;
		}
		if (!travel.equalsIgnoreCase("lilycove"))
		{
			m_locations.add("Lilycove - $125k") ;
		}
		if (!travel.equalsIgnoreCase("canalave"))
		{
			m_locations.add("Canalave - $175k") ;
		}
		getContentPane().setX(getContentPane().getX() - 1);
		getContentPane().setY(getContentPane().getY() + 1);
//		m_travelDisplay = new Label();
//		m_travelDisplay.setSize(124,204);
//		m_travelDisplay.setLocation(105, 20);
//		getContentPane().add(m_travelDisplay);
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
		setLocation(300,150);
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
				choice =  m_travelList.getSelectedName();
				GameClient.getInstance().getDisplay().remove(thisDialog);
				String txt = "a trainer level > 24";
				if (choice.contains("Slateport"))
				{
					txt = "at least 16 badges";
				}
				else if (choice.contains("Lilycove"))
				{
					txt = "at least 16 badges";
				}
				else if (choice.contains("Canalave"))
				{
					txt = "at least 20 badges";
				}
				final ConfirmationDialog confirm = new ConfirmationDialog("Are you sure you want to travel?\nYou need "+ txt +" otherwise I can't take you with me!");
				confirm.addYesListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						confirm.setVisible(false);
						GameClient.getInstance().getDisplay().remove(confirm);
//						GameClient.getInstance().getPacketGenerator().writeTcpMessage("S" + m_travelList.getSelectedName());
						ClientMessage message = new ClientMessage(ServerPacket.TRAVEL);
						message.addString(choice);
						GameClient.getSession().send(message);
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
