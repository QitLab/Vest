package com.qit.plugin.vest;

import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.refactoring.RefactoringFactory;
import com.intellij.refactoring.RenameRefactoring;
import com.qit.plugin.bean.RePackage;
import com.qit.plugin.bean.VestTree;

import java.io.File;

public class Util {

    public static VestTree loopFiles(File file, String path, VestTree vestTree, int deep) {
        File[] fs = file.listFiles();
        if (fs != null) {
            for (File f : fs) {
                if (deep == 3)
                    vestTree.getPackagePath().add(path.substring(0, path.length() - 1));
                if (f.isDirectory()) {
                    path += f.getName() + ".";
                    loopFiles(f, path, vestTree, ++deep);
                } else if (f.isFile()) {
                    String suffix = f.getName().substring(f.getName().lastIndexOf(".") + 1);
                    if (!path.isEmpty() && (suffix.equals("kt") || suffix.equals("java"))) {
                        PsiFile[] psiFile = FilenameIndex.getFilesByName(VestPlugin.project, f.getName(), GlobalSearchScope.allScope(VestPlugin.project));
                        vestTree.getFile().add(psiFile[0]);
                    }
                }
            }
        }
        return vestTree;
    }


    public static void rename(PsiFile pisFile) {
        PsiElement[] psiElements = pisFile.getChildren();
        for (PsiElement psiElement : psiElements) {
            PsiNamedElement element;
            if (psiElement.toString().equals("CLASS")
                    || psiElement.toString().contains("PsiClass:")) {
                element = (PsiNamedElement) psiElement.getOriginalElement();
                String name = element.getName();
                if (name != null && name.length() > 2 && !"QQ".equals(name.substring(0, 2))) {
                    final RenameRefactoring refactoring =
                            RefactoringFactory.getInstance(VestPlugin.project).createRename(element, "QQ" + name.substring(2));
                    refactoring.setSearchInComments(true);
                    refactoring.setPreviewUsages(false);
                    refactoring.setSearchInNonJavaFiles(true);
                    refactoring.run();
                }
            }
        }
    }

    //java版成功了
    public static void renamePackage(PsiFile psiFile, RePackage rePackage) {
        PsiElement[] psiElements = psiFile.getChildren();
        for (PsiElement psiElement : psiElements) {
            PsiNamedElement element;
            if (//psiElement.toString().equals("PACKAGE_DIRECTIVE")||
                    psiElement.toString().contains("PsiPackageStatement:")) {
                PsiPackageStatement psiPackageStatement = (PsiPackageStatement) psiElement;
                PsiPackage psiPackage = (PsiPackage) psiPackageStatement.getPackageReference().resolve();
                String[] newNames = psiPackageStatement.getPackageName().replace(rePackage.getOldPackage(), rePackage.getNewPackage()).split("\\.");
                String[] oldNames = psiPackageStatement.getPackageName().split("\\.");
                boolean isStart = false;
                for (int i = newNames.length - 1; i >= 0; i--) {
                    String name = psiPackage.getName();
                    if (name.equals(oldNames[i])) {
                        if (!oldNames[i].equals(newNames[i])) {
                            isStart = true;
                            final RenameRefactoring refactoring = RefactoringFactory.getInstance(psiFile.getProject()).createRename(psiPackage.getOriginalElement(), newNames[i]);
                            refactoring.setSearchInComments(false);
                            refactoring.setSearchInNonJavaFiles(false);
                            refactoring.setPreviewUsages(false);
                            refactoring.respectAllAutomaticRenames();
                            refactoring.respectEnabledAutomaticRenames();
                            refactoring.run();
                        }
                    } else if (isStart) {
                        return;
                    }
                    if (i != 0) psiPackage = psiPackage.getParentPackage();
                }
            }

        }
    }
}
