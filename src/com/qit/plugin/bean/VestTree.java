package com.qit.plugin.bean;

import com.intellij.openapi.module.Module;
import com.intellij.psi.PsiFile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VestTree {
    private Module module;
    private List<PsiFile> file;
    private Set<String> packagePath;

    public VestTree(Module module) {
        file = new ArrayList<>();
        packagePath = new HashSet<>();
        this.module = module;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public List<PsiFile> getFile() {
        return file;
    }

    public void setFile(List<PsiFile> file) {
        this.file = file;
    }

    public Set<String> getPackagePath() {
        return packagePath;
    }

    public void setPackagePath(Set<String> packagePath) {
        this.packagePath = packagePath;
    }
}
