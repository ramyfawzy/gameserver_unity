package com.gameserver.network.packets.receivable;

import java.util.List;

import com.gameserver.managers.WorldManager;
import com.gameserver.model.WorldObject;
import com.gameserver.model.actor.Player;
import com.gameserver.network.GameClient;
import com.gameserver.network.ReceivablePacket;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * @author ribrahim
 */
public class TargetUpdateRequest implements IRequestHandler {
	@Override
	public void handle(GameClient client, ReceivablePacket packet) {
		// Read data.
		long targetObjectId = packet.readLong();

		// Get player.
		Player player = client.getActiveChar();

		// Remove target.
		if (targetObjectId < 0) {
			player.setTarget(null);
			return;
		}

		// Find target WorldObject.
		List<WorldObject> objects = WorldManager.getVisibleObjects(player);
		for (WorldObject obj : objects) {
			if ((obj != null) && (obj.getObjectId() == targetObjectId)) {
				player.setTarget(obj);
				return;
			}
		}
	}

}
