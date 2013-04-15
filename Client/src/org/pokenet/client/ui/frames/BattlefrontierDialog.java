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
import org.pokenet.client.backend.entity.Pokemon;
import org.pokenet.client.constants.ServerPacket;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.ui.base.ConfirmationDialog;
import org.pokenet.client.ui.base.ListBox;

//@author sadhi
public class BattlefrontierDialog extends Frame {

	protected ListBox m_travelList;
	protected Label m_travelDisplay;
	private List<String> m_locations;
	private String choice,pick;
	private OurPlayer p;
	
	public BattlefrontierDialog(String battle, final OurPlayer p)
	{
		this.p = p;
		choice = battle;
		m_locations = new ArrayList<String>();
		m_locations.add("Challenge") ;
		m_locations.add("Info") ;
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
		m_travelList.setSize(175, 70);
		getContentPane().add(m_travelList);
		setTitle("Please choose..");
		getCloseButton().setVisible(false);
		setSize(180, 130);
		setLocation(300,150);
		setResizable(false);
		setDraggable(true);
		setVisible(true);
		initUse();
//		System.out.println("end dialog");
	}
	
	public void initUse()
	{
		final BattlefrontierDialog thisDialog = this;
		Button use = new Button("Ok");
		use.pack();
		use.setLocation(25, 75);
		getContentPane().add(use);
		Button cancel = new Button("Cancel");
		cancel.pack();
		cancel.setLocation(80, 75);
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
				pick = m_travelList.getSelectedName();
				GameClient.getInstance().getDisplay().remove(thisDialog);
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
				final ConfirmationDialog confirm = new ConfirmationDialog(txt);
				confirm.addYesListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						confirm.setVisible(false);
						GameClient.getInstance().getDisplay().remove(confirm);
						//GameClient.getInstance().getPacketGenerator().writeTcpMessage("S" + m_travelList.getSelectedName());
						if(pick.equals("Challenge"))
						{
							ClientMessage message = new ClientMessage(ServerPacket.BATTLEFRONTIER_FACILITY);
							message.addString(choice);
							GameClient.getInstance().getSession().send(message);
						}
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
