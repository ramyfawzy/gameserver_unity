package com.gameserver.network;

import java.util.Map;
import java.util.logging.Logger;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import com.gameserver.enums.ClientRequestType;
import com.gameserver.managers.WorldManager;
import com.gameserver.model.actor.Player;
import com.gameserver.network.packets.receivable.AccountAuthenticationRequest;
import com.gameserver.network.packets.receivable.AnimatorUpdateRequest;
import com.gameserver.network.packets.receivable.CharacterCreationRequest;
import com.gameserver.network.packets.receivable.CharacterDeletionRequest;
import com.gameserver.network.packets.receivable.CharacterSelectUpdate;
import com.gameserver.network.packets.receivable.CharacterSelectionInfoRequest;
import com.gameserver.network.packets.receivable.CharacterSlotUpdate;
import com.gameserver.network.packets.receivable.ChatRequest;
import com.gameserver.network.packets.receivable.EnterWorldRequest;
import com.gameserver.network.packets.receivable.ExitWorldRequest;
import com.gameserver.network.packets.receivable.IRequestHandler;
import com.gameserver.network.packets.receivable.LocationUpdateRequest;
import com.gameserver.network.packets.receivable.ObjectInfoRequest;
import com.gameserver.network.packets.receivable.PlayerOptionsUpdate;
import com.gameserver.network.packets.receivable.TargetUpdateRequest;
import com.google.inject.Inject;

/**
 * @author Ramy Ibrahim
 */
public class GameClient extends SimpleChannelInboundHandler<byte[]>
{
	private static final Logger LOGGER = Logger.getLogger(GameClient.class.getName());
	
	private Channel _channel;
	private String _ip;
	private String _accountName;
	private Player _activeChar;
	
	private Map<String, IRequestHandler> clientRequestHandlers;
	
	public GameClient(Map<String, IRequestHandler> clientRequestHandlers) {
		this.clientRequestHandlers = clientRequestHandlers;
	}
	
	@Override
	public void handlerAdded(ChannelHandlerContext ctx)
	{
		// Connected.
		_channel = ctx.channel();
		_ip = _channel.remoteAddress().toString();
		_ip = _ip.substring(1, _ip.lastIndexOf(':')); // Trim out /127.0.0.1:12345
	}
	
	public void channelSend(SendablePacket packet)
	{
		if (_channel.isActive())
		{
			_channel.writeAndFlush(packet.getSendableBytes());
		}
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, byte[] bytes)
	{
//		for (var b : bytes)
//		{
//			System.out.print(b + " ");
//		}
//		System.out.println();
//		for (var b : Encryption.decrypt(bytes))
//		{
//			System.out.print(b + " ");
//		}
//		System.out.println();
//		System.out.println("-----------");
		var packet = new ReceivablePacket(Encryption.decrypt(bytes));
		switch (packet.readShort()) // Packet id.
		{
			case 1:
//				new AccountAuthenticationRequest(client, packet);
				clientRequestHandlers.get(ClientRequestType.ACCOUNT_AUTHENTICATION.name()).handle(this, packet);
				break;
			
			case 2:
//				new CharacterSelectionInfoRequest(client, packet);
				clientRequestHandlers.get(ClientRequestType.CHARACTER_SELECTION_INFO.name()).handle(this, packet);
				break;
			
			case 3:
//				new CharacterCreationRequest(client, packet);
				clientRequestHandlers.get(ClientRequestType.CHARACTER_CREATION.name()).handle(this, packet);
				break;
			
			case 4:
//				new CharacterDeletionRequest(client, packet);
				clientRequestHandlers.get(ClientRequestType.CHARACTER_DELETION.name()).handle(this, packet);
				break;
			
			case 5:
//				new CharacterSlotUpdate(client, packet);
				clientRequestHandlers.get(ClientRequestType.CHARACTER_SLOT_UPDATE.name()).handle(this, packet);
				break;
			
			case 6:
//				new CharacterSelectUpdate(client, packet);
				clientRequestHandlers.get(ClientRequestType.CHARACTER_SELECT_UPDATE.name()).handle(this, packet);
				break;
			
			case 7:
//				new EnterWorldRequest(client, packet);
				clientRequestHandlers.get(ClientRequestType.ENTER_WORLD.name()).handle(this, packet);
				break;
			
			case 8:
//				new ExitWorldRequest(client, packet);
				clientRequestHandlers.get(ClientRequestType.EXIT_WORLD.name()).handle(this, packet);
				break;
			
			case 9:
//				new LocationUpdateRequest(client, packet);
				clientRequestHandlers.get(ClientRequestType.LOCATION_UPDATE.name()).handle(this, packet);
				break;
			
			case 10:
//				new AnimatorUpdateRequest(client, packet);
				clientRequestHandlers.get(ClientRequestType.ANIMATOR_UPDATE.name()).handle(this, packet);
				break;
			
			case 11:
//				new ObjectInfoRequest(client, packet);
				clientRequestHandlers.get(ClientRequestType.OBJECT_INFO.name()).handle(this, packet);
				break;
			
			case 12:
//				new PlayerOptionsUpdate(client, packet);
				clientRequestHandlers.get(ClientRequestType.PLAYER_OPTIONS_UPDATE.name()).handle(this, packet);
				break;
			
			case 13:
//				new ChatRequest(client, packet);
				clientRequestHandlers.get(ClientRequestType.CHAT_REQUEST.name()).handle(this, packet);
				break;
			
			case 14:
//				new TargetUpdateRequest(client, packet);
				clientRequestHandlers.get(ClientRequestType.TARGET_UPDATE.name()).handle(this, packet);
				break;
			default:
				System.err.println("Undefined Request !! " + packet.readString());
				break;
		}
//		GameClientPacketHandler.handle(this, new ReceivablePacket(Encryption.decrypt(bytes)));
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
	{
		cause.printStackTrace();
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx)
	{
		// Disconnected.
		WorldManager.removeClient(this);
		LOGGER.finer("Client Disconnected: " + ctx.channel());
	}
	
	public String getIp()
	{
		return _ip;
	}
	
	public String getAccountName()
	{
		return _accountName;
	}
	
	public void setAccountName(String accountName)
	{
		_accountName = accountName;
	}
	
	public Player getActiveChar()
	{
		return _activeChar;
	}
	
	public void setActiveChar(Player activeChar)
	{
		_activeChar = activeChar;
	}
}