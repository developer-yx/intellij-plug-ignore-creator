package com.xiao.plug.git.ignore;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.xiao.plug.git.ignore.bean.IgnoreFile;
import com.xiao.plug.git.ignore.component.IgnoreItemCreator;
import com.xiao.plug.git.ignore.component.FileCreator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MainAction extends AnAction
{
    private static final String S_TEXT_STANDARD = "Create Default";

    private static final String S_TEXT_SELECT_MODULE = "Select Module";

    @Override
    public void actionPerformed(AnActionEvent event)
    {
        final Application application = ApplicationManager.getApplication();

        final VirtualFile projectPath = event.getProject().getBaseDir();

        int option = Messages.showYesNoDialog(event.getProject(), "Do you want create default Ignore file for standard Android project?" +
                "\n\nOr select module by yourself?", "Alert", S_TEXT_STANDARD, S_TEXT_SELECT_MODULE, Messages.getQuestionIcon());

        IgnoreItemCreator checker = application.getComponent(IgnoreItemCreator.class);

        final IgnoreFile rootIgnoreFile = checker.getRootIgnore(projectPath);

        List<IgnoreFile> moduleIgnoreFiles;

        if (option == 0)
        {
            moduleIgnoreFiles = checker.getDefaultModuleIgnore(projectPath);
        }
        else
        {
            VirtualFile[] modules = showModuleChooserDialog(event);

            moduleIgnoreFiles = new ArrayList<>();

            for (VirtualFile module : modules)
            {
                moduleIgnoreFiles.add(new IgnoreFile(new File(module.getPath()), IgnoreItemCreator.getModuleIgnoreItems()));
            }
        }

        FileCreator creator = application.getComponent(FileCreator.class);

        creator.createIgnoreFile(rootIgnoreFile);

        for (IgnoreFile file : moduleIgnoreFiles)
        {
            creator.createIgnoreFile(file);
        }

        showSuccessMessage(rootIgnoreFile, moduleIgnoreFiles);
    }

    private VirtualFile[] showModuleChooserDialog(AnActionEvent event)
    {
        FileChooserDescriptor descriptor = new FileChooserDescriptor(false, true, false, false, false, true);

        return FileChooser.chooseFiles(descriptor, event.getProject(), event.getProject().getBaseDir());
    }

    private void showSuccessMessage(IgnoreFile rootIgnore, List<IgnoreFile> moduleIgnores)
    {
        final StringBuilder builder = new StringBuilder();

        //        builder.append("\n");

        if (rootIgnore != null)
        {
            builder.append("-> ./.gitignore\n\n");
        }

        for (IgnoreFile file : moduleIgnores)
        {
            builder.append("-> ./" + file.getPath().getName() + "/.gitignore\n\n");
        }

        Messages.showMessageDialog(builder.toString(), "Created: ", Messages.getInformationIcon());
    }

    //
    //    private void showErrorMessage(String msg)
    //    {
    //        Messages.showMessageDialog("Error", msg, Messages.getInformationIcon());
    //    }
}
