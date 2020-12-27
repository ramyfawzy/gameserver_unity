package com.gameserver.network.packets.sendable;

import com.gameserver.model.actor.Player;
import com.gameserver.network.SendablePacket;

/**
 * @author Ramy Ibrahim
 */
public class EnterWorldInformation extends SendablePacket
{
	public EnterWorldInformation(Player player)
	{
		// Packet id.
		writeShort(5);
		writeLong(player.getObjectId());
		// TODO: Send more player information.
	}
}
