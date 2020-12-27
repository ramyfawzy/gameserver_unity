package com.gameserver.network.packets.sendable;

import com.gameserver.network.SendablePacket;

/**
 * @author Ramy Ibrahim
 */
public class CharacterDeletionResult extends SendablePacket
{
	public CharacterDeletionResult()
	{
		// Send the data.
		writeShort(4); // Packet id.
	}
}
