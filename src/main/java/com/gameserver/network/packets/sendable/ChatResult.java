package com.gameserver.network.packets.sendable;

import com.gameserver.network.SendablePacket;

/**
 * @author Ramy Ibrahim
 */
public class ChatResult extends SendablePacket
{
	public ChatResult(byte chatType, String sender, String message)
	{
		// Send the data.
		writeShort(12); // Packet id.
		writeByte(chatType); // 0 system, 1 normal chat, 2 personal message
		writeString(sender);
		writeString(message);
	}
}
