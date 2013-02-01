package com.thevoxelbox.voxelguest.modules.greylist.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author MikeMatrix
 */
@Entity
@Table(name = "greylist")
public class Greylistee
{
    @Id
    @GeneratedValue
    @Column
    private long id;
    @Column
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
