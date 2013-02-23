package com.thevoxelbox.voxelguest.modules.helper;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 *
 * @author TheCryoknight
 */
@DatabaseTable(tableName = "helpers")
public final class Helper
{
    @DatabaseField(generatedId = true)
    private long id;
    @DatabaseField
    private String name = "";
    @DatabaseField
    private int reviews = 0;
    @DatabaseField
    private long lastReview = 0;

    public Helper() {}

    public Helper(final String name)
    {
        this.name = name;
    }

    /**
     * Sets stats for reviews done by this helper
     */
    public void review()
    {
        this.reviews++;
        this.lastReview = System.currentTimeMillis();
    }

    /**
     * @return The number of reviews this helper has performed.
     */
    public int getReviews()
    {
        return this.reviews;
    }

    /**
     * @return The in game name of the helper.
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return The time (as a long) of the last review from this helper
     */
    public long getTimeOfLastReview()
    {
        return this.lastReview;
    }

    @Override
    public boolean equals(final Object other)
    {
        if (!(other instanceof Helper))
        {
            return false;
        }
        final Helper otherHelper = (Helper) other;
        if (!this.name.equalsIgnoreCase(otherHelper.getName()))
        {
            return false;
        }
        if (this.getTimeOfLastReview() != otherHelper.getTimeOfLastReview())
        {
            return false;
        }
        return true;
    }
}

