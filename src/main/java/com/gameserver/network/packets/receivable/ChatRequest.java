package com.gameserver.network.packets.receivable;

import com.gameserver.managers.ChatManager;
import com.gameserver.model.actor.Player;
import com.gameserver.network.GameClient;
import com.gameserver.network.ReceivablePacket;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * @author Ramy Ibrahim
 */
public class ChatRequest implements IRequestHandler {
	@Override
	public void handle(GameClient client, ReceivablePacket packet) {
		// Read data.
		final String message = packet.readString();

		// Handle message.
		final Player sender = client.getActiveChar();
		if (sender != null) {
			ChatManager.handleChat(sender, message);
		}
	}
}
