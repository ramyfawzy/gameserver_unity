package com.gameserver;

import java.awt.Toolkit;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import com.gameserver.data.IItemData;
import com.gameserver.data.INpcData;
import com.gameserver.data.ISkillData;
import com.gameserver.data.ISpawnData;
import com.gameserver.managers.IDatabaseManager;
import com.gameserver.managers.ThreadPoolManager;
import com.gameserver.managers.WorldManager;
import com.gameserver.network.ClientInitializer;
import com.gameserver.network.Encryption;
import com.gameserver.network.packets.receivable.IRequestHandler;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

/**
 * @author Ramy Ibrahim
 */
public class GameServer
{
	private static final Logger LOGGER = LogManager.getLogger(GameServer.class.getName());
	
	@Inject
	IDatabaseManager databaseManager;
	@Inject
	ISkillData skillData;
	@Inject
	ISpawnData spawnData;
	@Inject
	IItemData itemData;
	@Inject
	INpcData npcData;
	@Inject
	Map<String, IRequestHandler> clientRequestHandlers;
	
	public static void main(String[] args) throws Exception
	{
		final Injector injector = Guice.createInjector(new ServerModule());
		GameServer server = injector.getInstance(GameServer.class);
	    server.run();
	}
	
	private void run() throws Exception{
		
		// Keep start time for later.
		final long serverLoadStart = System.currentTimeMillis();
		
		LOGGER.info("Loading configs ...");
		Config.load();
		
		LOGGER.info("Initializing Database ...");
		databaseManager.init();
		
		LOGGER.info("Initializing World ...");
		WorldManager.init();
		
		LOGGER.info("Initializing Encryption ...");
		Encryption.init();
		
		LOGGER.info("Initializing ThreadPool ...");
		ThreadPoolManager.init();
		
		LOGGER.info("Initializing Skills ...");
		skillData.init();
		
		LOGGER.info("Initializing Items ...");
		itemData.init();
		
		LOGGER.info("Initializing NPCs ...");
		npcData.init();
		
		LOGGER.info("Initializing Spawn Data ...");
		spawnData.init();
		
		// Post info.
		LOGGER.info("Server loaded in " + ((System.currentTimeMillis() - serverLoadStart) / 1000) + " seconds.");
		System.gc();
		LOGGER.info("Started, using " + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576) + " of " + (Runtime.getRuntime().maxMemory() / 1048576) + " MB total memory.");
		
		// Initialize Network.
		var channelFuture = new ServerBootstrap() //
			.group(new NioEventLoopGroup(1), new NioEventLoopGroup(Config.IO_PACKET_THREAD_CORE_SIZE)) //
			.channel(NioServerSocketChannel.class) //
			.childHandler(new ClientInitializer(clientRequestHandlers)) //
			.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT) //
			.bind(Config.GAMESERVER_HOSTNAME, Config.GAMESERVER_PORT) //
			.sync();
		LOGGER.info("Listening on " + Config.GAMESERVER_HOSTNAME + ":" + Config.GAMESERVER_PORT);
		
		// Notify sound.
		Toolkit.getDefaultToolkit().beep();
	}
}