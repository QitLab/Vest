package com.qit.plugin.ui;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.Notifications;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.refactoring.RefactoringFactory;
import com.intellij.refactoring.RenameRefactoring;
import com.qit.plugin.bean.ModuleList;
import com.qit.plugin.bean.PathListModule;
import com.qit.plugin.bean.RePackage;
import com.qit.plugin.bean.VestTree;
import com.qit.plugin.vest.Util;
import com.qit.plugin.vest.VestPlugin;
import org.jetbrains.kotlin.psi.KtClass;
import org.jetbrains.kotlin.psi.KtFile;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class VestGui extends JFrame {

    private JButton mBtnConfirm;
    private JList mListModule;
    private JPanel mPanel;
    private JScrollPane mScrollPaneModule;
    private JList list2;
    private JScrollPane mScrollPanePath;
    private JTable mTable;
    private JFrame frame;


    private List<VestTree> mVestTreeList;
    private List<RePackage> rePackages = new ArrayList<>();

    /**
     * Launch the application.
     */
    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            try {
                new VestGui(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the frame.
     */
    public VestGui(List<VestTree> vestTreeList) {
        mVestTreeList = vestTreeList;
        frame = new JFrame("马甲包生成");
        frame.setSize(565, 576);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(mPanel);
        frame.setVisible(true);
        initModuleList();
        initPathList();
        confirmEvent();
    }

    private void initModuleList() {
        mListModule.setSelectionModel(new DefaultListSelectionModel() {

            @Override
            public void setSelectionInterval(int index0, int index1) {
                if (super.isSelectedIndex(index0)) {
                    super.removeSelectionInterval(index0, index1);
                } else {
                    super.addSelectionInterval(index0, index1);
                }
            }
        });
        mListModule.addListSelectionListener(e -> initPathList());
        mScrollPaneModule.setViewportView(mListModule);

        if (mVestTreeList != null)
            mListModule.setModel(new ModuleList(mVestTreeList));
        mListModule.setCellRenderer(new QCheckBox());
        for (int i = 0; i < mVestTreeList.size(); i++) {
            mListModule.setSelectedIndex(i);
        }
    }

    private void initPathList() {
        rePackages.clear();
        for (int i = 0; i < mVestTreeList.size(); i++) {
            if (mListModule.isSelectedIndex(i)) {
                Set<String> packagePathSet = mVestTreeList.get(i).getPackagePath();
                for (String str : packagePathSet) {
                    rePackages.add(new RePackage(str));
                }
            }
        }
        mTable.setModel(new PathListModule(rePackages));
        mTable.setPreferredScrollableViewportSize(new Dimension(550, 200));// 表格的显示尺寸
        mScrollPanePath.setViewportView(mTable);
    }


    private void confirmEvent() {
        mBtnConfirm.addActionListener(e -> {
            for (int i = 0; i < mVestTreeList.size(); i++) {
                if (mListModule.isSelectedIndex(i)) {
                    List<PsiFile> fs = mVestTreeList.get(i).getFile();
                    for (PsiFile psiFile : fs) {
                        Util.rename(psiFile);
//                        psiFile.accept(new PsiRecursiveElementWalkingVisitor() {
//                            @Override
//                            public void visitElement(PsiElement element) {
//                                super.visitElement(element);
////                                if (element.getFirstChild().getText().equals("package")) {
////                                    PsiPackageStatement psiPackageStatement = (PsiPackageStatement) element;
////                                    rePackages.forEach(rePackage ->
////                                            Util.renamePackage(psiPackageStatement, rePackage));
////                                }
//                                if (element instanceof PsiPackage) {
//                                   String a =  element.getText();
//                                } else if (element instanceof PsiPackage) {
//                                    String a =  element.getText();
//                                }
////                                if (element.toString().equals("CLASS")) {
////                                }
//                            }
//                        });

//
////                        if (f.toString().equals("CLASS")) {
////                            PsiFile[] psiFile = FilenameIndex.getFilesByName(VestPlugin.project, f.getName(), GlobalSearchScope.allScope(VestPlugin.project));
//                        PsiFile psiFile = f.getContainingFile();
//                        PsiElement[] psi = psiFile.getChildren();
//                        for (PsiElement psiElement : psi) {
//                            if (psiElement.toString().equals("PACKAGE_DIRECTIVE")) {
////                                String name = ((PsiNamedElement) psiElement).getName();
//                                final RenameRefactoring refactoring = RefactoringFactory.getInstance(VestPlugin.project).createRename(psiElement, "com.qit.grabs");
//                                refactoring.setSearchInComments(true);
//                                refactoring.setPreviewUsages(false);
//                                refactoring.setSearchInNonJavaFiles(true);
//                                refactoring.run();
//                                return;
//                            }
//                        }
                    }
                    frame.setVisible(false);
                }
            }
        });
    }

}
