package com.qit.plugin.bean;

import com.intellij.openapi.module.Module;
import com.intellij.psi.PsiFile;
import com.intellij.psi.xml.XmlAttributeValue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VestTree {
    private Module module;
    private List<PsiFile> classFile;
    private PsiFile manifestFile;
    private XmlAttributeValue packageElement;
    private List<PsiFile> layoutFile;
    private List<PsiFile> drawableFile;
    private Set<String> packagePath;

    public VestTree(Module module) {
        classFile = new ArrayList<>();
        packagePath = new HashSet<>();
        this.module = module;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public List<PsiFile> getClassFile() {
        return classFile;
    }

    public void setClassFile(List<PsiFile> classFile) {
        this.classFile = classFile;
    }

    public Set<String> getPackagePath() {
        return packagePath;
    }

    public void setPackagePath(Set<String> packagePath) {
        this.packagePath = packagePath;
    }

    public PsiFile getManifestFile() {
        return manifestFile;
    }

    public void setManifestFile(PsiFile manifestFile) {
        this.manifestFile = manifestFile;
    }

    public XmlAttributeValue getPackageElement() {
        return packageElement;
    }

    public void setPackageElement(XmlAttributeValue packageElement) {
        this.packageElement = packageElement;
    }
}
