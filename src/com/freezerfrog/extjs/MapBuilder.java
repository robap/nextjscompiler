/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.freezerfrog.extjs;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author rob
 */
public class MapBuilder
{
    public JsFileMap buildMap(String[] dirNames) throws IOException
    {
        String[] filters = {"js"};
        
        JsFileMap jsFiles = new JsFileMap();
        
        for (String dirName : dirNames) {
            File dir = new File(dirName);
            Iterator it = FileUtils.iterateFiles(dir, filters, true);
            while (it.hasNext()) {
                jsFiles.add(new JsFile((File) it.next()));
            }
        }
        
        return jsFiles;
    }
}
