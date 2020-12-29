package com.gameserver.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.gameserver.dao.model.Account;
import com.gameserver.managers.IDatabaseManager;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class AccountDao extends AbstractDao implements IAccountDao {

	private static final Logger LOGGER = LogManager.getLogger(AccountDao.class.getName());

	private static final String ACCOUNT_INFO_QUERY = "SELECT * FROM accounts WHERE account=?";

	@Inject
	public AccountDao(IDatabaseManager databaseManager) {
		super(databaseManager);
	}

	@Override
	public Account findByAccountName(String accountName) {
		Account account = null;
		try (Connection con = databaseManager.getConnection();
				PreparedStatement ps = con.prepareStatement(ACCOUNT_INFO_QUERY)) {
			
			ps.setString(1, accountName);
			try (ResultSet rset = ps.executeQuery()) {
				while (rset.next()) {
					account = new Account();
					account.setAccount(rset.getString("account"));
					account.setPassword(rset.getString("password"));
					account.setEmail(rset.getString("email"));
					account.setFirstName(rset.getString("first_name"));
					account.setLastName(rset.getString("last_name"));
					account.setCreationTime(rset.getTimestamp("creation_time").getTime());
					account.setLastActive(rset.getLong("last_active"));
					account.setStatus(rset.getInt("status"));
					account.setMembership(rset.getInt("membership"));
					account.setLastIp(rset.getString("last_ip"));
					account.setActiveCode(rset.getInt("active_code"));
				}
			}
		} catch (SQLException e) {
			LOGGER.error("{}",e.getMessage());
		}
		return account;
	}

}
