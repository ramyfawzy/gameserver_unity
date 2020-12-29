package com.gameserver.managers;

import java.util.List;

import com.gameserver.Config;
import com.gameserver.commands.player.LocCommand;
import com.gameserver.commands.player.ReturnCommand;
import com.gameserver.commands.player.TellCommand;
import com.gameserver.model.actor.Player;
import com.gameserver.network.packets.sendable.ChatResult;

/**
 * @author Ramy Ibrahim
 */
public class ChatManager
{
	private static final byte CHAT_TYPE_SYSTEM = 0;
	private static final byte CHAT_TYPE_NORMAL = 1;
	private static final byte CHAT_TYPE_MESSAGE = 2;
	private static final String SYS_NAME = "System";
	private static final String MSG_TO = "To ";
	// Normal player commands
	private static final String COMMAND_PERSONAL_MESSAGE = "/tell ";
	private static final String COMMAND_LOCATION = "/loc";
	private static final String COMMAND_RETURN = "/return";
	// Administrator commands
	private static final String COMMAND_SPAWN = "/spawn ";
	private static final String COMMAND_DELETE = "/delete";
	
	public static void handleChat(Player sender, String message)
	{
		// Check if message is empty.
		message = message.trim();
		if (message.length() == 0)
		{
			return;
		}
		
		boolean isAdmin = sender.getAccessLevel() > 99;
		String lowercaseMessage = message.toLowerCase().replace("\\s{2,}", " "); // Also remove all double spaces.
		if (lowercaseMessage.equals(COMMAND_LOCATION))
		{
			LocCommand.handle(sender);
		}
		else if (lowercaseMessage.equals(COMMAND_RETURN))
		{
			ReturnCommand.handle(sender);
		}
		else if (lowercaseMessage.startsWith(COMMAND_PERSONAL_MESSAGE))
		{
			TellCommand.handle(sender, lowercaseMessage, message);
		}
		else if (isAdmin && lowercaseMessage.startsWith(COMMAND_SPAWN))
		{
//			SpawnCommand.handle(sender, lowercaseMessage);
		}
		else if (isAdmin && lowercaseMessage.equals(COMMAND_DELETE))
		{
//			DeleteCommand.handle(sender);
		}
		else // Normal message.
		{
			sender.channelSend(new ChatResult(CHAT_TYPE_NORMAL, sender.getName(), message));
			List<Player> players = WorldManager.getVisiblePlayers(sender);
			for (Player player : players)
			{
				player.channelSend(new ChatResult(CHAT_TYPE_NORMAL, sender.getName(), message));
			}
			// Log chat.
			if (Config.LOG_CHAT)
			{
				StringBuilder sb = new StringBuilder();
				sb.append("[");
				sb.append(sender.getName());
				sb.append("] ");
				sb.append(message);
				// LogManager.LogChat(sb.ToString());
			}
		}
	}
	
	public static void sendPrivateMessage(Player sender, Player receiver, String message)
	{
		sender.channelSend(new ChatResult(CHAT_TYPE_MESSAGE, MSG_TO + receiver.getName(), message));
		receiver.channelSend(new ChatResult(CHAT_TYPE_MESSAGE, sender.getName(), message));
	}
	
	public static void sendSystemMessage(Player player, String message)
	{
		player.channelSend(new ChatResult(CHAT_TYPE_SYSTEM, SYS_NAME, message));
	}
}
