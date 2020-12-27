package com.gameserver.model.holders;

/**
 * @author Ramy Ibrahim
 */
public class SpawnHolder
{
	private final LocationHolder _location;
	private final int _respawnTime;
	
	public SpawnHolder(LocationHolder location, int respawnTime)
	{
		_location = location;
		_respawnTime = respawnTime;
	}
	
	public LocationHolder getLocation()
	{
		return _location;
	}
	
	public int getRespawnDelay()
	{
		return _respawnTime;
	}
}
