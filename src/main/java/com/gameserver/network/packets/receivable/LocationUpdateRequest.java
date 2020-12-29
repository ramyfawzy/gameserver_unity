package com.gameserver.network.packets.receivable;

import java.util.List;

import com.gameserver.Config;
import com.gameserver.managers.WorldManager;
import com.gameserver.model.actor.Player;
import com.gameserver.network.GameClient;
import com.gameserver.network.ReceivablePacket;
import com.gameserver.network.packets.sendable.LocationUpdate;
import com.gameserver.network.packets.sendable.Logout;
import org.apache.logging.log4j.LogManager;

/**
 * @author Ramy Ibrahim
 */
public class LocationUpdateRequest implements IRequestHandler {
	private static final int MAX_MOVE_DISTANCE = 300;

	@Override
	public void handle(GameClient client, ReceivablePacket packet) {
		// Read data.
		float posX = packet.readFloat();
		float posY = packet.readFloat();
		float posZ = packet.readFloat();
		float heading = packet.readFloat();

		// Get player.
		Player player = client.getActiveChar();

		// Check if player is outside of world bounds.
		if ((posX < Config.WORLD_MINIMUM_X) || (posX > Config.WORLD_MAXIMUM_X) || (posY < Config.WORLD_MINIMUM_Y)
				|| (posY > Config.WORLD_MAXIMUM_Y) || (posZ < Config.WORLD_MINIMUM_Z)
				|| (posZ > Config.WORLD_MAXIMUM_Z)) {
			player.setLocation(Config.STARTING_LOCATION);
			client.channelSend(new Logout());
			return;
		}

		// Check if player moved too far away via probable exploit.
		if (!player.isTeleporting() && (player.calculateDistance(posX, posY, posZ) > MAX_MOVE_DISTANCE)) {
			player.setLocation(Config.STARTING_LOCATION);
			client.channelSend(new Logout());
			return;
		}

		// Update player location.
		player.setLocation(posX, posY, posZ, heading);

		// Broadcast movement.
		LocationUpdate locationUpdate = new LocationUpdate(player);
		List<Player> players = WorldManager.getVisiblePlayers(player);
		for (Player player2 : players) {
			player2.channelSend(locationUpdate);
		}
	}
}
