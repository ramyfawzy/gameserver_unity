package com.gameserver.network;

import java.util.Map;

import com.gameserver.network.packets.receivable.IRequestHandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;

/**
 * @author Ramy Ibrahim
 */
public class ClientInitializer extends ChannelInitializer<SocketChannel>
{
	private Map<String, IRequestHandler> clientRequestHandlers;
	public ClientInitializer(Map<String, IRequestHandler> clientRequestHandlers) {
		this.clientRequestHandlers = clientRequestHandlers;
	}
	
	@Override
	protected void initChannel(SocketChannel ch)
	{
		ChannelPipeline pipeline = ch.pipeline();
		// Decoders.
		pipeline.addLast("decoder", new ByteArrayDecoder());
		pipeline.addLast("encoder", new ByteArrayEncoder());
		// Handle the client.
		pipeline.addLast("clientHandler", new GameClient(clientRequestHandlers));
	}
}