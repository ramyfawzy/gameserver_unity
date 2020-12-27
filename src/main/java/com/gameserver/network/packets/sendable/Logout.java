package com.gameserver.network.packets.sendable;

import com.gameserver.network.SendablePacket;

/**
 * @author Ramy Ibrahim
 */
public class Logout extends SendablePacket
{
	public Logout()
	{
		// Send the data.
		writeShort(9); // Packet id.
	}
}
