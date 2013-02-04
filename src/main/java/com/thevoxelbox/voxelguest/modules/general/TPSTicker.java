package com.thevoxelbox.voxelguest.modules.general;

/**
 * Helps determine the number of ticks in a second by polling the system time at select intervals.
 *
 * @author TheCryoknight
 */
public class TPSTicker implements Runnable
{
    private static final long pollInterval = 150L;
    private static long lastTimestamp = 0L;
    private static long lastDifference = 0L;

    public static double calculateTPS()
    {
        if (lastDifference == 0L)
        {
            lastDifference = 1L;
        }

        return (double) pollInterval / lastDifference;
    }

    /**
     * Checks to see if the ticker has ticked yet to ensure accurate results.
     *
     * @return true if ticker has been initiated and started
     */
    public static boolean hasTicked()
    {
        return !(lastDifference == 0L);
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
