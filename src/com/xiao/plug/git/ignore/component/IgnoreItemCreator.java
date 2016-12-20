package com.xiao.plug.git.ignore.component;

import com.intellij.openapi.components.ApplicationComponent;
import com.xiao.plug.git.ignore.bean.IgnoreFile;
import com.xiao.plug.git.ignore.bean.IgnoreItem;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
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

    public List<IgnoreFile> getIgnoreFiles(String rootPath)
    {
        List<IgnoreItem> rootIgnoreItems = new ArrayList<>();

        rootIgnoreItems.add(new IgnoreItem(".gradle/"));

        rootIgnoreItems.add(new IgnoreItem(".idea/"));

        rootIgnoreItems.add(new IgnoreItem("local.properties"));

        rootIgnoreItems.add(new IgnoreItem("*.iml"));

        rootIgnoreItems.add(new IgnoreItem("captures/"));

        rootIgnoreItems.add(new IgnoreItem("build/"));

        List<IgnoreFile> files = new ArrayList<>();

        files.add(new IgnoreFile(rootPath, rootIgnoreItems));

        List<IgnoreFile> moduleIgnoreFiles = getModuleIgnoreFiles(rootPath);

        if (moduleIgnoreFiles != null)
        {
            files.addAll(moduleIgnoreFiles);
        }

        return files;
    }

    private List<IgnoreFile> getModuleIgnoreFiles(String rootPath)
    {
        List<IgnoreFile> ignoreFiles = new ArrayList<>();

        File[] list = new File(rootPath).listFiles(new FilenameFilter()
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
                    ignoreFiles.add(new IgnoreFile(rootPath + "/" + module, getModuleIgnoreItems()));
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

    private static List<IgnoreItem> getModuleIgnoreItems()
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

        List<String> strings = new ArrayList<>();

        char[] chars = includeLine.toCharArray();

        int index = -1;

        for (int i = 0; i < chars.length; i++)
        {
            if ('\'' == chars[i])
            {
                if (index == -1)
                {
                    index = i + 1;
                }
                else
                {
                    try
                    {
                        strings.add(includeLine.substring(index, i)
                                .replace(":", ""));
                    }
                    catch (Exception e)
                    {

                    }

                    index = -1;
                }
            }
        }

        return strings;
    }
}
