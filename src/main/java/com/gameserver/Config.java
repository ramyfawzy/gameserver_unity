package com.gameserver;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.gameserver.model.holders.LocationHolder;
import com.gameserver.util.ConfigReader;
import com.gameserver.util.Util;

/**
 * @author Ramy Ibrahim
 */
public final class Config
{
	// --------------------------------------------------
	// Constants
	// --------------------------------------------------
	private static final Logger LOGGER = Logger.getLogger(Config.class.getName());
	public static final String EOL = System.lineSeparator();
	
	// --------------------------------------------------
	// Config File Definitions
	// --------------------------------------------------
	private static final String ACCOUNT_CONFIG_FILE = "gameserver/config/Account.ini";
	// private static final String DATABASE_CONFIG_FILE = "./config/Database.ini";
	private static final String LOGGING_CONFIG_FILE = "gameserver/config/Logging.ini";
	private static final String NETWORK_CONFIG_FILE = "gameserver/config/Network.ini";
	private static final String PLAYER_CONFIG_FILE = "gameserver/config/Player.ini";
	private static final String WORLD_CONFIG_FILE = "gameserver/config/World.ini";
	private static final String SERVER_CONFIG_FILE = "gameserver/config/Server.ini";
	
	// --------------------------------------------------
	// Accounts
	// --------------------------------------------------
	public static boolean ACCOUNT_AUTO_CREATE;
	public static int ACCOUNT_MAX_CHARACTERS;
	
	// --------------------------------------------------
	// Database
	// --------------------------------------------------
	public static String DATABASE_CONNECTION_PARAMETERS;
	public static String DATABASE_DRIVER;
	public static String DATABASE_URL;
	public static String DATABASE_LOGIN;
	public static String DATABASE_PASSWORD;
	public static int DATABASE_MAX_CONNECTIONS;
	public static int DATABASE_MAX_IDLE_TIME;
	public static int SCHEDULED_THREAD_POOL_COUNT;
	public static int THREADS_PER_SCHEDULED_THREAD_POOL;
	public static int INSTANT_THREAD_POOL_COUNT;
	public static int THREADS_PER_INSTANT_THREAD_POOL;
	public static int IO_PACKET_THREAD_CORE_SIZE;
	
	// --------------------------------------------------
	// Logging
	// --------------------------------------------------
	public static boolean LOG_FILE_SIZE_LIMIT_ENABLED = false; // Must be set before configs load.
	public static long LOG_FILE_SIZE_LIMIT = 1073741824; // Must be set before configs load.
	public static boolean LOG_CHAT;
	public static boolean LOG_WORLD;
	public static boolean LOG_ADMIN;
	
	// --------------------------------------------------
	// Network
	// --------------------------------------------------
	public static int SERVER_PORT;
	public static int MAXIMUM_ONLINE_USERS;
	public static double CLIENT_VERSION;
	public static byte[] ENCRYPTION_SECRET_KEYWORD;
	public static int GAMESERVER_PORT;
	public static String GAMESERVER_HOSTNAME;
	
	// --------------------------------------------------
	// Player
	// --------------------------------------------------
	public static LocationHolder STARTING_LOCATION;
	public static List<Integer> STARTING_ITEMS = new ArrayList<>();
	public static List<Long> VALID_SKIN_COLORS = new ArrayList<>();
	
	// --------------------------------------------------
	// World
	// --------------------------------------------------
	public static float WORLD_MINIMUM_X;
	public static float WORLD_MAXIMUM_X;
	public static float WORLD_MINIMUM_Y;
	public static float WORLD_MAXIMUM_Y;
	public static float WORLD_MINIMUM_Z;
	public static float WORLD_MAXIMUM_Z;
	
	public static void load()
	{
		Util.printSection("Configs");
		
		// Initialize first so we can use logs as configured.
		ConfigReader loggingConfigs = new ConfigReader(LOGGING_CONFIG_FILE);
		LOG_FILE_SIZE_LIMIT_ENABLED = loggingConfigs.getBoolean("LogFileSizeLimitEnabled", false);
		LOG_FILE_SIZE_LIMIT = loggingConfigs.getLong("LogFileSizeLimit", 1073741824);
		LOG_CHAT = loggingConfigs.getBoolean("LogChat", true);
		LOG_WORLD = loggingConfigs.getBoolean("LogWorld", true);
		LOG_ADMIN = loggingConfigs.getBoolean("LogAdmin", true);
		
		ConfigReader accountConfigs = new ConfigReader(ACCOUNT_CONFIG_FILE);
		ACCOUNT_AUTO_CREATE = accountConfigs.getBoolean("AccountAutoCreate", false);
		ACCOUNT_MAX_CHARACTERS = accountConfigs.getInt("AccountMaxCharacters", 5);
		
		ConfigReader serverConfigs = new ConfigReader(SERVER_CONFIG_FILE);
		
		GAMESERVER_PORT = serverConfigs.getInt("GameserverPort", 5055);
		GAMESERVER_HOSTNAME = serverConfigs.getString("GameserverHostname", "127.0.0.1");
		DATABASE_CONNECTION_PARAMETERS = serverConfigs.getString("DbConnectionParameters", "Server=127.0.0.1;User ID=root;Password=;Database=edws");
		DATABASE_MAX_CONNECTIONS = serverConfigs.getInt("MaximumDbConnections", 50);
		DATABASE_DRIVER = serverConfigs.getString("Driver", "org.mariadb.jdbc.Driver");
		DATABASE_URL = serverConfigs.getString("URL", "jdbc:mariadb://localhost/edws");
		DATABASE_LOGIN = serverConfigs.getString("Login", "root");
		DATABASE_PASSWORD = serverConfigs.getString("Password", "");
		DATABASE_MAX_CONNECTIONS = serverConfigs.getInt("MaximumDbConnections", 100);
		DATABASE_MAX_IDLE_TIME = serverConfigs.getInt("MaximumDbIdleTime", 0);
		SCHEDULED_THREAD_POOL_COUNT = serverConfigs.getInt("ScheduledThreadPoolCount", -1);
		if (SCHEDULED_THREAD_POOL_COUNT == -1)
		{
			SCHEDULED_THREAD_POOL_COUNT = Runtime.getRuntime().availableProcessors();
		}
		THREADS_PER_SCHEDULED_THREAD_POOL = serverConfigs.getInt("ThreadsPerScheduledThreadPool", 4);
		INSTANT_THREAD_POOL_COUNT = serverConfigs.getInt("InstantThreadPoolCount", -1);
		if (INSTANT_THREAD_POOL_COUNT == -1)
		{
			INSTANT_THREAD_POOL_COUNT = Runtime.getRuntime().availableProcessors();
		}
		THREADS_PER_INSTANT_THREAD_POOL = serverConfigs.getInt("ThreadsPerInstantThreadPool", 2);
		IO_PACKET_THREAD_CORE_SIZE = serverConfigs.getInt("UrgentPacketThreadCoreSize", 2);
		
		ConfigReader networkConfigs = new ConfigReader(NETWORK_CONFIG_FILE);
		SERVER_PORT = networkConfigs.getInt("ServerPort", 5055);
		MAXIMUM_ONLINE_USERS = networkConfigs.getInt("MaximumOnlineUsers", 2000);
		CLIENT_VERSION = networkConfigs.getDouble("ClientVersion", 1.0);
		
		try
		{
			ENCRYPTION_SECRET_KEYWORD = MessageDigest.getInstance("MD5").digest(networkConfigs.getString("SecretKeyword", "SECRET_KEYWORD").getBytes(Charset.forName("UTF8")));
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		
		ConfigReader playerConfigs = new ConfigReader(PLAYER_CONFIG_FILE);
		String[] startingLocation = playerConfigs.getString("StartingLocation", "3924.109;67.42678;2329.238").split(";");
		STARTING_LOCATION = new LocationHolder(Float.parseFloat(startingLocation[0]), Float.parseFloat(startingLocation[1]), Float.parseFloat(startingLocation[2]), startingLocation.length > 3 ? Float.parseFloat(startingLocation[3]) : 0);
		STARTING_ITEMS.clear();
		for (var itemString : playerConfigs.getString("StartingItems", "").split(";"))
		{
			var trimmedString = itemString.trim();
			if (!trimmedString.equals(""))
			{
				int itemId = Integer.parseInt(trimmedString);
				if (itemId > 0)
				{
					STARTING_ITEMS.add(itemId);
				}
			}
			
		}
		
		VALID_SKIN_COLORS.clear();
		
		for (var colorCode : playerConfigs.getString("ValidSkinColorCodes", "F1D1BD;F1C4AD;E7B79C;E19F7E;AF7152;7E472E;4A2410;F7DDC0;F3D1A9;C5775A;B55B44;863923;672818;3F1508").split(";"))
		{
			VALID_SKIN_COLORS.add(Util.hexStringToLong(colorCode));
		}
		
		ConfigReader worldConfigs = new ConfigReader(WORLD_CONFIG_FILE);
		WORLD_MINIMUM_X = worldConfigs.getFloat("MinimumX", -558.8f);
		WORLD_MAXIMUM_X = worldConfigs.getFloat("MaximumX", 4441.2f);
		WORLD_MINIMUM_Y = worldConfigs.getFloat("MinimumY", -100f);
		WORLD_MAXIMUM_Y = worldConfigs.getFloat("MaximumY", 1000f);
		WORLD_MINIMUM_Z = worldConfigs.getFloat("MinimumZ", -1445.3f);
		WORLD_MAXIMUM_Z = worldConfigs.getFloat("MaximumZ", 3554.7f);
		LOGGER.info("Configs: Initialized.");
	}
}
