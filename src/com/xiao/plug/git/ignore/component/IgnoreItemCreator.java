package com.xiao.plug.git.ignore.component;

import com.intellij.openapi.components.ApplicationComponent;
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

    public List<IgnoreItem> getIgnoreItems(String rootPath)
    {
        List<IgnoreItem> items = new ArrayList<>();

        items.add(new IgnoreItem(".gradle/", "gradle file folder, created by Android Studio atomically"));

        items.add(new IgnoreItem(".idea/", "idea file folder, created by Android Studio atomically"));

        items.add(new IgnoreItem("local.properties", "this file will be changed in different System (Windows/Mac OS)"));

        items.add(new IgnoreItem("*.iml", "this file is created by Android Studio atomically"));

        items.add(new IgnoreItem("captures/", "when user capture device screenshot, this folder will be created"));

        items.add(new IgnoreItem("build/", "compile folder, created atomically while compiling"));

        List<IgnoreItem> modulesBuildFolders = getSettingsFile(new File(rootPath));

        if (modulesBuildFolders != null)
        {
            items.addAll(modulesBuildFolders);
        }

        return items;
    }

    private List<IgnoreItem> getSettingsFile(File root)
    {
        File[] list = root.listFiles(new FilenameFilter()
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
            List<IgnoreItem> items = new ArrayList<>();

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
                    items.add(new IgnoreItem(module + "/build/"));
                }
            }

            return items;
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
