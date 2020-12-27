package com.gameserver.commands.player;

import com.gameserver.managers.ChatManager;
import com.gameserver.model.actor.Player;
import com.gameserver.model.holders.LocationHolder;

/**
 * @author ribrahim
 */
public class LocCommand
{
	public static void handle(Player player)
	{
		LocationHolder location = player.getLocation();
		
		// Send player success message.
		StringBuilder sb = new StringBuilder();
		sb.append("Your location is ");
		sb.append(location.getX());
		sb.append(" ");
		sb.append(location.getZ());
		sb.append(" ");
		sb.append(location.getY());
		ChatManager.sendSystemMessage(player, sb.toString());
	}
}
