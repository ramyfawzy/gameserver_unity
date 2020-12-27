package com.gameserver.network.packets.sendable;

import com.gameserver.network.SendablePacket;

/**
 * @author ribrahim
 */
public class AnimatorUpdate extends SendablePacket
{
	public AnimatorUpdate(long objectId, float velocityX, float velocityZ, boolean triggerJump, boolean isInWater, boolean isGrounded)
	{
		// Send the data.
		writeShort(11); // Packet id.
		writeLong(objectId);
		writeFloat(velocityX);
		writeFloat(velocityZ);
		writeByte(triggerJump ? 1 : 0);
		writeByte(isInWater ? 1 : 0);
		writeByte(isGrounded ? 1 : 0);
	}
	
}
