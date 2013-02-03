package com.thevoxelbox.voxelguest.modules.general;

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

	public static boolean hasTicked()
	{
		return !(lastDifference == 0L);
	}

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
