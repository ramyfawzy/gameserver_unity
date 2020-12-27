package com.gameserver.model;

import java.util.List;

import org.joda.time.DateTime;

import com.gameserver.managers.IdManager;
import com.gameserver.managers.WorldManager;
import com.gameserver.model.actor.Creature;
import com.gameserver.model.actor.Monster;
import com.gameserver.model.actor.Npc;
import com.gameserver.model.actor.Player;
import com.gameserver.model.holders.AnimationHolder;
import com.gameserver.model.holders.LocationHolder;
import com.gameserver.model.holders.RegionHolder;
import com.gameserver.network.packets.sendable.DeleteObject;
import com.gameserver.network.packets.sendable.LocationUpdate;
import com.gameserver.network.packets.sendable.NpcInformation;

/**
 * @author Ramy Ibrahim
 */
public class WorldObject
{
	private final long _objectId = IdManager.getNextId();
	private final DateTime _spawnTime = DateTime.now();
	private AnimationHolder _animations = null;
	private final LocationHolder _location = new LocationHolder(0, -1000, 0);
	private RegionHolder _region = null;
	private boolean _isTeleporting = false;
	
	public long getObjectId()
	{
		return _objectId;
	}
	
	public DateTime getSpawnTime()
	{
		return _spawnTime;
	}
	
	public AnimationHolder getAnimations()
	{
		return _animations;
	}
	
	public void setAnimations(AnimationHolder animations)
	{
		_animations = animations;
	}
	
	public LocationHolder getLocation()
	{
		return _location;
	}
	
	public void setLocation(LocationHolder location)
	{
		setLocation(location.getX(), location.getY(), location.getZ(), location.getHeading());
	}
	
	public synchronized void setLocation(float posX, float posY, float posZ, float heading)
	{
		_location.update(posX, posY, posZ, heading);
		
		// When changing location test for appropriate region.
		RegionHolder testRegion = WorldManager.getRegion(this);
		if (!testRegion.equals(_region))
		{
			if (_region != null)
			{
				// Remove this object from the region.
				_region.removeObject(this);
				
				// Broadcast change to players left behind when teleporting.
				if (_isTeleporting)
				{
					DeleteObject deleteObject = new DeleteObject(this);
					List<RegionHolder> regions = _region.getSurroundingRegions();
					for (RegionHolder region : regions)
					{
						List<WorldObject> objects = region.getObjects();
						for (WorldObject nearby : objects)
						{
							if (nearby == this)
							{
								continue;
							}
							if (nearby.isPlayer())
							{
								nearby.asPlayer().channelSend(deleteObject);
							}
						}
					}
				}
			}
			
			// Assign new region.
			_region = testRegion;
			_region.addObject(this);
			
			// Send visible NPC information.
			// TODO: Exclude known NPCs?
			if (isPlayer())
			{
				List<WorldObject> objects = WorldManager.getVisibleObjects(this);
				for (WorldObject nearby : objects)
				{
					if (!nearby.isNpc())
					{
						continue;
					}
					asPlayer().channelSend(new NpcInformation(nearby.asNpc()));
				}
			}
		}
	}
	
	public RegionHolder getRegion()
	{
		return _region;
	}
	
	public void setTeleporting()
	{
		_isTeleporting = true;
		new java.util.Timer().schedule(new java.util.TimerTask()
		{
			@Override
			public void run()
			{
				stopTeleporting();
			}
		}, 1000);
		
	}
	
	void stopTeleporting()
	{
		_isTeleporting = false;
		
		// Broadcast location to nearby players after teleporting.
		LocationUpdate locationUpdate = new LocationUpdate(this);
		List<Player> players = WorldManager.getVisiblePlayers(this);
		Player player = asPlayer();
		for (Player nearby : players)
		{
			if (nearby.isPlayer())
			{
				nearby.asPlayer().channelSend(locationUpdate);
			}
			if (isPlayer())
			{
				player.channelSend(new LocationUpdate(nearby));
			}
		}
	}
	
	public boolean isTeleporting()
	{
		return _isTeleporting;
	}
	
	public void deleteMe()
	{
		// Remove from region.
		_region.removeObject(this);
		
		// Broadcast NPC deletion.
		DeleteObject delete = new DeleteObject(this);
		List<RegionHolder> regions = _region.getSurroundingRegions();
		for (RegionHolder region : regions)
		{
			List<WorldObject> objects = region.getObjects();
			for (WorldObject nearby : objects)
			{
				if ((nearby != null) && nearby.isPlayer())
				{
					nearby.asPlayer().channelSend(delete);
				}
			}
		}
		
		// Set region to null.
		_region = null;
	}
	
	public double calculateDistance(float x, float y, float z)
	{
		return Math.pow(x - _location.getX(), 2) + Math.pow(y - _location.getY(), 2) + Math.pow(z - _location.getZ(), 2);
	}
	
	public double calculateDistance(WorldObject obj)
	{
		return calculateDistance(obj.getLocation().getX(), obj.getLocation().getY(), obj.getLocation().getZ());
	}
	
	public boolean isCreature()
	{
		return false;
	}
	
	public Creature asCreature()
	{
		return null;
	}
	
	public boolean isPlayer()
	{
		return false;
	}
	
	public Player asPlayer()
	{
		return null;
	}
	
	public boolean isNpc()
	{
		return false;
	}
	
	public Npc asNpc()
	{
		return null;
	}
	
	public boolean isMonster()
	{
		return false;
	}
	
	public Monster asMonster()
	{
		return null;
	}
	
	@Override
	public String toString()
	{
		return "WorldObject [" + _objectId + "]";
	}
}
