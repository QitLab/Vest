package com.qit.plugin.vest;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.refactoring.RefactoringFactory;
import com.intellij.refactoring.RefactoringManager;
import com.intellij.refactoring.RenameRefactoring;
import com.intellij.refactoring.rename.PsiPackageRenameValidator;
import com.intellij.rt.coverage.util.DictionaryLookup;
import com.intellij.spellchecker.DictionaryFileTypeFactory;
import com.qit.plugin.bean.VestTree;
import com.qit.plugin.ui.VestGui;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

//Messages.showMessageDialog(e.getData(PlatformDataKeys.PROJECT), named.getName(), file2.getName(), Messages.getInformationIcon());
public class VestPlugin extends AnAction {
    public static Project project;

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        project = e.getProject();
        Module[] modules = ModuleManager.getInstance(project).getModules();
        List<VestTree> vestTreeList = new ArrayList<>();
        for (int i = 1; i < modules.length; i++) {
            String moduleName = modules[i].getName().substring(modules[i].getName().indexOf('.') + 1);
            String modulePath = project.getBasePath() + "/" + moduleName + "/src/main/java/";
            VestTree vestTree = Util.loopFiles(new File(modulePath), "", new VestTree(modules[i]), 0);
            vestTreeList.add(vestTree);
        }
        EventQueue.invokeLater(() -> {
            try {
                new VestGui(vestTreeList);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

}
