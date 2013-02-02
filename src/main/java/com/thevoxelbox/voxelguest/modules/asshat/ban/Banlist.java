package com.thevoxelbox.voxelguest.modules.asshat.ban;

import com.google.common.base.Preconditions;
import com.thevoxelbox.voxelguest.persistence.Persistence;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.bukkit.Bukkit;

/**
 * @author Monofraps
 */
public class Banlist
{
	private List<BannedPlayer> bannedPlayers = new ArrayList<>();

	/**
	 * Bans the player named playerName and stores the ban reason.
	 * @param playerName The name of the player to ban.
	 * @param banReason The reason the player is banned for.
	 */
	public final void ban(final String playerName, final String banReason)
	{
		Preconditions.checkState(!isPlayerBanned(playerName), "Player %s already banned.", playerName);
		bannedPlayers.add(new BannedPlayer(playerName, banReason));
	}

	/**
	 * Unbans the player named playerName.
	 * @param playerName The name of the player to unban.
	 */
	public final void unban(final String playerName)
	{
		Preconditions.checkState(isPlayerBanned(playerName), "Player %s is not banned.", playerName);
		bannedPlayers.remove(getBannedPlayer(playerName));
	}

	private BannedPlayer getBannedPlayer(final String playerName)
	{
		for (BannedPlayer player : bannedPlayers)
		{
			if (player.getPlayerName().equalsIgnoreCase(playerName))
			{
				return player;
			}
		}

		return null;
	}

	/**
	 * Checks if a player is banned.
	 * @param playerName The name of the player to check.
	 * @return Returns true if the player is banned, otherwise false.
	 */
	public final boolean isPlayerBanned(final String playerName)
	{
		return getBannedPlayer(playerName) != null;

	}

	/**
	 * Gets the reason why a player is banned.
	 * @param playerName The name of the player to check.
	 * @return Returns the reason the player is banned for.
	 */
	public final String whyIsPlayerBanned(final String playerName)
	{
		Preconditions.checkState(isPlayerBanned(playerName), "Player %s must be banned in order to get the ban reason.", playerName);

		return getBannedPlayer(playerName).getBanReason();
	}

	/**
	 * Loads banned players list from persistence system.
	 */
	public final void load()
	{
		bannedPlayers.clear();
		
		File f = new File("plugins/VoxelGuest/asshatmitigation/banned.properties");
        FileInputStream fi = null;

        if (f.exists()) {
            try {
                fi = new FileInputStream(f);
                DataInputStream in = new DataInputStream(fstream);
    	        BufferedReader br = new BufferedReader(new InputStreamReader(in));
    	        String str;
    	        while((str = br.readLine()) != null) {
    	        	
    	        }
            } catch (FileNotFoundException ex) {
            	Bukkit.getLogger().warning("[VoxelGuest] File not found: " + f.getAbsolutePath());
            } catch (IOException ex) {
            	Bukkit.getLogger().warning("[VoxelGuest] Incorrectly loaded properties from " + f.getAbsolutePath());
            } finally {
                try {
                    if (fi != null) {
                        fi.close();
                    }
                } catch (IOException ex) {
                	Bukkit.getLogger().severe("##### -- FATAL ERROR -- ##### Failed to store data to " + f.getAbsolutePath());
                    ex.printStackTrace();
                }
            }
        }

		/*List<Object> protoList = Persistence.getInstance().loadAll(BannedPlayer.class);
		for (Object protoPlayer : protoList)
		{
			bannedPlayers.add((BannedPlayer) protoPlayer);
		}*/
	}

	/**
	 * Saves banned players list to persistence system.
	 */
	public final void save()
	{
		List<Object> protoList = new ArrayList<>();
		for (BannedPlayer protoPlayer : bannedPlayers)
		{
			protoList.add(protoPlayer);
		}

		Persistence.getInstance().saveAll(protoList);
	}
}
