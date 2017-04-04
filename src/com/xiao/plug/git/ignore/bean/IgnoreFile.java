package com.xiao.plug.git.ignore.bean;


import java.io.File;
import java.util.List;

public class IgnoreFile
{
    private File path;

    private List<IgnoreItem> ignoreItems;

    public IgnoreFile(File path, List<IgnoreItem> ignoreItems)
    {
        this.path = path;

        this.ignoreItems = ignoreItems;
    }

    public File getPath()
    {
        return path;
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
