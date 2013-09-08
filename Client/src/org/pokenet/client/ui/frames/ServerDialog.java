package org.pokenet.client.ui.frames;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.pokenet.client.GameClient;
import org.pokenet.client.backend.Translator;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.Widget;

/**
 * Handles server selection
 * 
 * @author shadowkanji
 **/
public class ServerDialog extends ResizableFrame
{
	private String[] m_host;
	private Label m_info;
	private Button[] m_servers;
	private EditField privateIP;
	private Button privateServer, m_back;
	private final GameClient m_client;
	
	private Widget panel;

	/** Default constructor **/
	public ServerDialog()
	{
		m_client = GameClient.getInstance(); // hacky, rewrite.
		List<String> translate = Translator.translate("_LOGIN");
		setSize(316, 300);
		setPosition(400 - 160, 280);
		setTitle(translate.get(0));
		setDraggable(false);
		setResizableAxis(ResizableAxis.NONE);
		
		panel = new Widget();
		panel.setTheme("content");
		
		/* Create the info label */
		m_info = new Label(translate.get(1));
		m_info.setPosition(24, 40);
		m_info.setTheme("label_info");
		panel.add(m_info);

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
			m_servers[0].setVisible(true);
			m_servers[0].addCallback(new Runnable()
			{
				@Override
				public void run()
				{
					m_client.connect(m_host[0]);
				}
			});
			panel.add(m_servers[0]);
			m_host[0] = in.readLine();

			m_servers[1] = new Button(in.readLine());
			m_servers[1].setSize(280, 24);
			m_servers[1].setVisible(true);
			m_servers[1].addCallback(new Runnable()
			{
				@Override
				public void run()
				{
					m_client.connect(m_host[1]);
				}
			});
			panel.add(m_servers[1]);
			m_host[1] = in.readLine();

			m_servers[2] = new Button(in.readLine());
			m_servers[2].setSize(280, 24);
			m_servers[2].setVisible(true);
			m_servers[2].addCallback(new Runnable()
			{
				@Override
				public void run()
				{
					m_client.connect(m_host[2]);
				}
			});
			panel.add(m_servers[2]);
			m_host[2] = in.readLine();

			m_servers[3] = new Button(in.readLine());
			m_servers[3].setSize(280, 24);
			m_servers[3].setVisible(true);
			m_servers[3].addCallback(new Runnable()
			{
				@Override
				public void run()
				{
					m_client.connect(m_host[3]);
				}
			});
			panel.add(m_servers[3]);
			m_host[3] = in.readLine();

			m_servers[4] = new Button(in.readLine());
			m_servers[4].setSize(280, 24);
			m_servers[4].setVisible(true);
			m_servers[4].addCallback(new Runnable()
			{
				@Override
				public void run()
				{
					m_client.connect(m_host[4]);
				}
			});
			panel.add(m_servers[4]);
			m_host[4] = in.readLine();
			/* Finally, check which servers don't exist and disable their buttons */
			for(int i = 0; i < m_host.length; i++)
			{
				if(m_host[i] == null || m_host[i].equals("-"))
				{
					m_host[i] = "";
					m_servers[i].setEnabled(false);
				}
			}
			in.close();
			stream.close();
			for(int i = 0; i < m_servers.length; i++)
			{
				m_servers[i].setPosition(16, 64 + 32*i);
			}
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
			m_servers[0].setPosition(16, 64);
			m_servers[0].setVisible(true);
			m_servers[0].addCallback(new Runnable()
			{
				@Override
				public void run()
				{
					m_client.connect(m_host[0]);
				}
			});
			panel.add(m_servers[0]);
		}
		privateIP = new EditField();
		privateIP.setPosition(16, 236);
		privateIP.setSize(128, 24);
		panel.add(privateIP);
		privateServer = new Button();
		privateServer.setText(translate.get(2));
		privateServer.setSize(128, 24);
		privateServer.setPosition(168, 236);
		privateServer.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				m_client.connect(getPrivateServer());
			}
		});
		panel.add(privateServer);
		m_back = new Button();
		m_back.setText("Back");
		m_back.setSize(128, 24);
		m_back.setPosition(94, 267);
		m_back.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				m_client.returnToLanguageSelect();
			}
		});
		panel.add(m_back);
		add(panel);
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
