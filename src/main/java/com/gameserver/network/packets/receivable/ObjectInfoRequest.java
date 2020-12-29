package com.gameserver.network.packets.receivable;

import java.util.List;

import com.gameserver.managers.WorldManager;
import com.gameserver.model.WorldObject;
import com.gameserver.model.actor.Player;
import com.gameserver.model.holders.AnimationHolder;
import com.gameserver.network.GameClient;
import com.gameserver.network.ReceivablePacket;
import com.gameserver.network.packets.sendable.AnimatorUpdate;
import com.gameserver.network.packets.sendable.NpcInformation;
import com.gameserver.network.packets.sendable.PlayerInformation;
import org.apache.logging.log4j.LogManager;

/**
 * @author Ramy Ibrahim
 */
public class ObjectInfoRequest implements IRequestHandler {
	void sendAnimationInfo(GameClient client, WorldObject obj) {
		if (obj != null) {
			AnimationHolder animations = obj.getAnimations();
			if (animations != null) {
				client.channelSend(
						new AnimatorUpdate(obj.getObjectId(), animations.getVelocityX(), animations.getVelocityZ(),
								animations.isTriggerJump(), animations.isInWater(), animations.isGrounded()));
			}
		}
	}

	@Override
	public void handle(GameClient client, ReceivablePacket packet) {
		System.out.println("ObjectInfoRequest LONGGG");
		// Read data.
		long objectId = packet.readLong();

		// Get the acting player.
		Player player = client.getActiveChar();
		// Send the information.
		List<WorldObject> objects = WorldManager.getVisibleObjects(player);
		for (WorldObject obj : objects) {
			if (obj.getObjectId() == objectId) {
				if (obj.isPlayer()) {
					client.channelSend(new PlayerInformation(obj.asPlayer()));
				} else if (obj.isNpc()) {
					client.channelSend(new NpcInformation(obj.asNpc()));
				}

				// Send delayed animation update in case object was already moving.
				new java.util.Timer().schedule(new java.util.TimerTask() {
					@Override
					public void run() {
						sendAnimationInfo(client, obj);
					}
				}, 1000);

				break;
			}
		}
	}
}
