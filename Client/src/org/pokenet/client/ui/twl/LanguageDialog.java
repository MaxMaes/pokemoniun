package org.pokenet.client.ui.twl;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.Widget;

/**
 * Handles language selection
 * 
 * @author Nushio
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
		pane = new Widget();
		pane.setSize(350, 320);
		pane.setTheme("content");
		//setBackground(new Color(0, 0, 0, 140));
		/*getTitleBar().setForeground(m_black);
		setDraggable(false);
		setResizable(false);
		getTitleBar().getCloseButton().setVisible(false);*/

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
			m_languages[0].addCallback(new Runnable(){
				@Override
				public void run() {
					//GameClient.getInstance().setLanguage(Language.ENGLISH);
					//GameClient.getInstance().getLoginScreen().showServerSelect();
				}
			});
			

			m_languages[1] = new Button("Espanol");
			m_languages[1].addCallback(new Runnable(){
				@Override
				public void run() {
					//GameClient.getInstance().setLanguage(Language.SPANISH);
					//GameClient.getInstance().getLoginScreen().showServerSelect();
				}
			});

			m_languages[2] = new Button("Francais");
			m_languages[2].addCallback(new Runnable(){
				@Override
				public void run() {
					//GameClient.getInstance().setLanguage(Language.FRENCH);
					//GameClient.getInstance().getLoginScreen().showServerSelect();
				}
			});

			m_languages[3] = new Button("Portugues");
			m_languages[3].addCallback(new Runnable(){
				@Override
				public void run() {
					//GameClient.getInstance().setLanguage(Language.PORTUGESE);
					//GameClient.getInstance().getLoginScreen().showServerSelect();
				}
			});

			m_languages[4] = new Button("Suomi");
			m_languages[4].addCallback(new Runnable(){
				@Override
				public void run() {
					//GameClient.getInstance().setLanguage(Language.FINNISH);
					//GameClient.getInstance().getLoginScreen().showServerSelect();
				}
			});

			m_languages[5] = new Button("Italiano");
			m_languages[5].addCallback(new Runnable(){
				@Override
				public void run() {
					//GameClient.getInstance().setLanguage(Language.ITALIAN);
					//GameClient.getInstance().getLoginScreen().showServerSelect();
				}
			});

			m_languages[6] = new Button("Nederlands");
			m_languages[6].addCallback(new Runnable(){
				@Override
				public void run() {
					//GameClient.getInstance().setLanguage(Language.DUTCH);
					//GameClient.getInstance().getLoginScreen().showServerSelect();
				}
			});

			m_languages[7] = new Button("Deutsch");
			m_languages[7].addCallback(new Runnable(){
				@Override
				public void run() {
					//GameClient.getInstance().setLanguage(Language.GERMAN);
					//GameClient.getInstance().getLoginScreen().showServerSelect();
				}
			});

			for(int i = 0; i < m_languages.length; i++) {
				m_languages[i].setTheme("button_language");
				m_languages[i].setPosition(30, 62 + (28*i));
				m_languages[i].setSize(280, 24);
				m_languages[i].setVisible(true);
				pane.add(m_languages[i]);
			}

			setVisible(true);
			pane.setVisible(true);
			add(pane);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		setVisible(true);
		
		/*DialogLayout layout = new DialogLayout();
		Group vbuttonGroup = layout.createSequentialGroup(m_languages[0], m_languages[1], m_languages[2], m_languages[3], m_languages[4], m_languages[5], m_languages[6], m_languages[7]);
		Group hbuttonGroup = layout.createParallelGroup(m_languages[0], m_languages[1], m_languages[2], m_languages[3], m_languages[4], m_languages[5], m_languages[6], m_languages[7]);
		layout.setHorizontalGroup(hbuttonGroup);
		layout.setVerticalGroup(vbuttonGroup);
		layout.setTheme("content");
		add(layout);*/
	}
}
