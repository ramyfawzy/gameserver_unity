package com.gameserver.network.packets.receivable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.gameserver.Config;
import com.gameserver.data.ItemData;
import com.gameserver.enums.ItemSlot;
import com.gameserver.enums.ItemType;
import com.gameserver.managers.DatabaseManager;
import com.gameserver.managers.IDatabaseManager;
import com.gameserver.model.holders.ItemTemplateHolder;
import com.gameserver.network.GameClient;
import com.gameserver.network.ReceivablePacket;
import com.gameserver.network.packets.sendable.CharacterCreationResult;
import com.gameserver.util.Util;
import com.google.inject.Inject;

/**
 * @author Ramy Ibrahim
 */
public class CharacterCreationRequest implements IRequestHandler
{
	private static final Logger LOGGER = LogManager.getLogger(CharacterCreationRequest.class.getName());
	
	private static final String ACCOUNT_CHARACTER_QUERY = "SELECT * FROM characters WHERE access_level>'-1' AND account=?";
	private static final String NAME_EXISTS_QUERY = "SELECT * FROM characters WHERE name=?";
	private static final String CHARACTER_SELECTED_RESET_QUERY = "UPDATE characters SET selected=0 WHERE account=?";
	private static final String CHARACTER_CREATE_QUERY = "INSERT INTO characters (account, name, slot, selected, race, height, belly, hair_type, hair_color, skin_color, eye_color, x, y, z, heading, experience, hp, mp)  values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String CHARACTER_CREATE_OPTIONS_QUERY = "INSERT INTO character_options (name) values (?)";
	private static final String CHARACTER_ITEM_START = "INSERT INTO character_items VALUES";
	
	private static final int INVALID_NAME = 0;
	private static final int NAME_IS_TOO_SHORT = 1;
	private static final int NAME_ALREADY_EXISTS = 2;
	private static final int CANNOT_CREATE_ADDITIONAL_CHARACTERS = 3;
	private static final int INVALID_PARAMETERS = 4;
	private static final int SUCCESS = 100;
	
	@Inject
	DatabaseManager databaseManager;

	@Override
	public void handle(GameClient client, ReceivablePacket packet) {
		// Make sure player has authenticated.
		if ((client.getAccountName() == null) || (client.getAccountName().length() == 0))
		{
			return;
		}
		
		// Read data.
		String characterName = packet.readString();
		int race = packet.readByte();
		float height = packet.readFloat();
		float belly = packet.readFloat();
		int hairType = packet.readByte();
		int hairColor = packet.readInt();
		int skinColor = packet.readInt();
		int eyeColor = packet.readInt();
		
		// Replace illegal characters.
		for (char c : Util.ILLEGAL_CHARACTERS)
		{
			characterName = characterName.replace(c, '\'');
		}
		
		// Name character checks.
		if (characterName.contains("'"))
		{
			client.channelSend(new CharacterCreationResult(INVALID_NAME));
			return;
		}
		if ((characterName.length() < 2) || (characterName.length() > 12)) // 12 should not happen, checking it here in case of client cheat.
		{
			client.channelSend(new CharacterCreationResult(NAME_IS_TOO_SHORT));
			return;
		}
		
		// Visual exploit checks.
		if (((race < 0) || (race > 1)) || ((height < 0.39) || (height > 0.61)) || ((hairType < 0) || (hairType > 3))
		/* || (!Config.VALID_SKIN_COLORS.Contains(skinColor)) */) // TODO: Check palette.
		{
			client.channelSend(new CharacterCreationResult(INVALID_PARAMETERS));
			return;
		}
		
		// Account character count database check.
		int characterCount = 0;
		int lastCharacterSlot = 0;
		try (Connection con = databaseManager.getConnection();
			PreparedStatement ps = con.prepareStatement(ACCOUNT_CHARACTER_QUERY))
		{
			ps.setString(1, client.getAccountName());
			try (ResultSet rset = ps.executeQuery())
			{
				while (rset.next())
				{
					characterCount++;
					final int slot = rset.getInt("slot");
					if (slot > lastCharacterSlot)
					{
						lastCharacterSlot = slot;
					}
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.error(e.getMessage());
		}
		if (characterCount >= Config.ACCOUNT_MAX_CHARACTERS)
		{
			client.channelSend(new CharacterCreationResult(CANNOT_CREATE_ADDITIONAL_CHARACTERS));
			return;
		}
		
		// Check database if name exists.
		boolean characterExists = false;
		try (Connection con = databaseManager.getConnection();
			PreparedStatement ps = con.prepareStatement(NAME_EXISTS_QUERY))
		{
			ps.setString(1, characterName);
			try (ResultSet rset = ps.executeQuery())
			{
				while (rset.next())
				{
					characterExists = true;
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.error(e.getMessage());
		}
		if (characterExists)
		{
			client.channelSend(new CharacterCreationResult(NAME_ALREADY_EXISTS));
			return;
		}
		
		// Make existing characters selected value false.
		try (Connection con = databaseManager.getConnection();
			PreparedStatement ps = con.prepareStatement(CHARACTER_SELECTED_RESET_QUERY))
		{
			ps.setString(1, client.getAccountName());
			ps.execute();
		}
		catch (Exception e)
		{
			LOGGER.error(e.getMessage());
		}
		
		// Create character.
		try (Connection con = databaseManager.getConnection();
			PreparedStatement ps = con.prepareStatement(CHARACTER_CREATE_QUERY))
		{
			ps.setString(1, client.getAccountName());
			ps.setString(2, characterName);
			ps.setInt(3, lastCharacterSlot + 1);
			ps.setInt(4, 1); // Selected character.
			ps.setInt(5, race);
			ps.setFloat(6, height);
			ps.setFloat(7, belly);
			ps.setInt(8, hairType);
			ps.setInt(9, hairColor);
			ps.setInt(10, skinColor);
			ps.setInt(11, eyeColor);
			ps.setFloat(12, Config.STARTING_LOCATION.getX());
			ps.setFloat(13, Config.STARTING_LOCATION.getY());
			ps.setFloat(14, Config.STARTING_LOCATION.getZ());
			ps.setFloat(15, Config.STARTING_LOCATION.getHeading());
			ps.setLong(16, 0); // TODO: Starting level experience.
			ps.setLong(17, 1); // TODO: Character stats HP.
			ps.setLong(18, 1); // TODO: Character stats MP.
			ps.execute();
		}
		catch (Exception e)
		{
			LOGGER.error(e.getMessage());
		}
		
		try (Connection con = databaseManager.getConnection();
			PreparedStatement ps = con.prepareStatement(CHARACTER_CREATE_OPTIONS_QUERY))
		{
			ps.setString(1, characterName);
			ps.execute();
		}
		catch (Exception e)
		{
			LOGGER.error(e.getMessage());
		}
		
		// Add starting items.
		int itemCount = Config.STARTING_ITEMS.size();
		if (itemCount > 0)
		{
			// Prepare query.
			StringBuilder query = new StringBuilder(CHARACTER_ITEM_START);
			List<ItemSlot> usedEquipableSlots = new ArrayList<>();
			int inventorySlotCounter = 8; // First inventory item slot.
			for (var itemId : Config.STARTING_ITEMS)
			{
				query.append("('");
				query.append(characterName);
				query.append("',");
				ItemTemplateHolder itemHolder = ItemData.getItemTemplate(itemId);
				ItemSlot itemSlot = itemHolder.getItemSlot();
				if ((itemHolder.getItemType() == ItemType.EQUIP) && !usedEquipableSlots.contains(itemSlot))
				{
					usedEquipableSlots.add(itemSlot);
					query.append(itemHolder.getItemSlot().ordinal());
				}
				else
				{
					query.append(inventorySlotCounter++);
				}
				query.append(",");
				query.append(itemId);
				query.append(",1,0"); // quantity, enchant
				query.append(")");
				query.append(itemCount-- == 1 ? ";" : ",");
			}
			try (Connection con = databaseManager.getConnection();
				PreparedStatement ps = con.prepareStatement(query.toString()))
			{
				ps.execute();
			}
			catch (Exception e)
			{
				LOGGER.error(e.getMessage());
			}
		}
		
		// Send success result.
		client.channelSend(new CharacterCreationResult(SUCCESS));
		
	}
}
