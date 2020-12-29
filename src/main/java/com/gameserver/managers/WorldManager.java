package com.gameserver.managers;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.gameserver.Config;
import com.gameserver.model.WorldObject;
import com.gameserver.model.actor.Player;
import com.gameserver.model.holders.RegionHolder;
import com.gameserver.network.GameClient;
import com.gameserver.network.packets.sendable.DeleteObject;

/**
 * @author Ramy Ibrahim
 */
public class WorldManager
{
	private static final Logger LOGGER = LogManager.getLogger(WorldManager.class.getName());
	private static int VISIBILITY_RADIUS = 10000;
	private static int MOVEMENT_BROADCAST_RADIUS = VISIBILITY_RADIUS + 100; // Need the extra distance to send location of objects getting out of range.
	private static double REGION_RADIUS = Math.sqrt(VISIBILITY_RADIUS);
	private static int REGION_SIZE_X = (int) (Config.WORLD_MAXIMUM_X / REGION_RADIUS);
	private static int REGION_SIZE_Z = (int) (Config.WORLD_MAXIMUM_Z / REGION_RADIUS);
	private static RegionHolder[][] REGIONS = new RegionHolder[REGION_SIZE_X][];
	private static List<GameClient> ONLINE_CLIENTS = new ArrayList<>();
	private static List<Player> PLAYER_OBJECTS = new ArrayList<>();
	
	public static void init()
	{
		// Initialize regions.
		for (int x = 0; x < REGION_SIZE_X; x++)
		{
			REGIONS[x] = new RegionHolder[REGION_SIZE_Z];
			for (int z = 0; z < REGION_SIZE_Z; z++)
			{
				REGIONS[x][z] = new RegionHolder(x, z);
			}
		}
		
		// Set surrounding regions.
		for (int x = 0; x < REGION_SIZE_X; x++)
		{
			for (int z = 0; z < REGION_SIZE_Z; z++)
			{
				List<RegionHolder> surroundingRegions = new ArrayList<>();
				for (int sx = x - 1; sx <= (x + 1); sx++)
				{
					for (int sz = z - 1; sz <= (z + 1); sz++)
					{
						if ((sx == x) && (sz == z))
						{
							// continue;
						}
						
						if (((sx >= 0) && (sx < REGION_SIZE_X) && (sz >= 0) && (sz < REGION_SIZE_Z)))
						{
							surroundingRegions.add(REGIONS[sx][sz]);
						}
					}
				}
				REGIONS[x][z].setSurroundingRegions(surroundingRegions);
				
			}
		}
		
		LOGGER.info("WorldManager: Initialized {} by {} regions.", REGION_SIZE_X , REGION_SIZE_Z);
	}
	
	public static RegionHolder getRegion(WorldObject obj)
	{
		int x = (int) (obj.getLocation().getX() / REGION_RADIUS);
		int z = (int) (obj.getLocation().getZ() / REGION_RADIUS);
		if (x < 0)
		{
			x = 0;
		}
		if (z < 0)
		{
			z = 0;
		}
		if (x >= REGION_SIZE_X)
		{
			x = REGION_SIZE_X - 1;
		}
		if (z >= REGION_SIZE_Z)
		{
			z = REGION_SIZE_Z - 1;
		}
		return REGIONS[x][z];
	}
	
	public static void addObject(WorldObject obj)
	{
		if (obj.isPlayer())
		{
			Player player = obj.asPlayer();
			
			if (!PLAYER_OBJECTS.contains(player))
			{
				synchronized (PLAYER_OBJECTS)
				{
					PLAYER_OBJECTS.add(player);
				}
				synchronized (ONLINE_CLIENTS)
				{
					ONLINE_CLIENTS.add(player.getClient());
				}
				
				LOGGER.info("Player: {} Account: {} entered the world",	player.getName(),player.getClient().getAccountName());
			}
		}
	}
	
	public static void removeObject(WorldObject obj)
	{
		// Broadcast deletion to nearby players.
		List<Player> players = getVisiblePlayers(obj);
		for (Player player : players)
		{
			player.channelSend(new DeleteObject(obj));
		}
		
		// Remove from list and take necessary actions.
		if (obj.isPlayer())
		{
			Player player = obj.asPlayer();
			
			synchronized (PLAYER_OBJECTS)
			{
				PLAYER_OBJECTS.remove(player);
			}
			
			// Store player.
			player.storeMe();
			
			// Log world access.
			if ((player.getClient().getActiveChar() != null))
			{
				LOGGER.info("Player: {} Account: {} left the world",player.getName(),player.getClient().getAccountName());
			}
		}
		
		obj.getRegion().removeObject(obj);
	}
	
	public static List<WorldObject> getVisibleObjects(WorldObject obj)
	{
		List<WorldObject> result = new ArrayList<>();
		List<RegionHolder> regions = obj.getRegion().getSurroundingRegions();
		for (RegionHolder region : regions)
		{
			List<WorldObject> objects = region.getObjects();
			for (int j = 0; j < objects.size(); j++)
			{
				WorldObject nearby = objects.get(j);
				if (nearby.getObjectId() == obj.getObjectId())
				{
					continue;
				}
				if (obj.calculateDistance(nearby) < MOVEMENT_BROADCAST_RADIUS)
				{
					result.add(nearby);
				}
			}
		}
		return result;
	}
	
	public static List<Player> getVisiblePlayers(WorldObject obj)
	{
		List<Player> result = new ArrayList<>();
		List<RegionHolder> regions = obj.getRegion().getSurroundingRegions();
		for (RegionHolder region : regions)
		{
			List<WorldObject> objects = region.getObjects();
			for (WorldObject nearby : objects)
			{
				if (!nearby.isPlayer())
				{
					continue;
				}
				if (nearby.getObjectId() == obj.getObjectId())
				{
					continue;
				}
				if (obj.calculateDistance(nearby) < MOVEMENT_BROADCAST_RADIUS)
				{
					result.add(nearby.asPlayer());
				}
			}
		}
		return result;
	}
	
	public static Player getPlayerByName(String name)
	{
		for (Player player : PLAYER_OBJECTS)
		{
			if (player.getName().toLowerCase().equals(name.toLowerCase()))
			{
				return player;
			}
		}
		return null;
	}
	
	public static int getOnlineCount()
	{
		return ONLINE_CLIENTS.size();
	}
	
	public static void addClient(GameClient client)
	{
		synchronized (ONLINE_CLIENTS)
		{
			if (!ONLINE_CLIENTS.contains(client))
			{
				ONLINE_CLIENTS.add(client);
			}
		}
	}
	
	public static void removeClient(GameClient client)
	{
		// Store and remove player.
		Player player = client.getActiveChar();
		if (player != null)
		{
			removeObject(player);
			client.setActiveChar(null);
		}
		
		// Remove from list.
		synchronized (ONLINE_CLIENTS)
		{
			ONLINE_CLIENTS.remove(client);
		}
	}
	
	public static GameClient getClientByAccountName(String accountName)
	{
		for (GameClient client : ONLINE_CLIENTS)
		{
			if (client.getAccountName().equals(accountName))
			{
				return client;
			}
		}
		return null;
	}
}
