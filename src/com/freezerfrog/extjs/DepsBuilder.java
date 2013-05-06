/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.freezerfrog.extjs;

import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author rob
 */
public class DepsBuilder
{
    private ArrayList<JsFile> lastDeps;
    
    public ArrayList<JsFile> buildDeps(JsFileMap jsFileMap, JsFile appFile) throws IOException
    {
        lastDeps = new ArrayList();
        
        return buildRecursiveDeps(appFile, jsFileMap);
    }
    
    private ArrayList<JsFile> buildRecursiveDeps(JsFile inputFile, JsFileMap jsFileMap) throws IOException
    {
        ArrayList<JsFile> deps = new ArrayList();
        
        ArrayList<JsFile> possibleDeps = getDependencies(inputFile, jsFileMap);
        for (JsFile possibleDep : possibleDeps) {
            //guard against circular dependencies?
            if (lastDeps.contains(possibleDep)) {
                continue;
            }
            lastDeps.add(possibleDep);
            
            ArrayList<JsFile> nestedDeps = buildRecursiveDeps(possibleDep, jsFileMap);
            for (JsFile nestedDep : nestedDeps) {
                deps.add(nestedDep);
            }
            
            deps.add(possibleDep);
        }
        
        deps.add(inputFile);
        return deps;
    }
    
    private ArrayList<JsFile> getDependencies(JsFile jsFile, JsFileMap jsFileMap) throws IOException
    {
        ArrayList<JsFile> deps = new ArrayList();
        
        for (String possibleDepName : jsFile.getDependencies()) {
            JsFile depFile = jsFileMap.getFileByClassname(possibleDepName);
            if (depFile != null) {
                deps.add(depFile);
            } else {
                System.out.println("WARN class:" + jsFile.getClassname() 
                        + " depname:" + possibleDepName 
                        + " was not found in the map");
            }
        }
        
        return deps;
    }
}
