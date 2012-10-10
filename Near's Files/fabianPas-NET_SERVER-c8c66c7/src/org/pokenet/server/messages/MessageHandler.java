package org.pokenet.server.messages;

import java.util.HashMap;
import java.util.Map;

import org.pokenet.server.messages.events.AcceptRequestEvent;
import org.pokenet.server.messages.events.AddFriendEvent;
import org.pokenet.server.messages.events.AllowEvolutionEvent;
import org.pokenet.server.messages.events.AnnounceMessageEvent;
import org.pokenet.server.messages.events.BanPlayerEvent;
import org.pokenet.server.messages.events.BattleRequestEvent;
import org.pokenet.server.messages.events.BoxInfoEvent;
import org.pokenet.server.messages.events.CanLearnMoveEvent;
import org.pokenet.server.messages.events.CancelEvolutionEvent;
import org.pokenet.server.messages.events.CancelTradeEvent;
import org.pokenet.server.messages.events.ChangePasswordEvent;
import org.pokenet.server.messages.events.ChatEvent;
import org.pokenet.server.messages.events.DeclineRequestEvent;
import org.pokenet.server.messages.events.DropItemEvent;
import org.pokenet.server.messages.events.FinishBoxingEvent;
import org.pokenet.server.messages.events.FinishTalkingEvent;
import org.pokenet.server.messages.events.ForceLoginEvent;
import org.pokenet.server.messages.events.KickEvent;
import org.pokenet.server.messages.events.LoginEvent;
import org.pokenet.server.messages.events.MoveDownEvent;
import org.pokenet.server.messages.events.MoveLeftEvent;
import org.pokenet.server.messages.events.MoveRightEvent;
import org.pokenet.server.messages.events.MoveUpEvent;
import org.pokenet.server.messages.events.MuteEvent;
import org.pokenet.server.messages.events.PartySwapEvent;
import org.pokenet.server.messages.events.PlayersOnlineEvent;
import org.pokenet.server.messages.events.PokemonSwitchEvent;
import org.pokenet.server.messages.events.RegisterEvent;
import org.pokenet.server.messages.events.ReleasePokemonEvent;
import org.pokenet.server.messages.events.RemoveFriendEvent;
import org.pokenet.server.messages.events.RunEvent;
import org.pokenet.server.messages.events.SelectedMoveEvent;
import org.pokenet.server.messages.events.ServerAnnouncementEvent;
import org.pokenet.server.messages.events.ServerNotificationEvent;
import org.pokenet.server.messages.events.ShopEvent;
import org.pokenet.server.messages.events.SpriteEvent;
import org.pokenet.server.messages.events.StartTalkingEvent;
import org.pokenet.server.messages.events.StopServerEvent;
import org.pokenet.server.messages.events.SwapPokemonBoxPartyEvent;
import org.pokenet.server.messages.events.TradeCancelOfferEvent;
import org.pokenet.server.messages.events.TradeOfferEvent;
import org.pokenet.server.messages.events.TradeReadyEvent;
import org.pokenet.server.messages.events.TradeRequestEvent;
import org.pokenet.server.messages.events.UnMuteEvent;
import org.pokenet.server.messages.events.UnableLearnMoveEvent;
import org.pokenet.server.messages.events.UnbanEvent;
import org.pokenet.server.messages.events.UseItemEvent;
import org.pokenet.server.messages.events.WarpEvent;
import org.pokenet.server.messages.events.WeatherEvent;

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
		this.messages.put(0, new LoginEvent());
		this.messages.put(1, new RegisterEvent());
		this.messages.put(2, new ChangePasswordEvent());
		this.messages.put(3, new ForceLoginEvent());
		this.messages.put(4, new MoveUpEvent());
		this.messages.put(5, new MoveDownEvent());
		this.messages.put(6, new MoveLeftEvent());
		this.messages.put(7, new MoveRightEvent());
		this.messages.put(8, new CanLearnMoveEvent());
		this.messages.put(9, new UnableLearnMoveEvent());
		this.messages.put(10, new CancelEvolutionEvent());
		this.messages.put(11, new AllowEvolutionEvent());
		this.messages.put(12, new PartySwapEvent());
		this.messages.put(13, new ShopEvent());
		this.messages.put(14, new SpriteEvent());
		this.messages.put(15, new BattleRequestEvent());
		this.messages.put(16, new TradeRequestEvent());
		this.messages.put(17, new AcceptRequestEvent());
		this.messages.put(18, new DeclineRequestEvent());
		this.messages.put(19, new BoxInfoEvent());
		this.messages.put(20, new ReleasePokemonEvent());
		this.messages.put(21, new SwapPokemonBoxPartyEvent());
		this.messages.put(22, new FinishBoxingEvent());
		this.messages.put(23, new PlayersOnlineEvent());
		this.messages.put(24, new ServerAnnouncementEvent());
		this.messages.put(25, new ServerNotificationEvent());
		this.messages.put(26, new BanPlayerEvent());
		this.messages.put(27, new UnbanEvent());
		this.messages.put(28, new WarpEvent());
		this.messages.put(29, new MuteEvent());
		this.messages.put(30, new UnMuteEvent());
		this.messages.put(31, new KickEvent());
		this.messages.put(32, new WeatherEvent());
		this.messages.put(33, new StopServerEvent());
		this.messages.put(34, new AnnounceMessageEvent());
		this.messages.put(35, new SelectedMoveEvent());
		this.messages.put(36, new PokemonSwitchEvent());
		this.messages.put(37, new RunEvent());
		this.messages.put(38, new AddFriendEvent());
		this.messages.put(39, new RemoveFriendEvent());
		this.messages.put(40, new UseItemEvent());
		this.messages.put(41, new DropItemEvent());
		this.messages.put(42, new TradeOfferEvent());
		this.messages.put(43, new TradeReadyEvent());
		this.messages.put(44, new TradeCancelOfferEvent());
		this.messages.put(45, new CancelTradeEvent());
		this.messages.put(46, new ChatEvent());
		this.messages.put(47, new StartTalkingEvent());
		this.messages.put(48, new FinishTalkingEvent());
	}
}
