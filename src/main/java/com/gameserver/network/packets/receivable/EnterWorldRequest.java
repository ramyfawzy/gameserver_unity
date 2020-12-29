package com.gameserver.network.packets.receivable;

import java.util.List;

import com.gameserver.managers.IDatabaseManager;
import com.gameserver.managers.WorldManager;
import com.gameserver.model.WorldObject;
import com.gameserver.model.actor.Player;
import com.gameserver.network.GameClient;
import com.gameserver.network.ReceivablePacket;
import com.gameserver.network.packets.sendable.NpcInformation;
import com.gameserver.network.packets.sendable.PlayerInformation;
import com.gameserver.network.packets.sendable.PlayerInventoryUpdate;
import com.gameserver.network.packets.sendable.PlayerOptionsInformation;
import com.google.inject.Inject;

import org.apache.logging.log4j.LogManager;

/**
 * @author Ramy Ibrahim
 */
public class EnterWorldRequest implements IRequestHandler {
	
	@Inject
	IDatabaseManager databaseManager;

	void broadcastAndReceiveInfo(Player player) {
		// Send and receive visible object information.
		PlayerInformation playerInfo = new PlayerInformation(player);
		List<Player> players = WorldManager.getVisiblePlayers(player);
		for (Player nearby : players) {
			// Send the information to the current player.
			player.channelSend(new PlayerInformation(nearby));
			// Send information to the other player as well.
			nearby.channelSend(playerInfo);
		}

		// Send nearby NPC information.
		List<WorldObject> objects = WorldManager.getVisibleObjects(player);
		for (WorldObject nearby : objects) {
			if (!nearby.isNpc()) {
				continue;
			}
			player.channelSend(new NpcInformation(nearby.asNpc()));
		}
	}

	@Override
	public void handle(GameClient client, ReceivablePacket packet) {
		// Read data.
		String characterName = packet.readString();

		// Create a new PlayerInstance.
		Player player = new Player(client, characterName, databaseManager);
		// Add object to the world.
		WorldManager.addObject(player);
		// Assign this player to client.
		client.setActiveChar(player);

		// Send user interface information to client.
		client.channelSend(new PlayerOptionsInformation(player, databaseManager));

		// Send all inventory items to client.
		client.channelSend(new PlayerInventoryUpdate(player));

		// Use a task to send and receive nearby player information,
		// because we need to have player initialization be complete in client side.
		new java.util.Timer().schedule(new java.util.TimerTask() {
			@Override
			public void run() {
				broadcastAndReceiveInfo(player);
			}
		}, 1000);
	}
}
