package com.gameserver.dao;

import com.gameserver.managers.IDatabaseManager;

public abstract class AbstractDao {
	
	protected final IDatabaseManager databaseManager;
	
	public AbstractDao(IDatabaseManager databaseManager) {
		this.databaseManager = databaseManager;
	}

}
