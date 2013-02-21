package com.thevoxelbox.voxelguest.modules.helper;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 *
 * @author TheCryoknight
 */
@DatabaseTable(tableName = "helpers")
public class Helper
{
    @DatabaseField(generatedId = true)
    private long id;
    @DatabaseField
    private final String name;
    @DatabaseField
    private int reviews = 0;
    @DatabaseField
    private long lastReview = 0;

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

    public int getReviews()
    {
        return this.reviews;
    }

    public String getName()
    {
        return name;
    }

    public long getTimeOfLastReview()
    {
        return this.lastReview;
    }
}
