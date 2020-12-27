package com.gameserver.model.items;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.gameserver.data.ItemData;
import com.gameserver.managers.DatabaseManager;
import com.gameserver.managers.IDatabaseManager;
import com.gameserver.model.holders.ItemHolder;
import com.google.inject.Inject;

/**
 * @author Ramy Ibrahim
 */
public class Inventory
{
	private static final Logger LOGGER = LogManager.getLogger(Inventory.class.getName());
	
	private static final String RESTORE_INVENTORY = "SELECT * FROM character_items WHERE owner=?";
	private static final String DELETE_INVENTORY = "DELETE FROM character_items WHERE owner=?";
	private static final String STORE_ITEM = "INSERT INTO character_items (owner, slot_id, item_id, quantity, enchant) values (?, ?, ?, ?, ?)";
	
	private final Map<Integer, ItemHolder> _items = new ConcurrentHashMap<>();
	
	private IDatabaseManager databaseManager;
	
	public Inventory(String ownerName, IDatabaseManager databaseManager)
	{
		this.databaseManager = databaseManager;
		synchronized (_items)
		{
			// Restore information from database.
			try (Connection con = databaseManager.getConnection();
				PreparedStatement ps = con.prepareStatement(RESTORE_INVENTORY))
			{
				ps.setString(1, ownerName);
				try (ResultSet rset = ps.executeQuery())
				{
					while (rset.next())
					{
						ItemHolder itemHolder = new ItemHolder(ItemData.getItemTemplate(rset.getInt("item_id")));
						itemHolder.setQuantity(rset.getInt("quantity"));
						itemHolder.setEnchant(rset.getInt("enchant"));
						_items.put(rset.getInt("slot_id"), itemHolder);
					}
				}
			}
			catch (Exception e)
			{
				LOGGER.warn(e.getMessage());
			}
			
		}
		
	}
	
	/**
	 * Only used when player exits the game.
	 * @param ownerName
	 */
	public void store(String ownerName)
	{
		// Delete old records.
		try (Connection con = databaseManager.getConnection();
			PreparedStatement ps = con.prepareStatement(DELETE_INVENTORY))
		{
			ps.setString(1, ownerName);
			ps.execute();
		}
		catch (Exception e)
		{
			LOGGER.warn(e.getMessage());
		}
		
		// No need to store if item list is empty.
		if (_items.isEmpty())
		{
			return;
		}
		
		// Store new records.
		try (Connection con = databaseManager.getConnection();
			PreparedStatement ps = con.prepareStatement(STORE_ITEM))
		{
			for (Entry<Integer, ItemHolder> slot : _items.entrySet())
			{
				ps.setString(1, ownerName);
				ps.setInt(2, slot.getKey());
				ps.setInt(3, slot.getValue().getItemTemplate().getItemId());
				ps.setInt(4, slot.getValue().getQuantity());
				ps.setInt(5, slot.getValue().getEnchant());
				ps.addBatch();
			}
			ps.executeBatch();
		}
		catch (Exception e)
		{
			LOGGER.warn(e.getMessage());
		}
		
		// Clear item list.
		_items.clear();
	}
	
	public ItemHolder getSlot(int slotId)
	{
		if (!_items.containsKey(slotId))
		{
			return null;
		}
		return _items.get(slotId);
	}
	
	public int getItemIdBySlot(int slotId)
	{
		if (!_items.containsKey(slotId))
		{
			return 0;
		}
		return _items.get(slotId).getItemTemplate().getItemId();
	}
	
	public void setSlot(int slotId, int itemId)
	{
		synchronized (_items)
		{
			_items.put(slotId, new ItemHolder(ItemData.getItemTemplate(itemId)));
		}
	}
	
	public void removeSlot(int slotId)
	{
		synchronized (_items)
		{
			_items.remove(slotId);
		}
	}
	
	public Map<Integer, ItemHolder> getItems()
	{
		return _items;
	}
}
