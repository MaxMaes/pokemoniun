package org.pokenet.client.ui.frames;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.pokenet.client.GameClient;
import org.pokenet.client.backend.FileLoader;
import org.pokenet.client.constants.ServerPacket;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.ui.components.Image;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.CallbackWithReason;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.ListBox;
import de.matthiasmann.twl.ListBox.CallbackReason;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.model.SimpleChangableListModel;

public class SpriteChooserDialog extends ResizableFrame
{
	protected String m_mustLoadSprite;
	protected Image m_spriteDisplay;
	protected ListBox<String> m_spriteList;
	private SimpleChangableListModel<String> spritelistmodel;
	private String m_respath;
	private List<String> m_sprites;

	public SpriteChooserDialog()
	{
		m_sprites = new ArrayList<String>();
		m_respath = System.getProperty("res.path");
		if(m_respath == null)
			m_respath = "";
		for(int i = 1; i <= 384; i++)
			m_sprites.add(String.valueOf(i));
		/* Handle blocked sprites */
		InputStream in = null;
		in = FileLoader.loadFile(m_respath + "res/characters/sprites.txt");
		Scanner s = new Scanner(in);
		while(s.hasNextLine())
			m_sprites.remove(s.nextLine());
		s.close();
		m_spriteDisplay = new Image();
		m_spriteDisplay.setSize(124, 204);
		m_spriteDisplay.setPosition(105, 20);
		add(m_spriteDisplay);
		spritelistmodel = new SimpleChangableListModel<>();
		m_spriteList = new ListBox<String>(spritelistmodel);
		m_spriteList.addCallback(new CallbackWithReason<ListBox.CallbackReason>()
		{

			@Override
			public void callback(CallbackReason arg0)
			{
				if(arg0 == CallbackReason.SET_SELECTED)
				{
					m_mustLoadSprite = m_respath + "res/characters/" + spritelistmodel.getEntry(m_spriteList.getSelected()) + ".png";
				}
			}
		});
		m_spriteList.setSize(105, 317);
		add(m_spriteList);
		setTitle("Please choose your character..");
		setSize(265, 340);
		setResizableAxis(ResizableAxis.NONE);
		setDraggable(false);
		setVisible(true);
		initUse();
	}

	public int getChoice()
	{
		return m_spriteList.getSelected();
	}

	public void initUse()
	{
		Button use = new Button("Use new sprite!");
		use.setPosition(130, 245);
		add(use);
		Button cancel = new Button("Cancel");
		cancel.setPosition(130, 280);
		add(cancel);
		cancel.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				// GameClient.getInstance().getDisplay().remove(thisDialog); TODO:
			}
		});
		use.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				// GameClient.getInstance().getDisplay().remove(thisDialog); TODO:
				Runnable yes = new Runnable()
				{
					@Override
					public void run()
					{
						GameClient.getInstance().getGUIPane().hideConfirmationDialog();
						// GameClient.getInstance().getPacketGenerator().writeTcpMessage("0E" + m_spriteList.getSelectedName());
						ClientMessage message = new ClientMessage(ServerPacket.BUY_SPRITE);
						message.addInt(Integer.parseInt(spritelistmodel.getEntry(m_spriteList.getSelected())));
						GameClient.getInstance().getSession().send(message);
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
				GameClient.getInstance().getGUIPane().showConfirmationDialog("Are you sure you want to change sprites?\nIt'll cost you P500!", yes, no);
			}
		});
	}

	@Override
	public void paintWidget(GUI gui)
	{
		super.paintWidget(gui);
		if(m_mustLoadSprite != null)
		{
			m_spriteDisplay.setImage(FileLoader.loadImage(m_mustLoadSprite));
			m_mustLoadSprite = null;
		}
	}
}