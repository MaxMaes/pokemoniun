package org.pokemonium.client.ui.frames;

import org.pokemonium.client.GameClient;
import org.pokemonium.client.constants.Language;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.Widget;

/**
 * Handles language selection
 * 
 * @author Myth1c
 */
public class LanguageDialog extends ResizableFrame
{
	private Label m_info;
	private Button[] m_languages;
	private Widget pane;

	/**
	 * Default constructor
	 */
	public LanguageDialog()
	{
		setSize(350, 320);
		setPosition(400 - 170, 250);
		setTheme("languagedialog");
		setTitle("Pokemonium Language Selection");
		setResizableAxis(ResizableAxis.NONE);
		setDraggable(false);
		pane = new Widget();
		pane.setSize(350, 320);
		pane.setTheme("content");

		/* Create the info label */
		m_info = new Label("  Welcome | Bienvenido | Bienvenue \n         Bem-vindo | Tervetuloa");
		m_info.setPosition(60, 40);
		m_info.setTheme("label_welcome");
		pane.add(m_info);

		/* Create all the server buttons */
		try
		{
			m_languages = new Button[8];

			m_languages[0] = new Button("English");
			m_languages[0].addCallback(new Runnable()
			{
				@Override
				public void run()
				{
					GameClient.getInstance().setLanguage(Language.ENGLISH);
					GameClient.getInstance().getGUIPane().getLoginScreen().showServerSelect();
				}
			});

			m_languages[1] = new Button("Espanol");
			m_languages[1].addCallback(new Runnable()
			{
				@Override
				public void run()
				{
					GameClient.getInstance().setLanguage(Language.SPANISH);
					GameClient.getInstance().getGUIPane().getLoginScreen().showServerSelect();
				}
			});

			m_languages[2] = new Button("Francais");
			m_languages[2].addCallback(new Runnable()
			{
				@Override
				public void run()
				{
					GameClient.getInstance().setLanguage(Language.FRENCH);
					GameClient.getInstance().getGUIPane().getLoginScreen().showServerSelect();
				}
			});

			m_languages[3] = new Button("Portugues");
			m_languages[3].addCallback(new Runnable()
			{
				@Override
				public void run()
				{
					GameClient.getInstance().setLanguage(Language.PORTUGESE);
					GameClient.getInstance().getGUIPane().getLoginScreen().showServerSelect();
				}
			});

			m_languages[4] = new Button("Suomi");
			m_languages[4].addCallback(new Runnable()
			{
				@Override
				public void run()
				{
					GameClient.getInstance().setLanguage(Language.FINNISH);
					GameClient.getInstance().getGUIPane().getLoginScreen().showServerSelect();
				}
			});

			m_languages[5] = new Button("Italiano");
			m_languages[5].addCallback(new Runnable()
			{
				@Override
				public void run()
				{
					GameClient.getInstance().setLanguage(Language.ITALIAN);
					GameClient.getInstance().getGUIPane().getLoginScreen().showServerSelect();
				}
			});

			m_languages[6] = new Button("Nederlands");
			m_languages[6].addCallback(new Runnable()
			{
				@Override
				public void run()
				{
					GameClient.getInstance().setLanguage(Language.DUTCH);
					GameClient.getInstance().getGUIPane().getLoginScreen().showServerSelect();
				}
			});

			m_languages[7] = new Button("Deutsch");
			m_languages[7].addCallback(new Runnable()
			{
				@Override
				public void run()
				{
					GameClient.getInstance().setLanguage(Language.GERMAN);
					GameClient.getInstance().getGUIPane().getLoginScreen().showServerSelect();
				}
			});

			for(int i = 0; i < m_languages.length; i++)
			{
				m_languages[i].setTheme("button_language");
				m_languages[i].setPosition(30, 62 + (28 * i));
				m_languages[i].setSize(280, 24);
				m_languages[i].setVisible(true);
				m_languages[i].setCanAcceptKeyboardFocus(false);
				pane.add(m_languages[i]);
			}
			add(pane);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		setVisible(true);
	}
}
