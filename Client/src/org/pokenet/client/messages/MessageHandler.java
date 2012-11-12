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
import org.pokenet.client.messages.events.InitPokedexEvent;
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
import org.pokenet.client.messages.events.UpdatePokedexEvent;
import org.pokenet.client.messages.events.WeatherChangeEvent;

public class MessageHandler
{
	private Map<Integer, MessageEvent> messages;

	public MessageHandler()
	{
		messages = new HashMap<Integer, MessageEvent>();
	}

	public boolean contains(int id)
	{
		return messages.containsKey(id);
	}

	public MessageEvent get(int id)
	{
		if(messages.containsKey(id))
			return messages.get(id);
		else
			return null;
	}

	public void register()
	{
		messages.put(0, new ServerRevisionEvent()); // R
		messages.put(1, new ServerNotificationEvent()); // !
		messages.put(2, new ServerAnnouncementEvent()); // q
		messages.put(3, new StartTradeEvent()); // Ts
		messages.put(4, new TradeOfferEvent()); // To
		messages.put(5, new TradeCancelEvent()); // //Tc
		messages.put(6, new TradeAddPokemonEvent()); // Ti
		messages.put(7, new EndTradeEvent()); // Tf
		messages.put(8, new PartySwapEvent()); // s
		messages.put(9, new ShopListEvent()); // s
		messages.put(10, new ShopNoMoneyEvent()); // s
		messages.put(11, new ShopFullPocketEvent()); // s
		messages.put(12, new ShopTypeFullEvent()); // s
		messages.put(13, new ShopNoItemEvent());
		messages.put(14, new ShopBuyItemEvent());
		messages.put(15, new ShopSellItemEvent());
		messages.put(16, new ShopSelectSpriteEvent());
		messages.put(17, new BoxAccessEvent());
		messages.put(18, new BattleStartEvent());
		messages.put(19, new BattleWonItemEvent());
		messages.put(20, new BattleNoPPEvent());
		messages.put(21, new BattleEnemyDataEvent());
		messages.put(22, new BattleTrainerNameEvent());
		messages.put(23, new BattleUnknownMessageEvent());
		messages.put(24, new BattleVictoryEvent());
		messages.put(25, new BattleFaintEvent());
		messages.put(26, new BattleMoveEvent());
		messages.put(27, new BattleRequestMoveEvent());
		messages.put(28, new BattleExpEvent());
		messages.put(29, new PokemonReceiveEffectEvent());
		messages.put(30, new PokemonRemoveEffectEvent());
		messages.put(31, new BattleSwitchPokemonEvent());
		messages.put(32, new BattleSwitchOccurEvent());
		messages.put(33, new BattleNotifyHealthEvent());
		messages.put(34, new BattleRunEvent());
		messages.put(35, new BattleEarningsEvent());
		messages.put(36, new BattleLevelupEvent());
		messages.put(37, new StatsUpdateEvent());
		messages.put(38, new PokemonLeavePartyEvent());
		messages.put(39, new InitPokemonEvent());
		messages.put(40, new LevelLearnMoveEvent());
		messages.put(41, new TMLearnMoveEvent());
		messages.put(42, new PokemonGainExpEvent());
		messages.put(43, new PokemonRequestEvolveEvent());
		messages.put(44, new PokemonLevelChangeEvent());
		messages.put(45, new PokemonHPChangeEvent());
		messages.put(46, new FishingRodEvent());
		messages.put(47, new FishingUnableEvent());
		messages.put(48, new FishingGotAwayEvent());
		messages.put(49, new FishingNoNibbleEvent());
		messages.put(50, new ChatEvent());
		messages.put(51, new HMHigherLevelEvent());
		messages.put(52, new BadgeChangeEvent());
		messages.put(53, new BreedLevelEvent());
		messages.put(54, new FishLevelEvent());
		messages.put(55, new TrainerLevelEvent());
		messages.put(56, new CoordinateLevelEvent());
		messages.put(57, new WeatherChangeEvent());
		messages.put(58, new HealPokemonEvent());
		messages.put(59, new FaceDownEvent());
		messages.put(60, new FaceLeftEvent());
		messages.put(61, new FaceRightEvent());
		messages.put(62, new FaceUpEvent());
		messages.put(63, new ChangeSpriteEvent());
		messages.put(64, new UpdateCoordinatesEvent());
		messages.put(65, new PlayerMoveEvent());
		messages.put(66, new MapInitPlayersEvent());
		messages.put(67, new MapAddPlayerEvent());
		messages.put(68, new InitFriendListEvent());
		messages.put(69, new AddFriendListEvent());
		messages.put(70, new RemoveFriendListEvent());
		messages.put(71, new MapRemovePlayerEvent());
		messages.put(72, new SetMapEvent());
		messages.put(73, new PasswordChangeEvent());
		messages.put(74, new LoginSuccessEvent());
		messages.put(75, new LoginUnknownEvent());
		messages.put(76, new LoginErrorEvent());
		messages.put(77, new LoginOfflineEvent());
		messages.put(78, new LoginFullEvent());
		messages.put(79, new LoginBanEvent());
		messages.put(80, new UpdateItemsEvent());
		messages.put(81, new BagRemoveItemEvent());
		messages.put(82, new ItemUseEvent());
		messages.put(82, new RequestNotificationEvent());
		messages.put(83, new TradeRequestEvent());
		messages.put(84, new BattleRequestEvent());
		messages.put(85, new CancelRequestEvent());
		messages.put(86, new RegistrationSuccessEvent());
		messages.put(87, new RegisterNotificationEvent());
		messages.put(88, new MoneyChangeEvent());
		messages.put(89, new PokemonPPEvent());
		messages.put(90, new InitPokedexEvent());
		messages.put(91, new UpdatePokedexEvent());

		// Login
		// this.messages.put(254, new PingEvent());
		// this.messages.put(1, new LoginRequestEvent());
		// this.messages.put(2, new HandshakeEvent());
	}
}
