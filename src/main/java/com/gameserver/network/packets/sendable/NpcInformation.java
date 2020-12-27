package com.gameserver.network.packets.sendable;

import com.gameserver.model.actor.Npc;
import com.gameserver.network.SendablePacket;

/**
 * @author ribrahim
 */
public class NpcInformation extends SendablePacket
{
	public NpcInformation(Npc npc)
	{
		// Packet id.
		writeShort(7);
		// Npc information.
		writeLong(npc.getObjectId());
		writeInt(npc.getNpcHolder().getNpcId());
		writeFloat(npc.getLocation().getX());
		writeFloat(npc.getLocation().getY());
		writeFloat(npc.getLocation().getZ());
		writeFloat(npc.getLocation().getHeading());
		writeLong(npc.getCurrentHp());
	}
	
}
