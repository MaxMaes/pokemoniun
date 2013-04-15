package org.pokenet.client.twl.ui.frames;

import java.util.List;

import org.pokenet.client.GameClient;
import org.pokenet.client.backend.Options;
import org.pokenet.client.backend.Translator;
import org.pokenet.client.twl.ui.base.Checkbox;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.ToggleButton;

public class OptionsDialog extends ResizableFrame {
	private Checkbox m_disableMaps;
	private Checkbox m_disableWeather;
	private Checkbox m_fullScreen;

	private ToggleButton m_muteSound;
	private Options m_options;
	private Button m_save;

	public OptionsDialog() {
		m_options = GameClient.getInstance().getOptions();
		initGUI();
	}

	public void initGUI() {
		List<String> translated = Translator.translate("_GUI");
		m_fullScreen = new Checkbox(translated.get(16));
		m_fullScreen.setPosition(10, 10);

		m_fullScreen.setActive(m_options.isFullscreenEnabled());
		add(m_fullScreen);
		m_muteSound = new ToggleButton(translated.get(17));
		m_muteSound.setPosition(150, 10);

		m_muteSound.setActive(m_options.isSoundMuted());
		add(m_muteSound);
		m_disableMaps = new Checkbox(translated.get(48));
		m_disableMaps.setPosition(10, 45);
		m_disableMaps.setActive(!m_options.isSurroundingMapsEnabled());
		add(m_disableMaps);

		m_disableWeather = new Checkbox("Disable Weather");
		m_disableWeather.setPosition(10, 78);
		m_disableWeather.setActive(!m_options.isWeatherEnabled());
		add(m_disableWeather);

		m_save = new Button(translated.get(18));
		m_save.setSize(50, 25);
		m_save.setPosition(88, 108);
		add(m_save);

		m_save.addCallback(new Runnable() {
			@Override
			public void run() {
				List<String> translated = Translator.translate("_GUI");

				m_options.setFullscreenEnabled(m_fullScreen.isActive());
				if (m_muteSound.isActive())
					m_options.setVolume(0);
				else
					m_options.setVolume(100);
				GameClient.getInstance().getSoundPlayer()
						.mute(m_options.isSoundMuted());

				m_options.setSurroundingMapsEnabled(!m_disableMaps.isActive());
				GameClient.getInstance().setLoadSurroundingMaps(
						m_options.isSurroundingMapsEnabled());

				m_options.setWeatherEnabled(!m_disableWeather.isActive());
				GameClient.getInstance().getWeatherService()
						.setEnabled(!m_disableWeather.isActive());

				m_options.saveSettings();
				// GameClient.getInstance().messageDialog(translated.get(19),
				// getDisplay()); TODO
				GameClient.getInstance().reloadOptions();
			}
		});
		setTitle(translated.get(15));
		setSize(400, 160);
		setResizableAxis(ResizableAxis.NONE);
	}

	@Override
	public void setVisible(boolean state) {
		m_options = GameClient.getInstance().getOptions();
		super.setVisible(state);
	}
}
