package com.gameserver.network.packets.sendable;

import com.gameserver.network.SendablePacket;

/**
 * @author Ramy Ibrahim
 */
public class AccountAuthenticationResult extends SendablePacket
{
	public AccountAuthenticationResult(int result)
	{
		// Send the data.
		writeShort(1); // Packet id.
		writeByte(result); // 0 does not exist, 1 banned, 2 requires activation, 3 wrong password, 4 already logged, 5 too many online, 6 incorrect client, 100 authenticated
	}
}
