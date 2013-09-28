package org.pokemonium.client.ui.frames;

import org.pokemonium.client.GameClient;
import org.pokemonium.client.backend.PokemonSpriteDatabase;
import org.pokemonium.client.backend.entity.OurPokemon;
import org.pokemonium.client.constants.ServerPacket;
import org.pokemonium.client.protocol.ClientMessage;
import org.pokemonium.client.ui.components.ImageButton;
import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.ResizableFrame;

/**
 * The trade interface
 * 
 * @author Myth1c
 */
public class TradeDialog extends ResizableFrame
{
	private Button m_cancelBtn;
	private boolean m_madeOffer = false;
	private Button m_makeOfferBtn;
	private Runnable m_offerListener;
	private int m_offerNum = 6;
	private Label m_ourCashLabel;
	private EditField m_ourMoneyOffer;
	private ImageButton[] m_ourPokes;
	private boolean m_receivedOffer = false;
	private Label m_theirMoneyOffer;
	private PokemonInfoDialog[] m_theirPokeInfo;
	private ImageButton[] m_theirPokes;
	private Button m_tradeBtn;

	/**
	 * Default constructor
	 */
	public TradeDialog(String trainerName)
	{
		initGUI();
		setPlayerData();
		setVisible(true);
		setTitle("Trade with " + trainerName);
		setCenter();
	}

	/**
	 * Adds a pokemon to the other player's side
	 * 
	 * @param data
	 */
	public void addPoke(final int index, final String[] data)
	{
		final int iconID = Integer.parseInt(data[0]);
		/* if(iconID > 389){iconID -= 2;}else{iconID++;} */
		GameClient.getInstance().getGUI().invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				m_theirPokes[index].setImage(PokemonSpriteDatabase.getIcon(iconID));
				// Load pokemon data
				OurPokemon tempPoke = new OurPokemon().initTradePokemon(data);

				// Create a pokemon information panel with stats for informed decisions during trade
				m_theirPokeInfo[index] = new PokemonInfoDialog(tempPoke, 6);
				m_theirPokes[index].setTooltipContent(m_theirPokeInfo[index]);
				// Commented because line above should do the same, to be tested /*m_theirPokes[index].getModel().addStateCallback(new Runnable(){@Override public void run(){if(m_theirPokes[index].getModel().isHover()){m_theirPokeInfo[j].setVisible(true);} else{m_theirPokeInfo[j].setVisible(false);}}});*/
				add(m_theirPokes[index]);
			}
		});
	}

	/**
	 * Updates the UI when the other player cancels his/her offer
	 */
	public void cancelTheirOffer()
	{
		for(int i = 0; i < 6; i++)
			m_theirPokes[i].setEnabled(false);
		m_theirMoneyOffer.setText("$0");
		m_tradeBtn.setEnabled(false);
	}

	/**
	 * Receives an offer
	 * 
	 * @param index
	 * @param cash
	 */
	public void getOffer(int index, int cash)
	{
		for(int i = 0; i < 6; i++)
		{
			m_theirPokes[i].setEnabled(false);
		}
		if(index < 6)
		{
			m_theirPokes[index].setEnabled(true);
		}
		m_theirMoneyOffer.setText("$" + cash);
		m_receivedOffer = true;
		if(m_madeOffer)
			m_tradeBtn.setEnabled(true);
	}

	/**
	 * Centers the frame
	 */
	public void setCenter()
	{
		int height = (int) GameClient.getInstance().getGUIPane().getHeight();
		int width = (int) GameClient.getInstance().getGUIPane().getWidth();
		int x = width / 2 - (int) getWidth() / 2;
		int y = height / 2 - (int) getHeight() / 2;
		this.setPosition(x, y);
	}

	/**
	 * Cancels a sent offer
	 */
	private void cancelOffer()
	{
		// GameClient.getInstance().getPacketGenerator().writeTcpMessage("37");
		ClientMessage message = new ClientMessage(ServerPacket.CANCEL_OFFER);
		GameClient.getInstance().getSession().send(message);
		m_makeOfferBtn.setText("Make Offer");

		m_tradeBtn.setEnabled(false);
	}

	public void cancelOurOffer()
	{
		m_makeOfferBtn.setText("Make Offer");
		m_tradeBtn.setEnabled(false);
	}

	/**
	 * Cancels the trade
	 */
	private void cancelTrade()
	{
		Runnable yes = new Runnable()
		{
			@Override
			public void run()
			{
				ClientMessage message = new ClientMessage(ServerPacket.TRADE_CANCEL);
				// message.addInt(m_offerNum);
				// message.addInt(Integer.parseInt(m_ourMoneyOffer.getText()));
				GameClient.getInstance().getSession().send(message);
				GameClient.getInstance().getGUIPane().hideConfirmationDialog();
				setVisible(false);
				GameClient.getInstance().getGUIPane().getHUD().removeTradeDialog();
				System.out.println("Trade Cancelled");
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
		GameClient.getInstance().getGUIPane().showConfirmationDialog("Are you sure you want to cancel the trade?", yes, no);
	}

	/**
	 * Initializes the interface
	 */
	private void initGUI()
	{
		m_ourPokes = new ImageButton[6];
		m_theirPokes = new ImageButton[6];
		m_theirPokeInfo = new PokemonInfoDialog[6];
		m_ourMoneyOffer = new EditField();
		m_makeOfferBtn = new Button();
		m_makeOfferBtn.setCanAcceptKeyboardFocus(false);
		m_tradeBtn = new Button();
		m_tradeBtn.setCanAcceptKeyboardFocus(false);
		m_cancelBtn = new Button();
		m_cancelBtn.setCanAcceptKeyboardFocus(false);

		// Action Listener for the offer button
		m_offerListener = new Runnable()
		{
			@Override
			public void run()
			{
				if(m_makeOfferBtn.getText().equalsIgnoreCase("Make Offer"))
				{
					if(m_ourMoneyOffer.getText().equals("") || m_ourMoneyOffer.getText() == null)
					{
						m_ourMoneyOffer.setText("0");
					}
					makeOffer();
				}
				else
				{
					cancelOffer();
				}
			}
		};

		for(int i = 0; i < 6; i++)
		{
			// Show Our Pokemon for Trade
			m_ourPokes[i] = new ImageButton();
			m_ourPokes[i].setCanAcceptKeyboardFocus(false);
			m_ourPokes[i].setVisible(true);
			add(m_ourPokes[i]);

			// Show the Other Character's Pokemon for Trade
			m_theirPokes[i] = new ImageButton();
			m_theirPokes[i].setCanAcceptKeyboardFocus(false);
			m_theirPokes[i].setVisible(true);
			add(m_theirPokes[i]);
		}
		m_ourPokes[0].addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				if(m_offerNum == 0)
				{
					m_offerNum = 6;
					untoggleOthers(6);
				}
				else
				{
					m_offerNum = 0;
					untoggleOthers(0);
				}
				m_makeOfferBtn.setEnabled(true);

			};
		});
		m_ourPokes[1].addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				if(m_offerNum == 1)
				{
					m_offerNum = 6;
					untoggleOthers(6);
				}
				else
				{
					m_offerNum = 1;
					untoggleOthers(1);
				}
				m_makeOfferBtn.setEnabled(true);

			};
		});
		m_ourPokes[2].addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				if(m_offerNum == 2)
				{
					m_offerNum = 6;
					untoggleOthers(6);
				}
				else
				{
					m_offerNum = 2;
					untoggleOthers(2);
				}
				m_makeOfferBtn.setEnabled(true);

			};
		});
		m_ourPokes[3].addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				if(m_offerNum == 3)
				{
					m_offerNum = 6;
					untoggleOthers(6);
				}
				else
				{
					m_offerNum = 3;
					untoggleOthers(3);
				}
				m_makeOfferBtn.setEnabled(true);

			};
		});
		m_ourPokes[4].addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				if(m_offerNum == 4)
				{
					m_offerNum = 6;
					untoggleOthers(6);
				}
				else
				{
					m_offerNum = 4;
					untoggleOthers(4);
				}

				m_makeOfferBtn.setEnabled(true);
			};
		});
		m_ourPokes[5].addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				if(m_offerNum == 5)
				{
					m_offerNum = 6;
					untoggleOthers(6);
				}
				else
				{
					m_offerNum = 5;
					untoggleOthers(5);
				}

				m_makeOfferBtn.setEnabled(true);
			};
		});
		// UI Buttons
		m_makeOfferBtn.setText("Make Offer");
		m_makeOfferBtn.setEnabled(false);
		m_makeOfferBtn.addCallback(m_offerListener);
		add(m_makeOfferBtn);
		m_tradeBtn.setText("Trade");
		m_tradeBtn.setEnabled(false);
		m_tradeBtn.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				Runnable yes = new Runnable()
				{
					@Override
					public void run()
					{
						performTrade();
						GameClient.getInstance().getGUIPane().hideConfirmationDialog();
						setVisible(false);
					}

				};
				Runnable no = new Runnable()
				{
					@Override
					public void run()
					{
						GameClient.getInstance().getGUIPane().hideConfirmationDialog();
						setVisible(true);
					}

				};
				GameClient.getInstance().getGUIPane().showConfirmationDialog("Are you sure you want to trade?", yes, no);
				setVisible(false);
			}
		});
		add(m_tradeBtn);
		m_cancelBtn.setText("Cancel Trade");
		m_cancelBtn.addCallback(new Runnable()
		{
			@Override
			public void run()
			{
				cancelTrade();
			};
		});
		add(m_cancelBtn);

		// Our money trade info
		m_ourCashLabel = new Label("$");
		add(m_ourCashLabel);

		m_ourMoneyOffer = new EditField();
		add(m_ourMoneyOffer);

		// Their money trade info
		m_theirMoneyOffer = new Label("$0");
		add(m_theirMoneyOffer);

		// Window Settings
		setResizableAxis(ResizableAxis.NONE);
	}

	private void setPlayerData()
	{
		for(int i = 0; i < GameClient.getInstance().getOurPlayer().getPartyCount(); i++)
		{
			m_ourPokes[i].setImage(GameClient.getInstance().getOurPlayer().getPokemon()[i].getIcon());
		}
	}

	/**
	 * Sends the offer to the server
	 */
	private void makeOffer()
	{
		if(m_ourMoneyOffer.getText().equals(""))
		{
			m_ourMoneyOffer.setText("0");
		}
		if(!m_ourMoneyOffer.getText().equals(""))
		{
			ClientMessage message = new ClientMessage(ServerPacket.TRADE_OFFER);
			message.addInt(m_offerNum);
			message.addInt(Integer.parseInt(m_ourMoneyOffer.getText()));
			GameClient.getInstance().getSession().send(message);
		}
		else
		{
			ClientMessage message = new ClientMessage(ServerPacket.TRADE_OFFER);
			message.addInt(m_offerNum);
			message.addInt(0);
			GameClient.getInstance().getSession().send(message);
		}
		m_makeOfferBtn.setText("Cancel Offer");
		m_madeOffer = true;
		if(m_receivedOffer)
		{
			m_tradeBtn.setEnabled(true);
		}
	}

	/**
	 * Performs the trade
	 */
	private void performTrade()
	{
		ClientMessage message = new ClientMessage(ServerPacket.TRADE_ACCEPTED);
		GameClient.getInstance().getSession().send(message);
		System.out.println("Trade complete");
		setVisible(false);
	}

	/**
	 * Allows only one pokemon to be toggled
	 * 
	 * @param btnIndex
	 */
	private void untoggleOthers(int btnIndex)
	{
		for(int i = 0; i < 6; i++)
		{
			if(i != btnIndex)
			{
				m_ourPokes[i].setEnabled(true);
			}
			else
			{
				m_ourPokes[btnIndex].setEnabled(false);
			}
		}
	}

	public void forceCancelTrade()
	{
		setVisible(false);
		GameClient.getInstance().getHUD().removeTradeDialog();
		System.out.println("Trade Cancelled");
	}

	@Override
	public void layout()
	{
		int x = getInnerX() + 10, y = getInnerY() + 10;
		for(int i = 0; i < 6; i++)
		{
			m_ourPokes[i].setSize(32, 32);
			if(i < 3)
			{
				m_ourPokes[i].setPosition(x, y);
			}
			else
			{
				m_ourPokes[i].setPosition(x + 40, y);
			}
			m_theirPokes[i].setSize(32, 32);
			if(i < 3)
			{
				m_theirPokes[i].setPosition(x + 178, y);
			}
			else
			{
				m_theirPokes[i].setPosition(x + 218, y);
			}
			if(i == 2)
			{
				y = getInnerY() + 10;
			}
			else
			{
				y += 40;
			}
		}

		m_makeOfferBtn.setSize(90, 30);
		m_makeOfferBtn.setPosition(getInnerX() + 90, getInnerY() + 10);
		m_tradeBtn.setSize(90, 30);
		m_tradeBtn.setPosition(getInnerX() + 90, getInnerY() + 50);
		m_cancelBtn.setSize(90, 30);
		m_cancelBtn.setPosition(getInnerX() + 90, getInnerY() + 90);
		m_ourCashLabel.setPosition(getInnerX() + 10, getInnerY() + 130);
		m_ourMoneyOffer.setSize(60, 20);
		m_ourMoneyOffer.setPosition(getInnerX() + 20, getInnerY() + 128);
		m_theirMoneyOffer.setPosition(getInnerX() + 188, getInnerY() + 130);
		setSize(270, 178);
		setCenter();
	}
}
