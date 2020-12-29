package com.gameserver.network.packets.receivable;

import com.gameserver.managers.DatabaseManager;
import com.gameserver.network.GameClient;
import com.gameserver.network.ReceivablePacket;
import com.gameserver.network.packets.sendable.CharacterSelectionInfoResult;
import com.google.inject.Inject;

/**
 * @author Ramy Ibrahim
 */
public class CharacterSelectionInfoRequest implements IRequestHandler {
	
	@Inject
	DatabaseManager databaseManager;
	
	@Override
	public void handle(GameClient client, ReceivablePacket packet) {
		// Read data.
		final String accountName = packet.readString().toLowerCase();

		// If account has logged send the information.
		if (client.getAccountName().equals(accountName)) {
			client.channelSend(new CharacterSelectionInfoResult(accountName, databaseManager));
		}
	}
}
