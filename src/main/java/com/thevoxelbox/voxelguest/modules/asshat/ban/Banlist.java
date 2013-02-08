package com.thevoxelbox.voxelguest.modules.asshat.ban;

import com.google.common.base.Preconditions;
import com.thevoxelbox.voxelguest.persistence.Persistence;

import java.util.HashMap;
import java.util.List;

/**
 * Represents a list of banned players.
 * @author Monofraps
 */
public class Banlist
{
    /**
     * Bans the player named playerName and stores the ban reason.
     *
     * @param playerName The name of the player to ban.
     * @param banReason  The reason the player is banned for.
     *
     * @return Returns true if the ban operation was successful. False indicates that the player is already banned.
     */
    public final boolean ban(final String playerName, final String banReason)
    {
        if (isPlayerBanned(playerName))
        {
            return false;
        }

        Persistence.getInstance().save(new BannedPlayer(playerName.toLowerCase(), banReason));
        return true;
    }

    /**
     * Unbans the player named playerName.
     *
     * @param playerName The name of the player to unban.
     *
     * @return Returns true if the unban operation was successful. False indicates that the player is not banned.
     */
    public final boolean unban(final String playerName)
    {
        if (!isPlayerBanned(playerName))
        {
            return false;
        }

        Persistence.getInstance().delete(getBannedPlayer(playerName));
        return true;
    }

    private BannedPlayer getBannedPlayer(final String playerName)
    {
        HashMap<String, Object> selectRestrictions = new HashMap<>();
        selectRestrictions.put("playerName", playerName.toLowerCase());

        final List<BannedPlayer> bannedPlayers = Persistence.getInstance().loadAll(BannedPlayer.class, selectRestrictions);

        for (BannedPlayer bannedPlayer : bannedPlayers)
        {
            if (bannedPlayer.getPlayerName().equalsIgnoreCase(playerName))
            {
                return bannedPlayer;
            }
        }

        return null;
    }

    /**
     * Checks if a player is banned.
     *
     * @param playerName The name of the player to check.
     *
     * @return Returns true if the player is banned, otherwise false.
     */
    public final boolean isPlayerBanned(final String playerName)
    {
        return getBannedPlayer(playerName) != null;
    }

    /**
     * Gets the reason why a player is banned.
     *
     * @param playerName The name of the player to check.
     *
     * @return Returns the reason the player is banned for.
     */
    public final String whyIsPlayerBanned(final String playerName)
    {
        Preconditions.checkState(isPlayerBanned(playerName), "Player %s must be banned in order to get the ban reason.", playerName);
        return getBannedPlayer(playerName).getBanReason();
    }
}
