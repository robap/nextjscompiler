/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.freezerfrog.extjs;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author rob
 */
public class JsFileMap
{
    private Map<String, JsFile> map;
    
    JsFileMap()
    {
        map = new HashMap<String, JsFile>();
    }
    
    public void add(JsFile jsFile)
    {
        map.put(jsFile.getClassname(), jsFile);
    }
    
    public JsFile getFileByClassname(String classname)
    {
        return map.get(classname);
    }

    @Override
    public String toString()
    {
        String s = "";
        for (Map.Entry pairs : map.entrySet()) {
            JsFile jsFile = (JsFile) pairs.getValue();
            s += jsFile.getClassname() + " => " + jsFile.getPath() + "\n";
        }
        
        return s;
    }
}
