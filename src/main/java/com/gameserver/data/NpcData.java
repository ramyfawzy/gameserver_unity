package com.gameserver.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.gameserver.enums.NpcType;
import com.gameserver.managers.IDatabaseManager;
import com.gameserver.model.holders.NpcHolder;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author Ramy Ibrahim
 */

@Singleton
public class NpcData implements INpcData
{
	private static final Logger LOGGER = LogManager.getLogger(NpcData.class.getName());
	
	private static final String RESTORE_NPCS = "SELECT * FROM npcs";
	
	private static final Map<Integer, NpcHolder> _npcs = new HashMap<>();
	
	@Inject
	IDatabaseManager databaseManager;
	
	@Override
	public void init()
	{
		_npcs.clear();
		
		try (Connection con = databaseManager.getConnection();
			PreparedStatement ps = con.prepareStatement(RESTORE_NPCS))
		{
			try (ResultSet rset = ps.executeQuery())
			{
				while (rset.next())
				{
					final int npcId = rset.getInt("npc_id");
					_npcs.put(npcId, new NpcHolder(npcId, NpcType.valueOf(rset.getString("type")), rset.getInt("level"), rset.getBoolean("sex"), rset.getInt("hp"), rset.getInt("stamina"), rset.getInt("strength"), rset.getInt("dexterity"), rset.getInt("intelect")));
					
				}
				con.close();
			}
			catch (Exception e)
			{
				LOGGER.error(e.toString());
			}
		}
		catch (SQLException e1)
		{
			e1.printStackTrace();
		}
		
		LOGGER.info("NpcData: Loaded " + _npcs.size() + " NPCs.");
	}
	
	public static NpcHolder getNpcHolder(int npcId)
	{
		if (!_npcs.containsKey(npcId))
		{
			return null;
		}
		return _npcs.get(npcId);
	}
	
}
