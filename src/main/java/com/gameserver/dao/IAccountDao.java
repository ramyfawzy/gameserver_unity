package com.gameserver.dao;

import com.gameserver.dao.model.Account;
import com.google.inject.ImplementedBy;

@ImplementedBy(value = AccountDao.class)
public interface IAccountDao {
	
	Account findByAccountName(String accountName);

}
