package com.xiao.plug.git.ignore.component;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.vfs.VirtualFile;
import com.xiao.plug.git.ignore.bean.IgnoreFile;
import com.xiao.plug.git.ignore.bean.IgnoreItem;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IgnoreItemCreator implements ApplicationComponent
{
    public IgnoreItemCreator()
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
        return "FileChecker";
    }

    public IgnoreFile getRootIgnore(VirtualFile rootPath)
    {
        List<IgnoreItem> rootIgnoreItems = new ArrayList<>();

        rootIgnoreItems.add(new IgnoreItem(".gradle/"));

        rootIgnoreItems.add(new IgnoreItem(".idea/"));

        rootIgnoreItems.add(new IgnoreItem("local.properties"));

        rootIgnoreItems.add(new IgnoreItem("*.iml"));

        rootIgnoreItems.add(new IgnoreItem("captures/"));

        rootIgnoreItems.add(new IgnoreItem("build/"));

        return new IgnoreFile(new File(rootPath.getPath()), rootIgnoreItems);
    }

    public List<IgnoreFile> getDefaultModuleIgnore(VirtualFile rootPath)
    {
        List<IgnoreItem> ignoreItems = getModuleIgnoreItems();

        List<IgnoreFile> ignoreFiles = new ArrayList<>();

        File[] list = new File(rootPath.getPath()).listFiles(new FilenameFilter()
        {
            @Override
            public boolean accept(File dir, String name)
            {
                return "settings.gradle".equals(name);
            }
        });

        if (list == null || list.length != 1)
        {
            return null;
        }

        BufferedReader bufferedReader = null;

        try
        {
            bufferedReader = new BufferedReader(new FileReader(list[0]));

            String line;

            while ((line = bufferedReader.readLine()) != null)
            {
                if (!line.contains("include"))
                {
                    continue;
                }

                List<String> modules = getModules(line);

                for (String module : modules)
                {
                    ignoreFiles.add(new IgnoreFile(new File(rootPath.getPath() + "/" + module), ignoreItems));
                }
            }

            return ignoreFiles;
        }
        catch (Exception e)
        {
            return null;
        }
        finally
        {
            try
            {
                bufferedReader.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static List<IgnoreItem> getModuleIgnoreItems()
    {
        List<IgnoreItem> items = new ArrayList<>();

        items.add(new IgnoreItem("*.iml"));

        items.add(new IgnoreItem("build/"));

        items.add(new IgnoreItem("local.properties"));

        return items;
    }

    private static List<String> getModules(String includeLine)
    {
        if (includeLine == null || !includeLine.startsWith("include"))
        {
            return null;
        }

        includeLine = includeLine.replaceAll("include", " ");

        includeLine = includeLine.replaceAll("\'", " ");

        includeLine = includeLine.replaceAll(":", " ");

        includeLine = includeLine.replaceAll(",", " ");

        String[] items = includeLine.split(" ");

        List<String> moduleNames = new ArrayList<>();

        for (String item : items)
        {
            if (item != null && !item.trim().isEmpty())
            {
                moduleNames.add(item);
            }
        }

        return moduleNames;
    }
}
