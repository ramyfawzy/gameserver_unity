package com.gameserver.model.holders;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.gameserver.model.WorldObject;

/**
 * @author ribrahim
 */
public class RegionHolder
{
	private final int _x;
	private final int _z;
	private final List<WorldObject> _objects = new ArrayList<>();
	private List<RegionHolder> _surroundingRegions;
	
	public RegionHolder(int x, int z)
	{
		_x = x;
		_z = z;
	}
	
	public void setSurroundingRegions(List<RegionHolder> regions)
	{
		_surroundingRegions = regions;
		
		// Make sure that this region is always first to improve bulk operations.
		for (int i = 0; i < _surroundingRegions.size(); i++)
		{
			if (_surroundingRegions.get(i) == this)
			{
				RegionHolder first = _surroundingRegions.get(0);
				_surroundingRegions.add(0, this);
				_surroundingRegions.add(i, first);
				break;
			}
		}
	}
	
	public List<RegionHolder> getSurroundingRegions()
	{
		return _surroundingRegions;
	}
	
	public void addObject(WorldObject obj)
	{
		synchronized (_objects)
		{
			_objects.remove(obj);
			_objects.add(obj);
		}
	}
	
	public void removeObject(WorldObject obj)
	{
		synchronized (_objects)
		{
			_objects.remove(obj);
		}
	}
	
	public List<WorldObject> getObjects()
	{
		return _objects;
	}
	
	public int getX()
	{
		return _x;
	}
	
	public int getZ()
	{
		return _z;
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(_x, _z);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (!(obj instanceof RegionHolder))
		{
			return false;
		}
		RegionHolder other = (RegionHolder) obj;
		return (_x == other._x) && (_z == other._z);
	}
	
	@Override
	public String toString()
	{
		return "Region [" + _x + " " + _z + "]";
	}
	
}
