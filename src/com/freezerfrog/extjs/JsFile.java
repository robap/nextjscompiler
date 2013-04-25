/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.freezerfrog.extjs;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
     * @param file file java script class is contained in
     */
    JsFile(File file)
    {
        this.classname = "";
        this.file = file;
    }

    public String getClassname() throws IOException
    {
        if(classname.length() > 0) {
            return classname;
        }
        
        //rid contents of c style comments
        //http://stackoverflow.com/a/3945705/231774
        String contents = this.getContents()
                .replaceAll("/\\*[^*]*\\*+(?:[^*/][^*]*\\*+)*/", "");
        
        //Should match: Ext.define("Foo.bar", Ext.define('Foo.bar', etc
        Pattern pattern = Pattern.compile("Ext\\.define\\(['\\\"]([A-Za-z\\.]+)['\\\"]");
        Matcher matcher = pattern.matcher(contents);
        while (matcher.find()) {
            classname = matcher.group(1);
        }
        
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
