package com.gameserver.commands.player;

import com.gameserver.Config;
import com.gameserver.managers.ChatManager;
import com.gameserver.managers.WorldManager;
import com.gameserver.model.actor.Player;

/**
 * @author ribrahim
 */
public class TellCommand
{
	public static void handle(Player sender, String lowercaseMessage, String message)
	{
		String[] lowercaseMessageSplit = lowercaseMessage.split(" ");
		if (lowercaseMessageSplit.length < 3) // Check for parameters.
		{
			ChatManager.sendSystemMessage(sender, "Incorrect syntax. Use /tell [name] [message].");
			return;
		}
		
		Player receiver = WorldManager.getPlayerByName(lowercaseMessageSplit[1]);
		if (receiver == null)
		{
			ChatManager.sendSystemMessage(sender, "Player was not found.");
		}
		else
		{
			// Step by step cleanup, to avoid problems with extra/double spaces on original message.
			// message = message.substring(lowercaseMessageSplit[0].length(), message.length() - lowercaseMessageSplit[0].length()).trim(); // Remove command.
			// message = message.substring(lowercaseMessageSplit[1].length(), message.length() - lowercaseMessageSplit[1].length()).trim(); // Remove receiver name.
			// Send message.
			ChatManager.sendPrivateMessage(sender, receiver, lowercaseMessageSplit[2]);
			// Log chat.
			if (Config.LOG_CHAT)
			{
				StringBuilder sb = new StringBuilder();
				sb.append("[");
				sb.append(sender.getName());
				sb.append("] to [");
				sb.append(receiver.getName());
				sb.append("] ");
				sb.append(message);
				// LogManager.LogChat(sb.toString());
			}
		}
	}
}
