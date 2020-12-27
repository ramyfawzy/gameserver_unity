package com.gameserver;

import java.awt.Toolkit;
import java.io.File;
import java.util.Map;
import java.util.logging.Logger;

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
import com.google.inject.multibindings.MapBinder;

/**
 * @author Ramy Ibrahim
 */
public class GameServer
{
	private static final Logger LOGGER = Logger.getLogger(GameServer.class.getName());
	
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
//		new GameServer();
	}
	
//	private GameServer() throws Exception
//	{
//		 
//	}
	
	private void run() throws Exception{
		
		// Create log folder.
		final File logFolder = new File(".", "log");
		logFolder.mkdir();
		clientRequestHandlers.entrySet().forEach(System.out::println);
		// Create input stream for log file.
//		try (InputStream is = new FileInputStream(new File("./gameserver/log.cfg")))
//		{
//			LogManager.getLogManager().readConfiguration(is);
//		}
		
		// Keep start time for later.
		final long serverLoadStart = System.currentTimeMillis();
		
		printSection("Configs");
		Config.load();
//		databaseManager = injector.getInstance(IDatabaseManager.class);
		printSection("Database");
		databaseManager.init();
		
		WorldManager.init();
		
		printSection("Encryption");
		Encryption.init();
		
		printSection("ThreadPool");
		ThreadPoolManager.init();
		
		printSection("Skills");
//		skillData = injector.getInstance(ISkillData.class);
		skillData.init();
		
		printSection("Items");
//		itemData = injector.getInstance(IItemData.class);
		itemData.init();
		
		printSection("NPCs");
//		npcData = injector.getInstance(INpcData.class);
		npcData.init();
		
		printSection("spawnData");
//		spawnData = injector.getInstance(ISpawnData.class);
		spawnData.init();
		
		// Post info.
		printSection("Info");
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
	
	private void printSection(String s)
	{
		s = "=[ " + s + " ]";
		while (s.length() < 62)
		{
			s = "-" + s;
		}
		LOGGER.info(s);
	}
}