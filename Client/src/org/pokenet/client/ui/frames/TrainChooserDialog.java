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
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.ui.base.ConfirmationDialog;
import org.pokenet.client.ui.base.ListBox;

//@author sadhi
public class TrainChooserDialog extends Frame {

	protected ListBox m_travelList;
	protected Label m_travelDisplay;
	private List<String> m_locations;
	private String choice;
	
	public TrainChooserDialog(String travel, final OurPlayer p)
	{
		m_locations = new ArrayList<String>();
		if (travel.equalsIgnoreCase("kanto"))
		{
			m_locations.add("Goldenrod City - $10k") ;
		}
		else if (travel.equalsIgnoreCase("johto"))
		{
			m_locations.add("Saffron City - $10k") ;
		}
		else if (travel.equalsIgnoreCase("sinnoh"))
		{
			m_locations.add("Resort Area - $100k") ;
		}
		else if (travel.equalsIgnoreCase("Resort"))
		{
			m_locations.add("Snowpoint City - $10k") ;
		}
		else if (travel.equalsIgnoreCase("Fuchsia"))
		{
			m_locations.add("Safari Zone - $30k") ;
		}
		else if (travel.equalsIgnoreCase("mt.silver"))
		{
			m_locations.add("Mt.Silver") ;
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
		m_travelList.setSize(105, 317);
		getContentPane().add(m_travelList);
		setTitle("Please choose your destination..");
		getCloseButton().setVisible(false);
		setSize(265, 340);
		setResizable(false);
		setDraggable(false);
		setVisible(true);
		initUse();
//		System.out.println("end dialog");
	}
	
	public void initUse()
	{
		final TrainChooserDialog thisDialog = this;
		Button use = new Button("Let's travel!");
		use.pack();
		use.setLocation(130, 245);
		getContentPane().add(use);
		Button cancel = new Button("Cancel");
		cancel.pack();
		cancel.setLocation(130, 280);
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
				String txt = "Do you wish to go to "+ choice +"? You need a trainer level of atleast 25.";
				if (choice.contains("Snowpoint"))
				{
					txt = "Are you sure you want to go back to snowpoint?";
				}
				else if (choice.contains("Resort Area"))
				{
					txt = "Are you sure you wish to go there?\nThe Trainers and Pokémon there are quite strong.\nThat is why you need atleast 30 badges";
				}
				else if (choice.contains("Safari Zone"))
				{
					txt = "Entry to the safari zone costs 30k,\nYou also need to be atleast trainer level 25.\nDo you wish to enter?";
				}
				else if (choice.contains("Mt.Silver"))
				{
					txt = "To enter Mt.Silver you need the 16 badges of Kanto and Johto.\nDo you wish to enter?";
				}
				final ConfirmationDialog confirm = new ConfirmationDialog(txt);
				confirm.addYesListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						confirm.setVisible(false);
						GameClient.getInstance().getDisplay().remove(confirm);
						//GameClient.getInstance().getPacketGenerator().writeTcpMessage("S" + m_travelList.getSelectedName());
						ClientMessage message = new ClientMessage(53);
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
