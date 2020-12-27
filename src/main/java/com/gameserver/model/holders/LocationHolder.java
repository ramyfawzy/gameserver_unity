package com.gameserver.model.holders;

/**
 * @author ribrahim
 */
public class LocationHolder
{
	float _x;
	float _y;
	float _z;
	float _heading;
	
	public LocationHolder(float _x, float _y, float _z)
	{
		this._x = _x;
		this._y = _y;
		this._z = _z;
		_heading = 0;
	}
	
	public LocationHolder(float x, float y, float z, float heading)
	{
		_x = x;
		_y = y;
		_z = z;
		_heading = heading;
	}
	
	/**
	 * @return the _x
	 */
	public float getX()
	{
		return _x;
	}
	
	/**
	 * @param _x the _x to set
	 */
	public void setX(float _x)
	{
		this._x = _x;
	}
	
	/**
	 * @return the _y
	 */
	public float getY()
	{
		return _y;
	}
	
	/**
	 * @param _y the _y to set
	 */
	public void setY(float _y)
	{
		this._y = _y;
	}
	
	/**
	 * @return the _z
	 */
	public float getZ()
	{
		return _z;
	}
	
	/**
	 * @param _z the _z to set
	 */
	public void setZ(float _z)
	{
		this._z = _z;
	}
	
	/**
	 * @return the _heading
	 */
	public float getHeading()
	{
		return _heading;
	}
	
	/**
	 * @param _heading the _heading to set
	 */
	public void setHeading(float _heading)
	{
		this._heading = _heading;
	}
	
	public void update(float x, float y, float z, float heading)
	{
		_x = x;
		_y = y;
		_z = z;
		_heading = heading;
	}
	
	@Override
	public String toString()
	{
		String result = _x + " " + _y + " " + _z;
		return "Location [" + (_heading > 0 ? result + " " + _heading : result) + "]";
	}
	
}
