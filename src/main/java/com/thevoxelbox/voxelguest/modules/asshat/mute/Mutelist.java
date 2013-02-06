package com.thevoxelbox.voxelguest.modules.asshat.mute;

import com.google.common.base.Preconditions;
import com.thevoxelbox.voxelguest.persistence.Persistence;

import java.util.HashMap;
import java.util.List;

/**
 * @author Monofraps
 */
public class Mutelist
{
    /**
     * Mutes a player and stores the reason he was muted for.
     *
     * @param playerName The name of the player to mute.
     * @param muteReason The reason the player is muted for.
     */
    public final void mute(final String playerName, final String muteReason)
    {
        Preconditions.checkState(!isPlayerMuted(playerName), "Player %s already muted.", playerName);
        Persistence.getInstance().save(new MutedPlayer(playerName.toLowerCase(), muteReason));
    }

    /**
     * Unmutes a player.
     *
     * @param playerName The name of the player to unmute.
     */
    public final void unmute(final String playerName)
    {
        Preconditions.checkState(isPlayerMuted(playerName), "Player %s is not muted.", playerName);
        Persistence.getInstance().delete(getMutedPlayer(playerName));
    }

    private MutedPlayer getMutedPlayer(final String playerName)
    {
        HashMap<String, Object> selectRestrictions = new HashMap<>();
        selectRestrictions.put("playerName", playerName.toLowerCase());

        final List<MutedPlayer> mutedPlayers = Persistence.getInstance().loadAll(MutedPlayer.class, selectRestrictions);

        for (MutedPlayer mutedPlayer : mutedPlayers)
        {
            if (mutedPlayer.getPlayerName().equalsIgnoreCase(playerName))
            {
                return mutedPlayer;
            }
        }

        return null;
    }

    /**
     * Checks if a player is muted.
     *
     * @param playerName The name of the player to check.
     *
     * @return Returns true if the player is muted, otherwise false.
     */
    public final boolean isPlayerMuted(final String playerName)
    {
        return getMutedPlayer(playerName) != null;
    }

    /**
     * Gets the reason a player is muted for.
     *
     * @param playerName The name of the player.
     *
     * @return Returns the reason a player is banned for.
     */
    public final String whyIsPlayerMuted(final String playerName)
    {
        Preconditions.checkState(isPlayerMuted(playerName), "Player %s must be muted in order to get the mute reason.", playerName);
        return getMutedPlayer(playerName).getMuteReason();
    }
}
