package com.qit.plugin.ui;

import com.intellij.psi.PsiFile;
import com.intellij.psi.xml.XmlAttributeValue;
import com.qit.plugin.bean.ModuleList;
import com.qit.plugin.bean.PathListModule;
import com.qit.plugin.bean.RePackage;
import com.qit.plugin.bean.VestTree;
import com.qit.plugin.vest.Util;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

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
        initFilesName();
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
    private void initFilesName() {
        rePackages.clear();
        for (int i = 0; i < mVestTreeList.size(); i++) {
            if (mListModule.isSelectedIndex(i)) {
                XmlAttributeValue packageElement = mVestTreeList.get(i).getPackageElement();
                rePackages.add(new RePackage(packageElement.getValue()));
            }
        }
        mTable.setModel(new PathListModule(rePackages));
        mTable.setPreferredScrollableViewportSize(new Dimension(550, 200));// 表格的显示尺寸
        mScrollPanePath.setViewportView(mTable);
    }

    private void initPathList() {
        rePackages.clear();
        for (int i = 0; i < mVestTreeList.size(); i++) {
            if (mListModule.isSelectedIndex(i)) {
                XmlAttributeValue packageElement = mVestTreeList.get(i).getPackageElement();
                rePackages.add(new RePackage(packageElement.getValue()));
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
                    List<PsiFile> fs = mVestTreeList.get(i).getClassFile();
//                    Util.renamePackage(fs.get(0), rePackages.get(0));
                    Util.renameManiPackage(mVestTreeList.get(i).getPackageElement(), rePackages.get(0));
//                    for (PsiFile psiFile : fs) {
//                        Util.rename(psiFile);
//                    }
                    frame.setVisible(false);
                }
            }
        });
    }

}
