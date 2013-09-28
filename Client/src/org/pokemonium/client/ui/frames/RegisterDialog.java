package org.pokemonium.client.ui.frames;

import java.util.List;
import java.util.regex.Pattern;
import org.pokemonium.client.GameClient;
import org.pokemonium.client.backend.FileLoader;
import org.pokemonium.client.backend.Translator;
import org.pokemonium.client.ui.components.ImageButton;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.ResizableFrame;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.renderer.Image;

/**
 * Handles registration dialog
 * 
 * @author shadowkanji
 */
public class RegisterDialog extends ResizableFrame
{
	private int m_gender = 0;
	private int m_region = 0;
	private Button[] m_regions;
	private Button m_register, m_cancel, m_male, m_female, m_terms;
	private int m_starter;
	private ImageButton[] m_starters;
	private Label m_u, m_p, m_cp, m_d, m_e, m_ce, m_s, m_g, m_tos, m_reg;
	private EditField m_username, m_password, m_confirmPass, m_email, m_confirmEmail, m_day, m_month, m_year;

	private Widget pane;

	/**
	 * Default constructor
	 */
	public RegisterDialog()
	{
		List<String> translated = Translator.translate("_LOGIN");
		setVisible(false);
		this.setSize(320, 360);
		setTitle(translated.get(9));
		setPosition(420, 220);
		setResizableAxis(ResizableAxis.NONE);
		setDraggable(false);

		pane = new Widget();
		pane.setTheme("content");
		pane.setSize(320, 360);

		m_u = new Label(translated.get(5));
		m_u.setPosition(4, 31);
		pane.add(m_u);

		m_username = new EditField();
		m_username.setSize(128, 24);
		m_username.setPosition(4, 24 + 22);
		m_username.setVisible(true);
		m_username.requestKeyboardFocus();
		pane.add(m_username);

		m_p = new Label(translated.get(6));
		m_p.setPosition(4, 52 + 27);
		pane.add(m_p);

		m_password = new EditField();
		m_password.setSize(128, 24);
		m_password.setPosition(4, 72 + 22);
		m_password.setPasswordMasking(true);
		m_password.setVisible(true);
		pane.add(m_password);

		m_cp = new Label(translated.get(10));
		m_cp.setPosition(4, 100 + 27);
		pane.add(m_cp);

		m_confirmPass = new EditField();
		m_confirmPass.setSize(128, 24);
		m_confirmPass.setPosition(4, 122 + 22);
		m_confirmPass.setPasswordMasking(true);
		m_confirmPass.setVisible(true);
		pane.add(m_confirmPass);

		m_d = new Label(translated.get(11));
		m_d.setPosition(4, 152 + 27);
		pane.add(m_d);

		m_day = new EditField();
		m_day.setSize(22, 24);
		m_day.setPosition(4, 172 + 22);
		m_day.setVisible(true);
		m_day.setMaxTextLength(2); // Days 01-31
		pane.add(m_day);

		m_month = new EditField();
		m_month.setSize(22, 24);
		m_month.setPosition(40, 172 + 22);
		m_month.setVisible(true);
		m_month.setMaxTextLength(2); // Month 01-12
		pane.add(m_month);

		m_year = new EditField();
		m_year.setSize(42, 24);
		m_year.setPosition(76, 172 + 22);
		m_year.setVisible(true);
		m_year.setMaxTextLength(4); // Years 0000 - 9999
		pane.add(m_year);

		m_e = new Label(translated.get(12));
		m_e.setPosition(4, 202 + 27);
		pane.add(m_e);

		m_email = new EditField();
		m_email.setSize(128, 24);
		m_email.setPosition(4, 220 + 22);
		m_email.setVisible(true);
		pane.add(m_email);

		m_ce = new Label(translated.get(13));
		m_ce.setPosition(4, 248 + 27);
		pane.add(m_ce);

		m_confirmEmail = new EditField();
		m_confirmEmail.setSize(128, 24);
		m_confirmEmail.setPosition(4, 268 + 22);
		m_confirmEmail.setVisible(true);
		pane.add(m_confirmEmail);

		m_s = new Label(translated.get(14));
		m_s.setPosition(170, 4 + 27);
		pane.add(m_s);

		generateStarters();

		m_g = new Label(translated.get(15));
		m_g.setPosition(170, 128 + 27);
		pane.add(m_g);

		m_male = new Button(translated.get(16));
		m_male.setCanAcceptKeyboardFocus(false);
		m_male.setSize(54, 24);
		m_male.setPosition(170, 150 + 17);
		m_male.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				m_female.setEnabled(true);
				m_gender = 0;
				m_male.setEnabled(false);
			}
		});
		pane.add(m_male);

		m_female = new Button(translated.get(17));
		m_female.setCanAcceptKeyboardFocus(false);
		m_female.setSize(54, 24);
		m_female.setPosition(234, 150 + 17);
		m_female.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				m_female.setEnabled(false);
				m_gender = 1;
				m_male.setEnabled(true);
			}
		});
		pane.add(m_female);

		m_reg = new Label("Starting Region:");
		m_reg.setPosition(170, 182 + 27);
		m_reg.setVisible(true);
		pane.add(m_reg);

		/* Generate region selection */
		m_regions = new Button[4];
		m_regions[0] = new Button("Kanto");
		m_regions[0].setCanAcceptKeyboardFocus(false);
		m_regions[0].setSize(54, 24);
		m_regions[0].setPosition(170, 206 + 17);
		m_regions[0].setTooltipContent("A town-filled region\nconnected to the\neast of Johto");
		m_regions[0].addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				m_region = 0;
				m_regions[0].setEnabled(false);
				m_regions[1].setEnabled(true);
				m_regions[2].setEnabled(false);
				m_regions[3].setEnabled(false);
			}
		});
		m_regions[0].setVisible(true);
		pane.add(m_regions[0]);

		m_regions[1] = new Button("Johto");
		m_regions[1].setCanAcceptKeyboardFocus(false);
		m_regions[1].setSize(54, 24);
		m_regions[1].setPosition(234, 206 + 17);
		m_regions[1].setTooltipContent("A city-filled\nregion connected\nto the west\nof Kanto");
		m_regions[1].addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				m_region = 1;
				m_regions[0].setEnabled(true);
				m_regions[1].setEnabled(false);
				m_regions[2].setEnabled(false);
				m_regions[3].setEnabled(false);
			}
		});
		m_regions[1].setVisible(true);
		pane.add(m_regions[1]);

		m_regions[2] = new Button("Hoenn");
		m_regions[2].setCanAcceptKeyboardFocus(false);
		m_regions[2].setSize(54, 24);
		m_regions[2].setPosition(170, 230 + 17);
		m_regions[2].setTooltipContent("An island region\nsouth-west\nof Johto");
		m_regions[2].addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				m_region = 0;
				m_regions[0].setEnabled(false);
				m_regions[1].setEnabled(false);
				m_regions[2].setEnabled(false);
				m_regions[3].setEnabled(false);
			}
		});
		m_regions[2].setVisible(true);
		m_regions[2].setEnabled(false);
		pane.add(m_regions[2]);

		m_regions[3] = new Button("Sinnoh");
		m_regions[3].setCanAcceptKeyboardFocus(false);
		m_regions[3].setSize(54, 24);
		m_regions[3].setPosition(234, 230 + 17);
		m_regions[3].setTooltipContent("A mountainous\nregion north\nof Kanto");
		m_regions[3].addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				m_region = 0;
				m_regions[0].setEnabled(false);
				m_regions[1].setEnabled(false);
				m_regions[2].setEnabled(false);
				m_regions[3].setEnabled(false);
			}
		});
		m_regions[3].setVisible(true);
		m_regions[3].setEnabled(false);
		pane.add(m_regions[3]);

		m_tos = new Label(translated.get(18));
		m_tos.setPosition(172, 256 + 27);
		pane.add(m_tos);

		m_terms = new Button(translated.get(19));
		m_terms.setCanAcceptKeyboardFocus(false);
		m_terms.setSize(118, 24);
		m_terms.setPosition(170, 276 + 22);
		m_terms.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				m_terms.setEnabled(false);
			}
		});
		pane.add(m_terms);

		m_register = new Button(translated.get(8));
		m_register.setCanAcceptKeyboardFocus(false);
		m_register.setSize(49, 24);
		m_register.setPosition(96, 308 + 22);
		m_register.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				register();
			}
		});
		pane.add(m_register);

		m_cancel = new Button(translated.get(20));
		m_cancel.setCanAcceptKeyboardFocus(false);
		m_cancel.setSize(49, 24);
		m_cancel.setPosition(160, 308 + 22);
		m_cancel.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				cancel();
			}
		});
		pane.add(m_cancel);
		add(pane);
	}

	/**
	 * Enables the registration
	 */
	public void enableRegistration()
	{
		m_register.setEnabled(true);
	}

	/**
	 * Tabs to the next field.
	 * 
	 * @param email
	 * @return
	 */
	public void goToNext()
	{
		if(m_username.hasKeyboardFocus())
		{
			m_password.requestKeyboardFocus();
		}
		else if(m_password.hasKeyboardFocus())
		{
			m_confirmPass.requestKeyboardFocus();
		}
		else if(m_confirmPass.hasKeyboardFocus())
		{
			m_day.requestKeyboardFocus();
		}
		else if(m_day.hasKeyboardFocus())
		{
			m_month.requestKeyboardFocus();
		}
		else if(m_month.hasKeyboardFocus())
		{
			m_year.requestKeyboardFocus();
		}
		else if(m_year.hasKeyboardFocus())
		{
			m_email.requestKeyboardFocus();
		}
		else if(m_email.hasKeyboardFocus())
		{
			m_confirmEmail.requestKeyboardFocus();
		}
		else if(m_confirmEmail.hasKeyboardFocus())
		{
			m_username.requestKeyboardFocus();
		}
		else
			m_username.requestKeyboardFocus();
	}

	public void reloadStrings()
	{
		List<String> translated = Translator.translate("_LOGIN");
		setTitle(translated.get(9));
		m_u.setText(translated.get(5));
		m_p.setText(translated.get(6));
		m_cp.setText(translated.get(10));
		m_d.setText(translated.get(11));
		m_e.setText(translated.get(12));
		m_ce.setText(translated.get(13));
		m_s.setText(translated.get(14));
		m_g.setText(translated.get(15));
		m_male.setText(translated.get(16));
		m_female.setText(translated.get(17));
		m_tos.setText(translated.get(18));
		m_terms.setText(translated.get(19));
		m_register.setText(translated.get(8));
		m_cancel.setText(translated.get(20));
	}

	/**
	 * Cancels the registration
	 */
	private void cancel()
	{
		GameClient.getInstance().getLoginScreen().showLogin();
		m_register.setEnabled(true);
	}

	/**
	 * Generates starter buttons
	 */
	private void generateStarters()
	{
		String respath = System.getProperty("res.path");
		if(respath == null)
			respath = "";
		try
		{
			m_starters = new ImageButton[12];
			for(int i = 0; i < m_starters.length; i++)
			{
				String starterPath = "";
				switch(i)
				{
					case 0:
						starterPath = "res/pokemon/icons/001.png";
						break;
					case 1:
						starterPath = "res/pokemon/icons/152.png";
						break;
					case 2:
						starterPath = "res/pokemon/icons/252.png";
						break;
					case 3:
						starterPath = "res/pokemon/icons/387.png";
						break;
					case 4:
						starterPath = "res/pokemon/icons/004.png";
						break;
					case 5:
						starterPath = "res/pokemon/icons/155.png";
						break;
					case 6:
						starterPath = "res/pokemon/icons/255.png";
						break;
					case 7:
						starterPath = "res/pokemon/icons/390.png";
						break;
					case 8:
						starterPath = "res/pokemon/icons/007.png";
						break;
					case 9:
						starterPath = "res/pokemon/icons/158.png";
						break;
					case 10:
						starterPath = "res/pokemon/icons/258.png";
						break;
					case 11:
						starterPath = "res/pokemon/icons/393.png";
						break;
				}
				Image img = FileLoader.loadImage(respath + starterPath);
				m_starters[i] = new ImageButton(img);
				m_starters[i].setSize(22, 32);
				m_starters[i].setVisible(true);
			}

			m_starters[0].setPosition(170, 24 + 22);
			m_starters[0].addCallback(new Runnable()
			{
				@Override
				public void run()
				{
					m_starter = 1;
					selectStarter(m_starter);
				}
			});

			m_starters[1].setPosition(202, 24 + 22);
			m_starters[1].addCallback(new Runnable()
			{
				@Override
				public void run()
				{
					m_starter = 152;
					selectStarter(m_starter);
				}
			});

			m_starters[2].setPosition(234, 24 + 22);
			m_starters[2].addCallback(new Runnable()
			{
				@Override
				public void run()
				{
					m_starter = 252;
					selectStarter(m_starter);
				}
			});

			m_starters[3].setPosition(266, 24 + 22);
			m_starters[3].addCallback(new Runnable()
			{
				@Override
				public void run()
				{
					m_starter = 387;
					selectStarter(m_starter);
				}
			});

			m_starters[4].setPosition(170, 56 + 22);
			m_starters[4].addCallback(new Runnable()
			{
				@Override
				public void run()
				{
					m_starter = 4;
					selectStarter(m_starter);
				}
			});

			m_starters[5].setPosition(202, 56 + 22);
			m_starters[5].addCallback(new Runnable()
			{
				@Override
				public void run()
				{
					m_starter = 155;
					selectStarter(m_starter);
				}
			});

			m_starters[6].setPosition(234, 56 + 22);
			m_starters[6].addCallback(new Runnable()
			{
				@Override
				public void run()
				{
					m_starter = 255;
					selectStarter(m_starter);
				}
			});

			m_starters[7].setPosition(266, 56 + 22);
			m_starters[7].addCallback(new Runnable()
			{
				@Override
				public void run()
				{
					m_starter = 390;
					selectStarter(m_starter);
				}
			});

			m_starters[8].setPosition(170, 88 + 22);
			m_starters[8].addCallback(new Runnable()
			{
				@Override
				public void run()
				{
					m_starter = 7;
					selectStarter(m_starter);
				}
			});

			m_starters[9].setPosition(202, 88 + 22);
			m_starters[9].addCallback(new Runnable()
			{
				@Override
				public void run()
				{
					m_starter = 158;
					selectStarter(m_starter);
				}
			});

			m_starters[10].setPosition(234, 88 + 22);
			m_starters[10].addCallback(new Runnable()
			{
				@Override
				public void run()
				{
					m_starter = 258;
					selectStarter(m_starter);
				}
			});

			m_starters[11].setPosition(266, 88 + 22);
			m_starters[11].addCallback(new Runnable()
			{
				@Override
				public void run()
				{
					m_starter = 393;
					selectStarter(m_starter);
				}
			});

			for(ImageButton i : m_starters)
			{
				i.setImagePosition(-4, -4);
				pane.add(i);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Returns true if the email is a valid email address
	 * 
	 * @param email
	 * @return
	 */
	private boolean isValidEmail(String email)
	{
		String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		if(pattern.matcher(email).matches())
			return true;
		else
			return false;
	}

	/**
	 * Registers the player
	 */
	private void register()
	{
		List<String> translated = Translator.translate("_LOGIN");
		if(m_username.getText() != null && m_username.getText().length() >= 4 && m_username.getText().length() <= 12)
		{
			if(m_password.getText() != null & !m_password.getText().equalsIgnoreCase("") && m_confirmPass.getText() != null && !m_confirmPass.getText().equalsIgnoreCase("")
					&& m_password.getText().compareTo(m_confirmPass.getText()) == 0)
			{
				if(m_email.getText() != null && isValidEmail(m_email.getText()) && m_confirmEmail.getText() != null && m_confirmEmail.getText().compareTo(m_email.getText()) == 0)
				{
					if(m_day.getText() != null && m_day.getText().length() > 0 && m_day.getText().length() < 3 && m_month.getText() != null && m_month.getText().length() > 0
							&& m_month.getText().length() < 3 && m_year.getText() != null && m_year.getText().length() == 4)
					{
						if(m_starter != 0)
						{
							if(!m_terms.isEnabled())
							{
								m_register.setEnabled(false);
								String bday = m_day.getText() + "/" + m_month.getText() + "/" + m_year.getText();
								GameClient.getInstance().getUserManager().register(m_username.getText(), m_password.getText(), m_email.getText(), bday, m_starter, m_gender == 0 ? 11 : 20, m_region);
							}
							else
							{
								GameClient.getInstance().showMessageDialog(translated.get(28));
							}
						}
						else
						{
							GameClient.getInstance().showMessageDialog("No starter selected");
						}
					}
					else
					{
						GameClient.getInstance().showMessageDialog(translated.get(29));
					}
				}
				else
				{
					GameClient.getInstance().showMessageDialog(translated.get(30));
				}
			}
			else
			{
				GameClient.getInstance().showMessageDialog(translated.get(31));
			}
		}
		else
		{
			GameClient.getInstance().showMessageDialog(translated.get(32));
		}
	}

	/**
	 * Called when a starter is selected (disables the appropriate button)
	 * 
	 * @param m_starter
	 */
	private void selectStarter(int m_starter)
	{
		for(int i = 0; i < m_starters.length; i++)
			m_starters[i].setEnabled(true);
		switch(m_starter)
		{
			case 1:
				m_starters[0].setEnabled(false);
				break;
			case 4:
				m_starters[4].setEnabled(false);
				break;
			case 7:
				m_starters[8].setEnabled(false);
				break;
			case 152:
				m_starters[1].setEnabled(false);
				break;
			case 155:
				m_starters[5].setEnabled(false);
				break;
			case 158:
				m_starters[9].setEnabled(false);
				break;
			case 252:
				m_starters[2].setEnabled(false);
				break;
			case 255:
				m_starters[6].setEnabled(false);
				break;
			case 258:
				m_starters[10].setEnabled(false);
				break;
			case 387:
				m_starters[3].setEnabled(false);
				break;
			case 390:
				m_starters[7].setEnabled(false);
				break;
			case 393:
				m_starters[11].setEnabled(false);
				break;
		}
	}

	/* Resets the registration dialog. Called when registration is successfull. */
	public void clear()
	{
		// Enable all starters
		for(int i = 0; i < m_starters.length; i++)
			m_starters[i].setEnabled(true);

		m_confirmEmail.setText("");
		m_email.setText("");
		m_username.setText("");
		m_confirmPass.setText("");
		m_password.setText("");
		m_day.setText("");
		m_month.setText("");
		m_year.setText("");
		m_register.setEnabled(true);
		m_regions[0].setEnabled(true);
		m_regions[1].setEnabled(true);
		m_regions[2].setEnabled(false);
		m_regions[3].setEnabled(false);
		m_terms.setEnabled(true);
		m_male.setEnabled(true);
		m_female.setEnabled(true);
	}
}