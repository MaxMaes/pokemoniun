package org.pokenet.server.battle.impl;

import org.pokenet.server.backend.entity.NPC;
import org.pokenet.server.backend.entity.Player;
import org.pokenet.server.battle.BattleField;
import org.pokenet.server.battle.BattleTurn;
import org.pokenet.server.battle.Pokemon;
import org.pokenet.server.battle.mechanics.BattleMechanics;
import org.pokenet.server.battle.mechanics.MoveQueueException;
import org.pokenet.server.battle.mechanics.statuses.BurnEffect;
import org.pokenet.server.battle.mechanics.statuses.FreezeEffect;
import org.pokenet.server.battle.mechanics.statuses.ParalysisEffect;
import org.pokenet.server.battle.mechanics.statuses.PoisonEffect;
import org.pokenet.server.battle.mechanics.statuses.SleepEffect;
import org.pokenet.server.battle.mechanics.statuses.StatusEffect;
import org.pokenet.server.battle.mechanics.statuses.field.FieldEffect;
import org.pokenet.server.battle.mechanics.statuses.field.HailEffect;
import org.pokenet.server.battle.mechanics.statuses.field.RainEffect;
import org.pokenet.server.battle.mechanics.statuses.field.SandstormEffect;
import org.pokenet.server.feature.TimeService;
import org.pokenet.server.protocol.ServerMessage;

/**
 * A battlefield for NPC battles
 * 
 * @author shadowkanji
 */
public class NpcBattleField extends BattleField
{
	private boolean m_finished = false;
	private NPC m_npc;
	private Player m_player;
	private BattleTurn[] m_turn = new BattleTurn[2];

	/**
	 * Constructor
	 * 
	 * @param mech
	 * @param p
	 * @param n
	 */
	public NpcBattleField(BattleMechanics mech, Player p, NPC n)
	{
		super(mech, new Pokemon[][] { p.getParty(), n.getParty(p) });
		/* Store the player and npc */
		m_player = p;
		m_npc = n;

		/* Start the battle */
		// TcpProtocolHandler.writeMessage(p.getTcpSession(), new BattleInitMessage(false, getAliveCount(1)));
		m_player.ensureHealthyPokemon();
		ServerMessage startBattle = new ServerMessage(m_player.getSession());
		startBattle.Init(18);
		startBattle.addBool(false);
		startBattle.addInt(getAliveCount(1));
		startBattle.sendResponse();
		/* Check if this player has seen this wild pokemon before, if not, update pokedex */
		if(!m_player.isPokemonSeen(getActivePokemon()[1].getPokemonNumber() + 1))
			m_player.setPokemonSeen(getActivePokemon()[1].getPokemonNumber() + 1);
		/* Send enemy name */
		// m_player.getTcpSession().write("bn" + m_npc.getName());
		ServerMessage enemyName = new ServerMessage(m_player.getSession());
		enemyName.Init(22);
		enemyName.addString(m_npc.getName());
		enemyName.sendResponse();
		/* Send enemy's Pokemon data */
		sendPokemonData(p);
		/* Set the player's battle id */
		m_player.setBattleId(0);
		
		/* Apply weather and request moves */
		applyWeather();
		requestMoves();
	}

	@Override
	public void applyWeather()
	{
		if(m_player.getMap().isWeatherForced())
			switch(m_player.getMap().getWeather())
			{
				case NORMAL:
					return;
				case RAIN:
					applyEffect(new RainEffect());
					return;
				case HAIL:
					applyEffect(new HailEffect());
					return;
				case SANDSTORM:
					applyEffect(new SandstormEffect());
					return;
				default:
					return;
			}
		else
		{
			FieldEffect f = TimeService.getWeatherEffect();
			if(f != null)
				applyEffect(f);
		}
	}

	@Override
	public void clearQueue()
	{
		m_turn[0] = null;
		m_turn[1] = null;
	}

	@Override
	public void executeItemTurn(int i)
	{
		if(m_turn[0] == null)
			m_turn[0] = BattleTurn.getItemTurn(i);
		if(m_turn[1] == null)
			m_turn[1] = BattleTurn.getItemTurn(i);
		executeTurn(m_turn);
	}

	@Override
	public void forceExecuteTurn()
	{
		if(m_turn[0] == null)
			m_turn[0] = BattleTurn.getMoveTurn(-1);
		if(m_turn[1] == null)
			m_turn[1] = BattleTurn.getMoveTurn(-1);
		executeTurn(m_turn);
	}

	@Override
	public BattleTurn[] getQueuedTurns()
	{
		return m_turn;
	}

	@Override
	public String getTrainerName(int idx)
	{
		if(idx == 0)
			return m_player.getName();
		else
			return m_npc.getName();
	}

	@Override
	public void informPokemonFainted(int trainer, int idx)
	{
		if(m_player != null)
		{
			// TcpProtocolHandler.writeMessage(m_player.getTcpSession(), new FaintMessage(getParty(trainer)[idx].getSpeciesName()));
			ServerMessage informFaint = new ServerMessage(m_player.getSession());
			informFaint.Init(25);
			informFaint.addString(getParty(trainer)[idx].getSpeciesName());
			informFaint.sendResponse();
		}
	}

	@Override
	public void informPokemonHealthChanged(Pokemon poke, int change)
	{
		if(m_player != null)
			if(getActivePokemon()[0] == poke)
			{
				// TcpProtocolHandler.writeMessage(m_player.getTcpSession(), new HealthChangeMessage(0, change));
				ServerMessage informHealth = new ServerMessage(m_player.getSession());
				informHealth.Init(33);
				informHealth.addInt(0);
				informHealth.addString("0," + change);
				informHealth.sendResponse();
			}
			else if(getActivePokemon()[1] == poke)
			{
				// TcpProtocolHandler.writeMessage(m_player.getTcpSession(), new HealthChangeMessage(1, change));
				ServerMessage informHealth = new ServerMessage(m_player.getSession());
				informHealth.Init(33);
				informHealth.addInt(1);
				informHealth.addString("1," + change);
				informHealth.sendResponse();
			}
			else
			{
				int index = getPokemonPartyIndex(0, poke);
				if(index > -1)
				{
					// m_player.getTcpSession().write("Ph" + String.valueOf(index) + poke.getHealth());
					ServerMessage informHealth = new ServerMessage(m_player.getSession());
					informHealth.Init(45);
					informHealth.addInt(index);
					informHealth.addInt(poke.getHealth());
					informHealth.sendResponse();
					return;
				}
				// TODO: Add support for NPC pokemon healing for pokemon in pokeballs
			}
	}

	@SuppressWarnings("unused")
	@Override
	//TODO check this code with checking moves
	public void informStatusApplied(Pokemon pokemon, StatusEffect eff)
	{
		if(m_finished)
			return;
		
			
		if(m_player != null)
			if(getActivePokemon()[0].equals(pokemon))
			{
				// TcpProtocolHandler.writeMessage(m_player.getTcpSession(), new StatusChangeMessage(0, pokemon.getSpeciesName(), eff.getName(), false));
				ServerMessage receiveEffect = new ServerMessage(m_player.getSession());
				receiveEffect.Init(29);
				receiveEffect.addInt(0);
				receiveEffect.addString(pokemon.getSpeciesName());
				if(eff == null)
					receiveEffect.addString("");
				else
					receiveEffect.addString(eff.getName());
				receiveEffect.sendResponse();
			}
			else if(pokemon.equals(getActivePokemon()[1]))
			{
				// TcpProtocolHandler.writeMessage(m_player.getTcpSession(), new StatusChangeMessage(1, pokemon.getSpeciesName(), eff.getName(), false));
				ServerMessage receiveEffect = new ServerMessage(m_player.getSession());
				receiveEffect.Init(29);
				receiveEffect.addInt(1);
				receiveEffect.addString(pokemon.getSpeciesName());
//				System.out.println(pokemon.getSpeciesName() + ", " + eff.getName());
				if(eff.getName() == null)
					receiveEffect.addString("");
				else
					receiveEffect.addString(eff.getName());
				receiveEffect.sendResponse();
			}
	}

	@Override
	public void informStatusRemoved(Pokemon pokemon, StatusEffect eff)
	{
		if(m_finished)
			return;
		if(m_player != null)
			if(getActivePokemon()[0].equals(pokemon) && !getActivePokemon()[0].isFainted())
			{
				// TcpProtocolHandler.writeMessage(m_player.getTcpSession(), new StatusChangeMessage(0, pokemon.getSpeciesName(), eff.getName(), true));
				ServerMessage removeEffect = new ServerMessage(m_player.getSession());
				removeEffect.Init(30);
				removeEffect.addInt(0);
				removeEffect.addString(pokemon.getSpeciesName());
				if(eff.getName() == null)
					removeEffect.addString("");
				else
					removeEffect.addString(eff.getName());
				removeEffect.sendResponse();
			}
			else if(pokemon.equals(getActivePokemon()[1]) && !getActivePokemon()[1].isFainted())
			{
				// TcpProtocolHandler.writeMessage(m_player.getTcpSession(), new StatusChangeMessage(1, pokemon.getSpeciesName(), eff.getName(), true));
				ServerMessage removeEffect = new ServerMessage(m_player.getSession());
				removeEffect.Init(30);
				removeEffect.addInt(1);
				removeEffect.addString(pokemon.getSpeciesName());
				if(eff.getName() == null)
					removeEffect.addString("");
				else
					removeEffect.addString(eff.getName());
				removeEffect.sendResponse();
			}
	}

	@Override
	public void informSwitchInPokemon(int trainer, Pokemon poke)
	{
		if(m_player != null)
			if(trainer == 0)
			{
				// TcpProtocolHandler.writeMessage(m_player.getTcpSession(), new SwitchMessage(m_player.getName(), poke.getSpeciesName(), trainer, getPokemonPartyIndex(trainer, poke)));
				ServerMessage switchInform = new ServerMessage(m_player.getSession());
				switchInform.Init(32);
				switchInform.addString(m_player.getName());
				switchInform.addString(poke.getSpeciesName());
				switchInform.addInt(trainer);
				switchInform.addInt(getPokemonPartyIndex(trainer, poke));
				switchInform.sendResponse();
				
				ServerMessage receiveEffect = new ServerMessage(m_player.getSession());
				receiveEffect.Init(29);
				receiveEffect.addInt(0);
				receiveEffect.addString(poke.getSpeciesName());
				
				if(poke.hasEffect(BurnEffect.class))
					receiveEffect.addString("Burn");
				else if(poke.hasEffect(FreezeEffect.class))
					receiveEffect.addString("Freeze");
				else if(poke.hasEffect(ParalysisEffect.class))
					receiveEffect.addString("paralysis");
				else if(poke.hasEffect(PoisonEffect.class))
					receiveEffect.addString("Poison");
				else if(poke.hasEffect(SleepEffect.class))
					receiveEffect.addString("Sleep");
				else
					receiveEffect.addString("Normal");
				receiveEffect.sendResponse();
				
				poke.removeStatusEffects(false);
			}
			else
			{
				// TcpProtocolHandler.writeMessage(m_player.getTcpSession(), new SwitchMessage(m_npc.getName(), poke.getSpeciesName(), trainer, getPokemonPartyIndex(trainer, poke)));
				ServerMessage switchInform = new ServerMessage(m_player.getSession());
				switchInform.Init(32);
				switchInform.addString(m_npc.getName());
				switchInform.addString(poke.getSpeciesName());
				switchInform.addInt(trainer);
				switchInform.addInt(getPokemonPartyIndex(trainer, poke));
				switchInform.sendResponse();
				
				ServerMessage receiveEffect = new ServerMessage(m_player.getSession());
				receiveEffect.Init(29);
				receiveEffect.addInt(1);
				receiveEffect.addString(poke.getSpeciesName());
				
				if(poke.hasEffect(BurnEffect.class))
					receiveEffect.addString("Burn");
				else if(poke.hasEffect(FreezeEffect.class))
					receiveEffect.addString("Freeze");
				else if(poke.hasEffect(ParalysisEffect.class))
					receiveEffect.addString("paralysis");
				else if(poke.hasEffect(PoisonEffect.class))
					receiveEffect.addString("Poison");
				else if(poke.hasEffect(SleepEffect.class))
					receiveEffect.addString("Sleep");
				else
					receiveEffect.addString("Normal");
				receiveEffect.sendResponse();
				
				poke.removeStatusEffects(false);
			}
	}

	@Override
	public void informUseMove(Pokemon poke, String name)
	{
		if(m_player != null)
		{
			// TcpProtocolHandler.writeMessage(m_player.getTcpSession(), new BattleMoveMessage(poke.getSpeciesName(), name));
			ServerMessage move = new ServerMessage(m_player.getSession());
			move.Init(26);
			move.addString(poke.getSpeciesName());
			move.addString(name);
			move.sendResponse();
		}
	}

	/* Ends the battle and the player gets rewarded with gold and experience.
	 * If the gym leader was already beaten the player gets triple the experience and 100 extra gold.
	 * If the enemy trainer was a Gym Leader the player is also rewarded with the badge. */
	@Override
	public void informVictory(int winner)
	{
		m_finished = true;
		int money = 30 * (getMechanics().getRandom().nextInt(5) + 1);	// The magic cookie is used as a base to reward gold (replace later).
		if(winner == 0)
		{
			int trainerExp = 0;
			for(int i = 0; i < getParty(1).length; i++)
				if(getParty(1)[i] != null)
					trainerExp += getParty(1)[i].getLevel() / 2;
			if(m_npc.isGymLeader() && !m_player.hasBadge(m_npc.getBadge()))
			{
				trainerExp *= 3;
				money += 100;
			}
			if(trainerExp > 0)
				m_player.addTrainingExp(trainerExp);
			if(m_npc.isGymLeader())
				m_player.addBadge(m_npc.getBadge());
			// TcpProtocolHandler.writeMessage(m_player.getTcpSession(), new BattleRewardMessage(BattleRewardType.MONEY, money));
			ServerMessage reward = new ServerMessage(m_player.getSession());
			reward.Init(35);
			reward.addInt(money);
			reward.sendResponse();
			m_player.setMoney(m_player.getMoney() + money);
			m_player.removeTempStatusEffects();
			// TcpProtocolHandler.writeMessage(m_player.getTcpSession(), new BattleEndMessage(BattleEnd.WON));
			ServerMessage victory = new ServerMessage(m_player.getSession());
			victory.Init(24);
			victory.addInt(0);
			victory.sendResponse();
		}
		else
		{
			if(m_player.getMoney() - money >= 0)
				m_player.setMoney(m_player.getMoney() - money);
			else
				m_player.setMoney(0);
			// TcpProtocolHandler.writeMessage(m_player.getTcpSession(), new BattleEndMessage(BattleEnd.LOST));
			ServerMessage loss = new ServerMessage(m_player.getSession());
			loss.Init(24);
			loss.addInt(1);
			loss.sendResponse();
			m_player.lostBattle();
		}
		m_player.updateClientMoney();
		m_player.setBattling(false);
		m_player.setTalking(false);
		dispose();
		if(m_dispatch != null)
		{
			/* This very bad programming but shoddy does it and forces us to do it */
			/* Thread t = m_dispatch;
			 * m_dispatch = null;
			 * t.stop(); let the thread manually return. */
		}
	}

	@Override
	public void queueMove(int trainer, BattleTurn move) throws MoveQueueException
	{
		/* Check if move exists */
		if(move.isMoveTurn() && move.getId() != -1 && getActivePokemon()[trainer].getMove(move.getId()) == null)
		{
			requestMove(trainer);
			return;
		}
		/* Handle forced switches */
		if(m_isWaiting && m_replace != null && m_replace[trainer])
		{
			if(!move.isMoveTurn())
				if(!getActivePokemon()[trainer].equals(getParty(trainer)[move.getId()]))
				{
					switchInPokemon(trainer, move.getId());
					m_replace[trainer] = false;
					m_isWaiting = false;
					return;
				}
			requestPokemonReplacement(trainer);
			return;
		}
		/* Queue the move */
		if(m_turn[trainer] == null)
		{
			/* Handle Pokemon being unhappy and ignoring you */
			if(trainer == 0 && !getActivePokemon()[0].isFainted())
				if(getActivePokemon()[0].getHappiness() <= 40)
				{
					/* Pokemon is unhappy, they'll do what they feel like */
					showMessage(getActivePokemon()[0].getSpeciesName() + " is unhappy!");
					int moveID = getMechanics().getRandom().nextInt(4);
					while(getActivePokemon()[0].getMove(moveID) == null)
						moveID = getMechanics().getRandom().nextInt(4);
					move = BattleTurn.getMoveTurn(moveID);
				}
				else if(getActivePokemon()[0].getHappiness() < 70)
					/* Pokemon is partially unhappy, 50% chance they'll listen to you */
					if(getMechanics().getRandom().nextInt(2) == 1)
					{
						showMessage(getActivePokemon()[0].getSpeciesName() + " is unhappy!");
						int moveID = getMechanics().getRandom().nextInt(4);
						while(getActivePokemon()[0].getMove(moveID) == null)
							moveID = getMechanics().getRandom().nextInt(4);
						move = BattleTurn.getMoveTurn(moveID);
					}
			if(move.getId() == -1)
			{
				if(m_dispatch == null && (trainer == 0 && m_turn[1] != null || trainer == 1 && m_turn[0] != null))
				{
					m_dispatch = new Thread(new Runnable()
					{
						public void run()
						{
							executeTurn(m_turn);
							m_dispatch = null;
						}
					});
					m_dispatch.start();
					return;
				}
			}
			else // Handle a fainted pokemon
			if(getActivePokemon()[trainer].isFainted())
			{
				if(!move.isMoveTurn() && getParty(trainer)[move.getId()] != null && getParty(trainer)[move.getId()].getHealth() > 0)
				{
					switchInPokemon(trainer, move.getId());
					requestMoves();
					return;
				}
				else // The player still has pokemon left
				if(getAliveCount(trainer) > 0)
				{
					requestPokemonReplacement(trainer);
					return;
				}
				else
				{
					// the player has no pokemon left. Announce winner
					if(trainer == 0)
						informVictory(1);
					else
						informVictory(0);
					return;
				}
			}
			else // The turn was used to attack!
			if(move.isMoveTurn())
			{
				// Handles Struggle
				if(getActivePokemon()[trainer].mustStruggle())
					m_turn[trainer] = BattleTurn.getMoveTurn(-1);
				else // The move has no more PP
				if(getActivePokemon()[trainer].getPp(move.getId()) <= 0)
				{
					if(trainer == 0)
					{
						// TcpProtocolHandler.writeMessage(m_player.getTcpSession(), new NoPPMessage(this.getActivePokemon()[trainer].getMoveName(move.getId())));
						ServerMessage noPP = new ServerMessage(m_player.getSession());
						noPP.Init(20);
						noPP.addString(getActivePokemon()[trainer].getMoveName(move.getId()));
						noPP.sendResponse();
						requestMove(0);
					}
					else
						/* Get another move from the npc */
						requestMove(1);
					return;
				}
				else
					// Assign the move to the turn
					m_turn[trainer] = move;
			}
			else if(move.isItemTurn())
				return;
			else if(getActivePokemon()[trainer].isActive() && getParty(trainer)[move.getId()] != null && getParty(trainer)[move.getId()].getHealth() > 0)
				m_turn[trainer] = move;
			else
			{
				requestMove(trainer);
				return;
			}
		}
		/* Ensures the npc selected a move */
		if(trainer == 0 && m_turn[0] != null && m_turn[1] == null)
		{
			requestMove(1);
			return;
		}
		if(m_dispatch != null)
			return;
		// Both turns are ready to be performed
		if(m_turn[0] != null && m_turn[1] != null)
		{
			m_dispatch = new Thread(new Runnable()
			{
				public void run()
				{
					executeTurn(m_turn);
					for(int i = 0; i < m_participants; ++i)
						m_turn[i] = null;
					m_dispatch = null;
				}
			});
			m_dispatch.start();
		}
	}

	@Override
	public void refreshActivePokemon()
	{
		// m_player.getTcpSession().write("bh0" + this.getActivePokemon()[0].getHealth());
		// m_player.getTcpSession().write("bh1" + this.getActivePokemon()[1].getHealth());
		ServerMessage informHealthFirst = new ServerMessage(m_player.getSession());
		informHealthFirst.Init(33);
		informHealthFirst.addInt(0);
		informHealthFirst.addString("0," + getActivePokemon()[0].getHealth());
		informHealthFirst.sendResponse();
		ServerMessage informHealthSecond = new ServerMessage(m_player.getSession());
		informHealthSecond.Init(33);
		informHealthSecond.addInt(1);
		informHealthSecond.addString("1," + getActivePokemon()[1].getHealth());
		informHealthSecond.sendResponse();
	}

	@Override
	public void requestAndWaitForSwitch(int party)
	{
		requestPokemonReplacement(party);
		if(party == 0)
		{
			/* Request a switch from the player */
			if(!m_replace[party])
				return;
			m_isWaiting = true;
			do
				synchronized(m_dispatch)
				{
					try
					{
						m_dispatch.wait(1000);
					}
					catch(InterruptedException e)
					{

					}
				}
			while(m_replace != null && m_replace[party]);
		}
	}

	@Override
	public void showMessage(String message)
	{
		if(m_finished)
			return;
		if(m_player != null)
		{
			// TcpProtocolHandler.writeMessage(m_player.getTcpSession(), new BattleMessage(message));
			ServerMessage Message = new ServerMessage(m_player.getSession());
			Message.Init(23);
			Message.addString(message);
			Message.sendResponse();
		}
	}

	@Override
	protected void requestMove(int trainer)
	{
		if(trainer == 0)
		{
			/* Request move from player */
			// TcpProtocolHandler.writeMessage(m_player.getTcpSession(), new BattleMoveRequest());
			ServerMessage moveRequest = new ServerMessage(m_player.getSession());
			moveRequest.Init(27);
			moveRequest.sendResponse();
		}
		else
			/* Request move from npc */
			try
			{
				if(getActivePokemon()[1].hasTypeWeakness(getActivePokemon()[0]) && getAliveCount(1) >= 3)
					/* The npc should switch out a different Pokemon */
					/* 50:50 chance they will switch */
					if(getMechanics().getRandom().nextInt(3) == 0)
					{
						int index = 0;
						while(getParty(1)[index] == null || getParty(1)[index].isFainted() || getParty(1)[index].compareTo(getActivePokemon()[1]) == 0)
						{
							try
							{
								Thread.sleep(100);
							}
							catch(Exception e)
							{
							}
							index = getMechanics().getRandom().nextInt(6);
						}
						queueMove(1, BattleTurn.getSwitchTurn(index));
						return;
					}
				/* If they did not switch, select a move */
				int moveID = getMechanics().getRandom().nextInt(4);
				while(getActivePokemon()[1].getMove(moveID) == null)
					moveID = getMechanics().getRandom().nextInt(4);
				queueMove(1, BattleTurn.getMoveTurn(moveID));
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
	}

	@Override
	protected void requestMoves()
	{
		clearQueue();
		requestMove(1);
		requestMove(0);
	}

	@Override
	protected void requestPokemonReplacement(int i)
	{
		if(i == 0)
		{
			/* Request Pokemon replacement from player */
			// TcpProtocolHandler.writeMessage(m_player.getTcpSession(), new SwitchRequest());
//			ServerMessage switchOccur = new ServerMessage(m_player.getSession());
//			switchOccur.Init(32);
//			switchOccur.sendResponse();
		}
		else /* Request Pokemon replacement from npc */
		if(getAliveCount(1) == 0)
			informVictory(0);
		else
			try
			{
				int index = 0;

				while(getParty(1)[index] == null || getParty(1)[index].isFainted())
				{
					try
					{
						Thread.sleep(100);
					}
					catch(Exception e)
					{
					}
					index = getMechanics().getRandom().nextInt(6);
				}
				switchInPokemon(1, BattleTurn.getSwitchTurn(index).getId());

				// Check if this player has seen this wild pokemon before, if not, update pokedex
				if(!m_player.isPokemonSeen(m_pokemon[1][index].getPokemonNumber() + 1))
					m_player.setPokemonSeen(m_pokemon[1][index].getPokemonNumber() + 1);
				requestMoves();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
	}

	/**
	 * Sends pokemon data to the client
	 * 
	 * @param receiver
	 */
	private void sendPokemonData(Player receiver)
	{
		for(int i = 0; i < getParty(1).length; i++)
			if(getParty(1)[i] != null)
			{
				// TcpProtocolHandler.writeMessage(receiver.getTcpSession(), new EnemyDataMessage(i, getParty(1)[i]));
				Pokemon p = getParty(1)[i];
				ServerMessage enemyData = new ServerMessage(m_player.getSession());
				enemyData.Init(21);
				enemyData.addInt(i);
				enemyData.addString(p.getName());
				enemyData.addInt(p.getLevel());
				enemyData.addInt(p.getGender());
				enemyData.addInt(p.getHealth());
				enemyData.addInt(p.getHealth());
				enemyData.addInt(p.getSpeciesNumber());
				enemyData.addBool(p.isShiny());
				enemyData.sendResponse();
			}
	}

}
