package com.gameserver.dao.model;

import java.util.Objects;

public class Account {
	
	private String account;
	private String password;
	private String email;
	private String firstName;
	private String lastName;
	private long creationTime;
	private long lastActive;
	private int status;
	private int membership;
	private String lastIp;
	private int activeCode;
	
	public Account() {
	}
	
	public Account(String account, String password, String email, String firstName, String lastName, long creationTime,
			long lastActive, int status, int membership, String lastIp, int activeCode) {
		this.account = account;
		this.password = password;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.creationTime = creationTime;
		this.lastActive = lastActive;
		this.status = status;
		this.membership = membership;
		this.lastIp = lastIp;
		this.activeCode = activeCode;
	}

	/**
	 * @return the account
	 */
	public String getAccount() {
		return account;
	}

	/**
	 * @param account the account to set
	 */
	public void setAccount(String account) {
		this.account = account;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the creationTime
	 */
	public long getCreationTime() {
		return creationTime;
	}

	/**
	 * @param creationTime the creationTime to set
	 */
	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}

	/**
	 * @return the lastActive
	 */
	public long getLastActive() {
		return lastActive;
	}

	/**
	 * @param lastActive the lastActive to set
	 */
	public void setLastActive(long lastActive) {
		this.lastActive = lastActive;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the membership
	 */
	public int getMembership() {
		return membership;
	}

	/**
	 * @param membership the membership to set
	 */
	public void setMembership(int membership) {
		this.membership = membership;
	}

	/**
	 * @return the lastIp
	 */
	public String getLastIp() {
		return lastIp;
	}

	/**
	 * @param lastIp the lastIp to set
	 */
	public void setLastIp(String lastIp) {
		this.lastIp = lastIp;
	}

	/**
	 * @return the activeCode
	 */
	public int getActiveCode() {
		return activeCode;
	}

	/**
	 * @param activeCode the activeCode to set
	 */
	public void setActiveCode(int activeCode) {
		this.activeCode = activeCode;
	}

	@Override
	public int hashCode() {
		return Objects.hash(account);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Account)) {
			return false;
		}
		Account other = (Account) obj;
		return Objects.equals(account, other.account);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Account [account=").append(account).append(", password=").append(password).append(", email=")
				.append(email).append(", firstName=").append(firstName).append(", lastName=").append(lastName)
				.append(", creationTime=").append(creationTime).append(", lastActive=").append(lastActive)
				.append(", status=").append(status).append(", membership=").append(membership).append(", lastIp=")
				.append(lastIp).append(", activeCode=").append(activeCode).append("]");
		return builder.toString();
	}
	
}
