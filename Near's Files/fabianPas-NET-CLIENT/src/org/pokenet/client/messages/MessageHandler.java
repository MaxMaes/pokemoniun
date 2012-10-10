package org.pokenet.client.messages;

import java.util.HashMap;
import java.util.Map;

import org.pokenet.client.messages.events.AddFriendListEvent;
import org.pokenet.client.messages.events.BadgeChangeEvent;
import org.pokenet.client.messages.events.BagRemoveItemEvent;
import org.pokenet.client.messages.events.BattleEarningsEvent;
import org.pokenet.client.messages.events.BattleEnemyDataEvent;
import org.pokenet.client.messages.events.BattleExpEvent;
import org.pokenet.client.messages.events.BattleFaintEvent;
import org.pokenet.client.messages.events.BattleLevelupEvent;
import org.pokenet.client.messages.events.BattleMoveEvent;
import org.pokenet.client.messages.events.BattleNoPPEvent;
import org.pokenet.client.messages.events.BattleNotifyHealthEvent;
import org.pokenet.client.messages.events.BattleRequestEvent;
import org.pokenet.client.messages.events.BattleRequestMoveEvent;
import org.pokenet.client.messages.events.BattleRunEvent;
import org.pokenet.client.messages.events.BattleStartEvent;
import org.pokenet.client.messages.events.BattleSwitchOccurEvent;
import org.pokenet.client.messages.events.BattleSwitchPokemonEvent;
import org.pokenet.client.messages.events.BattleTrainerNameEvent;
import org.pokenet.client.messages.events.BattleUnknownMessageEvent;
import org.pokenet.client.messages.events.BattleVictoryEvent;
import org.pokenet.client.messages.events.BattleWonItemEvent;
import org.pokenet.client.messages.events.BoxAccessEvent;
import org.pokenet.client.messages.events.BreedLevelEvent;
import org.pokenet.client.messages.events.CancelRequestEvent;
import org.pokenet.client.messages.events.ChangeSpriteEvent;
import org.pokenet.client.messages.events.ChatEvent;
import org.pokenet.client.messages.events.CoordinateLevelEvent;
import org.pokenet.client.messages.events.EndTradeEvent;
import org.pokenet.client.messages.events.FaceDownEvent;
import org.pokenet.client.messages.events.FaceLeftEvent;
import org.pokenet.client.messages.events.FaceRightEvent;
import org.pokenet.client.messages.events.FaceUpEvent;
import org.pokenet.client.messages.events.FishLevelEvent;
import org.pokenet.client.messages.events.FishingGotAwayEvent;
import org.pokenet.client.messages.events.FishingNoNibbleEvent;
import org.pokenet.client.messages.events.FishingRodEvent;
import org.pokenet.client.messages.events.FishingUnableEvent;
import org.pokenet.client.messages.events.HMHigherLevelEvent;
import org.pokenet.client.messages.events.HealPokemonEvent;
import org.pokenet.client.messages.events.InitFriendListEvent;
import org.pokenet.client.messages.events.InitPokemonEvent;
import org.pokenet.client.messages.events.ItemUseEvent;
import org.pokenet.client.messages.events.LevelLearnMoveEvent;
import org.pokenet.client.messages.events.LoginBanEvent;
import org.pokenet.client.messages.events.LoginErrorEvent;
import org.pokenet.client.messages.events.LoginFullEvent;
import org.pokenet.client.messages.events.LoginOfflineEvent;
import org.pokenet.client.messages.events.LoginSuccessEvent;
import org.pokenet.client.messages.events.LoginUnknownEvent;
import org.pokenet.client.messages.events.MapAddPlayerEvent;
import org.pokenet.client.messages.events.MapInitPlayersEvent;
import org.pokenet.client.messages.events.MapRemovePlayerEvent;
import org.pokenet.client.messages.events.MoneyChangeEvent;
import org.pokenet.client.messages.events.PartySwapEvent;
import org.pokenet.client.messages.events.PasswordChangeEvent;
import org.pokenet.client.messages.events.PlayerMoveEvent;
import org.pokenet.client.messages.events.PokemonGainExpEvent;
import org.pokenet.client.messages.events.PokemonHPChangeEvent;
import org.pokenet.client.messages.events.PokemonLeavePartyEvent;
import org.pokenet.client.messages.events.PokemonLevelChangeEvent;
import org.pokenet.client.messages.events.PokemonPPEvent;
import org.pokenet.client.messages.events.PokemonReceiveEffectEvent;
import org.pokenet.client.messages.events.PokemonRemoveEffectEvent;
import org.pokenet.client.messages.events.PokemonRequestEvolveEvent;
import org.pokenet.client.messages.events.RegisterNotificationEvent;
import org.pokenet.client.messages.events.RegistrationSuccessEvent;
import org.pokenet.client.messages.events.RemoveFriendListEvent;
import org.pokenet.client.messages.events.RequestNotificationEvent;
import org.pokenet.client.messages.events.ServerAnnouncementEvent;
import org.pokenet.client.messages.events.ServerNotificationEvent;
import org.pokenet.client.messages.events.ServerRevisionEvent;
import org.pokenet.client.messages.events.SetMapEvent;
import org.pokenet.client.messages.events.ShopBuyItemEvent;
import org.pokenet.client.messages.events.ShopFullPocketEvent;
import org.pokenet.client.messages.events.ShopListEvent;
import org.pokenet.client.messages.events.ShopNoItemEvent;
import org.pokenet.client.messages.events.ShopNoMoneyEvent;
import org.pokenet.client.messages.events.ShopSelectSpriteEvent;
import org.pokenet.client.messages.events.ShopSellItemEvent;
import org.pokenet.client.messages.events.ShopTypeFullEvent;
import org.pokenet.client.messages.events.StartTradeEvent;
import org.pokenet.client.messages.events.StatsUpdateEvent;
import org.pokenet.client.messages.events.TMLearnMoveEvent;
import org.pokenet.client.messages.events.TradeAddPokemonEvent;
import org.pokenet.client.messages.events.TradeCancelEvent;
import org.pokenet.client.messages.events.TradeOfferEvent;
import org.pokenet.client.messages.events.TradeRequestEvent;
import org.pokenet.client.messages.events.TrainerLevelEvent;
import org.pokenet.client.messages.events.UpdateCoordinatesEvent;
import org.pokenet.client.messages.events.UpdateItemsEvent;
import org.pokenet.client.messages.events.WeatherChangeEvent;

public class MessageHandler
{
	private Map<Integer, MessageEvent> messages;

	public MessageHandler()
	{
		this.messages = new HashMap<Integer, MessageEvent>();
	}
	public boolean contains(int id)
	{
		return this.messages.containsKey(id);
	}

	public MessageEvent get(int id)
	{
		if (this.messages.containsKey(id))
		{
			return this.messages.get(id);
		}
		else {
			return null;
		}
	}

	public void register()
	{
		this.messages.put(0, new ServerRevisionEvent()); // R
		this.messages.put(1, new ServerNotificationEvent()); // !
		this.messages.put(2, new ServerAnnouncementEvent()); // q
		this.messages.put(3, new StartTradeEvent()); // Ts
		this.messages.put(4, new TradeOfferEvent()); // To
		this.messages.put(5, new TradeCancelEvent()); // //Tc
		this.messages.put(6, new TradeAddPokemonEvent()); // Ti
		this.messages.put(7, new EndTradeEvent()); // Tf
		this.messages.put(8, new PartySwapEvent()); // s
		this.messages.put(9, new ShopListEvent()); // s
		this.messages.put(10, new ShopNoMoneyEvent()); // s
		this.messages.put(11, new ShopFullPocketEvent()); // s
		this.messages.put(12, new ShopTypeFullEvent()); // s
		this.messages.put(13, new ShopNoItemEvent());
		this.messages.put(14, new ShopBuyItemEvent());
		this.messages.put(15, new ShopSellItemEvent());
		this.messages.put(16, new ShopSelectSpriteEvent());
		this.messages.put(17, new BoxAccessEvent());
		this.messages.put(18, new BattleStartEvent());
		this.messages.put(19, new BattleWonItemEvent());
		this.messages.put(20, new BattleNoPPEvent());
		this.messages.put(21, new BattleEnemyDataEvent());
		this.messages.put(22, new BattleTrainerNameEvent());
		this.messages.put(23, new BattleUnknownMessageEvent());
		this.messages.put(24, new BattleVictoryEvent());
		this.messages.put(25, new BattleFaintEvent());
		this.messages.put(26, new BattleMoveEvent());
		this.messages.put(27, new BattleRequestMoveEvent());
		this.messages.put(28, new BattleExpEvent());
		this.messages.put(29, new PokemonReceiveEffectEvent());
		this.messages.put(30, new PokemonRemoveEffectEvent());
		this.messages.put(31, new BattleSwitchPokemonEvent());
		this.messages.put(32, new BattleSwitchOccurEvent());
		this.messages.put(33, new BattleNotifyHealthEvent());
		this.messages.put(34, new BattleRunEvent());
		this.messages.put(35, new BattleEarningsEvent());
		this.messages.put(36, new BattleLevelupEvent());
		this.messages.put(37, new StatsUpdateEvent());
		this.messages.put(38, new PokemonLeavePartyEvent());
		this.messages.put(39, new InitPokemonEvent());
		this.messages.put(40, new LevelLearnMoveEvent());
		this.messages.put(41, new TMLearnMoveEvent());
		this.messages.put(42, new PokemonGainExpEvent());
		this.messages.put(43, new PokemonRequestEvolveEvent());
		this.messages.put(44, new PokemonLevelChangeEvent());
		this.messages.put(45, new PokemonHPChangeEvent());
		this.messages.put(46, new FishingRodEvent());
		this.messages.put(47, new FishingUnableEvent());
		this.messages.put(48, new FishingGotAwayEvent());
		this.messages.put(49, new FishingNoNibbleEvent());
		this.messages.put(50, new ChatEvent());
		this.messages.put(51, new HMHigherLevelEvent());
		this.messages.put(52, new BadgeChangeEvent());
		this.messages.put(53, new BreedLevelEvent());
		this.messages.put(54, new FishLevelEvent());
		this.messages.put(55, new TrainerLevelEvent());
		this.messages.put(56, new CoordinateLevelEvent());
		this.messages.put(57, new WeatherChangeEvent());
		this.messages.put(58, new HealPokemonEvent());
		this.messages.put(59, new FaceDownEvent());
		this.messages.put(60, new FaceLeftEvent());
		this.messages.put(61, new FaceRightEvent());
		this.messages.put(62, new FaceUpEvent());
		this.messages.put(63, new ChangeSpriteEvent());
		this.messages.put(64, new UpdateCoordinatesEvent());
		this.messages.put(65, new PlayerMoveEvent());
		this.messages.put(66, new MapInitPlayersEvent());
		this.messages.put(67, new MapAddPlayerEvent());
		this.messages.put(68, new InitFriendListEvent());
		this.messages.put(69, new AddFriendListEvent());
		this.messages.put(70, new RemoveFriendListEvent());
		this.messages.put(71, new MapRemovePlayerEvent());
		this.messages.put(72, new SetMapEvent());
		this.messages.put(73, new PasswordChangeEvent());
		this.messages.put(74, new LoginSuccessEvent());
		this.messages.put(75, new LoginUnknownEvent());
		this.messages.put(76, new LoginErrorEvent());
		this.messages.put(77, new LoginOfflineEvent());
		this.messages.put(78, new LoginFullEvent());
		this.messages.put(79, new LoginBanEvent());
		this.messages.put(80, new UpdateItemsEvent());
		this.messages.put(81, new BagRemoveItemEvent());
		this.messages.put(82, new ItemUseEvent());
		this.messages.put(82, new RequestNotificationEvent());
		this.messages.put(83, new TradeRequestEvent());
		this.messages.put(84, new BattleRequestEvent());
		this.messages.put(85, new CancelRequestEvent());
		this.messages.put(86, new RegistrationSuccessEvent());
		this.messages.put(87, new RegisterNotificationEvent());
		this.messages.put(88, new MoneyChangeEvent());
		this.messages.put(89, new PokemonPPEvent());
		
		// Login
		//this.messages.put(254, new PingEvent());
		//this.messages.put(1, new LoginRequestEvent());
		//this.messages.put(2, new HandshakeEvent());
	}
}
