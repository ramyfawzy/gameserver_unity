package com.gameserver.enums;

import java.util.HashMap;
import java.util.Map;

public enum ClientRequestType {

	ACCOUNT_AUTHENTICATION(1), CHARACTER_SELECTION_INFO(2), CHARACTER_CREATION(3), CHARACTER_DELETION(4),
	CHARACTER_SLOT_UPDATE(5), CHARACTER_SELECT_UPDATE(6), ENTER_WORLD(7), EXIT_WORLD(8), LOCATION_UPDATE(9),
	ANIMATOR_UPDATE(10), OBJECT_INFO(11), PLAYER_OPTIONS_UPDATE(12), CHAT_REQUEST(13), TARGET_UPDATE(14);

	private final int packetId;

	private static final Map<Integer, ClientRequestType> map;
	static {
		map = new HashMap<Integer, ClientRequestType>();
		for (ClientRequestType v : ClientRequestType.values()) {
			map.put(v.packetId, v);
		}
	}

	public static ClientRequestType findByPacketId(int i) {
		return map.get(i);
	}

	private ClientRequestType(int packetId) {
		this.packetId = packetId;
	}

	public int getPacketId() {
		return packetId;
	}

}
