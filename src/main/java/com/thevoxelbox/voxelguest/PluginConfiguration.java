package com.thevoxelbox.voxelguest;

import com.thevoxelbox.voxelguest.configuration.annotations.ConfigurationGetter;
import com.thevoxelbox.voxelguest.configuration.annotations.ConfigurationSetter;

/**
 * @author Monofraps
 */
public class PluginConfiguration
{
	private String dbConnectionString = "localhost:3306/database";
	private String dbUsername = "username";
	private String dbPassword = "password";

	@ConfigurationGetter("connection-string")
	public String getDbConnectionString()
	{
		return dbConnectionString;
	}

	@ConfigurationSetter("connection-string")
	public void setDbConnectionString(final String dbConnectionString)
	{
		this.dbConnectionString = dbConnectionString;
	}

	@ConfigurationGetter("db-user")
	public String getDbUsername()
	{
		return dbUsername;
	}

	@ConfigurationSetter("db-user")
	public void setDbUsername(final String dbUsername)
	{
		this.dbUsername = dbUsername;
	}

	@ConfigurationGetter("db-pass")
	public String getDbPassword()
	{
		return dbPassword;
	}

	@ConfigurationSetter("db-pass")
	public void setDbPassword(final String dbPassword)
	{
		this.dbPassword = dbPassword;
	}
}
