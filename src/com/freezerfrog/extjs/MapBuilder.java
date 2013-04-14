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
        String classname = jsFile.getAbsolutePath()
                .replace(".js", "")
                .replace(baseDir.getAbsolutePath() + "/", "")
                .replace("/", ".");
        
        return classname;
    }
}
