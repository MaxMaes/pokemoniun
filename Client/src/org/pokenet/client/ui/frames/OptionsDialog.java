package org.pokenet.client.ui.frames;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import mdes.slick.sui.Button;
import mdes.slick.sui.CheckBox;
import mdes.slick.sui.Frame;
import mdes.slick.sui.event.ActionEvent;
import mdes.slick.sui.event.ActionListener;
import org.newdawn.slick.Color;
import org.newdawn.slick.muffin.FileMuffin;
import org.newdawn.slick.muffin.Muffin;
import org.pokenet.client.GameClient;
import org.pokenet.client.backend.Options;
import org.pokenet.client.backend.Translator;

public class OptionsDialog extends Frame
{
	private CheckBox m_disableMaps;
	private CheckBox m_disableWeather;

	private CheckBox m_fullScreen;

	private Muffin m_muffin = new FileMuffin();
	private CheckBox m_muteSound;
	private Options m_options;
	private Button m_save;

	// private SimpleColorPicker learnColor;

	public OptionsDialog()
	{
		m_options = GameClient.getOptions();
		getContentPane().setX(getContentPane().getX() - 1);
		getContentPane().setY(getContentPane().getY() + 1);
		initGUI();
	}

	public void initGUI()
	{
		/* { learnColor = new SimpleColorPicker(); try { learnColor.setSelectedColor(Color.decode(options.get("learnColor"))); } catch (RuntimeException e) { e.printStackTrace(); } learnColor.setLocation(10, 70); getContentPane().add(learnColor); } */
		List<String> translated = Translator.translate("_GUI");
		setBackground(new Color(0, 0, 0, 70));
		{

			m_fullScreen = new CheckBox(translated.get(16));
			m_fullScreen.pack();
			m_fullScreen.setLocation(10, 10);

			m_fullScreen.setSelected(m_options.isFullscreenEnabled());
			getContentPane().add(m_fullScreen);
		}
		{
			m_muteSound = new CheckBox(translated.get(17));
			m_muteSound.pack();
			m_muteSound.setLocation(150, 10);

			m_muteSound.setSelected(m_options.isSoundMuted());
			getContentPane().add(m_muteSound);
		}
		{
			m_disableMaps = new CheckBox(translated.get(48));
			m_disableMaps.pack();
			m_disableMaps.setLocation(10, 45);
			m_disableMaps.setSelected(!m_options.isSurroundingMapsEnabled());
			getContentPane().add(m_disableMaps);
		}
		{
			m_disableWeather = new CheckBox("Disable Weather");
			m_disableWeather.pack();
			m_disableWeather.setLocation(10, 78);
			m_disableWeather.setSelected(!m_options.isWeatherEnabled());
			getContentPane().add(m_disableWeather);
		}
		{
			m_save = new Button(translated.get(18));
			m_save.setSize(50, 25);
			m_save.setLocation(88, 108);
			getContentPane().add(m_save);

			m_save.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					List<String> translated = Translator.translate("_GUI");
					/* options.remove("learnColor"); options.put("learnColor", learnColor.getColorHexLabel(). getText()); */
					
					m_options.setFullscreenEnabled(m_fullScreen.isSelected());
					if(m_muteSound.isSelected())
						m_options.setVolume(0);
					else
						m_options.setVolume(100);
					GameClient.getSoundPlayer().mute(m_options.isSoundMuted());
					
					m_options.setSurroundingMapsEnabled(!m_disableMaps.isSelected());
					GameClient.getInstance().setLoadSurroundingMaps(m_options.isSurroundingMapsEnabled());
					
					m_options.setWeatherEnabled(!m_disableWeather.isSelected());
					GameClient.getInstance().getWeatherService().setEnabled(!m_disableWeather.isSelected());

					m_options.saveSettings();
					GameClient.messageDialog(translated.get(19), getDisplay());
					GameClient.reloadOptions();
				}
			});
		}
		setTitle(translated.get(15));
		setSize(400, 160);
		setResizable(false);
		getTitleBar().getCloseButton().setVisible(false);
	}

	@Override
	public void setVisible(boolean state)
	{
		m_options = GameClient.getOptions();
		super.setVisible(state);
	}
}
