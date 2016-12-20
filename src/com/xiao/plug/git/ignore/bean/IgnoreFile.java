package com.xiao.plug.git.ignore.bean;

import java.util.List;

public class IgnoreFile
{
    private String path;

    private List<IgnoreItem> ignoreItems;

    public IgnoreFile(String path, List<IgnoreItem> ignoreItems)
    {
        this.path = path;

        this.ignoreItems = ignoreItems;
    }

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public List<IgnoreItem> getIgnoreItems()
    {
        return ignoreItems;
    }

    public void setIgnoreItems(List<IgnoreItem> ignoreItems)
    {
        this.ignoreItems = ignoreItems;
    }

    @Override
    public String toString()
    {
        return "IgnoreFile{" +
                "path='" + path + '\'' +
                ", ignoreItems=" + ignoreItems +
                '}';
    }
}
