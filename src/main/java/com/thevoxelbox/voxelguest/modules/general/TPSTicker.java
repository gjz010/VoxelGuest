package com.thevoxelbox.voxelguest.modules.general;

/**
 * Helps determine the number of ticks in a second by polling the system time at select intervals.
 *
 * @author TheCryoknight
 */
public class TPSTicker implements Runnable
{
    private static final long pollInterval = 60L;
    private static long lastTimestamp = System.currentTimeMillis() - (pollInterval * 50);
    private static long lastDifference = pollInterval;

    /**
     * Calculates the number of ticks in a second
     *
     * @return Number of ticks in a second
     */
    public static double calculateTPS()
    {
        if (lastDifference == 0)
        {
            lastDifference = 1;
        }

        return (double) pollInterval / lastDifference;
    }

    /**
     * Get the time (in ticks) between polls, for use in ticker initiation.
     *
     * @return time in ticks between polls
     */
    public static long getPollInterval()
    {
        return pollInterval;
    }

    @Override
    public void run()
    {
        lastDifference = (System.currentTimeMillis() - lastTimestamp) / 1000;
        lastTimestamp = System.currentTimeMillis();
    }
}
