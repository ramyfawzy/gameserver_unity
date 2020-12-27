package com.gameserver.network.packets.sendable;

import java.util.Map;

import com.gameserver.model.actor.Player;
import com.gameserver.model.holders.ItemHolder;
import com.gameserver.network.SendablePacket;

/**
 * @author ribrahim
 */
public class PlayerInventoryUpdate extends SendablePacket
{
	public PlayerInventoryUpdate(Player player)
	{
		writeShort(13); // Packet id.
		
		// Get the item list.
		Map<Integer, ItemHolder> items = player.getInventory().getItems();
		
		// Write information.
		writeInt(items.size());
		for (var item : items.entrySet())
		{
			writeInt(item.getValue().getItemTemplate().getItemId()); // Item id.
			writeInt(item.getKey()); // Slot.
			writeInt(item.getValue().getQuantity()); // Quantity.
			writeInt(item.getValue().getEnchant()); // Enchant.
		}
	}
	
}
