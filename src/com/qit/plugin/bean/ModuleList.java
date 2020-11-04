package com.qit.plugin.bean;

import javax.swing.*;
import java.util.List;

public class ModuleList extends AbstractListModel {
    private List<VestTree> modules;

    public ModuleList(List<VestTree> modules) {
        this.modules = modules;
    }

    @Override
    public Object getElementAt(int index) {
        return modules.get(index).getModule().getName();
    }

    @Override
    public int getSize() {
        return modules.size();
    }
}
