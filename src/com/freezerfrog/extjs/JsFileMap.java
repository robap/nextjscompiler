/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.freezerfrog.extjs;

import java.io.IOException;
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
    
    public void add(JsFile jsFile) throws IOException
    {
        map.put(jsFile.getClassname(), jsFile);
    }
    
    public JsFile getFileByClassname(String classname)
    {
        return map.get(classname);
    }
}
