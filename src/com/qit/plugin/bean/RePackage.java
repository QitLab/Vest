package com.qit.plugin.bean;

public class RePackage {
    private String abPath;
    private String oldPackage;
    private String newPackage;

    public RePackage(String oldPackage) {
        this.oldPackage = oldPackage;
        this.newPackage = "";
    }


    public String getOldPackage() {
        return oldPackage;
    }

    public void setOldPackage(String oldPackage) {
        this.oldPackage = oldPackage;
    }

    public String getNewPackage() {
        return newPackage;
    }

    public void setNewPackage(String newPackage) {
        this.newPackage = newPackage;
    }
}
