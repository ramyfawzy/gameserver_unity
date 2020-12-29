package com.gameserver.network.packets.receivable;

import com.gameserver.managers.WorldManager;
import com.gameserver.network.GameClient;
import com.gameserver.network.ReceivablePacket;
import org.apache.logging.log4j.LogManager;

/**
 * @author ribrahim
 */
public class ExitWorldRequest implements IRequestHandler {
	@Override
	public void handle(GameClient client, ReceivablePacket packet) {
		WorldManager.removeObject(client.getActiveChar());
		client.setActiveChar(null);
	}

}
