package net.slqmy.freeze_wand_plugin.enums;

public enum Message {
	FREEZE_WAND_ITEM_NAME,
	FREEZE_WAND_ITEM_LORE,
	GAVE_FREEZE_WAND,
	RECEIVE_FREEZE_WAND,
	FREEZE_PLAYER,
	PLAYER_GOT_FROZEN,
	PLAYER_CANT_MOVE,
	UNFREEZE_PLAYER,
	PLAYER_GOT_UNFROZEN,
	NO_PERMISSION,
	PLAYER_IS_IMMUNE_TO_FREEZE;

	private final String key;

	Message() {
		key = name().toLowerCase().replace('_', '-');
	};

	@Override public String toString() {
		return key;
	}
}
