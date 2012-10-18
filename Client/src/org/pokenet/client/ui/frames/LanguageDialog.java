package org.pokenet.client.ui.frames;

import mdes.slick.sui.Button;
import mdes.slick.sui.Frame;
import mdes.slick.sui.Label;
import mdes.slick.sui.event.ActionEvent;
import mdes.slick.sui.event.ActionListener;
import org.newdawn.slick.Color;
import org.pokenet.client.GameClient;
import org.pokenet.client.constants.Language;

/**
 * Handles language selection
 * 
 * @author Nushio
 */
public class LanguageDialog extends Frame
{
	private Color m_black;
	private Label m_info;
	private Button[] m_languages;

	/**
	 * Default constructor
	 */
	public LanguageDialog()
	{
		getContentPane().setX(getContentPane().getX() - 1);
		getContentPane().setY(getContentPane().getY() + 1);
		m_black = new Color(0, 0, 0);

		this.setSize(350, 320);
		this.setLocation(400 - 170, 250);
		setTitle("Pokemonium Language Selection");
		setBackground(new Color(0, 0, 0, 140));
		getTitleBar().setForeground(m_black);
		setDraggable(false);
		setResizable(false);
		getTitleBar().getCloseButton().setVisible(false);

		/* Create the info label */
		m_info = new Label("  Welcome | Bienvenido | Bienvenue \n         Bem-vindo | Tervetuloa");
		m_info.pack();
		m_info.setLocation(60, 8);
		m_info.setForeground(new Color(255, 255, 255));
		this.add(m_info);

		/* Create all the server buttons */
		try
		{
			m_languages = new Button[8];

			m_languages[0] = new Button("English");
			m_languages[0].setSize(280, 24);
			m_languages[0].setLocation(30, 42);
			m_languages[0].setVisible(true);
			m_languages[0].addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					GameClient.getInstance().setLanguage(Language.ENGLISH);
				}
			});
			this.add(m_languages[0]);

			m_languages[1] = new Button("Espanol");
			m_languages[1].setSize(280, 24);
			m_languages[1].setLocation(30, 70);
			m_languages[1].setVisible(true);
			m_languages[1].addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					GameClient.getInstance().setLanguage(Language.SPANISH);
				}
			});
			this.add(m_languages[1]);

			m_languages[2] = new Button("Francais");
			m_languages[2].setSize(280, 24);
			m_languages[2].setLocation(30, 98);
			m_languages[2].setVisible(true);
			m_languages[2].addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					GameClient.getInstance().setLanguage(Language.FRENCH);
				}
			});
			this.add(m_languages[2]);

			m_languages[3] = new Button("Portugues");
			m_languages[3].setSize(280, 24);
			m_languages[3].setLocation(30, 126);
			m_languages[3].setVisible(true);
			m_languages[3].addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					GameClient.getInstance().setLanguage(Language.PORTUGESE);

				}
			});
			this.add(m_languages[3]);

			m_languages[4] = new Button("Suomi");
			m_languages[4].setSize(280, 24);
			m_languages[4].setLocation(30, 154);
			m_languages[4].setVisible(true);
			m_languages[4].addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					GameClient.getInstance().setLanguage(Language.FINNISH);

				}
			});
			this.add(m_languages[4]);

			m_languages[5] = new Button("Italiano");
			m_languages[5].setSize(280, 24);
			m_languages[5].setLocation(30, 182);
			m_languages[5].setVisible(true);
			m_languages[5].addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					GameClient.getInstance().setLanguage("italian");

				}
			});
			this.add(m_languages[5]);

			m_languages[6] = new Button("Nederlands");
			m_languages[6].setSize(280, 24);
			m_languages[6].setLocation(30, 210);
			m_languages[6].setVisible(true);
			m_languages[6].addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					GameClient.getInstance().setLanguage(Language.DUTCH);

				}
			});
			this.add(m_languages[6]);

			m_languages[7] = new Button("Deutsch");
			m_languages[7].setSize(280, 24);
			m_languages[7].setLocation(30, 238);
			m_languages[7].setVisible(true);
			m_languages[7].addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					GameClient.getInstance().setLanguage(Language.GERMAN);

				}
			});
			this.add(m_languages[7]);

			setVisible(true);

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		setVisible(true);
	}

}
