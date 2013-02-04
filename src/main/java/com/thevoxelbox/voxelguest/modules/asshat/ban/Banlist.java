package com.thevoxelbox.voxelguest.modules.asshat.ban;

import com.google.common.base.Preconditions;
import com.thevoxelbox.voxelguest.persistence.Persistence;

import java.util.HashMap;
import java.util.List;

/**
 * @author Monofraps
 */
public class Banlist
{
    /**
     * Bans the player named playerName and stores the ban reason.
     *
     * @param playerName The name of the player to ban.
     * @param banReason  The reason the player is banned for.
     */
    public final void ban(final String playerName, final String banReason)
    {
        Preconditions.checkState(!isPlayerBanned(playerName), "Player %s already banned.", playerName);
        Persistence.getInstance().save(new BannedPlayer(playerName, banReason));

    }

    /**
     * Unbans the player named playerName.
     *
     * @param playerName The name of the player to unban.
     */
    public final void unban(final String playerName)
    {
        Preconditions.checkState(isPlayerBanned(playerName), "Player %s is not banned.", playerName);
        Persistence.getInstance().delete(getBannedPlayer(playerName));
    }

    private BannedPlayer getBannedPlayer(final String playerName)
    {
        HashMap<String, Object> selectRestrictions = new HashMap<>();
        selectRestrictions.put("playerName", playerName);

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
