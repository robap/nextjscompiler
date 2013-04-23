/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.freezerfrog.extjs;

import java.io.File;
import java.util.Iterator;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author rob
 */
public class MapBuilder
{
    
    private String baseDir;
    private String namespace;
    
    public JsFileMap buildMap(String[] dirNames)
    {
        String[] filters = {"js"};
        
        JsFileMap jsFiles = new JsFileMap();
        
        for (String dirName : dirNames) {
            File dir = new File(dirName);
            Iterator it = FileUtils.iterateFiles(dir, filters, true);
            String classname;
            while (it.hasNext()) {
                File curFile = (File) it.next();
                classname = getClassname(dir, curFile);
                jsFiles.add(new JsFile(classname, curFile));
            }
        }
        
        return jsFiles;
    }
    
    private String getClassname(File baseDir, File jsFile)
    {
        String basePath = baseDir.getAbsolutePath();
        String jsPath = jsFile.getAbsolutePath();
        
        String classname = jsPath
                .replace(".js", "")
                .replace(basePath + "/", "")
                .replace("/", ".");
        
        if (this.baseDir.length() > 0 && this.namespace.length() > 0) {
            classname = this.namespace + "." + classname;
        }
        
        return classname;
    }

    /**
     * @todo This sucks. Find another way
     * @param namespaceMap e.g. foo:bar
     */
    void setNamespaceMap(String namespaceMap) {
        String[] parts = namespaceMap.split(":");
        this.baseDir = parts[0];
        this.namespace = parts[1];
    }
}
