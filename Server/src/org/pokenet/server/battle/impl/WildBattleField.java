package org.pokenet.server.battle.impl;

import java.util.LinkedHashSet;
import java.util.Set;
import org.pokenet.server.backend.ItemProcessor.PokeBall;
import org.pokenet.server.backend.entity.Player;
import org.pokenet.server.battle.BattleField;
import org.pokenet.server.battle.BattleTurn;
import org.pokenet.server.battle.DataService;
import org.pokenet.server.battle.Pokemon;
import org.pokenet.server.battle.PokemonEvolution;
import org.pokenet.server.battle.PokemonEvolution.EvolutionTypes;
import org.pokenet.server.battle.PokemonSpecies;
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
 * A battlefield for wild Pokemon battles
 * 
 * @author shadowkanji
 */
public class WildBattleField extends BattleField
{
	private String[] att = { "Beedrill,2", "Ekans,1", "Arbok,2", "Nidoran-m,1", "Nidorino,2", "Nidoking,3", "Paras,1", "Parasect,2", "Mankey,1", "Primeape,2", "Growlithe,1", "Arcanine,2", "Machop,1",
			"Machoke,2", "Machamp,3", "Bellsprout,1", "Weepingbell,2", "Victreebel,3", "Farfetchd,1", "Doduo,1", "Dodrio,2", "Muk,1", "Krabby,1", "Kingler,2", "Hitmonlee,2", "Rhydon,2", "Goldeen,1",
			"Seaking,2", "Scyther,1", "Pinser,2", "Tauros,1", "Gyarados,2", "Flareon,2", "Kabutops,2", "Dratini,1", "Dragonair,2", "Dragonite,3", "Totodile,1", "Croconaw,1", "Feraligatr,2",
			"Sentret,1", "Spinarak,1", "Ariados,2", "Unown,1", "Snubbull,1", "GranBull,2", "Qwilfish,1", "Scizor,2", "Heracross,2", "Teddiursa,1", "Ursaring,2", "Swinub,1", "Piloswine,1",
			"Octillery,1", "Kingdra,1", "Donphan,1", "Stantler,1", "Tyrogue,1", "Entei,2", "Larvitar,1", "Pupitar,2", "Tyranitar,3", "Combusken,1", "Blaziken,3", "Mudkip,1", "Marshtomp,2",
			"Swampert,3", "Poochyena,1", "Mightyena,2", "Nuzleaf,2", "Shiftry,3", "Breloom,2", "Sableye,1", "Mawile,1", "Caravanha,1", "Sharpedo,2", "Camerupt,1", "Trapinch,1", "Vibrava,1",
			"Flygon,1", "Cacturne,1", "Zangoose,2", "seviper,1", "solrock,2", "Corphish,1", "Crawdaunt,2", "Anorith,1", "Armaldo,2", "Shuppet,1", "Banette,2", "Absol,2", "Huntail,1", "Bagon,1",
			"Salamence,3", "Groudon,3", "Rayquaza,2", "Deoxys,1", "Turtwig,1", "Grotle,1", "Torterra,2", "Infernape,1", "Staraptor,3", "Bibarel,2", "Kricketune,2", "Shinx,1", "Luxio,2", "Luxray,3",
			"Cranidos,1", "Rampardos,2", "Mothim,1", "Honchkrow,2", "Chatot,1", "Gible,1", "Gabite.2", "Garchomp,3", "Riolu,1", "Lucario,1", "Croagunk,1", "Toxicroak,2", "Carnivine,2", "Snover,1",
			"Abomasnow,1", "Weavile,1", "Rhyperior,3", "electivire,3", "Yanmega,2", "Mamoswine,3", "Gallade,3", "Mesprit,1", "Azelf,2", "Regigigas,3" };
	private String[] def = { "Squitle,1", "WARTORTLE,1", "METAPOD,2", "KAKUNA,2", "SANDSHREW,1", "SANDSLASH,3", "PARASECT,1", "POLIWRATH,3", "GEODUDE,1", "GRAVELER,2", "GOLEM,3", "SLOWBRO,2",
			"SHELLDER,2", "CLOYSTER,2", "ONIX,1", "EXEGGCUTE,1", "CUBONE,1", "MAROWAK,2", "KOFFING,1", "WEEZING,2", "RHYHORN,1", "TANGELA,1", "SEADRA,1", "OMANYTE,1", "OMASTAR,2", "KABUTO,1",
			"BAYLEEF,1", "MEGANIUM,1", "CROCONAW,1", "FERALIGATR,1", "SUDOWOODO,2", "PINECO,1", "FORRETRESS,2", "GLIGAR,1", "STEELIX,2", "SHUCKLE,1", "MAGCARGO,2", "CORSOLA,1", "SKARMORY,2",
			"DONPHAN,1", "MILTANK,2", "SUICUNE,1", "SILCOON,2", "CASCOON,2", "SEEDOT,1", "PELIPPER,2", "NINCADA,1", "NOSEPASS,1", "SABLEYE,1", "MAWILE,1", "ARON,1", "LAIRON,1", "AGGRON,3",
			"TORKOAL,2", "DUSCLOPS,1", "CLAMPERL,1", "HUNTAIL,1", "RELICANTH,1", "SHELGON,2", "BELDUM,1", "METANG,2", "METAGROSS,3", "REGIROCK,3", "REGISTEEL,2", "GROTLE,1", "TORTERRA,1",
			"KRICKETOT", "SHIELDON,1", "BASTIODON,2", "VESPIQUEN,1", "BRONZOR,1", "BRONZONG,1", "BONSLY,1", "SPIRITOMB,1", "HIPPOPOTAS,1", "HIPPOWDON,2", "SKORUPI,1", "DRAPION,2", "TANGROWTH,2",
			"LEAFEON,2", "GLISCOR,2", "PROBOPASS,1", "DUSKNOIR,1", "UXIE,2" };
	/* TODO: fill lists (Done?) */
	private String[] hp = { "CATERPIE,1", "NIDORAN,1", "NIDORINA,2", "NIDOQUEEN,3", "CLEFAIRY,2", "CLEFABLE,3", "JIGGLYPUFF,2", "WIGGLYTUFF,3", "SLOWPOKE,1", "GRIMER,1", "MUK,1", "LICKITUNG,2",
			"CHANSEY,2", "KANGASKHAN,2", "LAPRAS,2", "DITTO,1", "VAPOREON,2", "SNORLAX,2", "MEW,3", "HOOTHOOT,1", "NOCTOWL,2", "CHINCHOU,1", "LANTURN,2", "IGGLYBUFF,1", "MARILL,2", "AZUMARILL,3",
			"WOOPER,1", "QUAGSIRE,2", "WOBBUFFET,2", "DUNSPARCE,1", "PILOSWINE,1", "PHANPY,1", "BLISSEY,3", "ENTEI,1", "CELEBI,3", "WURMPLE,1", "SHROOMISH,1", "SLAKOTH,1", "SLAKING,3", "SHEDINJA,2",
			"WHISMUR,1", "LOUDRED,2", "EXPLOUD,3", "MUKUHITA,1", "HARIYAMA,2", "AZURILL,1", "DELCATTY,2", "GULPIN,1", "SWALOT,2", "WAILMER,1", "WAILORD,2", "BARBOACH,1", "WHISCASH,2", "CASTFORM,1",
			"TROPIUS,2", "WYNAUT,1", "SNORUNT,1", "GLALIE,2", "SPHEAL,1", "SEALEO,2", "WALREIN,3", "RELICANTH,1", "JIRACHI,3", "BIDOOF,1", "SHELLOS,1", "GASTRODON,2", "DRIFLOON,1", "DRIFBLIM,2",
			"SKUNTANK,2", "HAPPINY,1", "MUNCHLAX,1", "LICKILICKY,3", "GIRATINA,3", "PHIONE,1", "MANAPHY,3", "SHAYMIN,3", "ARCEUS,3" };
	private boolean m_finished = false;
	private final Player m_player;
	private int m_runCount;
	private int m_takenTurns = 0;

	private final BattleTurn[] m_turn = new BattleTurn[2];
	private Pokemon m_wildPoke;
	private String[] spatt = { "bulbasaur,1", "ivysaur,1", "venusaur,2", "charmeleon,1", "charizard,3", "butterfree,2", "oddish,1", "gloom,2", "vileplume,3", "venomoth,1", "psyduck,1", "golduck,2",
			"abra,1", "kadabra,2", "alakazam,3", "megnemite,1", "magneton,2", "gastly,1", "haunter,2", "gengar,3", "exeggutor,2", "horsea,1", "seadra,2", "jynx,2", "magmar,2", "porygon,1",
			"zapdos,3", "moltres,3", "mewtwo,3", "quilava,1", "typhlosion,3", "natu,1", "xatu,1", "mareep,1", "flaaffy,2", "ampharos,3", "sunkern1", "sunflora,2", "espeon,2", "unown,1",
			"girafarig,2", "slugma,1", "remoraid,1", "octillery,1", "houndour,1", "houndoom,2", "kingdra,1", "porygon2,2", "smoochum,1", "raikou,1", "torchic,1", "combusken,1", "beautifly,3",
			"ralts,1", "kirlia,2", "gardevoir,3", "masquerain,1", "roselia,2", "numel,1", "camerupt,1", "spinda,1", "cacnea,1", "cacturne,1", "seviper,1", "lunatone,2", "chimecho,1", "gorebyss,2",
			"latios,3", "kyogre,3", "rayquaza,1", "deoxys,1", "monferno,1", "infernape,1", "piplup,1", "prinplup,2", "empoleon,3", "budew,1", "roserade,3", "mothim,1", "cherubi,1", "cherrim,2",
			"mismagius,1", "chingling,1", "lucario,1", "abomasnow,1", "magnezone,3", "magmortar,3", "togekiss,2", "glaceon,2", "porygon-z,3", "rotom,1", "mesprit,1", "azelf,1", "dialga,3",
			"palkia,3", "heatran,3", "darkrai,2" };
	private String[] spdef = { "ivysaur,1", "venusaur,1", "wartortle,1", "blastoise,3", "butterfree,1", "beedrill,1", "ninetales,1", "venonat,1", "tentacool,1", "tentacruel,2", "seel,1", "dewgong,2",
			"drowzee,1", "hypno,2", "hitmonchan,2", "mr.mime,2", "eevee,1", "articuno,3", "chikorita,1", "bayleef,1", "meganium,2", "ledyba,1", "ledian,2", "cleffa,1", "togepi,1", "togetic,2",
			"bellossom,3", "politoed,3", "hoppip,1", "umbreon,2", "slowking,3", "midreavus,1", "shuckle,1", "corsola,1", "mantine,2", "kingdra,1", "hitmontop,2", "suicune,2", "lugia,3", "ho-oh,3",
			"dustox,3", "lotad,1", "lombre,2", "ludicolo,3", "masquerain,1", "spoink,1", "grumpig,2", "swablu,1", "altaria,2", "baltoy,1", "claydol,2", "lileep,1", "cradily,2", "milotic,2",
			"kecleon,1", "duskull,1", "duclops,1", "chimecho,1", "regice,3", "registeel,1", "latias,3", "burmy,1", "wormadam,2", "vespiquen,1", "mismagius,1", "bronzong,1", "mime jr.,1",
			"spiritomb,1", "mantyke,1", "togekiss,1", "probopass,2", "dusknoir,2", "uxie,1", "mesprit,1", "cresselia,3" };
	private String[] speed = { "charmander,1", "charmeleon,1", "WEEDLE,1", "PIDGEY,1", "PIDGEOTTO,2", "PIDGEOT,3", "RATTATA,1", "RATICATE,2", "SPEAROW,1", "FEAROW,2", "PIKACHU,2", "RAICHU,3",
			"VULPIX,1", "NINETALES,1", "ZUBAT,1", "GOLBAT,2", "VENOMOTH,1", "DIGLETT,1", "DUGTRIO,2", "MEOWTH,1", "PERSIAN,2", "POLIWAG,1", "POLIWHIRL,2", "PONYTA,1", "RAPIDASH,2", "VOLTORB,1",
			"ELECTRODE,2", "STARYU,1", "STARMIE,2", "ELECTABUZZ,2", "TAUROS,1", "MAGIKARP,1", "JOLTEON,2", "AERODACTYL,2", "CYNDAQUIL,1", "QUILAVA,1", "FURRET,2", "CROBAT,3", "PICHU,1", "XATU,1",
			"SKIPLOOM,2", "JUMPLUFF,3", "AIPOM,1", "YANMA,1", "MURKROW,1", "SNEASEL,1", "DELIBIRD,1", "SMEARGLE,1", "ELEKID,1", "MAGBY,1", "RAIKOU,2", "TREECKO,1", "GROVYLE,2", "SCEPTILE,3",
			"ZIGZAGOON,1", "LINOONE,2", "TAILOW,1", "SWELLOW,2", "WINGULL,1", "SURSKIT,1", "VIGOROTH,2", "NINJASK,2", "SKITTY,1", "DELCATTY,1", "MEDITITE,1", "MEDICHAM,2", "ELECTRIKE,1",
			"MANECTRIC,2", "PLUSLE,1", "MINUN,1", "VOLBEAT,1", "ILLUMISE,1", "VIBRAVA,1", "FLYGON,2", "FEEBAS,1", "LUVDISC,1", "DEOXYS,1", "CHIMCHAR,1", "MONFERNO,1", "INFERNAPE,1", "STARLY,1",
			"STARAVIA,2", "COMBEE,1", "PACHIRISU,1", "BUIZEL,1", "FLOATZEL,2", "AMBIPOM,2", "BUNEARY,1", "LOPUNNY,2", "GLAMEOW,1", "PURUGLY,2", "STUNKY,1", "FINNEON,1", "LUMINEON,2", "WEAVILE,1",
			"FROSLASS,2", "ROTOM,1", "DARKRAI,1" };
	Set<Pokemon> m_participatingPokemon = new LinkedHashSet<Pokemon>();

	/**
	 * Constructor
	 * 
	 * @param m
	 * @param p
	 * @param wild
	 */
	public WildBattleField(BattleMechanics m, Player p, Pokemon wild)
	{
		super(m, new Pokemon[][] { p.getParty(), new Pokemon[] { wild } });
		/* Send information to client */
		p.setBattling(true);
		p.setBattleId(0);
		/* TcpProtocolHandler.writeMessage(p.getTcpSession(), new BattleInitMessage( true, 1)); TcpProtocolHandler.writeMessage(p.getTcpSession(), new EnemyDataMessage(0, wild)); */
		ServerMessage startBattle = new ServerMessage(p.getSession());
		startBattle.Init(18);
		startBattle.addBool(true);
		startBattle.addInt(1);
		startBattle.sendResponse();

		ServerMessage enemyData = new ServerMessage(p.getSession());
		enemyData.Init(21);
		enemyData.addInt(0);
		enemyData.addString(wild.getName());
		enemyData.addInt(wild.getLevel());
		enemyData.addInt(wild.getGender());
		enemyData.addInt(wild.getHealth());
		enemyData.addInt(wild.getHealth());
		enemyData.addInt(wild.getSpeciesNumber());
		enemyData.addBool(wild.isShiny());
		enemyData.sendResponse();
		/* Store variables */
		m_player = p;
		m_wildPoke = wild;
		m_participatingPokemon.add(p.getParty()[0]);
		/* Check if this player has seen this wild pokemon before, if not, update pokedex */
		if(!m_player.isPokemonSeen(wild.getPokemonNumber() + 1))
			m_player.setPokemonSeen(wild.getPokemonNumber() + 1);
		/* Call methods */
		// applyWeather();
		requestMoves();
	}

	/**
	 * Applies weather effect based on world/map weather
	 */
	@Override
	public void applyWeather()
	{
		if(m_player.getMap().isWeatherForced())
			switch(m_player.getMap().getWeather())
			{
				case NORMAL:
					break;
				case RAIN:
					applyEffect(new RainEffect());
					break;
				case HAIL:
					applyEffect(new HailEffect());
					break;
				case SANDSTORM:
					applyEffect(new SandstormEffect());
					break;
				default:
					break;
			}
		else
		{
			FieldEffect f = TimeService.getWeatherEffect();
			if(f != null)
				applyEffect(f);
		}
	}

	/**
	 * Clears the moves queue
	 */
	@Override
	public void clearQueue()
	{
		m_turn[0] = null;
		m_turn[1] = null;
	}

	@Override
	public void executeItemTurn(int i)
	{
		for(int turn = 0; turn < m_turn.length; turn++)
			if(m_turn[turn] == null)
				m_turn[turn] = BattleTurn.getItemTurn(i);
		executeTurn(m_turn);
	}

	@Override
	public void forceExecuteTurn()
	{
		for(int turn = 0; turn < m_turn.length; turn++)
			if(m_turn[turn] == null)
				m_turn[turn] = BattleTurn.getMoveTurn(-1);
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
			return m_wildPoke.getSpeciesName();
	}

	@Override
	public void informPokemonFainted(int trainer, int idx)
	{
		/* If the pokemon is the player's make sure it don't get exp */
		if(trainer == 0 && m_participatingPokemon.contains(getParty(trainer)[idx]))
			m_participatingPokemon.remove(getParty(trainer)[idx]);
		if(m_player != null)
		{
			/* TcpProtocolHandler.writeMessage(m_player.getTcpSession(), new FaintMessage(getParty(trainer)[idx].getSpeciesName())); */
			ServerMessage informFaint = new ServerMessage(m_player.getSession());
			informFaint.Init(25);
			informFaint.addString(getParty(trainer)[idx].getSpeciesName());
			informFaint.sendResponse();
		}
	}

	@Override
	public void informPokemonHealthChanged(Pokemon poke, int change)
	{
		int index = getPokemonPartyIndex(0, poke);
		if(m_player != null)
			if(getActivePokemon()[0] == poke)
			{
				/* TcpProtocolHandler.writeMessage(m_player.getTcpSession(), new HealthChangeMessage(0, change)); */
				ServerMessage informHealth = new ServerMessage(m_player.getSession());
				informHealth.Init(33);
				informHealth.addInt(0);
				informHealth.addString("0," + change);
				informHealth.sendResponse();
			}
			else if(getActivePokemon()[1] == poke)
			{
				/* TcpProtocolHandler.writeMessage(m_player.getTcpSession(), new HealthChangeMessage(1, change)); */
				ServerMessage informHealth = new ServerMessage(m_player.getSession());
				informHealth.Init(33);
				informHealth.addInt(1);
				informHealth.addString("1," + change);
				informHealth.sendResponse();
			}
			else if(index > -1)
			{
				/* m_player.getTcpSession().write( "Ph" + String.valueOf(index) + poke.getHealth()); */
				ServerMessage informHealth = new ServerMessage(m_player.getSession());
				informHealth.Init(45);
				informHealth.addInt(index);
				informHealth.addInt(poke.getHealth());
				informHealth.sendResponse();
			}
	}

	@Override
	public void informStatusApplied(Pokemon poke, StatusEffect eff)
	{
		if(m_finished)
			return;
		if(m_player != null)
			if(poke != m_wildPoke)
			{
				/* TcpProtocolHandler.writeMessage(m_player .getTcpSession(), new StatusChangeMessage(0, poke.getSpeciesName(), eff .getName(), false)); */
				ServerMessage receiveEffect = new ServerMessage(m_player.getSession());
				receiveEffect.Init(29);
				receiveEffect.addInt(0);
				receiveEffect.addString(poke.getSpeciesName());
				if(eff.getName() == null)
					receiveEffect.addString("");
				else
					receiveEffect.addString(eff.getName());
				receiveEffect.sendResponse();
			}
			else if(poke == m_wildPoke)
			{
				/* TcpProtocolHandler.writeMessage(m_player.getTcpSession(), new StatusChangeMessage(1, poke.getSpeciesName(), eff.getName(), false)); */
				ServerMessage receiveEffect = new ServerMessage(m_player.getSession());
				receiveEffect.Init(29);
				receiveEffect.addInt(1);
				receiveEffect.addString(poke.getSpeciesName());
				if(eff.getName() == null)
					receiveEffect.addString("");
				else
					receiveEffect.addString(eff.getName());
				receiveEffect.sendResponse();
			}
	}

	@Override
	public void informStatusRemoved(Pokemon poke, StatusEffect eff)
	{
		if(m_finished)
			return;
		if(m_player != null)
			if(poke != m_wildPoke && !poke.isFainted())
			{
				/* TcpProtocolHandler.writeMessage(m_player
				 * .getTcpSession(), new StatusChangeMessage(0, poke.getSpeciesName(), eff
				 * .getName(), true)); */
				ServerMessage removeEffect = new ServerMessage(m_player.getSession());
				removeEffect.Init(30);
				removeEffect.addInt(0);
				removeEffect.addString(poke.getSpeciesName());
				if(eff.getName() == null)
					removeEffect.addString("");
				else
					removeEffect.addString(eff.getName());
				removeEffect.sendResponse();
			}
			else if(poke == m_wildPoke && !poke.isFainted())
			{
				/* TcpProtocolHandler
				 * .writeMessage(m_player.getTcpSession(), new StatusChangeMessage(1,
				 * poke.getSpeciesName(), eff.getName(), true)); */
				ServerMessage removeEffect = new ServerMessage(m_player.getSession());
				removeEffect.Init(30);
				removeEffect.addInt(1);
				removeEffect.addString(poke.getSpeciesName());
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
		if(trainer == 0 && m_player != null)
			if(!m_participatingPokemon.contains(poke))
			{
				m_participatingPokemon.add(poke);
				/* TcpProtocolHandler.writeMessage(m_player.getTcpSession(),
				 * new SwitchMessage(m_player.getName(), poke.getSpeciesName(), trainer,
				 * getPokemonPartyIndex(trainer, poke))); */
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
	}

	@Override
	public void informUseMove(Pokemon poke, String name)
	{
		if(m_player != null)
		{
			/* TcpProtocolHandler.writeMessage(m_player.getTcpSession(),
			 * new BattleMoveMessage(poke.getSpeciesName(), name)); */
			ServerMessage move = new ServerMessage(m_player.getSession());
			move.Init(26);
			move.addString(poke.getSpeciesName());
			move.addString(name);
			move.sendResponse();
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void informVictory(int winner)
	{
		m_finished = true;
		if(winner == 0)
		{
			calculateExp();
			m_player.removeTempStatusEffects();
			/* TcpProtocolHandler.writeMessage(m_player.getTcpSession(),
			 * new BattleEndMessage(BattleEnd.WON)); */
			ServerMessage victory = new ServerMessage(m_player.getSession());
			victory.Init(24);
			victory.addInt(0);
			victory.sendResponse();
		}
		else
		{
			/* TcpProtocolHandler.writeMessage(m_player.getTcpSession(),
			 * new BattleEndMessage(BattleEnd.LOST)); */
			ServerMessage loss = new ServerMessage(m_player.getSession());
			loss.Init(24);
			loss.addInt(1);
			loss.sendResponse();
			m_player.lostBattle();
		}
		m_player.setBattling(false);
		m_player.setFishing(false);
		dispose();
		m_wildPoke = null;
		if(m_dispatch != null)
		{
			/* This very bad programming but shoddy does it and forces us to do it */
			Thread t = m_dispatch;
			m_dispatch = null;
			t.stop();
		}
		ServerMessage message = new ServerMessage();
		message.Init(64);
		message.addInt(m_player.getX());
		message.addInt(m_player.getY());
		m_player.getSession().Send(message);
	}

	/**
	 * Queues a battle turn
	 */
	@Override
	public void queueMove(int trainer, BattleTurn move) throws MoveQueueException
	{
		if(m_player.getBattleId() == trainer)
			m_takenTurns++;
		/* Checks the move exists */
		if(move.isMoveTurn() && move.getId() != -1 && getActivePokemon()[trainer].getMove(move.getId()) == null)
		{
			requestMove(trainer);
			return;
		}
		/* Handle forced switches */
		if(m_isWaiting && m_replace != null && m_replace[trainer])
		{
			if(!move.isMoveTurn())
				if(getActivePokemon()[trainer].compareTo(getParty(trainer)[move.getId()]) != 0)
				{
					switchInPokemon(trainer, move.getId());
					m_replace[trainer] = false;
					m_isWaiting = false;
					return;
				}
			requestPokemonReplacement(trainer);
			return;
		}
		/* Ensure they haven't queued a move already */
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
				if(m_dispatch == null && trainer == 0 && m_turn[1] != null)
				{
					m_dispatch = new Thread(new Runnable()
					{
						public void run()
						{
							executeTurn(m_turn);
							m_dispatch = null;
						}
					}, "BattleTurn-Thread");
					m_dispatch.start();
					return;
				}
			}
			else if(getActivePokemon()[trainer].isFainted())
			{
				if(!move.isMoveTurn() && getParty(trainer)[move.getId()] != null && getParty(trainer)[move.getId()].getHealth() > 0)
				{
					switchInPokemon(trainer, move.getId());
					requestMoves();
					if(!m_participatingPokemon.contains(getActivePokemon()[0]))
						m_participatingPokemon.add(getActivePokemon()[0]);
					return;
				}
				else if(trainer == 0 && getAliveCount(0) > 0)
					if(getAliveCount(0) > 0)
					{
						if(m_participatingPokemon.contains(getActivePokemon()[0]))
							m_participatingPokemon.remove(getActivePokemon()[0]);
						requestPokemonReplacement(0);
						return;
					}
					else
					{
						/* Player lost the battle */
						informVictory(1);
						return;
					}
			}
			else if(move.isMoveTurn())
			{
				if(getActivePokemon()[trainer].mustStruggle())
					m_turn[trainer] = BattleTurn.getMoveTurn(-1);
				else if(getActivePokemon()[trainer].getPp(move.getId()) <= 0)
				{
					if(trainer == 0)
					{
						/* TcpProtocolHandler.writeMessage(m_player.getTcpSession(),
						 * new NoPPMessage(this.getActivePokemon()[trainer]
						 * .getMoveName(move.getId()))); */
						ServerMessage noPP = new ServerMessage(m_player.getSession());
						noPP.Init(20);
						noPP.addString(getActivePokemon()[trainer].getMoveName(move.getId()));
						noPP.sendResponse();
						requestMove(0);
					}
					else
						requestMove(1);
					return;
				}
				else
					m_turn[trainer] = move;
			}
			else if(move.isItemTurn())
				return;
			else if(getActivePokemon()[trainer].isActive() && getParty(trainer)[move.getId()] != null && getParty(trainer)[move.getId()].getHealth() > 0)
				m_turn[trainer] = move;
			else
			{
				if(trainer == 0)
					requestMove(0);
				return;
			}
		}
		if(trainer == 0 && m_turn[1] == null)
		{
			requestMove(1);
			return;
		}
		if(m_dispatch != null)
			return;
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
			}, "BattleTurn-Thread");
			m_dispatch.start();
		}
	}

	/**
	 * Refreshes Pokemon on battlefield
	 */
	@Override
	public void refreshActivePokemon()
	{
		/* m_player.getTcpSession().write(
		 * "bh0" + this.getActivePokemon()[0].getHealth());
		 * //m_player.getTcpSession().write(
		 * "bh1" + this.getActivePokemon()[1].getHealth()); */
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

	/**
	 * Requests a new Pokemon (called by moves that force poke switches)
	 */
	@Override
	public void requestAndWaitForSwitch(int party)
	{
		if(party == 0)
		{
			requestPokemonReplacement(party);
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

	/**
	 * Attempts to run from this battle
	 */
	public void run()
	{
		if(canRun())
		{
			/* TcpProtocolHandler.writeMessage(m_player.getTcpSession(), new RunMessage( true)); */
			ServerMessage run = new ServerMessage(m_player.getSession());
			run.Init(34);
			run.addBool(true);
			run.sendResponse();
			m_player.setBattling(false);
			m_player.setFishing(false);
			dispose();
			ServerMessage message = new ServerMessage();
			message.Init(64);
			message.addInt(m_player.getX());
			message.addInt(m_player.getY());
			m_player.getSession().Send(message);
		}
		else
		{
			/* TcpProtocolHandler.writeMessage(m_player.getTcpSession(), new RunMessage( false)); */
			ServerMessage run = new ServerMessage(m_player.getSession());
			run.Init(34);
			run.addBool(false);
			run.sendResponse();
			if(m_turn[1] == null)
				getWildPokemonMove();
			try
			{
				queueMove(0, BattleTurn.getMoveTurn(-1));
			}
			catch(MoveQueueException e)
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	public void showMessage(String message)
	{
		if(m_finished)
			return;
		if(m_player != null)
		{
			/* TcpProtocolHandler.writeMessage(m_player.getTcpSession(),
			 * new BattleMessage(message)); */
			ServerMessage Message = new ServerMessage(m_player.getSession());
			Message.Init(23);
			Message.addString(message);
			Message.sendResponse();
		}
	}

	/**
	 * Throws a Pokeball. Returns true if pokemon was caught
	 * 
	 * @param p
	 * @return
	 */
	public boolean throwPokeball(PokeBall p)
	{
		/* Ensure user doesn't throw a Pokeball while battling */
		while(m_dispatch != null)
			try
			{
				Thread.sleep(500);
			}
			catch(InterruptedException e)
			{
			}
		double catchRate = 1.0;
		int pokeID = m_wildPoke.getPokemonNumber() + 1;
		boolean resetAfterCaught = false;
		switch(p)
		{
			case POKEBALL:
				showMessage(m_player.getName() + " threw a Pokeball!");
				m_wildPoke.setCaughtWith(35);
				break;

			case GREATBALL:
				showMessage(m_player.getName() + " threw a Great Ball!");
				catchRate = 1.5;
				m_wildPoke.setCaughtWith(36);
				break;

			case ULTRABALL:
				showMessage(m_player.getName() + " threw an Ultra Ball!");
				catchRate = 2.0;
				m_wildPoke.setCaughtWith(37);
				break;

			case MASTERBALL:
				showMessage(m_player.getName() + " threw a Master Ball!");
				catchRate = 255.0;
				m_wildPoke.setCaughtWith(38);
				break;

			case LEVELBALL:
				showMessage(m_player.getName() + " threw a Level Ball!");
				m_wildPoke.setCaughtWith(41);
				int m_pokemonLevel = getActivePokemon()[0].getLevel();
				int w_pokemonLevel = m_wildPoke.getLevel();
				if(m_pokemonLevel <= w_pokemonLevel)
					catchRate = 1.0;
				else if(m_pokemonLevel > w_pokemonLevel && m_pokemonLevel < w_pokemonLevel * 2)
					catchRate = 2.0;
				else if(m_pokemonLevel > w_pokemonLevel * 2 && m_pokemonLevel < w_pokemonLevel * 4)
					catchRate = 4.0;
				else if(m_pokemonLevel > w_pokemonLevel * 4)
					catchRate = 8.0;
				break;

			case LUREBALL:
				showMessage(m_player.getName() + " threw a Lure Ball!");
				m_wildPoke.setCaughtWith(42);
				if(m_player.isFishing())
					catchRate = 3.0;
				break;

			case MOONBALL:
				showMessage(m_player.getName() + " threw a Moon Ball!");
				m_wildPoke.setCaughtWith(43);
				if(pokeID == 29 || pokeID == 30 || pokeID == 31 || 		// Nidoran male family
						pokeID == 32 || pokeID == 33 || pokeID == 34 || 		// Nidoran female family
						pokeID == 35 || pokeID == 36 || pokeID == 173 ||  	// Clefairy family
						pokeID == 39 || pokeID == 40 || pokeID == 174 ||		// Jigglypuff family
						pokeID == 300 || pokeID == 301)
					catchRate = 4.0;
				break;

			case FRIENDBALL:
				showMessage(m_player.getName() + " threw a Friend Ball!");
				m_wildPoke.setCaughtWith(44);
				m_wildPoke.setHappiness(200);
				break;

			case LOVEBALL:
				showMessage(m_player.getName() + " threw a Love Ball!");
				m_wildPoke.setCaughtWith(47);
				if(m_wildPoke.getGender() == PokemonSpecies.GENDER_MALE && getActivePokemon()[0].getGender() == PokemonSpecies.GENDER_FEMALE
						&& m_wildPoke.getSpeciesName() == getActivePokemon()[0].getSpeciesName() || m_wildPoke.getGender() == PokemonSpecies.GENDER_FEMALE
						&& getActivePokemon()[0].getGender() == PokemonSpecies.GENDER_MALE && m_wildPoke.getSpeciesName() == getActivePokemon()[0].getSpeciesName())
					catchRate = 8.0;
				break;

			case HEAVYBALL:
				showMessage(m_player.getName() + " threw a Heavy Ball!");
				m_wildPoke.setCaughtWith(46);
				if(m_wildPoke.getWeight() < 204.796955)
					catchRate = -20.0;
				else if(m_wildPoke.getWeight() >= 204.796955 && m_wildPoke.getWeight() < 307.218112)
					catchRate = 20.0;
				else if(m_wildPoke.getWeight() >= 307.218112 && m_wildPoke.getWeight() < 409.59391)
					catchRate = 30.0;
				else if(m_wildPoke.getWeight() >= 409.59391)
					catchRate = 40.0;
				break;

			case FASTBALL:
				showMessage(m_player.getName() + " threw a Fast Ball!");
				m_wildPoke.setCaughtWith(45);
				if(m_wildPoke.getBase()[3] >= 100 				// Speed stat
						|| pokeID == 63 || pokeID == 58 || pokeID == 81 ||  // Pokemon that run from battle
						pokeID == 122 || pokeID == 231 || pokeID == 195 ||  //
						pokeID == 209 || pokeID == 114 || pokeID == 201 || pokeID == 243 || pokeID == 244 || pokeID == 245 ||	// Roaming pokemon
						pokeID == 380 || pokeID == 381 || pokeID == 144 || pokeID == 145 || pokeID == 146 || pokeID == 481 || pokeID == 488 || pokeID == 641 || pokeID == 642)						// Gen 5 roaming pokemon
					catchRate = 4.0;
				break;
			case PARKBALL:
				showMessage(m_player.getName() + " threw a Park Ball!");
				m_wildPoke.setCaughtWith(40);
				catchRate = 1.5;
				break;

			case PREMIERBALL:
				showMessage(m_player.getName() + " threw a Premier Ball!");
				m_wildPoke.setCaughtWith(49);
				break;

			case REPEATBALL:
				showMessage(m_player.getName() + " threw a Repeat Ball!");
				m_wildPoke.setCaughtWith(53);
				if(m_player.getPokedex().isPokemonCaught(pokeID))
					catchRate = 4.0;
				break;

			case TIMERBALL:
				showMessage(m_player.getName() + " threw a Timer Ball!");
				m_wildPoke.setCaughtWith(54);
				catchRate = (m_takenTurns + 10) / 10;
				if(catchRate > 4.0)
					catchRate = 4.0;
				break;

			case NESTBALL:
				showMessage(m_player.getName() + " threw a Nest Ball!");
				m_wildPoke.setCaughtWith(52);
				catchRate = (40 - m_wildPoke.getLevel()) / 10;
				if(catchRate < 1.0)
					catchRate = 1.0;
				break;

			case NETBALL:
				showMessage(m_player.getName() + " threw a Net Ball!");
				m_wildPoke.setCaughtWith(50);
				if(m_wildPoke.getType1().equalsIgnoreCase("BUG") || m_wildPoke.getType2().equalsIgnoreCase("BUG") || m_wildPoke.getType1().equalsIgnoreCase("WATER")
						|| m_wildPoke.getType2().equalsIgnoreCase("BUG"))
					catchRate = 3.5;
				break;

			case DIVEBALL:
				showMessage(m_player.getName() + " threw a Dive Ball!");
				m_wildPoke.setCaughtWith(51);
				if(m_player.isSurfing() || m_player.isFishing())

					break;

			case LUXURY:
				showMessage(m_player.getName() + " threw a Luxery Ball!");
				m_wildPoke.setCaughtWith(48);
				break;
			case HEALBALL:
				showMessage(m_player.getName() + " threw a Heal Ball!");
				m_wildPoke.setCaughtWith(55);
				resetAfterCaught = true;
				break;
			case QUICKBALL: // TODO: VERIFY IF THIS SHOULD BE TURN = 0 or TURN = 1!
				showMessage(m_player.getName() + " threw a Quick Ball!");
				m_wildPoke.setCaughtWith(58);
				if(m_takenTurns == 0)
					catchRate = 4.0;
				break;
			case DUSKBALL:
				showMessage(m_player.getName() + " threw a Dusk Ball!");
				m_wildPoke.setCaughtWith(56);
				if(TimeService.isNight()) // TODO: CAVES :/ FUCK CAVES
					catchRate = 3.5;
				break;
			case CHERISHBALL:
				showMessage(m_player.getName() + " threw a Cherish Ball!");
				m_wildPoke.setCaughtWith(57);
				break;
			case SAFARIBALL:
				showMessage(m_player.getName() + " threw a Safari Ball!");
				m_wildPoke.setCaughtWith(39);
				break;
		}

		if(getMechanics().isCaught(m_wildPoke, m_wildPoke.getRareness(), catchRate, 1))
		{
			m_wildPoke.calculateStats(resetAfterCaught);
			m_player.catchPokemon(m_wildPoke);
			showMessage("You successfuly caught " + m_wildPoke.getSpeciesName());
			/* TcpProtocolHandler.writeMessage(m_player.getTcpSession(), new BattleEndMessage(BattleEnd.POKEBALL)); */
			ServerMessage victory = new ServerMessage(m_player.getSession());
			victory.Init(24);
			victory.addInt(2);
			victory.sendResponse();
			m_player.setBattling(false);
			dispose();
			return true;
		}
		else
			showMessage("...but it failed!");

		return false;
	}

	/**
	 * Generates a wild Pokemon move
	 */
	protected void getWildPokemonMove()
	{
		if(getActivePokemon()[1] == null)
			return;
		try
		{
			int moveID = getMechanics().getRandom().nextInt(4);
			while(getActivePokemon()[1].getMove(moveID) == null)
			{
				try
				{
					Thread.sleep(100);
				}
				catch(Exception e)
				{
				}
				moveID = getMechanics().getRandom().nextInt(4);
				/* Stop infinite loops when player disconnects */
				if(m_player.getSession() == null || m_player.getSession().getChannel() == null)
					break;
			}
			queueMove(1, BattleTurn.getMoveTurn(moveID));
		}
		catch(MoveQueueException x)
		{
			x.printStackTrace();
		}
	}

	/**
	 * Requests a move from a specific player
	 */
	@Override
	protected void requestMove(int trainer)
	{
		if(trainer == 0)
		{
			/* If its the player, send a move request packet */
			/* TcpProtocolHandler.writeMessage(m_player.getTcpSession(), new BattleMoveRequest()); */
			ServerMessage moveRequest = new ServerMessage(m_player.getSession());
			moveRequest.Init(27);
			moveRequest.sendResponse();
		}
		else
			/* If its the wild Pokemon, just get the moves */
			getWildPokemonMove();
	}

	/**
	 * Requests moves
	 */
	@Override
	protected void requestMoves()
	{
		clearQueue();
		if(getActivePokemon()[0].isActive() && getActivePokemon()[1].isActive())
		{
			getWildPokemonMove();
			/* TcpProtocolHandler.writeMessage(m_player.getTcpSession(),
			 * new BattleMoveRequest()); */
			ServerMessage moveRequest = new ServerMessage(m_player.getSession());
			moveRequest.Init(27);
			moveRequest.sendResponse();
		}
	}

	/**
	 * Requests a pokemon replacement
	 */
	@Override
	protected void requestPokemonReplacement(int i)
	{
		if(i == 0)
		{
			/* 0 = our player in this case */
			/* TcpProtocolHandler.writeMessage(m_player.getTcpSession(),
			 * new SwitchRequest()); */
			ServerMessage switchOccur = new ServerMessage(m_player.getSession());
			switchOccur.Init(32);
			switchOccur.sendResponse();
		}
	}

	private void calcEV(Pokemon p, int position, int ammount)
	{
		int evTotal = p.getEvTotal();
		if(evTotal < 510)
			if(evTotal + ammount < 255)
			{
				if(p.getEv(position) < 255)
					if(p.getEv(position) + ammount < 255)
						p.setEv(position, p.getEv(position) + ammount);
					else
						p.setEv(position, 255);
			}
			else if(p.getEv(position) + 510 - evTotal < 255)
				p.setEv(position, p.getEv(position) + 510 - evTotal);
			else
				p.setEv(position, 255);
	}

	/**
	 * Calculates exp gained for Pokemon at the end of battles
	 */
	private void calculateExp()
	{
		/* First calculate earnings */
		int item = PokemonSpecies.getDefaultData().getPokemonByName(m_wildPoke.getSpeciesName()).getRandomItem();
		if(item > -1)
		{
			m_player.getBag().addItem(item, 1);
			/* TcpProtocolHandler.writeMessage(m_player.getTcpSession(), new BattleRewardMessage(BattleRewardType.ITEM, item)); */
			ServerMessage wonItem = new ServerMessage(m_player.getSession());
			wonItem.Init(19);
			wonItem.addInt(item);
			wonItem.sendResponse();
		}
		else
		{
			int money = 5;
			m_player.setMoney(m_player.getMoney() + money);
			m_player.updateClientMoney();
			/* TcpProtocolHandler.writeMessage(m_player.getTcpSession(), new BattleRewardMessage(BattleRewardType.MONEY, money)); */
			ServerMessage wonItem = new ServerMessage(m_player.getSession());
			wonItem.Init(35);
			wonItem.addInt(money);
			wonItem.sendResponse();
		}

		if(m_participatingPokemon.size() > 0)
		{
			double exp = DataService.getBattleMechanics().calculateExpGain(m_wildPoke, m_participatingPokemon.size());
			if(exp == 0)
				exp = 1;

			/* Secondly, calculate EVs and exp */
			// int[] evs = m_wildPoke.getEffortPoints();

			/* Finally, add the EVs and exp to the participating Pokemon */
			for(Pokemon p : m_participatingPokemon)
			{
				/* Add the evs */
				/* Ensure EVs don't go over limit, before or during addition */
				for(String s : hp)
					if(m_wildPoke.getSpeciesName().equalsIgnoreCase(s.split(",")[0]))
						calcEV(p, 0, Integer.parseInt(s.split(",")[1]));
				for(String s : att)
					if(m_wildPoke.getSpeciesName().equalsIgnoreCase(s.split(",")[0]))
						calcEV(p, 1, Integer.parseInt(s.split(",")[1]));

				for(String s : def)
					if(m_wildPoke.getSpeciesName().equalsIgnoreCase(s.split(",")[0]))
						calcEV(p, 2, Integer.parseInt(s.split(",")[1]));

				for(String s : spatt)
					if(m_wildPoke.getSpeciesName().equalsIgnoreCase(s.split(",")[0]))
						calcEV(p, 4, Integer.parseInt(s.split(",")[1]));

				for(String s : spdef)
					if(m_wildPoke.getSpeciesName().equalsIgnoreCase(s.split(",")[0]))
						calcEV(p, 5, Integer.parseInt(s.split(",")[1]));

				for(String s : speed)
					if(m_wildPoke.getSpeciesName().equalsIgnoreCase(s.split(",")[0]))
						calcEV(p, 3, Integer.parseInt(s.split(",")[1]));

				/* Gain exp/level up and update client */
				int index = m_player.getPokemonIndex(p);
				double ratio = (m_wildPoke.getLevel() / p.getLevel());
				if(ratio > 5)
				{
					ratio = 5;
				}
				else if(ratio < 0.1)
				{
					ratio = 0.1;
				}
				double expX = exp * ratio;
				if(expX > 15000)
				{
					expX = 15000;
				}
				else if(expX < 5)
				{
					expX = 5;
				}
				p.setExp(p.getExp() + expX);
				// Calculate how much exp is left to next level
				int expTillLvl = (int) (DataService.getBattleMechanics().getExpForLevel(p, p.getLevel() + 1) - p.getExp());
				// Make sure that value isn't negative.
				if(expTillLvl < 0)
					expTillLvl = 0;
				/* TcpProtocolHandler.writeMessage(m_player.getTcpSession(), new BattleExpMessage(p.getSpeciesName(), exp, expTillLvl)); */
				ServerMessage expMessage = new ServerMessage(m_player.getSession());
				expMessage.Init(28);
				expMessage.addString(p.getSpeciesName() + "," + expX + "," + expTillLvl);
				expMessage.sendResponse();
				String expGain = expX + "";
				expGain = expGain.substring(0, expGain.indexOf('.'));
				// m_player.getTcpSession().write("Pe" + index + expGain);
				ServerMessage expGainMessage = new ServerMessage(m_player.getSession());
				expGainMessage.Init(42);
				expGainMessage.addInt(index);
				expGainMessage.addInt(Integer.parseInt(expGain));
				expGainMessage.sendResponse();

				double levelExp = DataService.getBattleMechanics().getExpForLevel(p, p.getLevel() + 1) - p.getExp();
				if(levelExp <= 0)
				{
					PokemonSpecies pokeData = PokemonSpecies.getDefaultData().getPokemonByName(p.getSpeciesName());
					boolean evolve = false;
					/* Handle evolution */
					for(int i = 0; i < pokeData.getEvolutions().length; i++)
					{
						PokemonEvolution evolution = pokeData.getEvolutions()[i];
						if(evolution.getType() == EvolutionTypes.Level)
						{
							if(evolution.getLevel() <= p.getLevel() + 1)
							{
								p.setEvolution(evolution);
								// m_player.getTcpSession().write("PE" + index);
								ServerMessage evolveMessage = new ServerMessage(m_player.getSession());
								evolveMessage.Init(43);
								evolveMessage.addInt(index);
								evolveMessage.sendResponse();
								evolve = true;
								i = pokeData.getEvolutions().length;
							}
						}
						else if(evolution.getType() == EvolutionTypes.HappinessDay)
						{
							if(p.getHappiness() > 220 && !TimeService.isNight())
							{
								p.setEvolution(evolution);
								// m_player.getTcpSession().write("PE" + index);
								ServerMessage evolveMessage = new ServerMessage(m_player.getSession());
								evolveMessage.Init(43);
								evolveMessage.addInt(index);
								evolveMessage.sendResponse();
								evolve = true;
								i = pokeData.getEvolutions().length;
							}
						}
						else if(evolution.getType() == EvolutionTypes.HappinessNight)
						{
							if(p.getHappiness() > 220 && TimeService.isNight())
							{
								p.setEvolution(evolution);
								// m_player.getTcpSession().write("PE" + index);
								ServerMessage evolveMessage = new ServerMessage(m_player.getSession());
								evolveMessage.Init(43);
								evolveMessage.addInt(index);
								evolveMessage.sendResponse();
								evolve = true;
								i = pokeData.getEvolutions().length;
							}
						}
						else if(evolution.getType() == EvolutionTypes.Happiness)
						{
							if(p.getHappiness() > 220)
							{
								p.setEvolution(evolution);
								// m_player.getTcpSession().write("PE" + index);
								ServerMessage evolveMessage = new ServerMessage(m_player.getSession());
								evolveMessage.Init(43);
								evolveMessage.addInt(index);
								evolveMessage.sendResponse();
								evolve = true;
								i = pokeData.getEvolutions().length;
							}
						}
						// else if(p.getSpeciesName().equalsIgnoreCase("Magneton") && m_player.getMapX() == 2)
						// {
						// p.setEvolution(evolution);
						// // m_player.getTcpSession().write("PE" + index);
						// ServerMessage evolveMessage = new ServerMessage(m_player.getSession());
						// evolveMessage.Init(43);
						// evolveMessage.addInt(index);
						// evolveMessage.sendResponse();
						// evolve = true;
						// i = pokeData.getEvolutions().length;
						// }
					}
					/* If the Pokemon is evolving, don't move learn just yet */
					if(evolve)
						continue;

					/* This Pokemon just levelled up! */
					p.setHappiness(p.getHappiness() + 5);
					p.calculateStats(false);

					int level = DataService.getBattleMechanics().calculateLevel(p);
					m_player.addTrainingExp(level * 5);
					int oldLevel = p.getLevel();
					String move = "";

					/* Move learning */
					p.getMovesLearning().clear();
					for(int i = oldLevel + 1; i <= level; i++)
						if(pokeData.getLevelMoves().get(i) != null)
						{
							move = pokeData.getLevelMoves().get(i);
							p.getMovesLearning().add(move);
							// m_player.getTcpSession().write("Pm" + index + move);
							ServerMessage moveLearn = new ServerMessage(m_player.getSession());
							moveLearn.Init(40);
							moveLearn.addInt(index);
							moveLearn.addString(move);
							moveLearn.sendResponse();
						}

					/* Save the level and update the client */
					p.setLevel(level);
					// m_player.getTcpSession().write("Pl" + index + "," + level + "," + (int)p.getExpForLevel(level+1));
					/* TcpProtocolHandler.writeMessage(m_player.getTcpSession(), new BattleLevelChangeMessage(p.getSpeciesName(), level); */
					ServerMessage levelMessage = new ServerMessage(m_player.getSession());
					levelMessage.Init(44);
					levelMessage.addString(index + "," + level);
					levelMessage.sendResponse();

					ServerMessage BattlelevelMessage = new ServerMessage(m_player.getSession());
					BattlelevelMessage.Init(36);
					BattlelevelMessage.addString(p.getSpeciesName() + "," + level);
					BattlelevelMessage.sendResponse();
					m_player.updateClientPokemonStats(index);
				}
			}
		}
	}

	/**
	 * Returns true if the player can run from the battle
	 * 
	 * @return
	 */
	private boolean canRun()
	{
		// Formula from http://bulbapedia.bulbagarden.net/wiki/Escape
		float A = getActivePokemon()[0].getStat(Pokemon.S_SPEED);
		float B = getActivePokemon()[1].getStat(Pokemon.S_SPEED);
		int C = ++m_runCount;

		float F = (A * 32 / (B / 4) + 30) * C;

		if(F > 255)
			return true;

		if(getMechanics().getRandom().nextInt(255) <= F)
			return true;

		return false;
	}
}
