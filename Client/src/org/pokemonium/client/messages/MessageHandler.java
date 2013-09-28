package org.pokemonium.client.messages;

import java.util.HashMap;
import java.util.Map;
import org.pokemonium.client.messages.events.*;

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
		messages.put(100, new ServerRevisionEvent());
		messages.put(1, new ServerNotificationEvent());
		messages.put(2, new ServerAnnouncementEvent());
		messages.put(3, new StartTradeEvent());
		messages.put(4, new TradeOfferEvent());
		messages.put(5, new TradeOfferCancelEvent());
		messages.put(6, new TradeAddPokemonEvent());
		messages.put(7, new TradeEndEvent());
		messages.put(8, new PartySwapEvent());
		messages.put(9, new ShopListEvent());
		messages.put(10, new ShopNoMoneyEvent());
		messages.put(11, new ShopFullPocketEvent());
		messages.put(12, new ShopTypeFullEvent());
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
		messages.put(53, new SkillLevelEvent());
		messages.put(54, new ReturnToLoginEvent());
		messages.put(55, new MoveTutorEvent());
		messages.put(56, new InfoEvent());
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
		messages.put(75, new LoginFailedEvent());
		messages.put(76, new LoginErrorEvent());
		messages.put(77, new LoginOfflineEvent());
		messages.put(78, new LoginFullEvent());
		messages.put(79, new LoginBanEvent());
		messages.put(80, new UpdateItemsEvent());
		messages.put(81, new BagRemoveItemEvent());
		messages.put(82, new RequestNotificationEvent());
		messages.put(83, new TradeRequestEvent());
		messages.put(84, new BattleRequestEvent());
		messages.put(85, new CancelRequestEvent());
		messages.put(86, new RegistrationSuccessEvent());
		messages.put(87, new RegisterNotificationEvent());
		messages.put(88, new MoneyChangeEvent());
		messages.put(89, new PokemonPPEvent());
		messages.put(90, new PokedexInitializeEvent());
		messages.put(91, new PokedexUpdateEvent());
		messages.put(92, new ItemUseEvent());
		messages.put(93, new AlertMessageEvent());
		messages.put(94, new TradeInvalidOfferEvent());
		messages.put(95, new BoatEvent());
		messages.put(96, new TravelEvent());
		messages.put(97, new LoggedElsewhereEvent());
		messages.put(98, new ItemCantUseEvent());
		messages.put(99, new BattlefrontierEvent());
		// 100 is used for server revision
		messages.put(101, new KurtEvent());
		messages.put(102, new PPUpdateEvent());
		/* TODO: Use commented numbers for future messages. */
	}
}
