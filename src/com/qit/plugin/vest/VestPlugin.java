package com.qit.plugin.vest;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
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
            String modulePath = project.getBasePath() + "/" + moduleName + "/src/main/";
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
