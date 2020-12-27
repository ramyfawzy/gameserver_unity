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
		var packet = new ReceivablePacket(Encryption.decrypt(bytes));
		clientRequestHandlers.get(ClientRequestType.findByPacketId(packet.readShort()).name()).handle(this, packet);
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