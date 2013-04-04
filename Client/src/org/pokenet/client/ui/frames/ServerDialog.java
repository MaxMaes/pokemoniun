package org.pokenet.client.ui.frames;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import mdes.slick.sui.Button;
import mdes.slick.sui.Frame;
import mdes.slick.sui.Label;
import mdes.slick.sui.TextField;
import mdes.slick.sui.event.ActionEvent;
import mdes.slick.sui.event.ActionListener;
import org.newdawn.slick.Color;
import org.pokenet.client.GameClient;
import org.pokenet.client.backend.Translator;

/**
 * Handles server selection
 * 
 * @author shadowkanji
 **/
public class ServerDialog extends Frame
{
	private Color m_black;
	private String[] m_host;
	private Label m_info;
	private Button[] m_servers;
	private TextField privateIP;
	private Button privateServer, m_back;
	private final GameClient m_client;

	/** Default constructor **/
	public ServerDialog()
	{
		m_client = GameClient.getInstance(); // hacky, rewrite.
		getContentPane().setX(getContentPane().getX() - 1);
		getContentPane().setY(getContentPane().getY() + 1);
		m_black = new Color(0, 0, 0);
		List<String> translate = Translator.translate("_LOGIN");
		this.setSize(316, 300);
		this.setLocation(400 - 160, 280);
		setTitle(translate.get(0));
		setBackground(new Color(0, 0, 0, 140));
		getTitleBar().setForeground(m_black);
		setDraggable(false);
		setResizable(false);
		getTitleBar().getCloseButton().setVisible(false);
		/* Create the info label */
		m_info = new Label(translate.get(1));
		m_info.pack();
		m_info.setLocation(24, 8);
		m_info.setForeground(new Color(255, 255, 255));
		this.add(m_info);

		/* Create all the server buttons */
		String respath = System.getProperty("res.path");
		if(respath == null)
			respath = "";
		try
		{
			m_servers = new Button[5];
			m_host = new String[5];
			InputStream stream;
			URL url = new URL("http://s1.pokemonium.com/launcher/servers.txt");
			stream = url.openStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(stream));
			m_servers[0] = new Button(in.readLine());
			m_servers[0].setSize(280, 24);
			m_servers[0].setLocation(16, 32);
			m_servers[0].setVisible(true);
			m_servers[0].addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					m_client.connect(m_host[0]);
				}
			});
			this.add(m_servers[0]);
			m_host[0] = in.readLine();

			m_servers[1] = new Button(in.readLine());
			m_servers[1].setSize(280, 24);
			m_servers[1].setLocation(16, 64);
			m_servers[1].setVisible(true);
			m_servers[1].addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					m_client.connect(m_host[1]);
				}
			});
			this.add(m_servers[1]);
			m_host[1] = in.readLine();

			m_servers[2] = new Button(in.readLine());
			m_servers[2].setSize(280, 24);
			m_servers[2].setLocation(16, 96);
			m_servers[2].setVisible(true);
			m_servers[2].addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					m_client.connect(m_host[2]);
				}
			});
			this.add(m_servers[2]);
			m_host[2] = in.readLine();

			m_servers[3] = new Button(in.readLine());
			m_servers[3].setSize(280, 24);
			m_servers[3].setLocation(16, 128);
			m_servers[3].setVisible(true);
			m_servers[3].addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					m_client.connect(m_host[3]);
				}
			});
			this.add(m_servers[3]);
			m_host[3] = in.readLine();

			m_servers[4] = new Button(in.readLine());
			m_servers[4].setSize(280, 24);
			m_servers[4].setLocation(16, 160);
			m_servers[4].setVisible(true);
			m_servers[4].addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					m_client.connect(m_host[4]);
				}
			});
			this.add(m_servers[4]);
			m_host[4] = in.readLine();
			/* Finally, check which servers don't exist and disable their buttons */
			for(int i = 0; i < m_host.length; i++)
			{
				if(m_host[i] == null || m_host[i].equals("-"))
				{
					m_host[i] = "";
					m_servers[i].setEnabled(false);
				}
				m_servers[i].setForeground(m_black);
			}
			in.close();
			stream.close();
		}
		catch(MalformedURLException mue)
		{
			System.out.println("There seems to be a problem with loading the serverlist.");
		}
		catch(IOException ioe)
		{
			System.out.println("The serverlist could not be loaded, please connect to a private server or try 'Pokemonium Server'.");
			ioe.printStackTrace();
			m_host[0] = "s1.pokemonium.com";
			m_servers[0] = new Button("Pokemonium Server");
			m_servers[0].setSize(280, 24);
			m_servers[0].setLocation(16, 32);
			m_servers[0].setVisible(true);
			m_servers[0].addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					m_client.connect(m_host[0]);
				}
			});
			this.add(m_servers[0]);
		}
		privateIP = new TextField();
		privateIP.setLocation(16, 204);
		privateIP.setSize(128, 24);
		this.add(privateIP);
		privateServer = new Button();
		privateServer.setText(translate.get(2));
		privateServer.setSize(128, 24);
		privateServer.setLocation(168, 204);
		privateServer.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				m_client.connect(getPrivateServer());
			}
		});
		this.add(privateServer);
		setVisible(false);
		m_back = new Button();
		m_back.setText("Back");
		m_back.setSize(128, 24);
		m_back.setLocation(94, 235);
		m_back.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent evt)
			{
				m_client.returnToLanguageSelect();
			}
		});
		this.add(m_back);
		setVisible(false);
	}

	public String getPrivateServer()
	{
		if(privateIP.getText().length() > 0)
			return privateIP.getText();
		return "localhost";
	}

	public void goServer()
	{
		if(privateIP.getText().length() > 0)
			m_client.connect(getPrivateServer());
	}

	public void reloadStrings()
	{
		List<String> translate = Translator.translate("_LOGIN");
		setTitle(translate.get(0));
		m_info.setText(translate.get(1));
		privateServer.setText(translate.get(2));
	}
}
