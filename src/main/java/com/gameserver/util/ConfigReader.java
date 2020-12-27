package com.gameserver.util;

import java.util.Properties;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * @author Ramy Ibrahim
 */
public final class ConfigReader
{
	private static final Logger LOGGER = LogManager.getLogger(ConfigReader.class.getName());
	
	private final Properties _properties = new Properties();
	private final String _fileName;
	
	public ConfigReader(String fileName)
	{
		_fileName = fileName;
		try
		{
			var inputStream = getClass().getClassLoader().getResourceAsStream(_fileName);
			_properties.load(inputStream);
		}
		catch (Exception e)
		{
			LOGGER.error("Could not load {} {}", fileName, e.getMessage());
		}
	}
	
	public String getString(String config, String defaultValue)
	{
		if (!_properties.containsKey(config))
		{
			LOGGER.warn("Missing config {} from file {}. Default value {} will be used instead.", config, _fileName , defaultValue);
			return defaultValue;
		}
		return _properties.getProperty(config);
	}
	
	public boolean getBoolean(String config, boolean defaultValue)
	{
		if (!_properties.containsKey(config))
		{
			LOGGER.warn("Missing config {} from file {}. Default value {} will be used instead.", config, _fileName , defaultValue);
			return defaultValue;
		}
		
		try
		{
			return Boolean.parseBoolean(_properties.getProperty(config));
		}
		catch (Exception e)
		{
			LOGGER.warn("Missing config {} from file {}. Default value {} will be used instead.", config, _fileName , defaultValue);
			return defaultValue;
		}
	}
	
	public int getInt(String config, int defaultValue)
	{
		if (!_properties.containsKey(config))
		{
			LOGGER.warn("Missing config {} from file {}. Default value {} will be used instead.", config, _fileName , defaultValue);
			return defaultValue;
		}
		
		try
		{
			return Integer.parseInt(_properties.getProperty(config));
		}
		catch (Exception e)
		{
			LOGGER.warn("Missing config {} from file {}. Default value {} will be used instead.", config, _fileName , defaultValue);
			return defaultValue;
		}
	}
	
	public long getLong(String config, long defaultValue)
	{
		if (!_properties.containsKey(config))
		{
			LOGGER.warn("Missing config {} from file {}. Default value {} will be used instead.", config, _fileName , defaultValue);
			return defaultValue;
		}
		
		try
		{
			return Long.parseLong(_properties.getProperty(config));
		}
		catch (Exception e)
		{
			LOGGER.warn("Missing config {} from file {}. Default value {} will be used instead.", config, _fileName , defaultValue);
			return defaultValue;
		}
	}
	
	public float getFloat(String config, float defaultValue)
	{
		if (!_properties.containsKey(config))
		{
			LOGGER.warn("Missing config {} from file {}. Default value {} will be used instead.", config, _fileName , defaultValue);
			return defaultValue;
		}
		
		try
		{
			return Float.parseFloat(_properties.getProperty(config));
		}
		catch (Exception e)
		{
			LOGGER.warn("Missing config {} from file {}. Default value {} will be used instead.", config, _fileName , defaultValue);
			return defaultValue;
		}
	}
	
	public double getDouble(String config, double defaultValue)
	{
		if (!_properties.containsKey(config))
		{
			LOGGER.warn("Missing config {} from file {}. Default value {} will be used instead.", config, _fileName , defaultValue);
			return defaultValue;
		}
		
		try
		{
			return Double.parseDouble(_properties.getProperty(config));
		}
		catch (Exception e)
		{
			LOGGER.warn("Missing config {} from file {}. Default value {} will be used instead.", config, _fileName , defaultValue);
			return defaultValue;
		}
	}
}
