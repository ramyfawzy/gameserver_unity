package com.gameserver.network.packets.receivable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.gameserver.managers.IDatabaseManager;
import com.gameserver.network.GameClient;
import com.gameserver.network.ReceivablePacket;
import com.google.inject.Inject;

/**
 * @author Ramy Ibrahim
 */
public class CharacterSelectUpdate implements IRequestHandler {
	private static final Logger LOGGER = LogManager.getLogger(CharacterSelectUpdate.class.getName());

	private static final String CHARACTER_SELECTED_RESET_QUERY = "UPDATE characters SET selected=0 WHERE account=?";
	private static final String CHARACTER_SELECTED_UPDATE_QUERY = "UPDATE characters SET selected=1 WHERE account=? AND slot=?";

	@Inject
	IDatabaseManager databaseManager;

	@Override
	public void handle(GameClient client, ReceivablePacket packet) {
		// Read data.
		final int slot = packet.readByte();

		// Make existing characters selected value false.
		try (Connection con = databaseManager.getConnection();
				PreparedStatement ps = con.prepareStatement(CHARACTER_SELECTED_RESET_QUERY)) {
			ps.setString(1, client.getAccountName());
			ps.execute();
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}

		// Set character selected.
		try (Connection con = databaseManager.getConnection();
				PreparedStatement ps = con.prepareStatement(CHARACTER_SELECTED_UPDATE_QUERY)) {
			ps.setString(1, client.getAccountName());
			ps.setInt(2, slot);
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
