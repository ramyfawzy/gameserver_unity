package com.gameserver.network.packets.receivable;

import java.util.List;

import com.gameserver.managers.WorldManager;
import com.gameserver.model.actor.Player;
import com.gameserver.model.holders.AnimationHolder;
import com.gameserver.network.GameClient;
import com.gameserver.network.ReceivablePacket;
import com.gameserver.network.packets.sendable.AnimatorUpdate;

/**
 * @author ribrahim
 */
public class AnimatorUpdateRequest implements IRequestHandler {

	@Override
	public void handle(GameClient client, ReceivablePacket packet) {
		// Read data.
		float velocityX = packet.readFloat();
		float velocityZ = packet.readFloat();
		boolean triggerJump = packet.readByte() == 1;
		boolean isInWater = packet.readByte() == 1;
		boolean isGrounded = packet.readByte() == 1;

		// Set last known world object animations.
		Player player = client.getActiveChar();
		player.setAnimations(new AnimationHolder(velocityX, velocityZ, triggerJump, isInWater, isGrounded));

		// Broadcast movement.
		AnimatorUpdate animatorUpdate = new AnimatorUpdate(player.getObjectId(), velocityX, velocityZ, triggerJump,
				isInWater, isGrounded);
		List<Player> players = WorldManager.getVisiblePlayers(player);
		for (Player player2 : players) {
			player2.channelSend(animatorUpdate);
		}

	}

}
