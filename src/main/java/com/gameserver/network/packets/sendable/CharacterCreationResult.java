package com.gameserver.network.packets.sendable;

import com.gameserver.network.SendablePacket;

/**
 * @author Ramy Ibrahim
 */
public class CharacterCreationResult extends SendablePacket
{
	public CharacterCreationResult(int result)
	{
		// Send the data.
		writeShort(3); // Packet id.
		writeByte(result);
	}
}
