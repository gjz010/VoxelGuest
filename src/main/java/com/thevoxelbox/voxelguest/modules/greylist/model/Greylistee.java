package com.thevoxelbox.voxelguest.modules.greylist.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author MikeMatrix
 */
@DatabaseTable(tableName = "greylist")
public class Greylistee
{
    @DatabaseField
    private long id;
    @DatabaseField
    private String name;

    public Greylistee()
    {
    }

    public Greylistee(final String name)
    {
        this.name = name;
    }

    public long getId()
    {
        return id;
    }

    public void setId(final long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }


}
