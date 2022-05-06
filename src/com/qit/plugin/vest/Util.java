package com.qit.plugin.vest;

import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.psi.*;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.psi.xml.XmlFile;
import com.intellij.refactoring.RefactoringFactory;
import com.intellij.refactoring.RenameRefactoring;
import com.qit.plugin.bean.RePackage;
import com.qit.plugin.bean.VestTree;
import org.jetbrains.kotlin.psi.KtPackageDirective;
import org.jetbrains.kotlin.psi.KtSimpleNameExpression;

import java.io.File;
import java.util.List;

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
//                    if (!path.isEmpty()) {
                    PsiFile psiFile = PsiManager.getInstance(VestPlugin.project).findFile(LocalFileSystem.getInstance().refreshAndFindFileByIoFile(f));
//                        PsiFile[] psiFile = FilenameIndex.getFilesByName(VestPlugin.project, f.getName(), GlobalSearchScope.allScope(VestPlugin.project));
                    if (suffix.equals("kt") || suffix.equals("java")) {
                        vestTree.getClassFile().add(psiFile);
                    } else if (f.getName().equals("AndroidManifest.xml")) {
                        vestTree.setManifestFile(psiFile);
                        XmlFile manifest = (XmlFile) psiFile;
                        XmlAttributeValue packageElement = manifest.getDocument().getRootTag().getAttribute("package").getValueElement();
                        vestTree.setPackageElement(packageElement);
                    }
//                    }
                }
            }
        }
        return vestTree;
    }


    public static void rename(PsiFile pisFile) {
        PsiElement[] psiElements = pisFile.getChildren();
        for (PsiElement psiElement : psiElements) {
            PsiNamedElement element;
            if (psiElement.toString().equals("CLASS") || psiElement.toString().contains("PsiClass:")) {
                element = ((PsiNamedElement) psiElement);
                final RenameRefactoring refactoring =
                        RefactoringFactory.getInstance(VestPlugin.project).createRename(element, "BE" + element.getName());
                refactoring.setSearchInComments(true);
                refactoring.setPreviewUsages(false);
                refactoring.setSearchInNonJavaFiles(true);
                refactoring.respectAllAutomaticRenames();
                refactoring.respectEnabledAutomaticRenames();
                refactoring.run();
            }

        }
    }

    //使用manifest修改package
    public static void renameManiPackage(XmlAttributeValue packageElement, RePackage rePackage) {
        final RenameRefactoring refactoring = RefactoringFactory.getInstance(VestPlugin.project).createRename(packageElement, "com.xxx.mmm", false, false);
        refactoring.setSearchInComments(false);
        refactoring.setSearchInNonJavaFiles(false);
        refactoring.setPreviewUsages(false);
        refactoring.run();
        refactoring.doRefactoring(refactoring.findUsages());
    }

    //java版成功了 // kotlin也终于成功了
    public static void renamePackage(PsiFile psiFile, RePackage rePackage) {
        PsiElement[] psiElements = psiFile.getChildren();
        for (PsiElement psiElement : psiElements) {

            if (psiElement.toString().contains("PsiPackageStatement:")) {
                PsiPackageStatement psiPackageStatement = (PsiPackageStatement) psiElement;
                PsiPackage psiPackage = (PsiPackage) psiPackageStatement.getPackageReference().resolve();
                String[] newNames = psiPackageStatement.getPackageName().replace(rePackage.getOldPackage(), rePackage.getNewPackage()).split("\\.");
                String[] oldNames = psiPackageStatement.getPackageName().split("\\.");
                boolean isStart = false;
                for (int i = newNames.length - 1; i > 0; i--) {
                    String name = psiPackage.getName();
                    if (name.equals(oldNames[i])) {
                        if (!oldNames[i].equals(newNames[i])) {
                            isStart = true;
                            final RenameRefactoring refactoring = RefactoringFactory.getInstance(psiFile.getProject()).createRename(psiPackage.getOriginalElement(), newNames[i], false, false);
                            refactoring.setSearchInComments(false);
                            refactoring.setSearchInNonJavaFiles(false);
                            refactoring.setPreviewUsages(false);
                            refactoring.respectAllAutomaticRenames();
                            refactoring.respectEnabledAutomaticRenames();
                            refactoring.run();
                            refactoring.doRefactoring(refactoring.findUsages());
                        }
                    } else if (isStart) {
                        return;
                    }
                    psiPackage = psiPackage.getParentPackage();
                }
            } else if (psiElement.toString().equals("PACKAGE_DIRECTIVE")) {
                String[] newNames = rePackage.getNewPackage().split("\\.");
                String[] oldNames = rePackage.getOldPackage().split("\\.");
                List<KtSimpleNameExpression> packages = ((KtPackageDirective) psiElement).getPackageNames();
                for (int i = 1; i < packages.size(); i++) {
                    PsiElement ktElement = packages.get(i).getReference().resolve();
                    final RenameRefactoring refactoring = RefactoringFactory.getInstance(psiFile.getProject()).createRename(ktElement, newNames[i], false, false);
                    refactoring.setSearchInComments(false);
                    refactoring.setSearchInNonJavaFiles(false);
                    refactoring.setPreviewUsages(false);
                    refactoring.respectAllAutomaticRenames();
                    refactoring.respectEnabledAutomaticRenames();
                    refactoring.run();
                    refactoring.doRefactoring(refactoring.findUsages());
                }
                return;
            }

        }
    }

    //test
    public static void renameKtPackage(PsiFile psiFile, RePackage rePackage) {
        PsiElement[] psiElements = psiFile.getChildren();
        String[] a = {"aaa", "bbb", "ccc"};
        for (PsiElement psiElement : psiElements) {
            if (psiElement.toString().equals("PACKAGE_DIRECTIVE")) {
                List<KtSimpleNameExpression> packages = ((KtPackageDirective) psiElement).getPackageNames();
                for (int i = 0; i < packages.size(); i++) {
                    PsiElement ktElement = packages.get(i).getReference().resolve();
                    final RenameRefactoring refactoring = RefactoringFactory.getInstance(psiFile.getProject()).createRename(ktElement, a[i]);
                    refactoring.setSearchInComments(false);
                    refactoring.setSearchInNonJavaFiles(false);
                    refactoring.setPreviewUsages(false);
                    refactoring.respectAllAutomaticRenames();
                    refactoring.respectEnabledAutomaticRenames();
                    refactoring.run();
                }

            }

        }
    }

}
