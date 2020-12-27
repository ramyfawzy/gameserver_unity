package com.gameserver.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import com.gameserver.managers.DatabaseManager;
import com.gameserver.managers.IDatabaseManager;
import com.gameserver.model.actor.Monster;
import com.gameserver.model.actor.Npc;
import com.gameserver.model.holders.LocationHolder;
import com.gameserver.model.holders.NpcHolder;
import com.gameserver.model.holders.SpawnHolder;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * @author Ramy Ibrahim
 */

@Singleton
public class SpawnData implements ISpawnData
{
	private static final Logger LOGGER = Logger.getLogger(SpawnData.class.getName());
	private static final String RESTORE_SPAWNS = "SELECT * FROM spawnlist";
	
	@Inject
	IDatabaseManager databaseManager;
	
	@Override
	public void init()
	{
		int spawnCount = 0;
		
		try (Connection con = databaseManager.getConnection();
			PreparedStatement ps = con.prepareStatement(RESTORE_SPAWNS))
		{
			try (ResultSet rset = ps.executeQuery())
			{
				while (rset.next())
				{
					spawnNpc(rset.getInt("npc_id"), new LocationHolder(rset.getFloat("x"), rset.getFloat("y"), rset.getFloat("z"), rset.getInt("heading")), rset.getInt("respawn_delay"));
					spawnCount++;
				}
				con.close();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
		}
		catch (SQLException e1)
		{
			e1.printStackTrace();
		}
		LOGGER.info("SpawnData: Loaded " + spawnCount + " spawns.");
	}
	
	public static Npc spawnNpc(int npcId, LocationHolder location, int respawnDelay)
	{
		NpcHolder npcHolder = NpcData.getNpcHolder(npcId);
		SpawnHolder spawn = new SpawnHolder(location, respawnDelay);
		Npc npc = null;
		switch (npcHolder.getNpcType())
		{
			case NPC:
				npc = new Npc(npcHolder, spawn);
				break;
			
			case MONSTER:
				npc = new Monster(npcHolder, spawn);
				break;
		}
		return npc;
	}
	
}
