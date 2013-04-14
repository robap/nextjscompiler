/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.freezerfrog.extjs;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author rob
 */
public class JsFile
{
    private String classname;
    
    private File file;
    
    /**
     * 
     * @param classname name of java script class
     * @param file file java script class is contained in
     */
    JsFile(String classname, File file)
    {
        this.classname = classname;
        this.file = file;
    }

    public String getClassname()
    {
        return classname;
    }
    
    public String getContents() throws IOException
    {
        return IOUtils.toString(new FileReader(this.file));
    }
    
    public String getPath()
    {
        return this.file.getAbsolutePath();
    }

    @Override
    public String toString()
    {
        return this.getPath();
    }
    
    
}
