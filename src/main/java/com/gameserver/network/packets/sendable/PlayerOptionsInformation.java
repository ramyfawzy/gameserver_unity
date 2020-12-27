package com.gameserver.network.packets.sendable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.gameserver.managers.DatabaseManager;
import com.gameserver.managers.IDatabaseManager;
import com.gameserver.model.actor.Player;
import com.gameserver.network.SendablePacket;
import com.google.inject.Inject;

/**
 * @author ribrahim
 */
public class PlayerOptionsInformation extends SendablePacket
{
	private static final String ACCOUNT_CHARACTER_QUERY = "SELECT * FROM character_options WHERE name=?";
	
	public PlayerOptionsInformation(Player player, IDatabaseManager databaseManager)
	{
		writeShort(5);
		
		try (Connection con = databaseManager.getConnection();
			PreparedStatement ps = con.prepareStatement(ACCOUNT_CHARACTER_QUERY))
		{
			ps.setString(1, player.getName());
			ResultSet rset = ps.executeQuery();
			while (rset.next())
			{
				writeInt(rset.getInt("chat_color_normal"));
				writeInt(rset.getInt("chat_color_message"));
				writeInt(rset.getInt("chat_color_system"));
				writeByte((byte) rset.getInt("chat_use_timestamps")); // TODO: Remove cast?
				writeShort(rset.getInt("key_up_1"));
				writeShort(rset.getInt("key_up_2"));
				writeShort(rset.getInt("key_down_1"));
				writeShort(rset.getInt("key_down_2"));
				writeShort(rset.getInt("key_left_1"));
				writeShort(rset.getInt("key_left_2"));
				writeShort(rset.getInt("key_right_1"));
				writeShort(rset.getInt("key_right_2"));
				writeShort(rset.getInt("key_jump_1"));
				writeShort(rset.getInt("key_jump_2"));
				writeShort(rset.getInt("key_character_1"));
				writeShort(rset.getInt("key_character_2"));
				writeShort(rset.getInt("key_inventory_1"));
				writeShort(rset.getInt("key_inventory_2"));
				writeShort(rset.getInt("key_skills_1"));
				writeShort(rset.getInt("key_skills_2"));
				writeShort(rset.getInt("key_shortcut_1_1"));
				writeShort(rset.getInt("key_shortcut_1_2"));
				writeShort(rset.getInt("key_shortcut_2_1"));
				writeShort(rset.getInt("key_shortcut_2_2"));
				writeShort(rset.getInt("key_shortcut_3_1"));
				writeShort(rset.getInt("key_shortcut_3_2"));
				writeShort(rset.getInt("key_shortcut_4_1"));
				writeShort(rset.getInt("key_shortcut_4_2"));
				writeShort(rset.getInt("key_shortcut_5_1"));
				writeShort(rset.getInt("key_shortcut_5_2"));
				writeShort(rset.getInt("key_shortcut_6_1"));
				writeShort(rset.getInt("key_shortcut_6_2"));
				writeShort(rset.getInt("key_shortcut_7_1"));
				writeShort(rset.getInt("key_shortcut_7_2"));
				writeShort(rset.getInt("key_shortcut_8_1"));
				writeShort(rset.getInt("key_shortcut_8_2"));
				writeShort(rset.getInt("key_shortcut_9_1"));
				writeShort(rset.getInt("key_shortcut_9_2"));
				writeShort(rset.getInt("key_shortcut_10_1"));
				writeShort(rset.getInt("key_shortcut_10_2"));
				writeShort(rset.getInt("key_shortcut_11_1"));
				writeShort(rset.getInt("key_shortcut_11_2"));
				writeShort(rset.getInt("key_shortcut_12_1"));
				writeShort(rset.getInt("key_shortcut_12_2"));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
