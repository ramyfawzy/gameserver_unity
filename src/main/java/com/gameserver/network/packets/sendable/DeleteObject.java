package com.gameserver.network.packets.sendable;

import com.gameserver.model.WorldObject;
import com.gameserver.network.SendablePacket;

/**
 * @author Ramy Ibrahim
 */
public class DeleteObject extends SendablePacket
{
	public DeleteObject(WorldObject object)
	{
		// Send the data.
		writeShort(8); // Packet id.
		writeLong(object.getObjectId()); // ID of object to delete.
	}
}
