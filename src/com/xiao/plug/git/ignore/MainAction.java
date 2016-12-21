package com.xiao.plug.git.ignore;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.ui.Messages;
import com.xiao.plug.git.ignore.bean.IgnoreFile;
import com.xiao.plug.git.ignore.bean.IgnoreItem;
import com.xiao.plug.git.ignore.component.IgnoreItemCreator;
import com.xiao.plug.git.ignore.component.FileCreator;

import java.util.List;


public class MainAction extends AnAction
{

    @Override
    public void actionPerformed(AnActionEvent event)
    {
        final String projectPath = event.getProject().getBasePath();

        Application application = ApplicationManager.getApplication();

        IgnoreItemCreator checker = application.getComponent(IgnoreItemCreator.class);

        List<IgnoreFile> files = checker.getIgnoreFiles(projectPath);

        FileCreator creator = application.getComponent(FileCreator.class);

        final StringBuilder builder = new StringBuilder();

        builder.append("Ignore file created successfully. ignored the items below:");

        boolean success = false;

        for (IgnoreFile file : files)
        {
            if (creator.createIgnoreFile(file))
            {
                success = true;

                builder.append("\n" + file.getPath() + " [SUCCESS]");

                for (IgnoreItem item : file.getIgnoreItems())
                {
                    builder.append("\n" + item.getContent());
                }
            }
            else
            {
                builder.append("\n" + file.getPath() + " [FAILED]");
            }
        }

        if (success)
        {
            Messages.showMessageDialog(builder.toString(), "Created Successfully", Messages.getInformationIcon());
        }
        else
        {
            showErrorMessage("Failed to create the ignore file !!!");
        }

    }

    private void showSuccessMessage(List<IgnoreFile> files)
    {
        final StringBuilder builder = new StringBuilder();

        builder.append("Ignore file created successfully. ignored the items below:");

        for (IgnoreFile file : files)
        {
            builder.append("\n" + file.getPath());

            for (IgnoreItem item : file.getIgnoreItems())
            {
                builder.append("\n" + item.getContent());
            }
        }

        Messages.showMessageDialog(builder.toString(), "Created Successfully", Messages.getInformationIcon());
    }

    private void showErrorMessage(String msg)
    {
        Messages.showMessageDialog("Error", msg, Messages.getInformationIcon());
    }
}
