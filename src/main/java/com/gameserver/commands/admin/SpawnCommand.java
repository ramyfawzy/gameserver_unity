package com.gameserver.commands.admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.gameserver.Config;
import com.gameserver.data.SpawnData;
import com.gameserver.managers.ChatManager;
import com.gameserver.managers.IDatabaseManager;
import com.gameserver.managers.WorldManager;
import com.gameserver.model.actor.Npc;
import com.gameserver.model.actor.Player;
import com.gameserver.model.holders.LocationHolder;
import com.gameserver.network.packets.sendable.NpcInformation;
import com.google.inject.Inject;

/**
 * @author ribrahim
 */
public class SpawnCommand
{
	private static final Logger LOGGER = LogManager.getLogger(SpawnCommand.class.getName());
	
	private static final String SPAWN_SAVE_QUERY = "INSERT INTO spawnlist (npc_id, x, y, z, heading, respawn_delay) values (?,?,?,?,?,?)";
	
	@Inject
	IDatabaseManager databaseManager;
	
	public void handle(Player player, String command)
	{
		// Gather information from parameters.
		String[] commandSplit = command.split(" ");
		int npcId;
		if (commandSplit.length > 1)
		{
			npcId = Integer.parseInt(commandSplit[1]);
		}
		else
		{
			ChatManager.sendSystemMessage(player, "Proper syntax is /spawn npcId delaySeconds(optional).");
			return;
		}
		int respawnDelay = 60;
		if (commandSplit.length > 2)
		{
			respawnDelay = Integer.parseInt(commandSplit[2]);
		}
		
		// Log admin activity.
		LocationHolder playerLocation = player.getLocation();
		LocationHolder npcLocation = new LocationHolder(playerLocation.getX(), playerLocation.getY(), playerLocation.getZ(), playerLocation.getHeading());
		StringBuilder sb = new StringBuilder();
		if (Config.LOG_ADMIN)
		{
			sb.append(player.getName());
			sb.append(" used command /spawn ");
			sb.append(npcId);
			sb.append(" ");
			sb.append(respawnDelay);
			sb.append(" at ");
			sb.append(npcLocation);
			// LogManager.logAdmin(sb.toString());
			System.out.println(sb.toString());
			// sb.clear();
		}
		
		// Spawn NPC.
		Npc npc = SpawnData.spawnNpc(npcId, npcLocation, respawnDelay);
		
		// Broadcast NPC information.
		NpcInformation info = new NpcInformation(npc);
		player.channelSend(info);
		List<Player> players = WorldManager.getVisiblePlayers(player);
		for (Player p : players)
		{
			p.channelSend(info);
		}
		
		// Send player success message.
		sb.append("You have spawned ");
		sb.append(npcId);
		sb.append(" at ");
		sb.append(npcLocation);
		ChatManager.sendSystemMessage(player, sb.toString());
		
		// Store new records.
		try (Connection con = databaseManager.getConnection();
			PreparedStatement ps = con.prepareStatement(SPAWN_SAVE_QUERY))
		{
			ps.setInt(1, npcId);
			ps.setFloat(2, npcLocation.getX());
			ps.setFloat(3, npcLocation.getY());
			ps.setFloat(4, npcLocation.getZ());
			ps.setFloat(5, npcLocation.getHeading());
			ps.setInt(6, respawnDelay);
			ps.execute();
		}
		catch (Exception e)
		{
			LOGGER.error(e.getMessage());
		}
	}
	
}
