package com.gameserver.managers;

import java.sql.Connection;

public interface IDatabaseManager {
	
	void init();
	
	Connection getConnection();

}
