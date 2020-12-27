package com.gameserver.network;

import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import com.gameserver.enums.ClientRequestType;
import com.gameserver.managers.WorldManager;
import com.gameserver.model.actor.Player;
import com.gameserver.network.packets.receivable.IRequestHandler;

/**
 * @author Ramy Ibrahim
 */
public class GameClient extends SimpleChannelInboundHandler<byte[]>
{
	private static final Logger LOGGER = LogManager.getLogger(GameClient.class.getName());
	
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
				clientRequestHandlers.get(ClientRequestType.ACCOUNT_AUTHENTICATION.name()).handle(this, packet);
				break;
			
			case 2:
				clientRequestHandlers.get(ClientRequestType.CHARACTER_SELECTION_INFO.name()).handle(this, packet);
				break;
			
			case 3:
				clientRequestHandlers.get(ClientRequestType.CHARACTER_CREATION.name()).handle(this, packet);
				break;
			
			case 4:
				clientRequestHandlers.get(ClientRequestType.CHARACTER_DELETION.name()).handle(this, packet);
				break;
			
			case 5:
				clientRequestHandlers.get(ClientRequestType.CHARACTER_SLOT_UPDATE.name()).handle(this, packet);
				break;
			
			case 6:
				clientRequestHandlers.get(ClientRequestType.CHARACTER_SELECT_UPDATE.name()).handle(this, packet);
				break;
			
			case 7:
				clientRequestHandlers.get(ClientRequestType.ENTER_WORLD.name()).handle(this, packet);
				break;
			
			case 8:
				clientRequestHandlers.get(ClientRequestType.EXIT_WORLD.name()).handle(this, packet);
				break;
			
			case 9:
				clientRequestHandlers.get(ClientRequestType.LOCATION_UPDATE.name()).handle(this, packet);
				break;
			
			case 10:
				clientRequestHandlers.get(ClientRequestType.ANIMATOR_UPDATE.name()).handle(this, packet);
				break;
			
			case 11:
				clientRequestHandlers.get(ClientRequestType.OBJECT_INFO.name()).handle(this, packet);
				break;
			
			case 12:
				clientRequestHandlers.get(ClientRequestType.PLAYER_OPTIONS_UPDATE.name()).handle(this, packet);
				break;
			
			case 13:
				clientRequestHandlers.get(ClientRequestType.CHAT_REQUEST.name()).handle(this, packet);
				break;
			
			case 14:
				clientRequestHandlers.get(ClientRequestType.TARGET_UPDATE.name()).handle(this, packet);
				break;
			default:
				LOGGER.error("Undefined Request -> {}", packet);
				break;
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
	{
		LOGGER.error("Exception in Client network channel {}", cause.getMessage());
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx)
	{
		// Disconnected.
		WorldManager.removeClient(this);
		LOGGER.debug("Client Disconnected: {}", ctx.channel());
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