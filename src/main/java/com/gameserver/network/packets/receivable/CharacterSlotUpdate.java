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
public class CharacterSlotUpdate implements IRequestHandler {
	private static final Logger LOGGER = LogManager.getLogger(CharacterSlotUpdate.class.getName());

	private static final String CHARACTER_SLOT_UPDATE_QUERY = "UPDATE characters SET slot=? WHERE account=? AND slot=?";

	@Inject
	IDatabaseManager databaseManager;

	@Override
	public void handle(GameClient client, ReceivablePacket packet) {
		// Read data.
		final byte oldSlot = (byte) packet.readByte();
		final byte newSlot = (byte) packet.readByte();

		// Database queries.
		try (Connection con = databaseManager.getConnection();
				PreparedStatement ps = con.prepareStatement(CHARACTER_SLOT_UPDATE_QUERY)) {
			ps.setInt(1, 0);
			ps.setString(2, client.getAccountName());
			ps.setInt(3, oldSlot);
			ps.execute();
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}

		try (Connection con = databaseManager.getConnection();
				PreparedStatement ps = con.prepareStatement(CHARACTER_SLOT_UPDATE_QUERY)) {
			ps.setInt(1, oldSlot);
			ps.setString(2, client.getAccountName());
			ps.setInt(3, newSlot);
			ps.execute();
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		}

		try (Connection con = databaseManager.getConnection();
				PreparedStatement ps = con.prepareStatement(CHARACTER_SLOT_UPDATE_QUERY)) {
			ps.setInt(1, newSlot);
			ps.setString(2, client.getAccountName());
			ps.setInt(3, 0);
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
