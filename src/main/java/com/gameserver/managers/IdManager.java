package com.gameserver.managers;

/**
 * @author Ramy Ibrahim
 */
public class IdManager
{
	private static volatile long _lastId = 0;
	
	public static synchronized long getNextId()
	{
		return _lastId++;
	}
}
