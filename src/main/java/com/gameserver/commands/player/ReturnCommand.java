package com.gameserver.commands.player;

import com.gameserver.Config;
import com.gameserver.model.actor.Player;
import com.gameserver.network.packets.sendable.LocationUpdate;

/**
 * @author ribrahim
 */
public class ReturnCommand
{
	public static void handle(Player player)
	{
		player.setTeleporting();
		player.setLocation(Config.STARTING_LOCATION);
		player.channelSend(new LocationUpdate(player, true));
	}
}
