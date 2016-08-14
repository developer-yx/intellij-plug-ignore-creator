package com.xiao.plug.git.ignore;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.ui.Messages;
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

        List<IgnoreItem> items = checker.getIgnoreItems(projectPath);

        FileCreator creator = application.getComponent(FileCreator.class);

        if (creator.createIgnoreFile(projectPath, items))
        {
            showSuccessMessage(items);
        }
        else
        {
            showErrorMessage("Failed to create the ignore file !!!");
        }

    }

    private void showSuccessMessage(List<IgnoreItem> items)
    {
        final StringBuilder builder = new StringBuilder();

        builder.append("Ignore file created successfully. ignored the items below:");

        for (IgnoreItem item : items)
        {
            builder.append("\n" + item.getName());
        }

        Messages.showMessageDialog(builder.toString(), "Created Successfully", Messages.getInformationIcon());
    }

    private void showErrorMessage(String msg)
    {
        Messages.showMessageDialog("Error", msg, Messages.getInformationIcon());
    }
}
