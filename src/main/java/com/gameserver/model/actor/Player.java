package com.gameserver.model.actor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.gameserver.Config;
import com.gameserver.managers.IDatabaseManager;
import com.gameserver.model.holders.LocationHolder;
import com.gameserver.model.items.Inventory;
import com.gameserver.network.GameClient;
import com.gameserver.network.SendablePacket;

/**
 * @author Ramy Ibrahim
 */
public class Player extends Creature
{
	private static final Logger LOGGER = LogManager.getLogger(Player.class.getName());
	
	private static final String RESTORE_CHARACTER = "SELECT * FROM characters WHERE name=?";
	private static final String STORE_CHARACTER = "UPDATE characters SET name=?, race=?, x=?, y=?, z=?, heading=?, experience=?, hp=?, mp=? WHERE account=? AND name=?";
	
	private final GameClient _client;
	private final String _name;
	private byte _raceId;
	private float _height;
	private float _belly;
	private int _hairType;
	private int _hairColor;
	private int _skinColor;
	private int _eyeColor;
	private long _experience;
	private byte _accessLevel;
	private final Inventory _inventory;
	
	private IDatabaseManager databaseManager;
	 
	
	public Player(GameClient client, String name, IDatabaseManager databaseManager)
	{
		_client = client;
		_name = name;
		this.databaseManager = databaseManager;
		
		// Load information from database.
		try (Connection con = databaseManager.getConnection();
			PreparedStatement ps = con.prepareStatement(RESTORE_CHARACTER))
		{
			ps.setString(1, name);
			try (ResultSet rset = ps.executeQuery())
			{
				while (rset.next())
				{
					_raceId = rset.getByte("race");
					_height = rset.getFloat("height");
					_belly = rset.getFloat("belly");
					_hairType = rset.getInt("hair_type");
					_hairColor = rset.getInt("hair_color");
					_skinColor = rset.getInt("skin_color");
					_eyeColor = rset.getInt("eye_color");
					
					float locX = rset.getFloat("x");
					float locY = rset.getFloat("y");
					float locZ = rset.getFloat("z");
					
					// Check if player is outside of world bounds.
					if ((locX < Config.WORLD_MINIMUM_X) || (locX > Config.WORLD_MAXIMUM_X) || (locY < Config.WORLD_MINIMUM_Y) || (locY > Config.WORLD_MAXIMUM_Y) || (locZ < Config.WORLD_MINIMUM_Z) || (locZ > Config.WORLD_MAXIMUM_Z))
					{
						// Move to initial area.
						setLocation(Config.STARTING_LOCATION);
					}
					else
					{
						setLocation(new LocationHolder(locX, locY, locZ, rset.getFloat("heading"))); // Fixes known MySQL float issue.
					}
					
					_experience = rset.getInt("experience");
					setCurrentHp(rset.getInt("hp"));
					setCurrentMp(rset.getInt("mp"));
					_accessLevel = rset.getByte("access_level"); // TODO: Remove cast?
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.warn(e.getMessage());
		}
		
		// Initialize inventory.
		_inventory = new Inventory(name, databaseManager);
		// TODO: Send inventory slotId/itemId list packet (InventoryInformation) to client.
	}
	
	public void storeMe()
	{
		try (Connection con = databaseManager.getConnection();
			PreparedStatement ps = con.prepareStatement(STORE_CHARACTER))
		{
			ps.setString(1, _name);
			ps.setInt(2, _raceId);
			ps.setFloat(3, getLocation().getX());
			ps.setFloat(4, getLocation().getY());
			ps.setFloat(5, getLocation().getZ());
			ps.setFloat(6, getLocation().getHeading());
			
			ps.setLong(7, _experience);
			ps.setLong(8, getCurrentHp());
			ps.setLong(9, getCurrentMp());
			ps.setString(10, _client.getAccountName());
			ps.setString(11, _name);
			ps.execute();
		}
		catch (Exception e)
		{
			LOGGER.warn(e.getMessage());
		}
		
		// Save inventory.
		_inventory.store(_name);
	}
	
	public GameClient getClient()
	{
		return _client;
	}
	
	public String getName()
	{
		return _name;
	}
	
	public int getRaceId()
	{
		return _raceId;
	}
	
	public float getHeight()
	{
		return _height;
	}
	
	public float getBelly()
	{
		return _belly;
	}
	
	public int getHairType()
	{
		return _hairType;
	}
	
	public int getHairColor()
	{
		return _hairColor;
	}
	
	public int getSkinColor()
	{
		return _skinColor;
	}
	
	public int getEyeColor()
	{
		return _eyeColor;
	}
	
	public long getExperience()
	{
		return _experience;
	}
	
	public byte getAccessLevel()
	{
		return _accessLevel;
	}
	
	public Inventory getInventory()
	{
		return _inventory;
	}
	
	public void channelSend(SendablePacket packet)
	{
		_client.channelSend(packet);
	}
	
	@Override
	public boolean isPlayer()
	{
		return true;
	}
	
	@Override
	public Player asPlayer()
	{
		return this;
	}
	
	@Override
	public String toString()
	{
		return "Player [" + _name + "]";
	}
}
