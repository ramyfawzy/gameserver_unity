package com.gameserver.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.gameserver.enums.ItemSlot;
import com.gameserver.enums.ItemType;
import com.gameserver.managers.IDatabaseManager;
import com.gameserver.model.holders.ItemTemplateHolder;
import com.gameserver.model.holders.SkillHolder;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author Ramy Ibrahim
 */

@Singleton
public class ItemData implements IItemData
{
	private static final Logger LOGGER = LogManager.getLogger(ItemData.class.getName());
	
	private static final String RESTORE_ITEMS = "SELECT * FROM items";
	
	private static final Map<Integer, ItemTemplateHolder> _items = new HashMap<>();
	
	@Inject
	IDatabaseManager databaseManager;
	
	@Override
	public void init()
	{
		_items.clear();
		
		try (Connection con = databaseManager.getConnection();
			PreparedStatement ps = con.prepareStatement(RESTORE_ITEMS))
		{
			try (ResultSet rset = ps.executeQuery())
			{
				while (rset.next())
				{
					final int itemId = rset.getInt("item_id");
					final int skillId = rset.getInt("skill_id");
					final int skillLevel = rset.getInt("skill_level");
					final SkillHolder skillHolder = SkillData.getSkillHolder(skillId, skillLevel);
					if ((skillId > 0) && (skillHolder == null))
					{
						LOGGER.warn("ItemData: Could not find skill with id " + skillId + " and level " + skillLevel + " for item " + itemId + ".");
					}
					else
					{
						_items.put(itemId, new ItemTemplateHolder(itemId, ItemSlot.valueOf(rset.getString("slot")), ItemType.valueOf(rset.getString("type")), rset.getBoolean("stackable"), rset.getBoolean("tradable"), rset.getInt("stamina"), rset.getInt("strength"), rset.getInt("dexterity"), rset.getInt("intelect"), skillHolder));
					}
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.error(e.getMessage());
		}
		
		LOGGER.info("ItemData: Loaded " + _items.size() + " items.");
	}
	
	public static ItemTemplateHolder getItemTemplate(int itemId)
	{
		return _items.get(itemId);
	}
}
