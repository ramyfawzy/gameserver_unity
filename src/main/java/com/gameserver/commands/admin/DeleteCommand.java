package com.gameserver.commands.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.gameserver.Config;
import com.gameserver.managers.ChatManager;
import com.gameserver.managers.IDatabaseManager;
import com.gameserver.model.WorldObject;
import com.gameserver.model.actor.Npc;
import com.gameserver.model.actor.Player;
import com.gameserver.model.holders.LocationHolder;
import com.gameserver.model.holders.SpawnHolder;
import com.google.inject.Inject;

/**
 * @author ribrahim
 */
public class DeleteCommand
{
	
	private static final Logger LOGGER = LogManager.getLogger(DeleteCommand.class.getName());
	private static final String SPAWN_DELETE_QUERY = "DELETE FROM spawnlist WHERE npc_id=? AND x=? AND y=? AND z=? AND heading=? AND respawn_delay=?";
	
	@Inject
	IDatabaseManager databaseManager;
	
	public void handle(Player player)
	{
		// Gather information.
		WorldObject target = player.getTarget();
		if (target == null)
		{
			ChatManager.sendSystemMessage(player, "You must select a target.");
			return;
		}
		Npc npc = target.asNpc();
		if (npc == null)
		{
			ChatManager.sendSystemMessage(player, "You must select an NPC.");
			return;
		}
		
		// Log admin activity.
		SpawnHolder npcSpawn = npc.getSpawnHolder();
		LocationHolder npcLocation = npcSpawn.getLocation();
		StringBuilder sb = new StringBuilder();
		if (Config.LOG_ADMIN)
		{
			sb.append(player.getName());
			sb.append(" used command /delete ");
			sb.append(npc);
			sb.append(" ");
			sb.append(npcLocation);
			System.out.println(sb.toString());
			// LogManager.LogAdmin(sb.toString());
			// sb.Clear();
		}
		
		// Delete NPC.
		npc.deleteMe();
		
		// Send player success message.
		int npcId = npc.getNpcHolder().getNpcId();
		sb.append("You have deleted ");
		sb.append(npcId);
		sb.append(" from ");
		sb.append(npcLocation);
		ChatManager.sendSystemMessage(player, sb.toString());
		
		try (Connection con = databaseManager.getConnection();
			PreparedStatement ps = con.prepareStatement(SPAWN_DELETE_QUERY))
		{
			ps.setInt(1, npcId);
			ps.setFloat(2, npcLocation.getX());
			ps.setFloat(3, npcLocation.getY());
			ps.setFloat(4, npcLocation.getZ());
			ps.setFloat(5, npcLocation.getHeading());
			ps.setInt(6, npcSpawn.getRespawnDelay());
			ps.execute();
			con.close();
		}
		catch (Exception e)
		{
			LOGGER.error(e.getMessage());
		}
	}
}
