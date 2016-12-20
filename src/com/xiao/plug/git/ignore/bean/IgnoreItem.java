package com.xiao.plug.git.ignore.bean;

public class IgnoreItem
{
    private String content;

    public IgnoreItem(String content)
    {
        this.content = content;
    }

    public String getContent()
    {
        return content;
    }

    @Override
    public String toString()
    {
        return "IgnoreItem{" +
                "content='" + content + '\'' +
                '}';
    }
}
