package com.gameserver.network.packets.sendable;

import com.gameserver.model.WorldObject;
import com.gameserver.network.SendablePacket;

/**
 * @author ribrahim
 */
public class LocationUpdate extends SendablePacket
{
	private final WorldObject obj;
	private final boolean teleport;
	
	public LocationUpdate(WorldObject obj)
	{
		this.obj = obj;
		teleport = false;
		writeData();
	}
	
	// Only used for client active player teleports.
	public LocationUpdate(WorldObject obj, boolean teleport)
	{
		this.obj = obj;
		this.teleport = teleport;
		writeData();
	}
	
	private void writeData()
	{
		writeShort(10); // Packet id.
		writeLong(teleport ? 0 : obj.getObjectId());
		writeFloat(obj.getLocation().getX());
		writeFloat(obj.getLocation().getY());
		writeFloat(obj.getLocation().getZ());
		writeFloat(obj.getLocation().getHeading());
	}
	
}
