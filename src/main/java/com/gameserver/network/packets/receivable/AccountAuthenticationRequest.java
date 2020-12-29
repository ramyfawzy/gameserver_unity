package com.gameserver.network.packets.receivable;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.gameserver.Config;
import com.gameserver.dao.IAccountDao;
import com.gameserver.dao.model.Account;
import com.gameserver.managers.IDatabaseManager;
import com.gameserver.managers.WorldManager;
import com.gameserver.network.GameClient;
import com.gameserver.network.ReceivablePacket;
import com.gameserver.network.packets.sendable.AccountAuthenticationResult;
import com.gameserver.network.packets.sendable.Logout;
import com.google.inject.Inject;

/**
 * @author Ramy Ibrahim
 */
public class AccountAuthenticationRequest implements IRequestHandler {
	private static final Logger LOGGER = LogManager.getLogger(AccountAuthenticationRequest.class.getName());

	private static final String ACCOUNT_INFO_QUERY = "SELECT * FROM accounts WHERE account=?";
	private static final String ACCOUNT_INFO_UPDATE_QUERY = "UPDATE accounts SET last_active=?, last_ip=? WHERE account=?";
	private static final String ACCOUNT_CREATE_QUERY = "INSERT INTO accounts (account, password, status) values (?, ?, 3)";
	private static final int STATUS_NOT_FOUND = 0;
	private static final int STATUS_WRONG_PASSWORD = 3;
	private static final int STATUS_ALREADY_ONLINE = 4;
	private static final int STATUS_TOO_MANY_ONLINE = 5;
	private static final int STATUS_INCORRECT_CLIENT = 6;
	private static final int STATUS_AUTHENTICATED = 100;

	@Inject
	IDatabaseManager databaseManager;
	
	@Inject
	IAccountDao accountDao;

	@Override
	public void handle(GameClient client, ReceivablePacket packet) {
		// Read data.
		final double clientVersion = packet.readDouble();
		String accountName = packet.readString().toLowerCase();
		final String passwordHash = packet.readString();

		// Client version check.
		if (clientVersion != Config.CLIENT_VERSION) {
			client.channelSend(new AccountAuthenticationResult(STATUS_INCORRECT_CLIENT));
			return;
		}

		// Replace illegal characters.
		// for (char c : Util.ILLEGAL_CHARACTERS)
		// {
		// accountName = accountName.replace(c, '\'');
		// }

		// Account name checks.
		if ((accountName.length() < 2) || (accountName.length() > 20) || accountName.contains("'")
				|| (passwordHash.length() == 0)) // 20 should not happen, checking it here in case of client cheat.
		{
			client.channelSend(new AccountAuthenticationResult(STATUS_NOT_FOUND));
			return;
		}

		// Get data from database.
		String storedPassword = "";
		int status = STATUS_NOT_FOUND;
		Account account;
//		try (Connection con = databaseManager.getConnection();
//				PreparedStatement ps = con.prepareStatement(ACCOUNT_INFO_QUERY)) {
//			ps.setString(1, accountName);
//			try (ResultSet rset = ps.executeQuery()) {
//				while (rset.next()) {
//					storedPassword = rset.getString("password");
//					status = rset.getInt("status");
//				}
//			}
//		} catch (Exception e) {
//			LOGGER.error(e.getMessage());
//		}
		account = accountDao.findByAccountName(accountName);
		if(account != null) {
			storedPassword = account.getPassword();
			status = account.getStatus();
		}
		// In case of auto create accounts configuration.
		if ((status == 0) && Config.ACCOUNT_AUTO_CREATE) {
			// Create account.
			try (Connection con = databaseManager.getConnection();
					PreparedStatement ps = con.prepareStatement(ACCOUNT_CREATE_QUERY)) {
				ps.setString(1, accountName);
				ps.setString(2, passwordHash);
				ps.execute();
			} catch (Exception e) {
				LOGGER.error(e.getMessage());
			}
			LOGGER.info("Created account " + accountName + ".");
		} else // Account status issue.
		{
			// 0 does not exist, 1 banned, 2 requires activation, 3 wrong password, 4 too
			// many online, 100 authenticated
			if (status < STATUS_WRONG_PASSWORD) {
				client.channelSend(new AccountAuthenticationResult(status));
				return;
			}

			// Wrong password.
			if (!passwordHash.equals(storedPassword)) {
				client.channelSend(new AccountAuthenticationResult(STATUS_WRONG_PASSWORD));
				return;
			}
		}

		// Kick existing logged client.
		final GameClient existingClient = WorldManager.getClientByAccountName(accountName);
		if (existingClient != null) {
			existingClient.channelSend(new Logout());
			client.channelSend(new AccountAuthenticationResult(STATUS_ALREADY_ONLINE));
			return;
		}

		// Too many online users.
		if (WorldManager.getOnlineCount() >= Config.MAXIMUM_ONLINE_USERS) {
			client.channelSend(new AccountAuthenticationResult(STATUS_TOO_MANY_ONLINE));
			return;
		}

		// Authentication was successful.
		WorldManager.addClient(client);
		client.setAccountName(accountName);
		client.channelSend(new AccountAuthenticationResult(STATUS_AUTHENTICATED));

		// Update last login date and IP address.
		try (Connection con = databaseManager.getConnection();
				PreparedStatement ps = con.prepareStatement(ACCOUNT_INFO_UPDATE_QUERY)) {
			ps.setLong(1, System.currentTimeMillis());
			ps.setString(2, client.getIp());
			ps.setString(3, accountName);
			ps.execute();
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}

	}

	/**
	 * @return the databaseManager
	 */
	public IDatabaseManager getDatabaseManager() {
		return databaseManager;
	}

	/**
	 * @param databaseManager the databaseManager to set
	 */
	public void setDatabaseManager(IDatabaseManager databaseManager) {
		this.databaseManager = databaseManager;
	}
}
