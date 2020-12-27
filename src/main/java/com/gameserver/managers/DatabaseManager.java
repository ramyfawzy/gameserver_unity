package com.gameserver.managers;

import java.sql.Connection;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.gameserver.Config;
import com.google.inject.Singleton;
import com.zaxxer.hikari.HikariDataSource;

/**
 * @author Ramy Ibrahim
 */
@Singleton
public class DatabaseManager implements IDatabaseManager
{
	private static final Logger LOGGER = LogManager.getLogger(DatabaseManager.class.getName());
	
	private static final HikariDataSource _hds = new HikariDataSource();
	
	
	
	public DatabaseManager() {
	}

	public void init()
	{
		_hds.setDriverClassName(Config.DATABASE_DRIVER);
		_hds.setJdbcUrl(Config.DATABASE_URL);
		_hds.setUsername(Config.DATABASE_LOGIN);
		_hds.setPassword(Config.DATABASE_PASSWORD);
		_hds.setMaximumPoolSize(Config.DATABASE_MAX_CONNECTIONS);
		_hds.setIdleTimeout(Config.DATABASE_MAX_IDLE_TIME);
		
		// Test if connection is valid.
		try
		{
			_hds.getConnection().close();
			LOGGER.info("Database: Initialized.");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public Connection getConnection()
	{
		Connection con = null;
		while (con == null)
		{
			try
			{
				con = _hds.getConnection();
			}
			catch (Exception e)
			{
				LOGGER.fatal("DatabaseManager: Cound not get a connection. {}", e);
			}
		}
		return con;
	}
	
	public static void close()
	{
		try
		{
			_hds.close();
		}
		catch (Exception e)
		{
			LOGGER.error("DatabaseManager: There was a problem closing the data source. {}", e);
		}
	}
}
