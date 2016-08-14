package com.xiao.plug.git.ignore.component;

import com.intellij.openapi.components.ApplicationComponent;
import com.xiao.plug.git.ignore.bean.IgnoreItem;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FileCreator implements ApplicationComponent
{
    public FileCreator()
    {
    }

    @Override
    public void initComponent()
    {

    }

    @Override
    public void disposeComponent()
    {

    }

    @Override
    @NotNull
    public String getComponentName()
    {
        return "FileCreator";
    }

    public boolean createIgnoreFile(String path, List<IgnoreItem> items)
    {
        BufferedWriter bufferedWriter = null;

        try
        {
            bufferedWriter = new BufferedWriter(new FileWriter(new File(path + "/" + ".gitignore")));

            for (IgnoreItem item : items)
            {
                bufferedWriter.write(item.getContent() + "\n\n");
            }

            bufferedWriter.flush();

            return true;
        }
        catch (IOException e)
        {
            return false;
        }
        finally
        {
            try
            {
                bufferedWriter.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
