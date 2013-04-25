/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.freezerfrog.extjs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author rob
 */
public class DepsBuilder
{
    private ArrayList<JsFile> lastDeps;
    
    public ArrayList<JsFile> buildDeps(JsFileMap jsFileMap, JsFile appFile) throws IOException
    {
        lastDeps = new ArrayList<JsFile>();
        
        return buildRecursiveDeps(appFile, jsFileMap, true);
    }
    
    private ArrayList<JsFile> buildRecursiveDeps(JsFile inputFile, JsFileMap jsFileMap, boolean isAppFile) throws IOException
    {
        ArrayList<JsFile> deps = new ArrayList<JsFile>();
        
        ArrayList<JsFile> possibleDeps = getDependencies(inputFile, jsFileMap, isAppFile);
        for (JsFile possibleDep : possibleDeps) {
            //guard against circular dependencies?
            if (lastDeps.contains(possibleDep)) {
                continue;
            }
            lastDeps.add(possibleDep);
            
            ArrayList<JsFile> nestedDeps = buildRecursiveDeps(possibleDep, jsFileMap, false);
            for (JsFile nestedDep : nestedDeps) {
                deps.add(nestedDep);
            }
            
            deps.add(possibleDep);
        }
        
        deps.add(inputFile);
        return deps;
    }
    
    private ArrayList<JsFile> getDependencies(JsFile jsFile, JsFileMap jsFileMap, boolean isAppFile) throws IOException
    {
        ArrayList<JsFile> deps = new ArrayList<JsFile>();
        
        //rid contents of c style comments
        //http://stackoverflow.com/a/3945705/231774
        String contents = jsFile.getContents()
                .replaceAll("/\\*[^*]*\\*+(?:[^*/][^*]*\\*+)*/", "");
        
        Pattern pattern = Pattern.compile("['\\\"]([A-z\\.]+)['\\\"]");
        Matcher matcher = pattern.matcher(contents);
        while (matcher.find()) {
            String possibleDepName = matcher.group(1);
            
            
            //exclude jsFile's class name as a dependency
            if (!isAppFile && possibleDepName.equals(jsFile.getClassname())) {
                continue;
            }
            
            //make sure it is in the map, otherwise it is not a dependency
            JsFile depFile = jsFileMap.getFileByClassname(possibleDepName);
            if (depFile == null) {
                continue;
            }
            
            deps.add(depFile);
        }
        
        return deps;
    }
}
