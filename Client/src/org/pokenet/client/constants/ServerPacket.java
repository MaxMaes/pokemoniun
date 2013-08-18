package org.pokenet.client.constants;

public enum ServerPacket
{
	REGISTRATION(1),
	CHANGE_PASSWORD(2),
	FORCE_LOGIN(3),
	MOVE_UP(4),
	MOVE_DOWN(5),
	MOVE_LEFT(6),
	MOVE_RIGHT(7),
	LEARN_MOVE(8),
	DONT_LEARN_MOVE(9),
	DONT_EVOLVE(10),
	EVOLVE(11),
	SWAP_PARTY(12),
	BUY_SELL_ITEMS(13),
	BUY_SPRITE(14),
	REQUEST_BATTLE(15),
	REQUEST_TRADE(16),
	REQUEST_ACCEPTED(17),
	REQUEST_DECLINED(18),
	REQUEST_INFO_BOX_NUMBER(19),
	RELEASE_POKEMON(20),
	SWAP_POKEMON_FROM_BOX(21),
	FINISH_BOX_INTERACTION(22),
	//X(23),
	//X(24),
	//X(25),
	//X(26),
	//X(27),
	//X(28),
	//X(29),
	//X(30),
	//X(31),
	//X(32),
	PLAYER_COMMAND(33),
	//X(34),
	MOVE_SELECTED(35),
	SWITCH_POKEMON(36),
	BATTLE_RUN(37),
	FRIEND_ADD(38),
	FRIEND_REMOVE(39),
	ITEM_USE(40),
	ITEM_DESTROY(41),
	TRADE_OFFER(42),
	TRADE_ACCEPTED(43),
	CANCEL_OFFER(44),
	TRADE_CANCEL(45),
	CHAT(46),
	TALKING_START(47),
	TALKING_FINISH(48),
	LOGOUT_REQUEST(49),
	LOGIN(50),
	ITEM_GIVE(51),
	//X(52),
	TRAVEL(53),
	//X(54),
	//X(55),
	//X(56),
	//X(57),
	BATTLEFRONTIER_FACILITY(58);

	private int code = -1;

	private ServerPacket(int code)
	{
		this.code = code;
	}

	public int getValue()
	{
		return code;
	}
}
