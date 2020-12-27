package com.gameserver.network.packets.sendable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.gameserver.managers.DatabaseManager;
import com.gameserver.managers.IDatabaseManager;
import com.gameserver.model.holders.CharacterDataHolder;
import com.gameserver.network.SendablePacket;
import com.google.inject.Inject;

/**
 * @author Ramy Ibrahim
 */
public class CharacterSelectionInfoResult extends SendablePacket
{
	private static final Logger LOGGER = LogManager.getLogger(CharacterSelectionInfoResult.class.getName());
	
	private static final String CHARACTER_QUERY = "SELECT * FROM characters WHERE account=? AND access_level>'-1' ORDER BY slot ASC";
	private static final String VISIBLE_ITEMS_QUERY = "SELECT * FROM character_items WHERE owner=? AND slot_id>'0' AND slot_id<'8' ORDER BY slot_id ASC";
	
	public CharacterSelectionInfoResult(String accountName, IDatabaseManager databaseManager)
	{
		// Local data.
		final List<CharacterDataHolder> characterList = new ArrayList<>();
		
		// Get data from database.
		try (Connection con = databaseManager.getConnection();
			PreparedStatement ps = con.prepareStatement(CHARACTER_QUERY))
		{
			ps.setString(1, accountName);
			try (ResultSet rset = ps.executeQuery())
			{
				while (rset.next())
				{
					final CharacterDataHolder characterData = new CharacterDataHolder();
					String name = rset.getString("name");
					characterData.setName(name);
					characterData.setSlot(rset.getByte("slot"));
					characterData.setSelected(rset.getBoolean("selected"));
					characterData.setRace(rset.getByte("race"));
					characterData.setHeight(rset.getFloat("height"));
					characterData.setBelly(rset.getFloat("belly"));
					characterData.setHairType(rset.getInt("hair_type"));
					characterData.setHairColor(rset.getInt("hair_color"));
					characterData.setSkinColor(rset.getInt("skin_color"));
					characterData.setEyeColor(rset.getInt("eye_color"));
					characterData.setX(rset.getFloat("x"));
					characterData.setY(rset.getFloat("y"));
					characterData.setZ(rset.getFloat("z"));
					characterData.setHeading(rset.getFloat("heading"));
					characterData.setExperience(rset.getLong("experience"));
					characterData.setHp(rset.getLong("hp"));
					characterData.setMp(rset.getLong("mp"));
					characterData.setAccessLevel(rset.getByte("access_level"));
					
					try (Connection con2 = databaseManager.getConnection();
						PreparedStatement ps2 = con.prepareStatement(VISIBLE_ITEMS_QUERY))
					{
						ps2.setString(1, name);
						ResultSet rset2 = ps2.executeQuery();
						while (rset2.next())
						{
							int slotId = rset2.getInt("slot_id");
							int itemId = rset2.getInt("item_id");
							switch (slotId)
							{
								case 1:
									characterData.setHeadItem(itemId);
									break;
								
								case 2:
									characterData.setChestItem(itemId);
									break;
								
								case 3:
									characterData.setLegsItem(itemId);
									break;
								
								case 4:
									characterData.setHandsItem(itemId);
									break;
								
								case 5:
									characterData.setFeetItem(itemId);
									break;
								
								case 6:
									characterData.setLeftHandItem(itemId);
									break;
								
								case 7:
									characterData.setRightHandItem(itemId);
									break;
							}
						}
						
					}
					catch (Exception e)
					{
						LOGGER.warn(e.getMessage());
					}
					
					characterList.add(characterData);
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.warn(e.getMessage());
		}
		
		// Send the data.
		writeShort(2); // Packet id.
		writeByte(characterList.size());
		for (CharacterDataHolder characterData : characterList)
		{
			writeString(characterData.getName());
			writeByte(characterData.getSlot());
			writeByte(characterData.isSelected() ? 1 : 0);
			writeByte(characterData.getRace());
			writeFloat(characterData.getHeight());
			writeFloat(characterData.getBelly());
			writeByte(characterData.getHairType());
			writeInt(characterData.getHairColor());
			writeInt(characterData.getSkinColor());
			writeInt(characterData.getEyeColor());
			writeInt(characterData.getHeadItem());
			writeInt(characterData.getChestItem());
			writeInt(characterData.getLegsItem());
			writeInt(characterData.getHandsItem());
			writeInt(characterData.getFeetItem());
			writeInt(characterData.getLeftHandItem());
			writeInt(characterData.getRightHandItem());
			writeFloat(characterData.getX());
			writeFloat(characterData.getY());
			writeFloat(characterData.getZ());
			writeFloat(characterData.getHeading());
			writeLong(characterData.getExperience());
			writeLong(characterData.getHp());
			writeLong(100); // TODO: Implement Player level data. - Max HP
			writeLong(characterData.getMp());
			writeLong(100); // TODO: Implement Player level data. - Max MP
			writeByte(characterData.getAccessLevel());
		}
	}
}
