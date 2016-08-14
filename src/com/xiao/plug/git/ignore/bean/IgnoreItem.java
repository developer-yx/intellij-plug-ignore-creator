package com.xiao.plug.git.ignore.bean;

public class IgnoreItem
{
    private String name;

    private String description;

    public IgnoreItem(String name, String description)
    {
        this.name = name;

        this.description = description;
    }

    public IgnoreItem(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public String getContent()
    {
        return (description == null ? "" : "# " + description + "\n") + name;
    }

    @Override
    public String toString()
    {
        return "IgnoreItem{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
