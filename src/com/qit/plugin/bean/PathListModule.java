package com.qit.plugin.bean;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class PathListModule extends AbstractTableModel {
    private List<RePackage> modules;
    private String[] TableHead = {"原包名", "修改为"};    //表格标题


    public PathListModule(List<RePackage> modules) {
        this.modules = modules;
    }

    public int getColumnCount() {
        return TableHead.length;
    }

    public int getRowCount() {
        return modules.size();
    }

    public String getColumnName(int column) {
        return TableHead[column];
    }

    @Override
    public Object getValueAt(int row, int column) {
        if (column == 0) return modules.get(row).getOldPackage();
        else return modules.get(row).getNewPackage();
    }

    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    public boolean isCellEditable(int row, int column) {
        return column >= 1;
    }

    //改变某个数据
    public void setValueAt(Object value, int row, int column) {
        modules.get(row).setNewPackage(value.toString());
        fireTableCellUpdated(row, column);
    }

}