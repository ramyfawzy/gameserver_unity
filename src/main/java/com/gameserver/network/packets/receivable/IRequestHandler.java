package com.gameserver.network.packets.receivable;

import com.gameserver.network.GameClient;
import com.gameserver.network.ReceivablePacket;

public interface IRequestHandler {
	public void handle(GameClient client, ReceivablePacket packet);
}
