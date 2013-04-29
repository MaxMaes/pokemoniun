package org.pokenet.client.twl.ui.frames;

import org.pokenet.client.GameClient;
import org.pokenet.client.backend.FileLoader;
import org.pokenet.client.backend.entity.OurPokemon;
import org.pokenet.client.backend.entity.Pokemon;
import org.pokenet.client.constants.ServerPacket;
import org.pokenet.client.protocol.ClientMessage;
import org.pokenet.client.ui.base.ConfirmationDialog;
import org.pokenet.client.ui.base.TWLImageButton;

import de.matthiasmann.twl.Button;
import de.matthiasmann.twl.EditField;
import de.matthiasmann.twl.Label;
import de.matthiasmann.twl.ResizableFrame;

/**
 * The trade interface
 * 
 * @author ZombieBear
 */
public class TradeDialog extends ResizableFrame
{
	private Button m_cancelBtn;
	private ConfirmationDialog m_confirm;
	private boolean m_madeOffer = false;
	private Button m_makeOfferBtn;
	private Runnable m_offerListener;
	private int m_offerNum = 6;
	private Label m_ourCashLabel;
	private EditField m_ourMoneyOffer;
	private TWLImageButton[] m_ourPokes;
	private boolean m_receivedOffer = false;
	private Label m_theirMoneyOffer;
	private PokemonInfoDialog[] m_theirPokeInfo;
	private TWLImageButton[] m_theirPokes;
	private Button m_tradeBtn;

	/**
	 * Default constructor
	 */
	public TradeDialog(String trainerName)
	{
		initGUI();
		setVisible(true);
		setTitle("Trade with " + trainerName);
		setCenter();
	}

	/**
	 * Adds a pokemon to the other player's side
	 * 
	 * @param data
	 */
	public void addPoke(final int index, String[] data)
	{
		final int j = index;
		int ic = Integer.parseInt(data[0]);
		if(ic > 389)
			ic -= 2;
		else
			ic++;

		m_theirPokes[index].setImage(FileLoader.loadImage(Pokemon.getIconPathByIndex(ic)));

		// Load pokemon data
		OurPokemon tempPoke = new OurPokemon().initTradePokemon(data);

		// Create a pokemon information panel with stats
		// for informed decisions during trade
		m_theirPokeInfo[index] = new PokemonInfoDialog(tempPoke);
		m_theirPokeInfo[index].setVisible(false);
		m_theirPokeInfo[index].setPosition(m_theirPokes[index].getX(), m_theirPokes[index].getY() + 32);
		//GameClient.getInstance().getDisplay().add(m_theirPokeInfo[index]);
		m_theirPokes[index].getModel().addStateCallback(new Runnable()
		{
			@Override
			public void run()
			{
				if(m_theirPokes[index].getModel().isHover())
				{
					m_theirPokeInfo[j].setVisible(true);
				} else {
					m_theirPokeInfo[j].setVisible(false);
				}
			}
		});
		add(m_theirPokes[index]);
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
		int height = (int) GameClient.getInstance().getDisplay().getHeight();
		int width = (int) GameClient.getInstance().getDisplay().getWidth();
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
				// GameClient.getInstance().getPacketGenerator().writeTcpMessage("38");
				ClientMessage message = new ClientMessage(ServerPacket.TRADE_CANCEL);
				//message.addInt(m_offerNum);
				//message.addInt(Integer.parseInt(m_ourMoneyOffer.getText()));
				GameClient.getInstance().getSession().send(message);
				m_confirm.setVisible(false);
				//removeChild(m_confirm); TODO:
				m_confirm = null;
				setVisible(false);
				GameClient.getInstance().getUi().stopTrade();
				System.out.println("Trade Cancelled");
			}

		};
		Runnable no = new Runnable()
		{
			@Override
			public void run()
			{
				//removeChild(m_confirm); TODO:
				m_confirm = null;
			}
		};
		//m_confirm = new ConfirmationDialog("Are you sure you want to cancel the trade?", yes, no); TODO: Waiting for Chappie
	}

	/**
	 * Initializes the interface
	 */
	private void initGUI()
	{
		m_ourPokes = new TWLImageButton[6];
		m_theirPokes = new TWLImageButton[6];
		m_theirPokeInfo = new PokemonInfoDialog[6];
		m_ourMoneyOffer = new EditField();
		m_makeOfferBtn = new Button();
		m_tradeBtn = new Button();
		m_cancelBtn = new Button();

		// Action Listener for the offer button
		m_offerListener = new Runnable()
		{
			@Override
			public void run()
			{
				if(m_makeOfferBtn.getText().equalsIgnoreCase("Make Offer"))
				{
					if(m_ourMoneyOffer.getText().equals("") || m_ourMoneyOffer.getText() == null)
						m_ourMoneyOffer.setText("0");
					makeOffer();
				}
				else
					cancelOffer();
			}
		};

		int x = 10, y = 10;
		for(int i = 0; i < 6; i++)
		{
			// Show Our Pokemon for Trade
			m_ourPokes[i] = new TWLImageButton(FileLoader.toTWLImage(GameClient.getInstance().getOurPlayer().getPokemon()[i].getIcon(), true));
			m_ourPokes[i].setSize(32, 32);
			m_ourPokes[i].setVisible(true);
			add(m_ourPokes[i]);
			if(i < 3)
				m_ourPokes[i].setPosition(x, y);
			else
				m_ourPokes[i].setPosition(x + 40, y);
			// Show the Other Character's Pokemon for Trade
			m_theirPokes[i] = new TWLImageButton();
			m_theirPokes[i].setSize(32, 32);
			m_theirPokes[i].setVisible(true);
			add(m_theirPokes[i]);
			// Item Location Algorithms
			if(i < 3)
				m_theirPokes[i].setPosition(x + 178, y);
			else
				m_theirPokes[i].setPosition(x + 218, y);
			if(i == 2)
				y = 10;
			else
				y += 40;
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
		m_makeOfferBtn.setSize(90, 30);
		m_makeOfferBtn.setPosition(90, 10);
		m_makeOfferBtn.setEnabled(false);
		m_makeOfferBtn.addCallback(m_offerListener);
		add(m_makeOfferBtn);
		m_tradeBtn.setText("Trade");
		m_tradeBtn.setEnabled(false);
		m_tradeBtn.setSize(90, 30);
		m_tradeBtn.setPosition(90, 50);
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
						//removeChild(m_confirm); TODO:
						m_confirm = null;
						setVisible(false);
					}

				};
				Runnable no = new Runnable()
				{
					@Override
					public void run()
					{
						m_confirm.setVisible(false);
						//remove(m_confirm); TODO: 
						m_confirm = null;
						setVisible(true);
					}

				};
				//m_confirm = new ConfirmationDialog("Are you sure you want to trade?", yes, no); TODO: Chappie
				setVisible(false);
			}
		});
		add(m_tradeBtn);
		m_cancelBtn.setText("Cancel Trade");
		m_cancelBtn.setSize(90, 30);
		m_cancelBtn.setPosition(90, 90);
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
		m_ourCashLabel.setPosition(10, 130);
		add(m_ourCashLabel);
		m_ourMoneyOffer = new EditField();
		m_ourMoneyOffer.setSize(60, 20);
		m_ourMoneyOffer.setPosition(20, 128);
		add(m_ourMoneyOffer);
		// Their money trade info
		m_theirMoneyOffer = new Label("$0");
		m_theirMoneyOffer.setPosition(188, 130);
		add(m_theirMoneyOffer);
		// Window Settings
		setSize(270, 178);
		setResizableAxis(ResizableAxis.NONE);
	}

	/**
	 * Sends the offer to the server
	 */
	private void makeOffer()
	{
		if(m_ourMoneyOffer.getText().equals(""))
			m_ourMoneyOffer.setText("0");

		if(!m_ourMoneyOffer.getText().equals(""))
		{
			// GameClient.getInstance().getPacketGenerator().writeTcpMessage("35" + m_offerNum + "," + m_ourMoneyOffer.getText());
			ClientMessage message = new ClientMessage(ServerPacket.TRADE_OFFER);
			message.addInt(m_offerNum);
			message.addInt(Integer.parseInt(m_ourMoneyOffer.getText()));
			GameClient.getInstance().getSession().send(message);
		}
		else
		{
			// GameClient.getInstance().getPacketGenerator().writeTcpMessage("35" + m_offerNum + ",0");
			ClientMessage message = new ClientMessage(ServerPacket.TRADE_OFFER);
			message.addInt(m_offerNum);
			message.addInt(0);
			GameClient.getInstance().getSession().send(message);
		}
		m_makeOfferBtn.setText("Cancel Offer");
		m_madeOffer = true;
		if(m_receivedOffer)
			m_tradeBtn.setEnabled(true);
	}

	/**
	 * Performs the trade
	 */
	private void performTrade()
	{
		// GameClient.getInstance().getPacketGenerator().writeTcpMessage("36");
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
			if(i != btnIndex)
			{
				m_ourPokes[i].setEnabled(true);
			}
			else
			{
				m_ourPokes[btnIndex].setEnabled(false);
			}
	}
	
	public void forceCancelTrade()
	{
		setVisible(false);
		GameClient.getInstance().getUi().stopTrade();
		System.out.println("Trade Cancelled");
	}
}
